CREATE TABLE monitor_check_results
(
    id             BIGSERIAL PRIMARY KEY,

    monitor_id     BIGINT    NOT NULL,

    timestamp      TIMESTAMP NOT NULL DEFAULT NOW(),

    success        BOOLEAN   NOT NULL,

    status_code    INT,

    latency_ms     BIGINT,

    failure_reason TEXT,

    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_monitor
        FOREIGN KEY (monitor_id)
            REFERENCES monitors (id)
            ON DELETE CASCADE
);