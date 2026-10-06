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
import top.continew.admin.system.enums.comfy.InstanceLeaseStatusEnum;
import top.continew.admin.system.enums.comfy.InstanceStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * ComfyUI 实例实体（地址、设备、当前页面执行租约）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_instance")
public class ComfyInstanceDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 实例名称
     */
    private String name;

    /**
     * ComfyUI 地址（HTTP/HTTPS）
     */
    private String endpointUrl;

    /**
     * 地址哈希（SHA-256 十六进制文本）
     */
    private String endpointHash;

    /**
     * 设备UUID（站点存储随机标识，非硬件身份）
     */
    private String deviceUuid;

    /**
     * 地址或设备变化时的配置版本（递增）
     */
    private Long configRevision;

    /**
     * 当前执行页UUID（启用本页执行后写入）
     */
    private String pageUuid;

    /**
     * 登录令牌摘要（SHA-256 原始字节）
     */
    private byte[] loginTokenDigest;

    /**
     * 执行租约递增 epoch
     */
    private Long leaseEpoch;

    /**
     * 执行租约状态
     */
    private InstanceLeaseStatusEnum leaseStatus;

    /**
     * 租约到期时间（UTC）
     */
    private LocalDateTime leaseExpiresAt;

    /**
     * 最后心跳时间（UTC）
     */
    private LocalDateTime lastHeartbeatAt;

    /**
     * 实例状态
     */
    private InstanceStatusEnum status;
}
