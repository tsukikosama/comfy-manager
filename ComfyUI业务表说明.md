# ComfyUI 业务表说明（精简版）

版本 v0.3 / MySQL 8.0 / 2026-10-05。Luna起草，主代理复核。

## 表关系

| 表 | 功能 |
|---|---|
| comfy_instance | 地址、设备、当前页面执行租约 |
| comfy_workflow | 原始JSON、API prompt及参数映射 |
| comfy_schedule | 用户计划、完整执行快照及补建游标 |
| comfy_task_batch | 计划触发记录和手动批次 |
| comfy_task | 批次内独立任务及固定实际参数 |
| comfy_task_attempt | 提交、重试、响应丢失的恢复证据 |
| comfy_task_output | 本地结果引用及可用状态 |

计划 → 批次 → 任务 → 提交尝试 → 输出。手动批次不需要计划。设备/session/租约并入instance，节点映射并入workflow，素材并入JSON，规则/工作流版本以执行快照替代。

用户、权限、字典、日志及SnailJob平台表复用ContiNew，不在本SQL创建。

## 调度和事务

公共SnailJob周期调用业务扫描ServiceImpl，只创建任务，不调用本地ComfyUI。平台启动之外仍需实现执行器、配置并启用公共触发规则。

扫描事务同时创建批次、全部任务及推进游标。唯一键：(schedule_id,scheduled_at_utc)、(user_id,request_key)、(task_batch_id,item_index)。NULL schedule_id允许多个手动批次。

计划保存完整execution_snapshot_json；补建不能读取后来编辑的workflow当前内容。任务再冻结最终prompt和种子。快照不可覆盖，状态和游标可更新。

未物化到期区间存在时，第一版拒绝编辑/暂停/归档；先补建或用户明确跳过。容量超限保留next_run与游标，不能跳到当前时刻。

领取及提交前验证可信登录、设备、页面UUID、租约epoch和令牌摘要。SUBMITTING/UNKNOWN不能因租约到期重提。task_attempt的最后序号和状态机用于幂等回报；可信迟到证据通过独立对账事务处理。

## 使用约定

- SQL文件是新的7表建库方案，不是从旧18表自动迁移的脚本；不执行DROP或清空数据。
- 在数据库工具先选择目标库，再执行comfy_business_mysql8.sql；没有IF NOT EXISTS，已有同名表会报错。
- MySQL DDL不能作为可整体回滚的事务；失败后先检查已创建结构，不盲目重跑。
- 主键默认有符号BIGINT自增，user_id须核对实际模板；不假定用户表名或建立物理外键。
- 全部连接用UTC。DATETIME(3)不会自动转换应用写入的时间；脚本SET时区不影响其他连接池。
- ServiceImpl事务校验关联、用户归属、状态、数量和JSON Schema。
- BINARY(32)存SHA-256原始字节，BINARY(16)存UUID原始字节；endpoint_hash的CHAR(64)才是十六进制文本。
- item_index及attempt_no从1开始；current_attempt_no未执行时为0。
- 输出通过task原目标快照读取，云端不保存文件；文件删除不改变SUCCEEDED。
- 复用框架业务日志记录跳过区间、迁移和重试；不另建审计表。
- 运行方式字典建议ONCE/DAILY/WEEKLY/INTERVAL，实际模板字典结构未知，本次不生成猜测INSERT。

已做字段、主键、索引引用与脚本结构检查，尚未在真实MySQL执行，也未实施Java/Vue功能。

[ContiNew定时任务依据](https://continew.top/docs/admin/backend/job.html)。
