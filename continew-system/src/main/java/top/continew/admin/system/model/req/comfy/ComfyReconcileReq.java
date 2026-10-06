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
import top.continew.admin.system.enums.comfy.AttemptStatusEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 任务对账请求（可信迟到回执，独立事务补充证据，不重新授予旧页发送权）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务对账请求")
public class ComfyReconcileReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求UUID（十六进制）
     */
    @Schema(description = "请求UUID（十六进制）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String requestUuid;

    /**
     * 尝试号
     */
    @Schema(description = "尝试号", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer attemptNo;

    /**
     * 尝试状态
     */
    @Schema(description = "尝试状态", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private AttemptStatusEnum status;

    /**
     * ComfyUI promptId
     */
    @Schema(description = "ComfyUI promptId", example = "p-abc")
    private String promptId;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMsg;

    /**
     * 响应内容
     */
    @Schema(description = "响应内容")
    private String responseJson;
}
