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

package top.continew.admin.system.model.entity.comfy;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import top.continew.admin.common.base.model.entity.BaseDO;
import top.continew.admin.system.enums.comfy.TaskStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务实体（批次内独立任务及固定实际参数）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_task")
public class ComfyTaskDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 来源计划ID
     */
    private Long scheduleId;

    /**
     * 所属批次ID
     */
    private Long taskBatchId;

    /**
     * 目标实例ID
     */
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    private Long workflowId;

    /**
     * 任务项序号（从1开始）
     */
    private Integer itemIndex;

    /**
     * 计划触发时间（UTC）
     */
    private LocalDateTime scheduledAtUtc;

    /**
     * 展开的实际参数、种子及素材引用
     */
    private String actualParamsJson;

    /**
     * 冻结的最终 prompt
     */
    private String finalPromptJson;

    /**
     * 原目标地址（从快照，不随实例变更改变）
     */
    private String endpointUrl;

    /**
     * 原目标设备UUID
     */
    private String targetDeviceUuid;

    /**
     * 任务状态
     */
    private TaskStatusEnum status;

    /**
     * 乐观锁版本号
     */
    @Version
    private Long versionNo;

    /**
     * 当前尝试号（未执行为0）
     */
    private Integer currentAttemptNo;

    /**
     * 领取令牌
     */
    private String claimToken;

    /**
     * 领取页UUID
     */
    private String claimPageUuid;

    /**
     * 领取设备UUID
     */
    private String claimDeviceUuid;

    /**
     * 领取时租约epoch
     */
    private Long claimEpoch;

    /**
     * 领取时间（UTC）
     */
    private LocalDateTime claimedAt;

    /**
     * 提交时间（UTC）
     */
    private LocalDateTime submittedAt;

    /**
     * ComfyUI promptId
     */
    private String promptId;

    /**
     * 完成时间（UTC）
     */
    private LocalDateTime finishedAt;
}
