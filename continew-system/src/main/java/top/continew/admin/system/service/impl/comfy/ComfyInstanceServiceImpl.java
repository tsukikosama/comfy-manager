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
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.system.comfy.ComfyScheduleTimeCalculator;
import top.continew.admin.system.enums.comfy.InstanceLeaseStatusEnum;
import top.continew.admin.system.enums.comfy.InstanceStatusEnum;
import top.continew.admin.system.enums.comfy.TaskStatusEnum;
import top.continew.admin.system.mapper.comfy.ComfyInstanceMapper;
import top.continew.admin.system.mapper.comfy.ComfyTaskMapper;
import top.continew.admin.system.model.entity.comfy.ComfyInstanceDO;
import top.continew.admin.system.model.entity.comfy.ComfyTaskDO;
import top.continew.admin.system.model.query.comfy.ComfyInstanceQuery;
import top.continew.admin.system.model.req.comfy.ComfyInstanceReq;
import top.continew.admin.system.model.req.comfy.ComfyLeaseReq;
import top.continew.admin.system.model.resp.comfy.ComfyInstanceResp;
import top.continew.admin.system.model.resp.comfy.ComfyLeaseResp;
import top.continew.admin.system.service.comfy.ComfyInstanceService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ComfyUI 实例业务实现
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyInstanceServiceImpl extends
    BaseServiceImpl<ComfyInstanceMapper, ComfyInstanceDO, ComfyInstanceResp, ComfyInstanceResp, ComfyInstanceQuery, ComfyInstanceReq>
    implements ComfyInstanceService {

    private static final int DEFAULT_TTL_SECONDS = 60;

    private final ComfyTaskMapper taskMapper;

    @Override
    public Long create(ComfyInstanceReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyInstanceDO entity = BeanUtil.toBean(req, ComfyInstanceDO.class);
        entity.setUserId(userId);
        entity.setEndpointHash(SecureUtil.sha256(req.getEndpointUrl()));
        entity.setConfigRevision(0L);
        entity.setLeaseEpoch(0L);
        entity.setLeaseStatus(InstanceLeaseStatusEnum.OFFLINE);
        entity.setStatus(InstanceStatusEnum.ENABLED);
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public ComfyInstanceResp get(Long id) {
        ComfyInstanceDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该实例");
        return BeanUtil.toBean(entity, ComfyInstanceResp.class);
    }

    @Override
    public void update(ComfyInstanceReq req, Long id) {
        ComfyInstanceDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权操作该实例");
        boolean endpointChanged = req.getEndpointUrl() != null && !req.getEndpointUrl().equals(entity.getEndpointUrl());
        boolean deviceChanged = req.getDeviceUuid() != null && !req.getDeviceUuid().equals(entity.getDeviceUuid());
        if (endpointChanged || deviceChanged) {
            entity.setConfigRevision((entity.getConfigRevision() == null ? 0L : entity.getConfigRevision()) + 1);
        }
        if (req.getName() != null) {
            entity.setName(req.getName());
        }
        if (endpointChanged) {
            entity.setEndpointUrl(req.getEndpointUrl());
            entity.setEndpointHash(SecureUtil.sha256(req.getEndpointUrl()));
        }
        if (deviceChanged) {
            entity.setDeviceUuid(req.getDeviceUuid());
        }
        baseMapper.updateById(entity);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyInstanceDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyInstanceDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该实例");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public ComfyLeaseResp acquireLease(Long instanceId, ComfyLeaseReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyInstanceDO entity = baseMapper.selectById(instanceId);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), userId, "无权操作该实例");
        long newEpoch = (entity.getLeaseEpoch() == null ? 0L : entity.getLeaseEpoch()) + 1;
        int ttl = req.getTtlSeconds() == null ? DEFAULT_TTL_SECONDS : req.getTtlSeconds();
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        entity.setLeaseEpoch(newEpoch);
        entity.setPageUuid(req.getPageUuid());
        entity.setDeviceUuid(req.getDeviceUuid());
        entity.setLoginTokenDigest(HexUtil.decodeHex(SecureUtil.sha256(req.getLoginTokenDigest())));
        entity.setLeaseStatus(InstanceLeaseStatusEnum.ACTIVE);
        entity.setLeaseExpiresAt(now.plusSeconds(ttl));
        entity.setLastHeartbeatAt(now);
        baseMapper.updateById(entity);
        // 执行页上线：把该实例已到期、仍在等待浏览器的任务提升为 READY（未到期的不动，避免提前执行）
        taskMapper.lambdaUpdate()
            .set(ComfyTaskDO::getStatus, TaskStatusEnum.READY)
            .eq(ComfyTaskDO::getInstanceId, instanceId)
            .eq(ComfyTaskDO::getStatus, TaskStatusEnum.WAITING_BROWSER)
            .le(ComfyTaskDO::getScheduledAtUtc, now)
            .update();
        return toLeaseResp(entity);
    }

    @Override
    public ComfyLeaseResp renewLease(Long instanceId, ComfyLeaseReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyInstanceDO entity = baseMapper.selectById(instanceId);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), userId, "无权操作该实例");
        CheckUtils.throwIfNotEqual(entity.getLeaseStatus(), InstanceLeaseStatusEnum.ACTIVE, "实例租约未激活");
        CheckUtils.throwIfNotEqual(entity.getPageUuid(), req.getPageUuid(), "执行页不一致");
        CheckUtils.throwIfNotEqual(entity.getDeviceUuid(), req.getDeviceUuid(), "设备不一致");
        LocalDateTime now = ComfyScheduleTimeCalculator.nowUtc();
        CheckUtils.throwIf(entity.getLeaseExpiresAt() != null && entity.getLeaseExpiresAt().isBefore(now), "租约已过期");
        int ttl = req.getTtlSeconds() == null ? DEFAULT_TTL_SECONDS : req.getTtlSeconds();
        entity.setLeaseExpiresAt(now.plusSeconds(ttl));
        entity.setLastHeartbeatAt(now);
        baseMapper.updateById(entity);
        return toLeaseResp(entity);
    }

    @Override
    public ComfyLeaseResp stopLease(Long instanceId) {
        Long userId = UserContextHolder.getUserId();
        ComfyInstanceDO entity = baseMapper.selectById(instanceId);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), userId, "无权操作该实例");
        entity.setLeaseStatus(InstanceLeaseStatusEnum.OFFLINE);
        entity.setLeaseExpiresAt(null);
        baseMapper.updateById(entity);
        return toLeaseResp(entity);
    }

    @Override
    public void heartbeat(Long instanceId) {
        Long userId = UserContextHolder.getUserId();
        ComfyInstanceDO entity = baseMapper.selectById(instanceId);
        CheckUtils.throwIfNull(entity, "实例不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), userId, "无权操作该实例");
        entity.setLastHeartbeatAt(ComfyScheduleTimeCalculator.nowUtc());
        baseMapper.updateById(entity);
    }

    @Override
    protected QueryWrapper<ComfyInstanceDO> buildQueryWrapper(ComfyInstanceQuery query) {
        QueryWrapper<ComfyInstanceDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }

    private ComfyLeaseResp toLeaseResp(ComfyInstanceDO entity) {
        ComfyLeaseResp resp = new ComfyLeaseResp();
        resp.setInstanceId(entity.getId());
        resp.setEpoch(entity.getLeaseEpoch());
        resp.setLeaseStatus(entity.getLeaseStatus());
        resp.setLeaseExpiresAt(entity.getLeaseExpiresAt());
        return resp;
    }
}
