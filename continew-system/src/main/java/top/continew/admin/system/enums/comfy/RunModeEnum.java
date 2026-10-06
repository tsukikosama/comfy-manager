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
 * ComfyUI 计划运行方式枚举
 *
 * <p>
 * 字典只控制标签和表单，后端白名单策略决定真实运行语义，不执行任意表达式。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Getter
@RequiredArgsConstructor
public enum RunModeEnum implements BaseEnum<String> {

    /**
     * 单次
     */
    ONCE("ONCE", "单次", UiConstants.COLOR_PRIMARY),

    /**
     * 每日
     */
    DAILY("DAILY", "每日", UiConstants.COLOR_PRIMARY),

    /**
     * 每周
     */
    WEEKLY("WEEKLY", "每周", UiConstants.COLOR_PRIMARY),

    /**
     * 间隔
     */
    INTERVAL("INTERVAL", "间隔", UiConstants.COLOR_PRIMARY);

    private final String value;
    private final String description;
    private final String color;
}
