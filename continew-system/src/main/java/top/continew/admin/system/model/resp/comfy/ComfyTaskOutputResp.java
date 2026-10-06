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
import top.continew.admin.system.enums.comfy.OutputAvailabilityEnum;
import top.continew.admin.system.enums.comfy.OutputCollectStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务输出信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务输出信息")
public class ComfyTaskOutputResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 所属任务ID
     */
    @Schema(description = "所属任务ID", example = "1")
    private Long taskId;

    /**
     * 所属批次ID
     */
    @Schema(description = "所属批次ID", example = "1")
    private Long taskBatchId;

    /**
     * 来源尝试ID
     */
    @Schema(description = "来源尝试ID", example = "1")
    private Long attemptId;

    /**
     * 输出文件名
     */
    @Schema(description = "输出文件名", example = "ComfyUI_0001.png")
    private String filename;

    /**
     * 输出子目录
     */
    @Schema(description = "输出子目录", example = "output")
    private String subfolder;

    /**
     * 输出类型（ComfyUI type）
     */
    @Schema(description = "输出类型", example = "output")
    private String type;

    /**
     * 产出节点ID
     */
    @Schema(description = "产出节点ID", example = "9")
    private String nodeId;

    /**
     * 原目标地址（从任务快照，不随实例变更改变）
     */
    @Schema(description = "原目标地址")
    private String endpointUrl;

    /**
     * 媒体信息
     */
    @Schema(description = "媒体信息")
    private String mediaInfoJson;

    /**
     * 收集状态
     */
    @Schema(description = "收集状态", example = "2")
    private OutputCollectStatusEnum collectStatus;

    /**
     * 可用性
     */
    @Schema(description = "可用性", example = "1")
    private OutputAvailabilityEnum availability;

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
