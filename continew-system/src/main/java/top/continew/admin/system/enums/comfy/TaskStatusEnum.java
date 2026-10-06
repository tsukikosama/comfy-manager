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
 * ComfyUI 任务状态枚举
 *
 * <p>
 * 状态机：WAITING_BROWSER → READY → CLAIMED → SUBMITTING → SUBMITTED → RUNNING → SUCCEEDED/FAILED/UNKNOWN；
 * 终态不可被旧进度回退；UNKNOWN 需通过对账补充证据。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum implements BaseEnum<Integer> {

    /**
     * 等待浏览器领取
     */
    WAITING_BROWSER(1, "等待浏览器", UiConstants.COLOR_DEFAULT),

    /**
     * 就绪（可被领取）
     */
    READY(2, "就绪", UiConstants.COLOR_PRIMARY),

    /**
     * 已领取
     */
    CLAIMED(3, "已领取", UiConstants.COLOR_PRIMARY),

    /**
     * 提交中（云端已记录实际 prompt，等待浏览器请求 ComfyUI）
     */
    SUBMITTING(4, "提交中", UiConstants.COLOR_WARNING),

    /**
     * 已提交（ComfyUI 已返回 promptId）
     */
    SUBMITTED(5, "已提交", UiConstants.COLOR_PRIMARY),

    /**
     * 运行中
     */
    RUNNING(6, "运行中", UiConstants.COLOR_PRIMARY),

    /**
     * 成功
     */
    SUCCEEDED(7, "成功", UiConstants.COLOR_SUCCESS),

    /**
     * 失败
     */
    FAILED(8, "失败", UiConstants.COLOR_ERROR),

    /**
     * 未知（响应丢失，需对账）
     */
    UNKNOWN(9, "未知", UiConstants.COLOR_WARNING),

    /**
     * 对账中
     */
    RECONCILING(10, "对账中", UiConstants.COLOR_WARNING),

    /**
     * 已请求取消
     */
    CANCEL_REQUESTED(11, "取消请求", UiConstants.COLOR_WARNING),

    /**
     * 已取消
     */
    CANCELLED(12, "已取消", UiConstants.COLOR_DEFAULT),

    /**
     * 阻塞（容量超限）
     */
    BLOCKED(13, "阻塞", UiConstants.COLOR_ERROR);

    private final Integer value;
    private final String description;
    private final String color;
}
