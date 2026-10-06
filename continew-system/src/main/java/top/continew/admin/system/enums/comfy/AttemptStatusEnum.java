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
 * ComfyUI 任务提交尝试状态枚举
 *
 * @author weilai
 * @since 2026/10/5
 */
@Getter
@RequiredArgsConstructor
public enum AttemptStatusEnum implements BaseEnum<Integer> {

    /**
     * 发送中
     */
    SENDING(1, "发送中", UiConstants.COLOR_WARNING),

    /**
     * 成功
     */
    SUCCESS(2, "成功", UiConstants.COLOR_SUCCESS),

    /**
     * 失败
     */
    FAILED(3, "失败", UiConstants.COLOR_ERROR),

    /**
     * 未知
     */
    UNKNOWN(4, "未知", UiConstants.COLOR_WARNING);

    private final Integer value;
    private final String description;
    private final String color;
}
