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
import top.continew.admin.system.enums.comfy.AttemptSendIntentEnum;
import top.continew.admin.system.enums.comfy.AttemptStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ComfyUI 任务提交尝试信息
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@Schema(description = "ComfyUI 任务提交尝试信息")
public class ComfyTaskAttemptResp implements Serializable {

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
     * 尝试号（从1开始）
     */
    @Schema(description = "尝试号", example = "1")
    private Integer attemptNo;

    /**
     * 请求键（防重复/乱序回报）
     */
    @Schema(description = "请求键", example = "rk-xxxx")
    private String requestKey;

    /**
     * 实际提交的 prompt
     */
    @Schema(description = "实际提交的 prompt")
    private String actualPromptJson;

    /**
     * 发送意图（提交/重试/对账/取消）
     */
    @Schema(description = "发送意图", example = "1")
    private AttemptSendIntentEnum sendIntent;

    /**
     * 请求元数据
     */
    @Schema(description = "请求元数据")
    private String requestMetaJson;

    /**
     * 响应内容
     */
    @Schema(description = "响应内容")
    private String responseJson;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息", example = "连接超时")
    private String errorMsg;

    /**
     * 尝试状态
     */
    @Schema(description = "尝试状态", example = "1")
    private AttemptStatusEnum status;

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
