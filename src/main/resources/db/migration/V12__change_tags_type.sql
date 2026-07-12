ALTER TABLE public.monitors
    ALTER COLUMN tag TYPE TEXT[]
        USING string_to_array(tag, ',');
