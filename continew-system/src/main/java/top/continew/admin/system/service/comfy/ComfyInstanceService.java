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

package top.continew.admin.system.service.comfy;

import top.continew.admin.common.base.service.BaseService;
import top.continew.admin.system.model.entity.comfy.ComfyInstanceDO;
import top.continew.admin.system.model.query.comfy.ComfyInstanceQuery;
import top.continew.admin.system.model.req.comfy.ComfyInstanceReq;
import top.continew.admin.system.model.req.comfy.ComfyLeaseReq;
import top.continew.admin.system.model.resp.comfy.ComfyInstanceResp;
import top.continew.admin.system.model.resp.comfy.ComfyLeaseResp;
import top.continew.starter.data.service.IService;

/**
 * ComfyUI 实例业务接口
 *
 * @author weilai
 * @since 2026/10/5
 */
public interface ComfyInstanceService
    extends BaseService<ComfyInstanceResp, ComfyInstanceResp, ComfyInstanceQuery, ComfyInstanceReq>,
    IService<ComfyInstanceDO> {

    /**
     * 获取实例执行租约（递增 epoch）
     *
     * @param instanceId 实例ID
     * @param req        租约请求
     * @return 租约信息
     */
    ComfyLeaseResp acquireLease(Long instanceId, ComfyLeaseReq req);

    /**
     * 续期实例执行租约（同一执行页，不递增 epoch）
     *
     * @param instanceId 实例ID
     * @param req        租约请求
     * @return 租约信息
     */
    ComfyLeaseResp renewLease(Long instanceId, ComfyLeaseReq req);

    /**
     * 停止实例执行租约
     *
     * @param instanceId 实例ID
     * @return 租约信息
     */
    ComfyLeaseResp stopLease(Long instanceId);

    /**
     * 上报实例心跳
     *
     * @param instanceId 实例ID
     */
    void heartbeat(Long instanceId);
}
