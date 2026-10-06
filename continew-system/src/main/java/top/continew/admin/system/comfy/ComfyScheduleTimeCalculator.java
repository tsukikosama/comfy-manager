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

package top.continew.admin.system.comfy;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import top.continew.admin.system.enums.comfy.RunModeEnum;
import top.continew.admin.system.model.entity.comfy.ComfyScheduleDO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * ComfyUI 计划时间计算工具（UTC 锚点，按用户时区解释运行规则）
 *
 * <p>
 * 规则 rule_json 结构（均为用户本地墙钟时间，最终换算为 UTC 存储）：
 * <ul>
 * <li>ONCE: {"onceAt":"yyyy-MM-dd'T'HH:mm:ss"}</li>
 * <li>DAILY: {"dailyTime":"HH:mm"}</li>
 * <li>WEEKLY: {"dailyTime":"HH:mm","weeklyDays":[1,3,5]}</li>
 * <li>INTERVAL: {"intervalMinutes":30,"intervalFrom":"yyyy-MM-dd'T'HH:mm:ss"}</li>
 * </ul>
 *
 * @author weilai
 * @since 2026/10/5
 */
public final class ComfyScheduleTimeCalculator {

    private static final int MAX_RUNS = 1000;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private ComfyScheduleTimeCalculator() {
    }

    /**
     * 计算下一次计划触发时间（UTC），无未来触发则返回 null
     *
     * @param schedule 计划
     * @param nowUtc   当前时间（UTC）
     * @return 下一次触发时间
     */
    public static LocalDateTime computeFirstRun(ComfyScheduleDO schedule, LocalDateTime nowUtc) {
        List<LocalDateTime> runs = generateRuns(schedule, nowUtc.minusSeconds(1), nowUtc.plusYears(10), 1);
        return runs.isEmpty() ? null : runs.get(0);
    }

    /**
     * 预览未来若干次触发时间（UTC）
     *
     * @param schedule 计划
     * @param count    次数
     * @param nowUtc   当前时间（UTC）
     * @return 触发时间列表
     */
    public static List<LocalDateTime> previewRuns(ComfyScheduleDO schedule, int count, LocalDateTime nowUtc) {
        return generateRuns(schedule, nowUtc.minusSeconds(1), nowUtc.plusYears(10), count);
    }

    /**
     * 生成 (fromExclusiveUtc, toUtc] 区间内的所有触发时间（UTC）
     *
     * @param schedule         计划
     * @param fromExclusiveUtc 起始（不含）
     * @param toUtc            结束（含）
     * @param max              最大条数
     * @return 触发时间列表
     */
    public static List<LocalDateTime> generateRuns(ComfyScheduleDO schedule, LocalDateTime fromExclusiveUtc,
        LocalDateTime toUtc, int max) {
        RunModeEnum mode = schedule.getRunMode();
        ZoneId zone = parseZone(schedule.getTimezone());
        JSONObject rule = JSONUtil.parseObj(schedule.getRuleJson() == null ? "{}" : schedule.getRuleJson());
        List<LocalDateTime> result = new ArrayList<>();
        if (mode == null) {
            return result;
        }
        switch (mode) {
            case ONCE -> generateOnce(rule, zone, fromExclusiveUtc, toUtc, result, max);
            case DAILY -> generateDaily(rule, zone, fromExclusiveUtc, toUtc, result, max);
            case WEEKLY -> generateWeekly(rule, zone, fromExclusiveUtc, toUtc, result, max);
            case INTERVAL -> generateInterval(rule, zone, fromExclusiveUtc, toUtc, result, max);
            default -> {
            }
        }
        return result;
    }

    private static void generateOnce(JSONObject rule, ZoneId zone, LocalDateTime fromExclusiveUtc, LocalDateTime toUtc,
        List<LocalDateTime> result, int max) {
        String onceAt = rule.getStr("onceAt");
        if (onceAt == null || result.size() >= max) {
            return;
        }
        LocalDateTime run = parseLocal(onceAt, zone);
        if (run != null && run.isAfter(fromExclusiveUtc) && !run.isAfter(toUtc)) {
            result.add(run);
        }
    }

    private static void generateDaily(JSONObject rule, ZoneId zone, LocalDateTime fromExclusiveUtc, LocalDateTime toUtc,
        List<LocalDateTime> result, int max) {
        LocalTime time = parseTime(rule.getStr("dailyTime", "00:00"));
        LocalDate cursor = fromExclusiveUtc.atZone(zone).toLocalDate();
        LocalDateTime candidate = cursor.atTime(time);
        if (!candidate.isAfter(fromExclusiveUtc)) {
            candidate = candidate.plusDays(1);
        }
        int guard = 0;
        while (!candidate.isAfter(toUtc) && result.size() < max && guard++ < MAX_RUNS) {
            result.add(candidate);
            candidate = candidate.plusDays(1);
        }
    }

    private static void generateWeekly(JSONObject rule, ZoneId zone, LocalDateTime fromExclusiveUtc, LocalDateTime toUtc,
        List<LocalDateTime> result, int max) {
        LocalTime time = parseTime(rule.getStr("dailyTime", "00:00"));
        List<Integer> days = new ArrayList<>();
        JSONArray arr = rule.getJSONArray("weeklyDays");
        if (arr != null) {
            days.addAll(arr.toList(Integer.class));
        }
        LocalDate cursor = fromExclusiveUtc.atZone(zone).toLocalDate();
        int guard = 0;
        while (result.size() < max && guard++ < MAX_RUNS * 7) {
            LocalDateTime candidate = cursor.atTime(time);
            if (candidate.isAfter(fromExclusiveUtc) && !candidate.isAfter(toUtc) && days.contains(candidate
                .getDayOfWeek().getValue())) {
                result.add(candidate);
            }
            cursor = cursor.plusDays(1);
            if (cursor.atTime(time).isAfter(toUtc)) {
                break;
            }
        }
    }

    private static void generateInterval(JSONObject rule, ZoneId zone, LocalDateTime fromExclusiveUtc,
        LocalDateTime toUtc, List<LocalDateTime> result, int max) {
        int minutes = rule.getInt("intervalMinutes", 30);
        if (minutes <= 0) {
            minutes = 30;
        }
        String from = rule.getStr("intervalFrom");
        LocalDateTime anchor = from == null ? fromExclusiveUtc : parseLocal(from, zone);
        if (anchor == null) {
            anchor = fromExclusiveUtc;
        }
        long steps = Duration.between(anchor, fromExclusiveUtc).toMinutes() / minutes;
        if (steps < 0) {
            steps = 0;
        }
        LocalDateTime candidate = anchor.plusMinutes(steps * minutes);
        if (!candidate.isAfter(fromExclusiveUtc)) {
            candidate = candidate.plusMinutes(minutes);
        }
        int guard = 0;
        while (!candidate.isAfter(toUtc) && result.size() < max && guard++ < MAX_RUNS) {
            result.add(candidate);
            candidate = candidate.plusMinutes(minutes);
        }
    }

    private static LocalDateTime parseLocal(String text, ZoneId zone) {
        if (text == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(text, FMT).atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalTime parseTime(String text) {
        if (text == null || text.isBlank()) {
            return LocalTime.MIDNIGHT;
        }
        try {
            return LocalTime.parse(text);
        } catch (DateTimeParseException e) {
            return LocalTime.MIDNIGHT;
        }
    }

    private static ZoneId parseZone(String timezone) {
        if (timezone == null || timezone.isBlank()) {
            return ZoneOffset.UTC;
        }
        try {
            return ZoneId.of(timezone);
        } catch (Exception e) {
            return ZoneOffset.UTC;
        }
    }

    /**
     * 当前 UTC 时间
     *
     * @return 当前 UTC 时间
     */
    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
