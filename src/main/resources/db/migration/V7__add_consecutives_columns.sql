ALTER TABLE monitors
    ADD COLUMN consecutive_failures  INT NOT NULL DEFAULT 0,
    ADD COLUMN consecutive_successes INT NOT NULL DEFAULT 0