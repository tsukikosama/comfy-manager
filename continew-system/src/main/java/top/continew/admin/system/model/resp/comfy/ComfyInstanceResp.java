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
import top.continew.admin.system.enums.comfy.InstanceLeaseStatusEnum;
import top.continew.admin.system.enums.comfy.InstanceStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 实例信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 实例信息")
public class ComfyInstanceResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

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
     * 地址或设备变化时的配置版本（递增）
     */
    @Schema(description = "配置版本", example = "0")
    private Long configRevision;

    /**
     * 当前执行页UUID
     */
    @Schema(description = "当前执行页UUID", example = "p-xxxx")
    private String pageUuid;

    /**
     * 执行租约递增 epoch
     */
    @Schema(description = "租约 epoch", example = "1")
    private Long leaseEpoch;

    /**
     * 执行租约状态
     */
    @Schema(description = "执行租约状态", example = "2")
    private InstanceLeaseStatusEnum leaseStatus;

    /**
     * 租约到期时间（UTC）
     */
    @Schema(description = "租约到期时间")
    private LocalDateTime leaseExpiresAt;

    /**
     * 最后心跳时间（UTC）
     */
    @Schema(description = "最后心跳时间")
    private LocalDateTime lastHeartbeatAt;

    /**
     * 实例状态
     */
    @Schema(description = "实例状态", example = "1")
    private InstanceStatusEnum status;

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
