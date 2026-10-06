-- liquibase formatted sql

-- changeset weilai:comfy-tables
-- comment ComfyUI 业务表（7 张）：实例/工作流/计划/批次/任务/尝试/输出

CREATE TABLE IF NOT EXISTS comfy_instance (
    id                  BIGSERIAL    PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    name                VARCHAR(100) NOT NULL,
    endpoint_url        VARCHAR(512) NOT NULL,
    endpoint_hash       CHAR(64)     NOT NULL,
    device_uuid         VARCHAR(64)  NOT NULL,
    config_revision     BIGINT       NOT NULL DEFAULT 0,
    page_uuid           VARCHAR(64),
    login_token_digest  BYTEA,
    lease_epoch         BIGINT       NOT NULL DEFAULT 0,
    lease_status        SMALLINT     NOT NULL DEFAULT 2,
    lease_expires_at    TIMESTAMP(3),
    last_heartbeat_at   TIMESTAMP(3),
    status              SMALLINT     NOT NULL DEFAULT 1,
    create_user         BIGINT,
    create_time         TIMESTAMP    NOT NULL,
    update_user         BIGINT,
    update_time         TIMESTAMP,
    deleted             BIGINT       NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uk_comfy_instance_user_endpoint ON comfy_instance (user_id, endpoint_hash);
CREATE INDEX idx_comfy_instance_user_lease ON comfy_instance (user_id, lease_status);
CREATE INDEX idx_comfy_instance_device ON comfy_instance (device_uuid);

CREATE TABLE IF NOT EXISTS comfy_workflow (
    id                  BIGSERIAL    PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    name                VARCHAR(100) NOT NULL,
    description         VARCHAR(255),
    raw_workflow_json   TEXT,
    api_prompt_json     TEXT         NOT NULL,
    node_bindings_json  TEXT,
    config_revision     BIGINT       NOT NULL DEFAULT 0,
    status              SMALLINT     NOT NULL DEFAULT 1,
    create_user         BIGINT,
    create_time         TIMESTAMP    NOT NULL,
    update_user         BIGINT,
    update_time         TIMESTAMP,
    deleted             BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_comfy_workflow_user ON comfy_workflow (user_id);
CREATE INDEX idx_comfy_workflow_status ON comfy_workflow (status);

CREATE TABLE IF NOT EXISTS comfy_schedule (
    id                    BIGSERIAL    PRIMARY KEY,
    user_id               BIGINT       NOT NULL,
    name                  VARCHAR(100) NOT NULL,
    instance_id           BIGINT       NOT NULL,
    workflow_id           BIGINT       NOT NULL,
    run_mode              VARCHAR(20)  NOT NULL,
    timezone              VARCHAR(64)  NOT NULL,
    rule_json             TEXT,
    fixed_target_json     TEXT,
    execution_snapshot_json TEXT       NOT NULL,
    params_json           TEXT,
    material_refs_json    TEXT,
    task_count_per_run    INT          NOT NULL DEFAULT 1,
    backlog_limit         INT          NOT NULL DEFAULT 1000,
    next_run_at_utc       TIMESTAMP(3),
    scan_cursor_utc       TIMESTAMP(3),
    has_unmaterialized    BOOLEAN      DEFAULT FALSE,
    status                SMALLINT     NOT NULL DEFAULT 4,
    config_revision       BIGINT       NOT NULL DEFAULT 0,
    create_user           BIGINT,
    create_time           TIMESTAMP    NOT NULL,
    update_user           BIGINT,
    update_time           TIMESTAMP,
    deleted               BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_comfy_schedule_user_status ON comfy_schedule (user_id, status);
CREATE INDEX idx_comfy_schedule_next_run ON comfy_schedule (status, next_run_at_utc);

CREATE TABLE IF NOT EXISTS comfy_task_batch (
    id                       BIGSERIAL    PRIMARY KEY,
    user_id                  BIGINT       NOT NULL,
    schedule_id              BIGINT,
    instance_id              BIGINT       NOT NULL,
    workflow_id              BIGINT       NOT NULL,
    scheduled_at_utc         TIMESTAMP(3) NOT NULL,
    rule_json                TEXT,
    execution_snapshot_json  TEXT         NOT NULL,
    task_count               INT          NOT NULL DEFAULT 0,
    request_key              VARCHAR(64),
    status                   SMALLINT     NOT NULL DEFAULT 1,
    create_user              BIGINT,
    create_time              TIMESTAMP    NOT NULL,
    update_user              BIGINT,
    update_time              TIMESTAMP,
    deleted                  BIGINT       NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uk_comfy_batch_schedule_scheduled ON comfy_task_batch (schedule_id, scheduled_at_utc);
CREATE UNIQUE INDEX uk_comfy_batch_user_request ON comfy_task_batch (user_id, request_key);
CREATE INDEX idx_comfy_batch_user_scheduled ON comfy_task_batch (user_id, scheduled_at_utc);

CREATE TABLE IF NOT EXISTS comfy_task (
    id                  BIGSERIAL    PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    schedule_id         BIGINT,
    task_batch_id       BIGINT       NOT NULL,
    instance_id         BIGINT       NOT NULL,
    workflow_id         BIGINT       NOT NULL,
    item_index          INT          NOT NULL DEFAULT 1,
    scheduled_at_utc    TIMESTAMP(3),
    actual_params_json  TEXT,
    final_prompt_json   TEXT,
    endpoint_url        VARCHAR(512),
    target_device_uuid  VARCHAR(64),
    status              SMALLINT     NOT NULL DEFAULT 1,
    version_no          BIGINT       NOT NULL DEFAULT 0,
    current_attempt_no  INT          NOT NULL DEFAULT 0,
    claim_token         VARCHAR(64),
    claim_page_uuid     VARCHAR(64),
    claim_device_uuid   VARCHAR(64),
    claim_epoch         BIGINT,
    claimed_at          TIMESTAMP(3),
    submitted_at        TIMESTAMP(3),
    prompt_id           VARCHAR(64),
    finished_at         TIMESTAMP(3),
    create_user         BIGINT,
    create_time         TIMESTAMP    NOT NULL,
    update_user         BIGINT,
    update_time         TIMESTAMP,
    deleted             BIGINT       NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uk_comfy_task_batch_item ON comfy_task (task_batch_id, item_index);
CREATE INDEX idx_comfy_task_user_status ON comfy_task (user_id, status);
CREATE INDEX idx_comfy_task_batch_status ON comfy_task (task_batch_id, status);
CREATE INDEX idx_comfy_task_status_scheduled ON comfy_task (status, scheduled_at_utc);

CREATE TABLE IF NOT EXISTS comfy_task_attempt (
    id                  BIGSERIAL    PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    task_id             BIGINT       NOT NULL,
    task_batch_id       BIGINT       NOT NULL,
    attempt_no          INT          NOT NULL DEFAULT 1,
    request_uuid        BYTEA,
    request_key         VARCHAR(64),
    actual_prompt_json  TEXT,
    send_intent         SMALLINT     NOT NULL DEFAULT 1,
    request_meta_json   TEXT,
    response_json       TEXT,
    error_msg           VARCHAR(512),
    status              SMALLINT     NOT NULL DEFAULT 1,
    version_no          BIGINT       NOT NULL DEFAULT 0,
    create_user         BIGINT,
    create_time         TIMESTAMP    NOT NULL,
    update_user         BIGINT,
    update_time         TIMESTAMP,
    deleted             BIGINT       NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uk_comfy_attempt_task_no ON comfy_task_attempt (task_id, attempt_no);
CREATE UNIQUE INDEX uk_comfy_attempt_request_key ON comfy_task_attempt (request_key);
CREATE INDEX idx_comfy_attempt_task ON comfy_task_attempt (task_id, attempt_no);

CREATE TABLE IF NOT EXISTS comfy_task_output (
    id                 BIGSERIAL    PRIMARY KEY,
    user_id            BIGINT       NOT NULL,
    task_id            BIGINT       NOT NULL,
    task_batch_id      BIGINT       NOT NULL,
    attempt_id         BIGINT,
    filename           VARCHAR(512),
    subfolder          VARCHAR(512),
    type               VARCHAR(64),
    node_id            VARCHAR(64),
    endpoint_url       VARCHAR(512),
    media_info_json    TEXT,
    collect_status     SMALLINT     NOT NULL DEFAULT 1,
    availability       SMALLINT     NOT NULL DEFAULT 1,
    version_no         BIGINT       NOT NULL DEFAULT 0,
    create_user         BIGINT,
    create_time         TIMESTAMP    NOT NULL,
    update_user         BIGINT,
    update_time         TIMESTAMP,
    deleted             BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_comfy_output_task ON comfy_task_output (task_id);
CREATE INDEX idx_comfy_output_user_task ON comfy_task_output (user_id, task_id);
CREATE INDEX idx_comfy_output_attempt ON comfy_task_output (attempt_id);
