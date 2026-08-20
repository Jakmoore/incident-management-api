ALTER TABLE public.monitors
    ADD COLUMN client_id VARCHAR(1000);

UPDATE public.monitors
SET client_id = 'dev_client_id';

ALTER TABLE public.monitors
    ALTER COLUMN client_id SET NOT NULL;