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
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * ComfyUI 任务管理 API（领取/提交意图/状态回报/对账/取消/重试）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Tag(name = "ComfyUI 任务管理 API")
@Validated
@RestController
@CrudRequestMapping(value = "/comfy/task", api = {Api.PAGE, Api.GET, Api.BATCH_DELETE})
public class ComfyTaskController
    extends BaseController<ComfyTaskService, ComfyTaskResp, ComfyTaskResp, ComfyTaskQuery, ComfyTaskReq> {

    @Operation(summary = "领取任务", description = "校验执行页、设备与租约 epoch 后领取任务，返回领取令牌")
    @SaCheckPermission("comfy:task:claim")
    @PostMapping("/{id}/claim")
    public ComfyTaskClaimResp claim(@PathVariable Long id, @RequestBody @Valid ComfyClaimReq req) {
        return baseService.claim(id, req);
    }

    @Operation(summary = "记录提交意图", description = "记录 SUBMITTING 与实际 prompt 后，浏览器才可请求 ComfyUI")
    @SaCheckPermission("comfy:task:submitIntent")
    @PostMapping("/{id}/submit-intent")
    public void submitIntent(@PathVariable Long id, @RequestBody @Valid ComfySubmitIntentReq req) {
        baseService.submitIntent(id, req);
    }

    @Operation(summary = "回报任务状态", description = "幂等回报，终态不被旧进度回退")
    @SaCheckPermission("comfy:task:report")
    @PostMapping("/{id}/report")
    public void report(@PathVariable Long id, @RequestBody @Valid ComfyReportReq req) {
        baseService.report(id, req);
    }

    @Operation(summary = "对账任务", description = "可信迟到回执，独立补充证据，不重新授予旧页发送权")
    @SaCheckPermission("comfy:task:reconcile")
    @PostMapping("/{id}/reconcile")
    public void reconcile(@PathVariable Long id, @RequestBody @Valid ComfyReconcileReq req) {
        baseService.reconcile(id, req);
    }

    @Operation(summary = "取消任务", description = "未提交直接取消，已提交仅记录取消请求")
    @SaCheckPermission("comfy:task:cancel")
    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id, @RequestBody @Valid ComfyCancelReq req) {
        baseService.cancel(id, req);
    }

    @Operation(summary = "重试任务", description = "仅未知状态可重试，需确认可能重复生成")
    @SaCheckPermission("comfy:task:retry")
    @PostMapping("/{id}/retry")
    public void retry(@PathVariable Long id, @RequestBody @Valid ComfyRetryReq req) {
        baseService.retry(id, req);
    }
}
