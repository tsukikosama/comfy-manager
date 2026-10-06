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

package top.continew.admin.system.model.resp.comfy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 任务领取响应
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务领取响应")
public class ComfyTaskClaimResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @Schema(description = "任务ID", example = "1")
    private Long taskId;

    /**
     * 任务项序号
     */
    @Schema(description = "任务项序号", example = "1")
    private Integer itemIndex;

    /**
     * 领取令牌
     */
    @Schema(description = "领取令牌", example = "t-xxxx")
    private String claimToken;

    /**
     * 领取时租约 epoch
     */
    @Schema(description = "领取时租约 epoch", example = "1")
    private Long epoch;

    /**
     * 原目标地址（从快照）
     */
    @Schema(description = "原目标地址")
    private String endpointUrl;

    /**
     * 原目标设备UUID
     */
    @Schema(description = "原目标设备UUID")
    private String targetDeviceUuid;

    /**
     * 展开的实际参数、种子及素材引用
     */
    @Schema(description = "展开的实际参数")
    private String actualParamsJson;

    /**
     * 冻结的最终 prompt
     */
    @Schema(description = "冻结的最终 prompt")
    private String finalPromptJson;
}
