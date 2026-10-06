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
import java.time.LocalDateTime;
import java.util.List;

/**
 * ComfyUI 计划时间预览
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 计划时间预览")
public class ComfySchedulePreviewResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 下一次计划触发时间（UTC）
     */
    @Schema(description = "下一次计划触发时间（UTC）")
    private LocalDateTime nextRunAtUtc;

    /**
     * 未来五次预览（UTC）
     */
    @Schema(description = "未来五次预览（UTC）")
    private List<LocalDateTime> upcomingRuns;

    /**
     * 最老等待任务触发时间（UTC）
     */
    @Schema(description = "最老等待任务触发时间（UTC）")
    private LocalDateTime oldestWaitingScheduledAtUtc;

    /**
     * 积压数量
     */
    @Schema(description = "积压数量", example = "3")
    private Long backlogCount;
}
