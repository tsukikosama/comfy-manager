/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.system.service.impl.comfy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.system.comfy.ComfyScheduleTimeCalculator;
import top.continew.admin.system.enums.comfy.BatchStatusEnum;
import top.continew.admin.system.enums.comfy.ScheduleStatusEnum;
import top.continew.admin.system.enums.comfy.TaskStatusEnum;
import top.continew.admin.system.mapper.comfy.ComfyInstanceMapper;
import top.continew.admin.system.mapper.comfy.ComfyScheduleMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskBatchMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskMapper;
import top.continew.admin.system.model.entity.comfy.ComfyInstanceDO;
import top.continew.admin.system.model.entity.comfy.ComfyScheduleDO;
import top.continew.admin.system.model.entity.comfy.ComfyTaskBatchDO;
import top.continew.admin.system.model.entity.comfy.ComfyTaskDO;
import top.continew.admin.system.service.comfy.ComfyPlanScanService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ComfyUI 计划扫描业务实现（由 SnailJob 执行器定时调用，物化到期触发区间）
 *
 * <p>
 * 扫描到期（next_run_at_utc &lt;= now）的启用计划，把 (扫描游标, now] 区间内的触发点
 * 物化为任务批次与任务项（固定最终 prompt 与原目标），推进补执行游标并重算下一次触发时间。
 * 批次唯一键 (schedule_id, scheduled_at_utc) 与 (user_id, request_key) 保证重复扫描幂等。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ComfyPlanScanServiceImpl implements ComfyPlanScanService {

    private static final long INIT_VERSION = 0L;

    private static final int DEFAULT_TASK_COUNT = 1;

    private static final int DEFAULT_BACKLOG_LIMIT = 1000;

    private final ComfyScheduleMapper scheduleMapper;

    private final ComfyTaskBatchMapper taskBatchMapper;

    private final ComfyTaskMapper taskMapper;

    private final ComfyInstanceMapper instanceMapper;

    @Override
    public void scan() {
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        List<ComfyScheduleDO> schedules = scheduleMapper.selectList(Wrappers.lambdaQuery(ComfyScheduleDO.class)
            .eq(ComfyScheduleDO::getStatus, ScheduleStatusEnum.ACTIVE)
            .le(ComfyScheduleDO::getNextRunAtUtc, now)
            .orderByAsc(ComfyScheduleDO::getNextRunAtUtc));
        for (ComfyScheduleDO schedule : schedules) {
            try {
                materialize(schedule, now);
            } catch (Exception e) {
                // 单个计划物化失败不影响其余计划，下一轮扫描会依据唯一键幂等重试
                log.error("物化计划失败，scheduleId={}", schedule.getId(), e);
            }
        }
    }

    /**
     * 物化单个计划的到期触发区间
     *
     * @param schedule 计划
     * @param now      当前 UTC 时间
     */
    private void materialize(ComfyScheduleDO schedule, LocalDateTime now) {
        LocalDateTime from = schedule.getScanCursorUtc() == null ? now.minusYears(10) : schedule.getScanCursorUtc();
        int max = Math.max(schedule.getBacklogLimit() == null ? DEFAULT_BACKLOG_LIMIT : schedule.getBacklogLimit(), 1);
        List<LocalDateTime> triggers = ComfyScheduleTimeCalculator.generateRuns(schedule, from, now, max);
        if (triggers.isEmpty()) {
            refreshCursor(schedule, now);
            return;
        }
        int taskCount = Math.max(schedule.getTaskCountPerRun() == null
            ? DEFAULT_TASK_COUNT
            : schedule.getTaskCountPerRun(), 1);
        String finalPrompt = resolveSnapshotField(schedule.getExecutionSnapshotJson(), "apiPromptJson");
        ComfyInstanceDO instance = instanceMapper.selectById(schedule.getInstanceId());
        String endpointUrl = firstNonBlank(resolveSnapshotField(schedule.getFixedTargetJson(), "endpointUrl"),
            instance == null ? null : instance.getEndpointUrl());
        String deviceUuid = firstNonBlank(resolveSnapshotField(schedule.getFixedTargetJson(), "deviceUuid"),
            instance == null ? null : instance.getDeviceUuid());
        LocalDateTime lastTrigger = null;
        for (LocalDateTime triggerAt : triggers) {
            if (createBatch(schedule, triggerAt, taskCount, finalPrompt, endpointUrl, deviceUuid)) {
                lastTrigger = triggerAt;
            }
        }
        if (lastTrigger != null) {
            schedule.setScanCursorUtc(lastTrigger);
            schedule.setHasUnmaterialized(true);
        }
        refreshCursor(schedule, now);
    }

    /**
     * 为单个触发点创建批次与任务项（已存在则跳过）
     *
     * @param schedule    计划
     * @param triggerAt   触发时刻（UTC）
     * @param taskCount   任务项数量
     * @param finalPrompt 冻结的最终 prompt
     * @param endpointUrl 原目标地址
     * @param deviceUuid  原目标设备UUID
     * @return 是否新建成功
     */
    private boolean createBatch(ComfyScheduleDO schedule, LocalDateTime triggerAt, int taskCount, String finalPrompt,
        String endpointUrl, String deviceUuid) {
        String requestKey = buildRequestKey(schedule.getId(), triggerAt);
        Long exists = taskBatchMapper.selectCount(Wrappers.lambdaQuery(ComfyTaskBatchDO.class)
            .eq(ComfyTaskBatchDO::getUserId, schedule.getUserId())
            .eq(ComfyTaskBatchDO::getRequestKey, requestKey));
        if (exists != null && exists > 0) {
            return false;
        }
        ComfyTaskBatchDO batch = new ComfyTaskBatchDO();
        batch.setUserId(schedule.getUserId());
        batch.setScheduleId(schedule.getId());
        batch.setInstanceId(schedule.getInstanceId());
        batch.setWorkflowId(schedule.getWorkflowId());
        batch.setScheduledAtUtc(triggerAt);
        batch.setRuleJson(schedule.getRuleJson());
        batch.setExecutionSnapshotJson(schedule.getExecutionSnapshotJson());
        batch.setTaskCount(0);
        batch.setRequestKey(requestKey);
        batch.setStatus(BatchStatusEnum.WAITING);
        taskBatchMapper.insert(batch);
        for (int i = 1; i <= taskCount; i++) {
            ComfyTaskDO task = new ComfyTaskDO();
            task.setUserId(schedule.getUserId());
            task.setScheduleId(schedule.getId());
            task.setTaskBatchId(batch.getId());
            task.setInstanceId(schedule.getInstanceId());
            task.setWorkflowId(schedule.getWorkflowId());
            task.setItemIndex(i);
            task.setScheduledAtUtc(triggerAt);
            task.setActualParamsJson(schedule.getParamsJson());
            task.setFinalPromptJson(finalPrompt);
            task.setEndpointUrl(endpointUrl);
            task.setTargetDeviceUuid(deviceUuid);
            // 物化后先等待浏览器：执行页获取实例租约时才提升为 READY
            task.setStatus(TaskStatusEnum.WAITING_BROWSER);
            task.setVersionNo(INIT_VERSION);
            task.setCurrentAttemptNo(0);
            taskMapper.insert(task);
        }
        ComfyTaskBatchDO batchUpdate = new ComfyTaskBatchDO();
        batchUpdate.setId(batch.getId());
        batchUpdate.setTaskCount(taskCount);
        taskBatchMapper.updateById(batchUpdate);
        return true;
    }

    /**
     * 推进补执行游标并重算下一次触发时间
     *
     * @param schedule 计划
     * @param now      当前 UTC 时间
     */
    private void refreshCursor(ComfyScheduleDO schedule, LocalDateTime now) {
        List<LocalDateTime> future = ComfyScheduleTimeCalculator.generateRuns(schedule, now, now.plusYears(10), 1);
        schedule.setNextRunAtUtc(future.isEmpty() ? null : future.get(0));
        ComfyScheduleDO update = new ComfyScheduleDO();
        update.setId(schedule.getId());
        update.setNextRunAtUtc(schedule.getNextRunAtUtc());
        update.setScanCursorUtc(schedule.getScanCursorUtc());
        update.setHasUnmaterialized(schedule.getHasUnmaterialized());
        scheduleMapper.updateById(update);
    }

    /**
     * 构造批次请求键（计划ID + 触发时刻）
     *
     * @param scheduleId 计划ID
     * @param triggerAt  触发时刻（UTC）
     * @return 请求键
     */
    private String buildRequestKey(Long scheduleId, LocalDateTime triggerAt) {
        return "%d|%s".formatted(scheduleId, triggerAt);
    }

    /**
     * 读取 JSON 快照中的字段
     *
     * @param json  快照 JSON
     * @param field 字段名
     * @return 字段值
     */
    private String resolveSnapshotField(String json, String field) {
        if (json == null) {
            return null;
        }
        try {
            return JSONUtil.parseObj(json).getStr(field);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回首个非空字符串
     *
     * @param first  首选值
     * @param second 备选值
     * @return 首个非空值
     */
    private String firstNonBlank(String first, String second) {
        return StrUtil.isNotBlank(first) ? first : second;
    }
}
