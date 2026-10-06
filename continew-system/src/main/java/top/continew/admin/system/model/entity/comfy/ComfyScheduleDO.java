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
import top.continew.admin.system.enums.comfy.RunModeEnum;
import top.continew.admin.system.enums.comfy.ScheduleStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * ComfyUI 用户计划实体（完整执行快照及补建游标）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_schedule")
public class ComfyScheduleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 固定目标实例ID
     */
    private Long instanceId;

    /**
     * 来源工作流ID
     */
    private Long workflowId;

    /**
     * 运行方式（ONCE/DAILY/WEEKLY/INTERVAL）
     */
    private RunModeEnum runMode;

    /**
     * IANA 时区
     */
    private String timezone;

    /**
     * 结构化运行规则
     */
    private String ruleJson;

    /**
     * 固定目标快照（原地址及设备）
     */
    private String fixedTargetJson;

    /**
     * 完整执行快照（API prompt+节点绑定+参数）
     */
    private String executionSnapshotJson;

    /**
     * 公共参数与逐项覆盖
     */
    private String paramsJson;

    /**
     * 素材引用
     */
    private String materialRefsJson;

    /**
     * 每次任务数量
     */
    private Integer taskCountPerRun;

    /**
     * 积压上限
     */
    private Integer backlogLimit;

    /**
     * 下一次计划触发时间（UTC）
     */
    private LocalDateTime nextRunAtUtc;

    /**
     * 补执行游标（UTC）
     */
    private LocalDateTime scanCursorUtc;

    /**
     * 是否存在未物化到期区间（存在时拒绝编辑/暂停/归档）
     */
    private Boolean hasUnmaterialized;

    /**
     * 状态
     */
    private ScheduleStatusEnum status;

    /**
     * 计划配置版本（递增）
     */
    private Long configRevision;
}
