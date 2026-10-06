-- liquibase formatted sql

-- changeset weilai:comfy-menu
-- comment 初始化 ComfyUI 模块菜单与权限（权限码由 @CrudRequestMapping 路径推导，/comfy/taskBatch => comfy:taskBatch）

INSERT INTO `sys_menu`
(`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
-- 目录
(3000, 'ComfyUI 管理', 0, 1, '/comfy', 'Comfy', 'Layout', '/comfy/instance', 'robot', b'0', b'0', b'0', NULL, 3, 1, 1, NOW()),
-- 实例管理
(3010, '实例管理', 3000, 2, '/comfy/instance', 'ComfyInstance', 'comfy/instance/index', NULL, 'storage', b'0', b'0', b'0', NULL, 1, 1, 1, NOW()),
(3011, '列表', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:list', 1, 1, 1, NOW()),
(3012, '详情', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:get', 2, 1, 1, NOW()),
(3013, '新增', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:create', 3, 1, 1, NOW()),
(3014, '修改', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:update', 4, 1, 1, NOW()),
(3015, '删除', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:delete', 5, 1, 1, NOW()),
(3016, '执行租约', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:lease', 6, 1, 1, NOW()),
(3017, '心跳', 3010, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:instance:heartbeat', 7, 1, 1, NOW()),
-- 工作流管理
(3020, '工作流管理', 3000, 2, '/comfy/workflow', 'ComfyWorkflow', 'comfy/workflow/index', NULL, 'file', b'0', b'0', b'0', NULL, 2, 1, 1, NOW()),
(3021, '列表', 3020, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:workflow:list', 1, 1, 1, NOW()),
(3022, '详情', 3020, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:workflow:get', 2, 1, 1, NOW()),
(3023, '新增', 3020, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:workflow:create', 3, 1, 1, NOW()),
(3024, '修改', 3020, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:workflow:update', 4, 1, 1, NOW()),
(3025, '删除', 3020, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:workflow:delete', 5, 1, 1, NOW()),
-- 计划管理
(3030, '计划管理', 3000, 2, '/comfy/schedule', 'ComfySchedule', 'comfy/schedule/index', NULL, 'calendar', b'0', b'0', b'0', NULL, 3, 1, 1, NOW()),
(3031, '列表', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:list', 1, 1, 1, NOW()),
(3032, '详情', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:get', 2, 1, 1, NOW()),
(3033, '新增', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:create', 3, 1, 1, NOW()),
(3034, '修改', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:update', 4, 1, 1, NOW()),
(3035, '删除', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:delete', 5, 1, 1, NOW()),
(3036, '触发预览', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:preview', 6, 1, 1, NOW()),
(3037, '跳过区间', 3030, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:schedule:skip', 7, 1, 1, NOW()),
-- 任务批次
(3040, '任务批次', 3000, 2, '/comfy/taskBatch', 'ComfyTaskBatch', 'comfy/taskBatch/index', NULL, 'apps', b'0', b'0', b'0', NULL, 4, 1, 1, NOW()),
(3041, '列表', 3040, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:taskBatch:list', 1, 1, 1, NOW()),
(3042, '详情', 3040, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:taskBatch:get', 2, 1, 1, NOW()),
(3043, '删除', 3040, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:taskBatch:delete', 3, 1, 1, NOW()),
-- 任务管理
(3050, '任务管理', 3000, 2, '/comfy/task', 'ComfyTask', 'comfy/task/index', NULL, 'list', b'0', b'0', b'0', NULL, 5, 1, 1, NOW()),
(3051, '列表', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:list', 1, 1, 1, NOW()),
(3052, '详情', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:get', 2, 1, 1, NOW()),
(3053, '删除', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:delete', 3, 1, 1, NOW()),
(3054, '领取', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:claim', 4, 1, 1, NOW()),
(3055, '提交意图', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:submitIntent', 5, 1, 1, NOW()),
(3056, '状态回报', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:report', 6, 1, 1, NOW()),
(3057, '对账', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:reconcile', 7, 1, 1, NOW()),
(3058, '取消', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:cancel', 8, 1, 1, NOW()),
(3059, '重试', 3050, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:task:retry', 9, 1, 1, NOW()),
-- 任务输出
(3060, '任务输出', 3000, 2, '/comfy/taskOutput', 'ComfyTaskOutput', 'comfy/taskOutput/index', NULL, 'image', b'0', b'0', b'0', NULL, 6, 1, 1, NOW()),
(3061, '列表', 3060, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:taskOutput:list', 1, 1, 1, NOW()),
(3062, '详情', 3060, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'comfy:taskOutput:get', 2, 1, 1, NOW());
