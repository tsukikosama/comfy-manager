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

package top.continew.admin.system.service.impl.comfy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.common.base.service.BaseServiceImpl;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.system.mapper.comfy.ComfyTaskAttemptMapper;
import top.continew.admin.system.model.entity.comfy.ComfyTaskAttemptDO;
import top.continew.admin.system.model.query.comfy.ComfyTaskAttemptQuery;
import top.continew.admin.system.model.req.comfy.ComfyTaskAttemptReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskAttemptResp;
import top.continew.admin.system.service.comfy.ComfyTaskAttemptService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.util.List;

/**
 * ComfyUI 任务提交尝试业务实现（尝试由回报流程写入，仅提供归属校验后的查询）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyTaskAttemptServiceImpl extends BaseServiceImpl<ComfyTaskAttemptMapper, ComfyTaskAttemptDO,
    ComfyTaskAttemptResp, ComfyTaskAttemptResp, ComfyTaskAttemptQuery, ComfyTaskAttemptReq>
    implements ComfyTaskAttemptService {

    @Override
    public ComfyTaskAttemptResp get(Long id) {
        ComfyTaskAttemptDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "提交尝试不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该提交尝试");
        return BeanUtil.toBean(entity, ComfyTaskAttemptResp.class);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyTaskAttemptDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyTaskAttemptDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该提交尝试");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    protected QueryWrapper<ComfyTaskAttemptDO> buildQueryWrapper(ComfyTaskAttemptQuery query) {
        QueryWrapper<ComfyTaskAttemptDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }
}
