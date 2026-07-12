ALTER TABLE public.monitors
    ADD COLUMN maintenance_start TIMESTAMPTZ,
    ADD COLUMN maintenance_end   TIMESTAMPTZ;