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

package top.continew.admin.system.mapper.comfy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import top.continew.admin.system.model.entity.comfy.ComfyTaskDO;
import top.continew.starter.data.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * ComfyUI 任务 Mapper
 *
 * @author weilai
 * @since 2026/10/5
 */
public interface ComfyTaskMapper extends BaseMapper<ComfyTaskDO> {

    /**
     * 统计计划下等待中的任务数量
     *
     * @param scheduleId 计划ID
     * @return 等待中任务数量
     */
    @Select("SELECT COUNT(1) FROM comfy_task WHERE schedule_id = #{scheduleId} AND status IN (1, 2, 3)")
    long countWaitingByScheduleId(@Param("scheduleId") Long scheduleId);

    /**
     * 查询计划下最老等待任务的触发时间
     *
     * @param scheduleId 计划ID
     * @return 最老等待任务的触发时间（UTC）
     */
    @Select("SELECT MIN(scheduled_at_utc) FROM comfy_task WHERE schedule_id = #{scheduleId} AND status IN (1, 2, 3)")
    LocalDateTime oldestWaitingScheduledAtUtc(@Param("scheduleId") Long scheduleId);
}
