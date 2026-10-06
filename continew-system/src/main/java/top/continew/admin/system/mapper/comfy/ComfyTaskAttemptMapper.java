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
import top.continew.admin.system.model.entity.comfy.ComfyTaskAttemptDO;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * ComfyUI 任务提交尝试 Mapper
 *
 * @author weilai
 * @since 2026/10/5
 */
public interface ComfyTaskAttemptMapper extends BaseMapper<ComfyTaskAttemptDO> {

    /**
     * 按请求键与任务ID查询尝试（用于幂等回报与对账）
     *
     * @param requestKey 请求键
     * @param taskId     所属任务ID
     * @return 尝试实体
     */
    @Select("""
        SELECT * FROM comfy_task_attempt WHERE request_key = #{requestKey} AND task_id = #{taskId}\
         AND deleted = 0 LIMIT 1""")
    ComfyTaskAttemptDO selectByRequestKey(@Param("requestKey") String requestKey, @Param("taskId") Long taskId);
}
