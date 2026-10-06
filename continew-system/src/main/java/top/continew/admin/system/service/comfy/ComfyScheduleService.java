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
import top.continew.admin.system.model.entity.comfy.ComfyScheduleDO;
import top.continew.admin.system.model.query.comfy.ComfyScheduleQuery;
import top.continew.admin.system.model.req.comfy.ComfyScheduleReq;
import top.continew.admin.system.model.req.comfy.ComfyScheduleSkipReq;
import top.continew.admin.system.model.resp.comfy.ComfySchedulePreviewResp;
import top.continew.admin.system.model.resp.comfy.ComfyScheduleResp;
import top.continew.starter.data.service.IService;

/**
 * ComfyUI 用户计划业务接口
 *
 * @author weilai
 * @since 2026/10/5
 */
public interface ComfyScheduleService
    extends BaseService<ComfyScheduleResp, ComfyScheduleResp, ComfyScheduleQuery, ComfyScheduleReq>,
    IService<ComfyScheduleDO> {

    /**
     * 预览计划未来触发时间
     *
     * @param id 计划ID
     * @return 预览信息
     */
    ComfySchedulePreviewResp preview(Long id);

    /**
     * 跳过未物化到期区间（仅此操作允许不创建对应批次）
     *
     * @param id  计划ID
     * @param req 跳过请求
     */
    void skip(Long id, ComfyScheduleSkipReq req);
}
