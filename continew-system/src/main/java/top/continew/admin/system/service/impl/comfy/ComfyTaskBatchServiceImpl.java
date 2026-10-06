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
import top.continew.admin.system.mapper.comfy.ComfyTaskBatchMapper;
import top.continew.admin.system.model.entity.comfy.ComfyTaskBatchDO;
import top.continew.admin.system.model.query.comfy.ComfyTaskBatchQuery;
import top.continew.admin.system.model.req.comfy.ComfyTaskBatchReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskBatchResp;
import top.continew.admin.system.service.comfy.ComfyTaskBatchService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.util.List;

/**
 * ComfyUI 任务批次业务实现
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyTaskBatchServiceImpl extends
    BaseServiceImpl<ComfyTaskBatchMapper, ComfyTaskBatchDO, ComfyTaskBatchResp, ComfyTaskBatchResp, ComfyTaskBatchQuery, ComfyTaskBatchReq>
    implements ComfyTaskBatchService {

    @Override
    public ComfyTaskBatchResp get(Long id) {
        ComfyTaskBatchDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "批次不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该批次");
        return BeanUtil.toBean(entity, ComfyTaskBatchResp.class);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyTaskBatchDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyTaskBatchDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该批次");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    protected QueryWrapper<ComfyTaskBatchDO> buildQueryWrapper(ComfyTaskBatchQuery query) {
        QueryWrapper<ComfyTaskBatchDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }
}
