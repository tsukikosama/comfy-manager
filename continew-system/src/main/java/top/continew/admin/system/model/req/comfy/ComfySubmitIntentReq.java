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

package top.continew.admin.system.model.req.comfy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 任务提交意图请求（云端先记录 SUBMITTING 与实际 prompt，再允许浏览器请求 ComfyUI）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务提交意图请求")
public class ComfySubmitIntentReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实际提交的 prompt（冻结的最终 prompt）
     */
    @Schema(description = "实际提交的 prompt", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualPromptJson;

    /**
     * 请求键（防重复/乱序回报，同任务唯一）
     */
    @Schema(description = "请求键", example = "rk-xxxx", requiredMode = Schema.RequiredMode.REQUIRED)
    private String requestKey;

    /**
     * 请求UUID（原始 UUID 的十六进制文本）
     */
    @Schema(description = "请求UUID（十六进制）", example = "u-xxxx", requiredMode = Schema.RequiredMode.REQUIRED)
    private String requestUuid;
}
