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
 * ComfyUI 任务重试请求（UNKNOWN 手动新尝试，需用户确认可能重复生成）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务重试请求")
public class ComfyRetryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 新请求UUID（十六进制），同任务唯一
     */
    @Schema(description = "新请求UUID（十六进制）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String requestUuid;

    /**
     * 重新提交的实际 prompt（缺省复用冻结的最终 prompt）
     */
    @Schema(description = "实际提交的 prompt")
    private String actualPromptJson;
}
