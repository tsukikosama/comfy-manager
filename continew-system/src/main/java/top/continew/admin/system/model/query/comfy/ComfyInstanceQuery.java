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

package top.continew.admin.system.model.query.comfy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.system.enums.comfy.InstanceLeaseStatusEnum;
import top.continew.admin.system.enums.comfy.InstanceStatusEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 实例查询条件
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 实例查询条件")
public class ComfyInstanceQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实例名称
     */
    @Schema(description = "实例名称", example = "客厅显卡机")
    private String name;

    /**
     * 实例状态
     */
    @Schema(description = "实例状态", example = "1")
    private InstanceStatusEnum status;

    /**
     * 执行租约状态
     */
    @Schema(description = "执行租约状态", example = "2")
    private InstanceLeaseStatusEnum leaseStatus;

    /**
     * 用户 ID
     */
    @Schema(hidden = true)
    private Long userId;
}
