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
import lombok.Data;
import top.continew.admin.common.base.model.entity.BaseDO;
import top.continew.admin.system.enums.comfy.BatchStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务批次实体（计划触发记录和手动批次）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_task_batch")
public class ComfyTaskBatchDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 来源计划ID（手动批次为 NULL）
     */
    private Long scheduleId;

    /**
     * 目标实例ID
     */
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    private Long workflowId;

    /**
     * 触发时刻（UTC）
     */
    private LocalDateTime scheduledAtUtc;

    /**
     * 复制的运行规则
     */
    private String ruleJson;

    /**
     * 复制的完整执行快照
     */
    private String executionSnapshotJson;

    /**
     * 任务项数量
     */
    private Integer taskCount;

    /**
     * 请求键（user_id+request_key 防重复创建）
     */
    private String requestKey;

    /**
     * 状态
     */
    private BatchStatusEnum status;
}
