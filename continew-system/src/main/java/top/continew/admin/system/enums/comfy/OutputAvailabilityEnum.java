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
 * ComfyUI 输出可用性枚举
 *
 * <p>
 * 文件删除不改变历史任务成功状态；输出通过任务原目标快照直接调用 ComfyUI /view。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Getter
@RequiredArgsConstructor
public enum OutputAvailabilityEnum implements BaseEnum<Integer> {

    /**
     * 可用
     */
    AVAILABLE(1, "可用", UiConstants.COLOR_SUCCESS),

    /**
     * 不可用（离线/设备不匹配）
     */
    UNAVAILABLE(2, "不可用", UiConstants.COLOR_WARNING),

    /**
     * 已删除
     */
    DELETED(3, "已删除", UiConstants.COLOR_ERROR);

    private final Integer value;
    private final String description;
    private final String color;
}
