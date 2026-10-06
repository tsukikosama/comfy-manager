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

package top.continew.admin.system.service.comfy;

import top.continew.admin.common.base.service.BaseService;
import top.continew.admin.system.model.entity.comfy.ComfyTaskDO;
import top.continew.admin.system.model.query.comfy.ComfyTaskQuery;
import top.continew.admin.system.model.req.comfy.ComfyCancelReq;
import top.continew.admin.system.model.req.comfy.ComfyClaimReq;
import top.continew.admin.system.model.req.comfy.ComfyReconcileReq;
import top.continew.admin.system.model.req.comfy.ComfyReportReq;
import top.continew.admin.system.model.req.comfy.ComfyRetryReq;
import top.continew.admin.system.model.req.comfy.ComfySubmitIntentReq;
import top.continew.admin.system.model.req.comfy.ComfyTaskReq;
import top.continew.admin.system.model.resp.comfy.ComfyTaskClaimResp;
import top.continew.admin.system.model.resp.comfy.ComfyTaskResp;
import top.continew.starter.data.service.IService;

/**
 * ComfyUI 任务业务接口
 *
 * @author weilai
 * @since 2026/10/5
 */
public interface ComfyTaskService
    extends BaseService<ComfyTaskResp, ComfyTaskResp, ComfyTaskQuery, ComfyTaskReq>, IService<ComfyTaskDO> {

    /**
     * 领取任务（验证用户/设备/页面/epoch/令牌，乐观锁条件更新）
     *
     * @param taskId 任务ID
     * @param req    领取请求
     * @return 领取响应
     */
    ComfyTaskClaimResp claim(Long taskId, ComfyClaimReq req);

    /**
     * 提交意图（先记录 SUBMITTING 与实际 prompt，再允许浏览器请求 ComfyUI）
     *
     * @param taskId 任务ID
     * @param req    提交意图请求
     */
    void submitIntent(Long taskId, ComfySubmitIntentReq req);

    /**
     * 状态回报（幂等，终态不被旧进度回退）
     *
     * @param taskId 任务ID
     * @param req    回报请求
     */
    void report(Long taskId, ComfyReportReq req);

    /**
     * 对账（可信迟到回执，独立事务补充证据）
     *
     * @param taskId 任务ID
     * @param req    对账请求
     */
    void reconcile(Long taskId, ComfyReconcileReq req);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @param req    取消请求
     */
    void cancel(Long taskId, ComfyCancelReq req);

    /**
     * 重试任务（UNKNOWN 手动新尝试）
     *
     * @param taskId 任务ID
     * @param req    重试请求
     */
    void retry(Long taskId, ComfyRetryReq req);
}
