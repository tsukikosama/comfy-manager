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
import top.continew.admin.system.mapper.comfy.ComfyTaskOutputMapper;
import top.continew.admin.system.model.entity.comfy.ComfyTaskOutputDO;
import top.continew.admin.system.model.query.comfy.ComfyTaskOutputQuery;
import top.continew.admin.system.model.req.comfy.ComfyTaskOutputReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskOutputResp;
import top.continew.admin.system.service.comfy.ComfyTaskOutputService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.util.List;

/**
 * ComfyUI 任务输出业务实现（输出由回报收集流程写入，仅提供归属校验后���查询）
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyTaskOutputServiceImpl extends
    BaseServiceImpl<ComfyTaskOutputMapper, ComfyTaskOutputDO, ComfyTaskOutputResp, ComfyTaskOutputResp, ComfyTaskOutputQuery, ComfyTaskOutputReq>
    implements ComfyTaskOutputService {

    @Override
    public ComfyTaskOutputResp get(Long id) {
        ComfyTaskOutputDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "任务输出不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该任务输出");
        return BeanUtil.toBean(entity, ComfyTaskOutputResp.class);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyTaskOutputDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyTaskOutputDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该任务输出");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    protected QueryWrapper<ComfyTaskOutputDO> buildQueryWrapper(ComfyTaskOutputQuery query) {
        QueryWrapper<ComfyTaskOutputDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }
}
