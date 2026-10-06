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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.system.comfy.ComfyScheduleTimeCalculator;
import top.continew.admin.system.mapper.comfy.ComfyInstanceMapper;
import top.continew.admin.system.mapper.comfy.ComfyScheduleMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskMapper;
import top.continew.admin.system.mapper.comfy.ComfyWorkflowMapper;
import top.continew.admin.system.model.entity.comfy.ComfyInstanceDO;
import top.continew.admin.system.model.entity.comfy.ComfyScheduleDO;
import top.continew.admin.system.model.entity.comfy.ComfyWorkflowDO;
import top.continew.admin.system.model.query.comfy.ComfyScheduleQuery;
import top.continew.admin.system.model.req.comfy.ComfyScheduleReq;
import top.continew.admin.system.model.req.comfy.ComfyScheduleSkipReq;
import top.continew.admin.system.model.resp.comfy.ComfySchedulePreviewResp;
import top.continew.admin.system.model.resp.comfy.ComfyScheduleResp;
import top.continew.admin.system.service.comfy.ComfyScheduleService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ComfyUI 用户计划业务实现
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyScheduleServiceImpl extends
    BaseServiceImpl<ComfyScheduleMapper, ComfyScheduleDO, ComfyScheduleResp, ComfyScheduleResp, ComfyScheduleQuery, ComfyScheduleReq>
    implements ComfyScheduleService {

    private static final int DEFAULT_TASK_COUNT = 1;

    private static final int DEFAULT_BACKLOG_LIMIT = 1000;

    private final ComfyWorkflowMapper workflowMapper;

    private final ComfyInstanceMapper instanceMapper;

    private final ComfyTaskMapper taskMapper;

    @Override
    public Long create(ComfyScheduleReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyScheduleDO entity = BeanUtil.toBean(req, ComfyScheduleDO.class);
        entity.setUserId(userId);
        entity.setConfigRevision(0L);
        entity.setHasUnmaterialized(false);
        entity.setScanCursorUtc(null);
        if (entity.getTaskCountPerRun() == null) {
            entity.setTaskCountPerRun(DEFAULT_TASK_COUNT);
        }
        if (entity.getBacklogLimit() == null) {
            entity.setBacklogLimit(DEFAULT_BACKLOG_LIMIT);
        }
        enrichSnapshot(entity);
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        entity.setNextRunAtUtc(ComfyScheduleTimeCalculator.computeFirstRun(entity, now));
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public ComfyScheduleResp get(Long id) {
        ComfyScheduleDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "计划不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该计划");
        return BeanUtil.toBean(entity, ComfyScheduleResp.class);
    }

    @Override
    public void update(ComfyScheduleReq req, Long id) {
        ComfyScheduleDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "计划不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权操作该计划");
        CheckUtils.throwIf(entity.getHasUnmaterialized(), "存在未物化到期区间，请先跳过该区间再编辑");
        if (req.getName() != null) {
            entity.setName(req.getName());
        }
        if (req.getInstanceId() != null) {
            entity.setInstanceId(req.getInstanceId());
        }
        boolean workflowChanged = req.getWorkflowId() != null && !req.getWorkflowId().equals(entity.getWorkflowId());
        if (req.getWorkflowId() != null) {
            entity.setWorkflowId(req.getWorkflowId());
        }
        if (req.getRunMode() != null) {
            entity.setRunMode(req.getRunMode());
        }
        if (req.getTimezone() != null) {
            entity.setTimezone(req.getTimezone());
        }
        if (req.getRuleJson() != null) {
            entity.setRuleJson(req.getRuleJson());
        }
        if (req.getParamsJson() != null) {
            entity.setParamsJson(req.getParamsJson());
        }
        if (req.getMaterialRefsJson() != null) {
            entity.setMaterialRefsJson(req.getMaterialRefsJson());
        }
        if (req.getTaskCountPerRun() != null) {
            entity.setTaskCountPerRun(req.getTaskCountPerRun());
        }
        if (req.getBacklogLimit() != null) {
            entity.setBacklogLimit(req.getBacklogLimit());
        }
        if (req.getStatus() != null) {
            entity.setStatus(req.getStatus());
        }
        if (workflowChanged || req.getParamsJson() != null) {
            enrichSnapshot(entity);
        }
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        entity.setNextRunAtUtc(ComfyScheduleTimeCalculator.computeFirstRun(entity, now));
        baseMapper.updateById(entity);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyScheduleDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyScheduleDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该计划");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public ComfySchedulePreviewResp preview(Long id) {
        ComfyScheduleDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "计划不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该计划");
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        ComfySchedulePreviewResp resp = new ComfySchedulePreviewResp();
        resp.setNextRunAtUtc(entity.getNextRunAtUtc());
        resp.setUpcomingRuns(ComfyScheduleTimeCalculator.previewRuns(entity, 5, now));
        Long waitingCount = taskMapper.countWaitingByScheduleId(entity.getId());
        resp.setBacklogCount(waitingCount);
        resp.setOldestWaitingScheduledAtUtc(taskMapper.oldestWaitingScheduledAtUtc(entity.getId()));
        return resp;
    }

    @Override
    public void skip(Long id, ComfyScheduleSkipReq req) {
        ComfyScheduleDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "计划不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权操作该计划");
        CheckUtils.throwIf(!Boolean.TRUE.equals(entity.getHasUnmaterialized()), "当前没有未物化到期区间，无需跳过");
        CheckUtils.throwIfNull(req.getUntilUtc(), "跳至时刻不能为空");
        LocalDateTime until = req.getUntilUtc();
        if (entity.getScanCursorUtc() == null || until.isAfter(entity.getScanCursorUtc())) {
            entity.setScanCursorUtc(until);
        }
        entity.setHasUnmaterialized(false);
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        entity.setNextRunAtUtc(ComfyScheduleTimeCalculator.computeFirstRun(entity, now));
        baseMapper.updateById(entity);
    }

    @Override
    protected QueryWrapper<ComfyScheduleDO> buildQueryWrapper(ComfyScheduleQuery query) {
        QueryWrapper<ComfyScheduleDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }

    /**
     * 由关联工作流与实例派生执行快照与固定目标
     *
     * @param entity 计划实体
     */
    private void enrichSnapshot(ComfyScheduleDO entity) {
        if (entity.getWorkflowId() != null) {
            ComfyWorkflowDO workflow = workflowMapper.selectById(entity.getWorkflowId());
            if (workflow != null) {
                JSONObject snapshot = new JSONObject();
                snapshot.set("apiPromptJson", workflow.getApiPromptJson());
                snapshot.set("nodeBindingsJson", workflow.getNodeBindingsJson());
                snapshot.set("paramsJson", entity.getParamsJson());
                entity.setExecutionSnapshotJson(JSONUtil.toJsonStr(snapshot));
            }
        }
        if (entity.getInstanceId() != null) {
            ComfyInstanceDO instance = instanceMapper.selectById(entity.getInstanceId());
            if (instance != null) {
                JSONObject target = new JSONObject();
                target.set("endpointUrl", instance.getEndpointUrl());
                target.set("deviceUuid", instance.getDeviceUuid());
                entity.setFixedTargetJson(JSONUtil.toJsonStr(target));
            }
        }
    }
}
