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

package top.continew.admin.system.enums.comfy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.continew.admin.common.constant.UiConstants;
import top.continew.starter.core.enums.BaseEnum;

/**
 * ComfyUI 输出收集状态枚举
 *
 * <p>
 * 输出收集状态独立于任务成功状态；无文件工作流也可能成功，收集失败重查不重新生成。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Getter
@RequiredArgsConstructor
public enum OutputCollectStatusEnum implements BaseEnum<Integer> {

    /**
     * 待收集
     */
    PENDING(1, "待收集", UiConstants.COLOR_DEFAULT),

    /**
     * 已收集
     */
    COMPLETE(2, "已收集", UiConstants.COLOR_SUCCESS),

    /**
     * 收集失败
     */
    ERROR(3, "收集失败", UiConstants.COLOR_ERROR);

    private final Integer value;
    private final String description;
    private final String color;
}
