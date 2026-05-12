CREATE TABLE public.monitors
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    url              VARCHAR(500) NOT NULL,
    expected_status  INTEGER      NOT NULL DEFAULT 200,
    interval_seconds INTEGER      NOT NULL DEFAULT 60,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    callback_url     VARCHAR(500) NOT NULL,
    next_run_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE public.incidents
(
    id              BIGSERIAL PRIMARY KEY,
    monitor_id      BIGINT       NOT NULL,
    url             VARCHAR(500) NOT NULL,
    incident_type   VARCHAR(100) NOT NULL,
    expected_status INTEGER,
    actual_status   INTEGER,
    failure_reason  VARCHAR(255),
    callback_url    VARCHAR(500),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT fk_incidents_monitor
        FOREIGN KEY (monitor_id)
            REFERENCES public.monitors (id)
            ON DELETE CASCADE
);