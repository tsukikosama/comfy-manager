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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.comfy.ComfyScheduleQuery;
import top.continew.admin.system.model.req.comfy.ComfyScheduleReq;
import top.continew.admin.system.model.req.comfy.ComfyScheduleSkipReq;
import top.continew.admin.system.model.resp.comfy.ComfySchedulePreviewResp;
import top.continew.admin.system.model.resp.comfy.ComfyScheduleResp;
import top.continew.admin.system.service.comfy.ComfyScheduleService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * ComfyUI 计划管理 API
 *
 * @author weilai
 * @since 2026/10/5
 */
@Tag(name = "ComfyUI 计划管理 API")
@Validated
@RestController
@CrudRequestMapping(value = "/comfy/schedule",
    api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE})
public class ComfyScheduleController extends
    BaseController<ComfyScheduleService, ComfyScheduleResp, ComfyScheduleResp, ComfyScheduleQuery, ComfyScheduleReq> {

    @Operation(summary = "预览计划触发时间", description = "返回下次触发时间、未来若干次触发与积压情况")
    @SaCheckPermission("comfy:schedule:preview")
    @GetMapping("/{id}/preview")
    public ComfySchedulePreviewResp preview(@PathVariable Long id) {
        return baseService.preview(id);
    }

    @Operation(summary = "跳过未物化到期区间", description = "推进补执行游标，放弃该区间不再物化")
    @SaCheckPermission("comfy:schedule:skip")
    @PostMapping("/{id}/skip")
    public void skip(@PathVariable Long id, @RequestBody @Valid ComfyScheduleSkipReq req) {
        baseService.skip(id, req);
    }
}
