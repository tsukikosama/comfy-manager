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

package top.continew.admin.system.model.query.comfy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.system.enums.comfy.ScheduleStatusEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 用户计划查询条件
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 用户计划查询条件")
public class ComfyScheduleQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 计划名称
     */
    @Schema(description = "计划名称", example = "每日头像")
    private String name;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private ScheduleStatusEnum status;

    /**
     * 固定目标实例ID
     */
    @Schema(description = "固定目标实例ID", example = "1")
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    @Schema(description = "来源工作流ID", example = "1")
    private Long workflowId;

    /**
     * 用户 ID
     */
    @Schema(hidden = true)
    private Long userId;
}
