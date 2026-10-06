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

package top.continew.admin.system.model.req.comfy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ComfyUI 实例创建/修改请求
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 实例创建/修改请求")
public class ComfyInstanceReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实例名称
     */
    @Schema(description = "实例名称", example = "客厅显卡机")
    private String name;

    /**
     * ComfyUI 地址（HTTP/HTTPS）
     */
    @Schema(description = "ComfyUI 地址", example = "http://192.168.1.50:8188")
    private String endpointUrl;

    /**
     * 设备UUID（站点存储随机标识，非硬件身份）
     */
    @Schema(description = "设备UUID", example = "a1b2c3d4-...")
    private String deviceUuid;

    /**
     * 用户 ID（框架自动填充，前端无需传递）
     */
    @Schema(hidden = true)
    private Long userId;
}
