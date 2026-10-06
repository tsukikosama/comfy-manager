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
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import top.continew.admin.common.base.model.entity.BaseDO;
import top.continew.admin.system.enums.comfy.AttemptSendIntentEnum;
import top.continew.admin.system.enums.comfy.AttemptStatusEnum;

import java.io.Serial;

/**
 * ComfyUI 任务提交尝试实体（提交、重试、响应丢失的恢复证据）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_task_attempt")
public class ComfyTaskAttemptDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 所属任务ID
     */
    private Long taskId;

    /**
     * 所属批次ID
     */
    private Long taskBatchId;

    /**
     * 尝试号（从1开始）
     */
    private Integer attemptNo;

    /**
     * 请求UUID（原始字节）
     */
    private byte[] requestUuid;

    /**
     * 请求键（防重复/乱序回报）
     */
    private String requestKey;

    /**
     * 实际提交的 prompt
     */
    private String actualPromptJson;

    /**
     * 发送意图（提交/重试/对账/取消）
     */
    private AttemptSendIntentEnum sendIntent;

    /**
     * 请求元数据
     */
    private String requestMetaJson;

    /**
     * 响应内容
     */
    private String responseJson;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 尝试状态
     */
    private AttemptStatusEnum status;

    /**
     * 乐观锁版本号
     */
    @Version
    private Long versionNo;
}
