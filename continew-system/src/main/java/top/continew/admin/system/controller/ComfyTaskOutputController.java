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

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.base.controller.BaseController;
import top.continew.admin.system.model.query.comfy.ComfyTaskOutputQuery;
import top.continew.admin.system.model.req.comfy.ComfyTaskOutputReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskOutputResp;
import top.continew.admin.system.service.comfy.ComfyTaskOutputService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * ComfyUI 任务输出管理 API（输出由回报收集流程写入，仅提供查询）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Tag(name = "ComfyUI 任务输出管理 API")
@Validated
@RestController
@CrudRequestMapping(value = "/comfy/taskOutput", api = {Api.PAGE, Api.GET})
public class ComfyTaskOutputController extends
    BaseController<ComfyTaskOutputService, ComfyTaskOutputResp, ComfyTaskOutputResp, ComfyTaskOutputQuery, ComfyTaskOutputReq> {
}
