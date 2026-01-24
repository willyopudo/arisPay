CREATE OR REPLACE FUNCTION public.generate_client_id()
              RETURNS trigger
              LANGUAGE plpgsql
              AS $function$
              DECLARE
              v_company_code VARCHAR(10);
v_padded_id VARCHAR(20);
BEGIN
    -- Retrieve company code from related companies table
    -- Only generate client_id if it is null
    IF NEW.client_id IS NULL THEN
SELECT code INTO v_company_code
FROM companies
WHERE id = NEW.company_id;

-- Pad the ID with leading zeros (6 digits)
v_padded_id := v_company_code || LPAD(NEW.id::TEXT, 6, '0');

    -- Update the client_id field
NEW.client_id := v_padded_id;
END IF;

RETURN NEW;
END;
$function$
;
