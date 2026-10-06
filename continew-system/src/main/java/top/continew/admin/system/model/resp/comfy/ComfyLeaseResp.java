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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 实例执行租约信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 实例执行租约信息")
public class ComfyLeaseResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实例ID
     */
    @Schema(description = "实例ID", example = "1")
    private Long instanceId;

    /**
     * 执行租约递增 epoch
     */
    @Schema(description = "租约 epoch", example = "1")
    private Long epoch;

    /**
     * 执行租约状态
     */
    @Schema(description = "执行租约状态", example = "1")
    private InstanceLeaseStatusEnum leaseStatus;

    /**
     * 租约到期时间（UTC）
     */
    @Schema(description = "租约到期时间（UTC）")
    private LocalDateTime leaseExpiresAt;
}
