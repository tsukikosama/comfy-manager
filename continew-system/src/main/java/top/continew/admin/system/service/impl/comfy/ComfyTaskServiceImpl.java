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
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.system.comfy.ComfyScheduleTimeCalculator;
import top.continew.admin.system.enums.comfy.AttemptSendIntentEnum;
import top.continew.admin.system.enums.comfy.AttemptStatusEnum;
import top.continew.admin.system.enums.comfy.InstanceLeaseStatusEnum;
import top.continew.admin.system.enums.comfy.TaskStatusEnum;
import top.continew.admin.system.mapper.comfy.ComfyInstanceMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskAttemptMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskMapper;
import top.continew.admin.system.model.entity.comfy.ComfyInstanceDO;
import top.continew.admin.system.model.entity.comfy.ComfyTaskAttemptDO;
import top.continew.admin.system.model.entity.comfy.ComfyTaskDO;
import top.continew.admin.system.model.query.comfy.ComfyTaskQuery;
import top.continew.admin.system.model.req.comfy.ComfyCancelReq;
import top.continew.admin.system.model.req.comfy.ComfyClaimReq;
import top.continew.admin.system.model.req.comfy.ComfyReconcileReq;
import top.continew.admin.system.model.req.comfy.ComfyReportReq;
import top.continew.admin.system.model.req.comfy.ComfyRetryReq;
import top.continew.admin.system.model.req.comfy.ComfySubmitIntentReq;
import top.continew.admin.system.model.req.comfy.ComfyTaskReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskClaimResp;
import top.continew.admin.system.model.resp.comfy.ComfyTaskResp;
import top.continew.admin.system.service.comfy.ComfyTaskService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ComfyUI 任务业务实现（领取/提交意图/状态回报/对账/取消/重试）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyTaskServiceImpl extends
    BaseServiceImpl<ComfyTaskMapper, ComfyTaskDO, ComfyTaskResp, ComfyTaskResp, ComfyTaskQuery, ComfyTaskReq>
    implements ComfyTaskService {

    private static final long INIT_VERSION = 0L;

    private final ComfyInstanceMapper instanceMapper;

    private final ComfyTaskAttemptMapper attemptMapper;

    @Override
    public ComfyTaskResp get(Long id) {
        ComfyTaskDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "任务不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该任务");
        return BeanUtil.toBean(entity, ComfyTaskResp.class);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyTaskDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyTaskDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该任务");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public ComfyTaskClaimResp claim(Long taskId, ComfyClaimReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyTaskDO task = baseMapper.selectById(taskId);
        CheckUtils.throwIfNull(task, "任务不存在");
        CheckUtils.throwIfNotEqual(task.getUserId(), userId, "无权领取该任务");
        CheckUtils.throwIfNotEqual(task.getStatus(), TaskStatusEnum.READY, "任务状态不可领取");
        ComfyInstanceDO instance = instanceMapper.selectById(task.getInstanceId());
        CheckUtils.throwIfNull(instance, "目标实例不存在");
        CheckUtils.throwIfNotEqual(instance.getUserId(), userId, "目标实例不属于当前用户");
        CheckUtils.throwIfNotEqual(instance.getLeaseStatus(), InstanceLeaseStatusEnum.ACTIVE, "实例执行租约未激活");
        CheckUtils.throwIfNotEqual(instance.getPageUuid(), req.getPageUuid(), "执行页不一致");
        CheckUtils.throwIfNotEqual(instance.getDeviceUuid(), req.getDeviceUuid(), "设备不一致");
        CheckUtils.throwIfNotEqual(instance.getLeaseEpoch(), req.getEpoch(), "租约 epoch 不一致");
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        CheckUtils.throwIf(instance.getLeaseExpiresAt() != null && instance.getLeaseExpiresAt()
            .isBefore(now), "租约已过期，请重新获取租约");
        String claimToken = IdUtil.fastSimpleUUID();
        ComfyTaskDO update = new ComfyTaskDO();
        update.setId(taskId);
        update.setVersionNo(task.getVersionNo());
        update.setStatus(TaskStatusEnum.CLAIMED);
        update.setClaimToken(claimToken);
        update.setClaimPageUuid(req.getPageUuid());
        update.setClaimDeviceUuid(req.getDeviceUuid());
        update.setClaimEpoch(req.getEpoch());
        update.setClaimedAt(now);
        int rows = baseMapper.updateById(update);
        CheckUtils.throwIf(rows == 0, "领取冲突，请重试");
        ComfyTaskClaimResp resp = new ComfyTaskClaimResp();
        resp.setTaskId(task.getId());
        resp.setItemIndex(task.getItemIndex());
        resp.setClaimToken(claimToken);
        resp.setEpoch(req.getEpoch());
        resp.setEndpointUrl(task.getEndpointUrl());
        resp.setTargetDeviceUuid(task.getTargetDeviceUuid());
        resp.setActualParamsJson(task.getActualParamsJson());
        resp.setFinalPromptJson(task.getFinalPromptJson());
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitIntent(Long taskId, ComfySubmitIntentReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyTaskDO task = baseMapper.selectById(taskId);
        CheckUtils.throwIfNull(task, "任务不存在");
        CheckUtils.throwIfNotEqual(task.getUserId(), userId, "无权操作该任务");
        CheckUtils.throwIfNotEqual(task.getStatus(), TaskStatusEnum.CLAIMED, "任务未被领取，无法提交");
        CheckUtils.throwIfNull(req.getRequestKey(), "请求键不能为空");
        CheckUtils.throwIfNull(req.getRequestUuid(), "请求UUID不能为空");
        ComfyTaskAttemptDO attempt = new ComfyTaskAttemptDO();
        attempt.setUserId(userId);
        attempt.setTaskId(taskId);
        attempt.setTaskBatchId(task.getTaskBatchId());
        attempt.setAttemptNo(nextAttemptNo(task));
        attempt.setRequestKey(req.getRequestKey());
        attempt.setRequestUuid(hexToBytes(req.getRequestUuid()));
        attempt.setActualPromptJson(req.getActualPromptJson() == null
            ? task.getFinalPromptJson()
            : req.getActualPromptJson());
        attempt.setSendIntent(AttemptSendIntentEnum.SUBMIT);
        attempt.setStatus(AttemptStatusEnum.SENDING);
        attempt.setVersionNo(INIT_VERSION);
        attemptMapper.insert(attempt);
        ComfyTaskDO update = new ComfyTaskDO();
        update.setId(taskId);
        update.setVersionNo(task.getVersionNo());
        update.setStatus(TaskStatusEnum.SUBMITTING);
        update.setCurrentAttemptNo(attempt.getAttemptNo());
        update.setSubmittedAt(ComfyScheduleTimeCalculator.nowUtc());
        baseMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(Long taskId, ComfyReportReq req) {
        handleReport(taskId, req.getRequestUuid(), req.getAttemptNo(), req.getStatus(), req.getPromptId(), req
            .getErrorMsg(), req.getResponseJson(), req.getActualPromptJson(), AttemptSendIntentEnum.SUBMIT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reconcile(Long taskId, ComfyReconcileReq req) {
        handleReport(taskId, req.getRequestUuid(), req.getAttemptNo(), req.getStatus(), req.getPromptId(), req
            .getErrorMsg(), req.getResponseJson(), null, AttemptSendIntentEnum.RECONCILE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long taskId, ComfyCancelReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyTaskDO task = baseMapper.selectById(taskId);
        CheckUtils.throwIfNull(task, "任务不存在");
        CheckUtils.throwIfNotEqual(task.getUserId(), userId, "无权取消该任务");
        ComfyTaskDO update = new ComfyTaskDO();
        update.setId(taskId);
        update.setVersionNo(task.getVersionNo());
        if (task.getStatus() == TaskStatusEnum.WAITING_BROWSER || task.getStatus() == TaskStatusEnum.READY
            || task.getStatus() == TaskStatusEnum.CLAIMED || task.getStatus() == TaskStatusEnum.SUBMITTING) {
            update.setStatus(TaskStatusEnum.CANCELLED);
            update.setFinishedAt(ComfyScheduleTimeCalculator.nowUtc());
        } else {
            update.setStatus(TaskStatusEnum.CANCEL_REQUESTED);
        }
        int rows = baseMapper.updateById(update);
        CheckUtils.throwIf(rows == 0, "取消冲突，请重试");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retry(Long taskId, ComfyRetryReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyTaskDO task = baseMapper.selectById(taskId);
        CheckUtils.throwIfNull(task, "任务不存在");
        CheckUtils.throwIfNotEqual(task.getUserId(), userId, "无权重试该任务");
        CheckUtils.throwIfNotEqual(task.getStatus(), TaskStatusEnum.UNKNOWN, "仅未知状态任务可重试");
        CheckUtils.throwIfNull(req.getRequestUuid(), "请求UUID不能为空");
        ComfyTaskAttemptDO attempt = new ComfyTaskAttemptDO();
        attempt.setUserId(userId);
        attempt.setTaskId(taskId);
        attempt.setTaskBatchId(task.getTaskBatchId());
        attempt.setAttemptNo(nextAttemptNo(task));
        attempt.setRequestKey(req.getRequestUuid());
        attempt.setRequestUuid(hexToBytes(req.getRequestUuid()));
        attempt.setActualPromptJson(req.getActualPromptJson() == null
            ? task.getFinalPromptJson()
            : req.getActualPromptJson());
        attempt.setSendIntent(AttemptSendIntentEnum.RETRY);
        attempt.setStatus(AttemptStatusEnum.SENDING);
        attempt.setVersionNo(INIT_VERSION);
        attemptMapper.insert(attempt);
        ComfyTaskDO update = new ComfyTaskDO();
        update.setId(taskId);
        update.setVersionNo(task.getVersionNo());
        update.setStatus(TaskStatusEnum.SUBMITTING);
        update.setCurrentAttemptNo(attempt.getAttemptNo());
        update.setSubmittedAt(ComfyScheduleTimeCalculator.nowUtc());
        baseMapper.updateById(update);
    }

    @Override
    protected QueryWrapper<ComfyTaskDO> buildQueryWrapper(ComfyTaskQuery query) {
        QueryWrapper<ComfyTaskDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }

    /**
     * 统一处理状态回报与对账（幂等：终态不被���进度回退）
     *
     * @param taskId           任务ID
     * @param requestUuid      请求UUID（十六进制）
     * @param requestAttemptNo 回报的尝试号
     * @param status           尝试状态
     * @param promptId         ComfyUI promptId
     * @param errorMsg         错误信息
     * @param responseJson     响应内容
     * @param actualPromptJson 实际提交的 prompt
     * @param sendIntent       发送意图
     */
    private void handleReport(Long taskId, String requestUuid, Integer requestAttemptNo, AttemptStatusEnum status,
        String promptId, String errorMsg, String responseJson, String actualPromptJson,
        AttemptSendIntentEnum sendIntent) {
        Long userId = UserContextHolder.getUserId();
        ComfyTaskDO task = baseMapper.selectById(taskId);
        CheckUtils.throwIfNull(task, "任务不存在");
        CheckUtils.throwIfNotEqual(task.getUserId(), userId, "无权操作该任务");
        CheckUtils.throwIfNull(requestUuid, "请求UUID不能为空");
        CheckUtils.throwIfNull(status, "回报状态不能为空");
        // 迟到回执：尝试可能不存在（提交意图未落库或响应丢失），此处补记证据
        ComfyTaskAttemptDO attempt = attemptMapper.selectByRequestKey(requestUuid, taskId);
        if (attempt == null) {
            attempt = new ComfyTaskAttemptDO();
            attempt.setUserId(userId);
            attempt.setTaskId(taskId);
            attempt.setTaskBatchId(task.getTaskBatchId());
            attempt.setAttemptNo(requestAttemptNo == null ? nextAttemptNo(task) : requestAttemptNo);
            attempt.setRequestKey(requestUuid);
            attempt.setRequestUuid(hexToBytes(requestUuid));
            attempt.setActualPromptJson(actualPromptJson == null ? task.getFinalPromptJson() : actualPromptJson);
            attempt.setSendIntent(sendIntent);
            attempt.setStatus(status);
            attempt.setVersionNo(INIT_VERSION);
            attemptMapper.insert(attempt);
        }
        ComfyTaskAttemptDO attemptUpdate = new ComfyTaskAttemptDO();
        attemptUpdate.setId(attempt.getId());
        attemptUpdate.setVersionNo(attempt.getVersionNo());
        attemptUpdate.setStatus(status);
        attemptUpdate.setErrorMsg(errorMsg);
        attemptUpdate.setResponseJson(responseJson);
        if (actualPromptJson != null) {
            attemptUpdate.setActualPromptJson(actualPromptJson);
        }
        attemptMapper.updateById(attemptUpdate);
        ComfyTaskDO update = new ComfyTaskDO();
        update.setId(taskId);
        update.setVersionNo(task.getVersionNo());
        if (promptId != null) {
            update.setPromptId(promptId);
        }
        applyReportStatus(task, status, update);
        baseMapper.updateById(update);
    }

    /**
     * 依据回报状态推进任务状态，终态不被覆盖
     *
     * @param task   当前任务
     * @param status 尝试状态
     * @param update 待更新实体
     */
    private void applyReportStatus(ComfyTaskDO task, AttemptStatusEnum status, ComfyTaskDO update) {
        // 幂等：已进入终态的任务不被旧进度回退
        if (task.getStatus() == TaskStatusEnum.SUCCEEDED || task.getStatus() == TaskStatusEnum.FAILED
            || task.getStatus() == TaskStatusEnum.CANCELLED) {
            return;
        }
        TaskStatusEnum next = switch (status) {
            case SUCCESS -> TaskStatusEnum.SUCCEEDED;
            case FAILED -> TaskStatusEnum.FAILED;
            case UNKNOWN -> TaskStatusEnum.UNKNOWN;
            default -> TaskStatusEnum.RUNNING;
        };
        if (next == TaskStatusEnum.SUCCEEDED || next == TaskStatusEnum.FAILED || next == TaskStatusEnum.UNKNOWN) {
            update.setStatus(next);
            update.setFinishedAt(ComfyScheduleTimeCalculator.nowUtc());
            return;
        }
        update.setStatus(TaskStatusEnum.RUNNING);
    }

    /**
     * 计算下一次尝试号
     *
     * @param task 任务
     * @return 尝试号
     */
    private int nextAttemptNo(ComfyTaskDO task) {
        return (task.getCurrentAttemptNo() == null ? 0 : task.getCurrentAttemptNo()) + 1;
    }

    /**
     * 十六进制文本转原始字节
     *
     * @param hex 十六进制文本
     * @return 原始字节
     */
    private byte[] hexToBytes(String hex) {
        if (hex == null) {
            return null;
        }
        try {
            return HexUtil.decodeHex(hex);
        } catch (Exception e) {
            return null;
        }
    }
}
