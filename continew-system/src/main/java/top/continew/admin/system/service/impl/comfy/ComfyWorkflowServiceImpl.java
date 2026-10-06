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
import top.continew.admin.system.mapper.comfy.ComfyWorkflowMapper;
import top.continew.admin.system.model.entity.comfy.ComfyWorkflowDO;
import top.continew.admin.system.model.query.comfy.ComfyWorkflowQuery;
import top.continew.admin.system.model.req.comfy.ComfyWorkflowReq;
import top.continew.admin.system.model.resp.comfy.ComfyWorkflowResp;
import top.continew.admin.system.service.comfy.ComfyWorkflowService;
import top.continew.starter.core.util.validation.CheckUtils;

import java.util.List;

/**
 * ComfyUI 工作流业务实现
 *
 * @author weilai
 * @since 2026/10/5
 */
@Service
@RequiredArgsConstructor
public class ComfyWorkflowServiceImpl extends
    BaseServiceImpl<ComfyWorkflowMapper, ComfyWorkflowDO, ComfyWorkflowResp, ComfyWorkflowResp, ComfyWorkflowQuery, ComfyWorkflowReq>
    implements ComfyWorkflowService {

    @Override
    public Long create(ComfyWorkflowReq req) {
        Long userId = UserContextHolder.getUserId();
        ComfyWorkflowDO entity = BeanUtil.toBean(req, ComfyWorkflowDO.class);
        entity.setUserId(userId);
        entity.setConfigRevision(0L);
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public ComfyWorkflowResp get(Long id) {
        ComfyWorkflowDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "工作流不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权访问该工作流");
        return BeanUtil.toBean(entity, ComfyWorkflowResp.class);
    }

    @Override
    public void update(ComfyWorkflowReq req, Long id) {
        ComfyWorkflowDO entity = baseMapper.selectById(id);
        CheckUtils.throwIfNull(entity, "工作流不存在");
        CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权操作该工作流");
        if (req.getName() != null) {
            entity.setName(req.getName());
        }
        if (req.getDescription() != null) {
            entity.setDescription(req.getDescription());
        }
        if (req.getRawWorkflowJson() != null) {
            entity.setRawWorkflowJson(req.getRawWorkflowJson());
        }
        if (req.getApiPromptJson() != null) {
            entity.setApiPromptJson(req.getApiPromptJson());
        }
        if (req.getNodeBindingsJson() != null) {
            entity.setNodeBindingsJson(req.getNodeBindingsJson());
        }
        if (req.getStatus() != null) {
            entity.setStatus(req.getStatus());
        }
        baseMapper.updateById(entity);
    }

    @Override
    public void delete(List<Long> ids) {
        List<ComfyWorkflowDO> list = baseMapper.selectBatchIds(ids);
        for (ComfyWorkflowDO entity : list) {
            CheckUtils.throwIfNotEqual(entity.getUserId(), UserContextHolder.getUserId(), "无权删除该工作流");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    protected QueryWrapper<ComfyWorkflowDO> buildQueryWrapper(ComfyWorkflowQuery query) {
        QueryWrapper<ComfyWorkflowDO> wrapper = super.buildQueryWrapper(query);
        wrapper.eq("user_id", UserContextHolder.getUserId());
        return wrapper;
    }
}
