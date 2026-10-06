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
import top.continew.admin.system.enums.comfy.OutputAvailabilityEnum;
import top.continew.admin.system.enums.comfy.OutputCollectStatusEnum;

import java.io.Serial;

/**
 * ComfyUI 任务输出实体（本地结果引用及可用状态）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Data
@TableName(value = "comfy_task_output")
public class ComfyTaskOutputDO extends BaseDO {

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
     * 来源尝试ID
     */
    private Long attemptId;

    /**
     * 输出文件名
     */
    private String filename;

    /**
     * 输出子目录
     */
    private String subfolder;

    /**
     * 输出类型（ComfyUI type）
     */
    private String type;

    /**
     * 产出节点ID
     */
    private String nodeId;

    /**
     * 原目标地址（从任务快照，不随实例变更改变）
     */
    private String endpointUrl;

    /**
     * 媒体信息
     */
    private String mediaInfoJson;

    /**
     * 收集状态
     */
    private OutputCollectStatusEnum collectStatus;

    /**
     * 可用性
     */
    private OutputAvailabilityEnum availability;

    /**
     * 乐观锁版本号
     */
    @Version
    private Long versionNo;
}
