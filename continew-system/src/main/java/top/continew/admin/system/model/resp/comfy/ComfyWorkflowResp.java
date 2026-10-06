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
import top.continew.admin.system.enums.comfy.WorkflowStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 工作流信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 工作流信息")
public class ComfyWorkflowResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 工作流名称
     */
    @Schema(description = "工作流名称", example = "SDXL 基础出图")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "正反向提示词出图")
    private String description;

    /**
     * 普通 UI workflow JSON
     */
    @Schema(description = "普通 UI workflow JSON")
    private String rawWorkflowJson;

    /**
     * API prompt JSON（执行格式）
     */
    @Schema(description = "API prompt JSON")
    private String apiPromptJson;

    /**
     * 参数绑定（nodeId/classType/inputName/类型）
     */
    @Schema(description = "参数绑定")
    private String nodeBindingsJson;

    /**
     * 工作流配置版本（递增）
     */
    @Schema(description = "配置版本", example = "0")
    private Long configRevision;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "2")
    private WorkflowStatusEnum status;

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
