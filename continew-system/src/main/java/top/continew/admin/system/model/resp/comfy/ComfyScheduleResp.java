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
import top.continew.admin.system.enums.comfy.RunModeEnum;
import top.continew.admin.system.enums.comfy.ScheduleStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 用户计划信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 用户计划信息")
public class ComfyScheduleResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 计划名称
     */
    @Schema(description = "计划名称", example = "每日头像")
    private String name;

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
     * 运行方式
     */
    @Schema(description = "运行方式", example = "DAILY")
    private RunModeEnum runMode;

    /**
     * IANA 时区
     */
    @Schema(description = "IANA 时区", example = "Asia/Shanghai")
    private String timezone;

    /**
     * 结构化运行规则
     */
    @Schema(description = "结构化运行规则")
    private String ruleJson;

    /**
     * 固定目标快照（原地址及设备）
     */
    @Schema(description = "固定目标快照")
    private String fixedTargetJson;

    /**
     * 完整执行快照（API prompt+节点绑定+参数）
     */
    @Schema(description = "完整执行快照")
    private String executionSnapshotJson;

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
     * 下一次计划触发时间（UTC）
     */
    @Schema(description = "下一次计划触发时间（UTC）")
    private LocalDateTime nextRunAtUtc;

    /**
     * 补执行游标（UTC）
     */
    @Schema(description = "补执行游标（UTC）")
    private LocalDateTime scanCursorUtc;

    /**
     * 是否存在未物化到期区间
     */
    @Schema(description = "是否存在未物化到期区间", example = "false")
    private Boolean hasUnmaterialized;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private ScheduleStatusEnum status;

    /**
     * 计划配置版本（递增）
     */
    @Schema(description = "配置版本", example = "0")
    private Long configRevision;

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
