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
import top.continew.admin.system.enums.comfy.WorkflowStatusEnum;

import java.io.Serial;

/**
 * ComfyUI 工作流实体（原始JSON、API prompt 及参数映射）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_workflow")
public class ComfyWorkflowDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 普通 UI workflow JSON
     */
    private String rawWorkflowJson;

    /**
     * API prompt JSON（执行格式）
     */
    private String apiPromptJson;

    /**
     * 参数绑定（nodeId/classType/inputName/类型）
     */
    private String nodeBindingsJson;

    /**
     * 工作流配置版本（递增）
     */
    private Long configRevision;

    /**
     * 状态
     */
    private WorkflowStatusEnum status;
}
