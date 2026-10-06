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

package top.continew.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.comfy.ComfyInstanceQuery;
import top.continew.admin.system.model.req.comfy.ComfyInstanceReq;
import top.continew.admin.system.model.req.comfy.ComfyLeaseReq;
import top.continew.admin.system.model.resp.comfy.ComfyInstanceResp;
import top.continew.admin.system.model.resp.comfy.ComfyLeaseResp;
import top.continew.admin.system.service.comfy.ComfyInstanceService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * ComfyUI 实例管理 API
 *
 * @author weilai
 * @since 2026/10/5
 */
@Tag(name = "ComfyUI 实例管理 API")
@Validated
@RestController
@CrudRequestMapping(value = "/comfy/instance",
    api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE})
public class ComfyInstanceController extends
    BaseController<ComfyInstanceService, ComfyInstanceResp, ComfyInstanceResp, ComfyInstanceQuery, ComfyInstanceReq> {

    @Operation(summary = "获取实例执行租约", description = "绑定当前执行页与设备，递增租约 epoch")
    @SaCheckPermission("comfy:instance:lease")
    @PostMapping("/{id}/lease/acquire")
    public ComfyLeaseResp acquireLease(@PathVariable Long id, @RequestBody @Valid ComfyLeaseReq req) {
        return baseService.acquireLease(id, req);
    }

    @Operation(summary = "续期实例执行租约", description = "同一执行页续期，不递增 epoch")
    @SaCheckPermission("comfy:instance:lease")
    @PostMapping("/{id}/lease/renew")
    public ComfyLeaseResp renewLease(@PathVariable Long id, @RequestBody @Valid ComfyLeaseReq req) {
        return baseService.renewLease(id, req);
    }

    @Operation(summary = "停止实例执行租约", description = "停止本实例执行，租约置为离线")
    @SaCheckPermission("comfy:instance:lease")
    @PostMapping("/{id}/lease/stop")
    public ComfyLeaseResp stopLease(@PathVariable Long id) {
        return baseService.stopLease(id);
    }

    @Operation(summary = "上报实例心跳", description = "刷新最后心跳时间")
    @SaCheckPermission("comfy:instance:heartbeat")
    @PostMapping("/{id}/heartbeat")
    public void heartbeat(@PathVariable Long id) {
        baseService.heartbeat(id);
    }
}
