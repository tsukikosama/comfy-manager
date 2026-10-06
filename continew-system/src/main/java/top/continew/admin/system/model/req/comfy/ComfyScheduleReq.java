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
import top.continew.admin.system.enums.comfy.RunModeEnum;
import top.continew.admin.system.enums.comfy.ScheduleStatusEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 用户计划创建/修改请求
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 用户计划创建/修改请求")
public class ComfyScheduleReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 计划名称
     */
    @Schema(description = "计划名称", example = "每日头像")
    private String name;

    /**
     * 固定目标实例ID
     */
    @Schema(description = "固定目标实例ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    @Schema(description = "来源工作流ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long workflowId;

    /**
     * 运行方式（ONCE/DAILY/WEEKLY/INTERVAL，后端白名单语义）
     */
    @Schema(description = "运行方式", example = "DAILY", requiredMode = Schema.RequiredMode.REQUIRED)
    private RunModeEnum runMode;

    /**
     * IANA 时区
     */
    @Schema(description = "IANA 时区", example = "Asia/Shanghai", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timezone;

    /**
     * 结构化运行规则
     */
    @Schema(description = "结构化运行规则")
    private String ruleJson;

    /**
     * 公共参数与逐项覆盖
     */
    @Schema(description = "公共参数与逐项覆盖")
    private String paramsJson;

    /**
     * 素材引用
     */
    @Schema(description = "素材引用")
    private String materialRefsJson;

    /**
     * 每次任务数量
     */
    @Schema(description = "每次任务数量", example = "1")
    private Integer taskCountPerRun;

    /**
     * 积压上限
     */
    @Schema(description = "积压上限", example = "1000")
    private Integer backlogLimit;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "4")
    private ScheduleStatusEnum status;

    /**
     * 用户 ID（框架自动填充，前端无需传递）
     */
    @Schema(hidden = true)
    private Long userId;
}
