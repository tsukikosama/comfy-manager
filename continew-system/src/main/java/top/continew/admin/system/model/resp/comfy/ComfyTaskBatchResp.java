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
import top.continew.admin.system.enums.comfy.BatchStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务批次信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务批次信息")
public class ComfyTaskBatchResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 来源计划ID（手动批次为 NULL）
     */
    @Schema(description = "来源计划ID", example = "1")
    private Long scheduleId;

    /**
     * 目标实例ID
     */
    @Schema(description = "目标实例ID", example = "1")
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    @Schema(description = "来源工作流ID", example = "1")
    private Long workflowId;

    /**
     * 触发时刻（UTC）
     */
    @Schema(description = "触发时刻（UTC）")
    private LocalDateTime scheduledAtUtc;

    /**
     * 复制的运行规则
     */
    @Schema(description = "复制的运行规则")
    private String ruleJson;

    /**
     * 复制的完整执行快照
     */
    @Schema(description = "复制的完整执行快照")
    private String executionSnapshotJson;

    /**
     * 任务项数量
     */
    @Schema(description = "任务项数量", example = "1")
    private Integer taskCount;

    /**
     * 请求键（user_id+request_key 防重复创建）
     */
    @Schema(description = "请求键", example = "r-xxxx")
    private String requestKey;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private BatchStatusEnum status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
