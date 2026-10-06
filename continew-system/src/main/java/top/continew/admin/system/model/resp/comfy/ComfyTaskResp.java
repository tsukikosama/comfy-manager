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
import top.continew.admin.system.enums.comfy.TaskStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务信息")
public class ComfyTaskResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 来源计划ID
     */
    @Schema(description = "来源计划ID", example = "1")
    private Long scheduleId;

    /**
     * 所属批次ID
     */
    @Schema(description = "所属批次ID", example = "1")
    private Long taskBatchId;

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
     * 任务项序号（从1开始）
     */
    @Schema(description = "任务项序号", example = "1")
    private Integer itemIndex;

    /**
     * 计划触发时间（UTC）
     */
    @Schema(description = "计划触发时间（UTC）")
    private LocalDateTime scheduledAtUtc;

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

    /**
     * 原目标地址（从快照，不随实例变更改变）
     */
    @Schema(description = "原目标地址")
    private String endpointUrl;

    /**
     * 原目标设备UUID
     */
    @Schema(description = "原目标设备UUID")
    private String targetDeviceUuid;

    /**
     * 任务状态
     */
    @Schema(description = "任务状态", example = "2")
    private TaskStatusEnum status;

    /**
     * 乐观锁版本号
     */
    @Schema(description = "乐观锁版本号", example = "0")
    private Long versionNo;

    /**
     * 当前尝试号（未执行为0）
     */
    @Schema(description = "当前尝试号", example = "0")
    private Integer currentAttemptNo;

    /**
     * 领取令牌
     */
    @Schema(description = "领取令牌", example = "t-xxxx")
    private String claimToken;

    /**
     * 领取页UUID
     */
    @Schema(description = "领取页UUID", example = "p-xxxx")
    private String claimPageUuid;

    /**
     * 领取设备UUID
     */
    @Schema(description = "领取设备UUID", example = "a1b2c3d4-...")
    private String claimDeviceUuid;

    /**
     * 领取时租约epoch
     */
    @Schema(description = "领取时租约epoch", example = "1")
    private Long claimEpoch;

    /**
     * 领取时间（UTC）
     */
    @Schema(description = "领取时间（UTC）")
    private LocalDateTime claimedAt;

    /**
     * 提交时间（UTC）
     */
    @Schema(description = "提交时间（UTC）")
    private LocalDateTime submittedAt;

    /**
     * ComfyUI promptId
     */
    @Schema(description = "ComfyUI promptId", example = "p-abc")
    private String promptId;

    /**
     * 完成时间（UTC）
     */
    @Schema(description = "完成时间（UTC）")
    private LocalDateTime finishedAt;

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
