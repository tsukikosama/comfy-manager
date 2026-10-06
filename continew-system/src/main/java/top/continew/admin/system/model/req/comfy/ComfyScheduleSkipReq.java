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
import java.time.LocalDateTime;

/**
 * ComfyUI 计划跳过未物化区间请求（仅此操作允许不创建对应批次）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 计划跳过未物化区间请求")
public class ComfyScheduleSkipReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 跳至该时刻（UTC，含）之前的区间不再物化
     */
    @Schema(description = "跳至该时刻（UTC）", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime untilUtc;

    /**
     * 跳过原因（记入业务日志）
     */
    @Schema(description = "跳过原因", example = "旧触发点已无意义")
    private String reason;
}
