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

package top.continew.admin.job;

import cn.hutool.extra.spring.SpringUtil;
import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.common.log.SnailJobLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.continew.admin.schedule.annotation.ConditionalOnEnabledScheduleJob;
import top.continew.admin.system.service.comfy.ComfyPlanScanService;
import top.continew.starter.core.constant.PropertiesConstants;
import top.continew.starter.extension.tenant.annotation.TenantIgnore;

/**
 * ComfyUI 计划扫描任务
 *
 * <p>
 * 扫描到期计划，把触发区间物化为任务批次与任务项。物化以 (schedule_id, scheduled_at_utc)
 * 与 (user_id, request_key) 唯一键做幂等，因此即使在多节点下被重复调度也不会产生重复批次。
 * </p>
 *
 * @author weilai
 * @since 2026/10/5
 */
@Slf4j
public class ComfyPlanScanJob {

    private ComfyPlanScanJob() {
    }

    /**
     * 扫描计划（未启用 Snail Job 时走 Spring 定时）
     */
    @Component
    @ConditionalOnProperty(prefix = "snail-job", name = PropertiesConstants.ENABLED, havingValue = "false")
    public static class Scheduler {

        /**
         * 定时扫描计划
         */
        @TenantIgnore
        @Scheduled(cron = "0 * * * * ?")
        public void scanWithSchedule() {
            log.info("定时任务 [ComfyUI 计划扫描] 开始执行。");
            scan();
            log.info("定时任务 [ComfyUI 计划扫描] 执行结束。");
        }
    }

    /**
     * 扫描计划（启用 Snail Job 时）
     */
    @Component
    @ConditionalOnEnabledScheduleJob
    public static class ScheduleJob {

        /**
         * 通过 Snail Job 定时扫描计划
         */
        @TenantIgnore
        @JobExecutor(name = "comfyPlanScan")
        public void scanWithScheduleJob() {
            SnailJobLog.REMOTE.info("定时任务 [ComfyUI 计划扫描] 开始执行。");
            scan();
            SnailJobLog.REMOTE.info("定时任务 [ComfyUI 计划扫描] 执行结束。");
        }
    }

    /**
     * 执行计划扫描
     */
    private static void scan() {
        SpringUtil.getBean(ComfyPlanScanService.class).scan();
    }
}
