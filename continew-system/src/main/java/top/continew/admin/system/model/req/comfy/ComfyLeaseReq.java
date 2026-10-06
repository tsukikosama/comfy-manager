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
 * ComfyUI 实例执行租约获取请求
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 实例执行租约获取请求")
public class ComfyLeaseReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前执行页UUID（启用本页执行时由前端生成）
     */
    @Schema(description = "执行页UUID", example = "p-xxxx", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pageUuid;

    /**
     * 设备UUID
     */
    @Schema(description = "设备UUID", example = "a1b2c3d4-...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceUuid;

    /**
     * 登录令牌摘要（SHA-256 十六进制文本，云端仅保存摘要）
     */
    @Schema(description = "登录令牌摘要（SHA-256 十六进制）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String loginTokenDigest;

    /**
     * 租约有效期（秒），默认 60
     */
    @Schema(description = "租约有效期（秒）", example = "60")
    private Integer ttlSeconds;
}
