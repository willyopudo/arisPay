CREATE OR REPLACE PROCEDURE public.insert_client(IN p_client_name character varying, IN p_client_id_in character varying, IN p_client_email character varying, IN p_identifier_type character varying, IN p_client_phone character varying, IN p_status smallint, IN p_created_by character varying, IN p_company_id integer, OUT p_client_id character varying)
              LANGUAGE plpgsql
              AS $procedure$
              DECLARE
              v_last_id INTEGER;
v_company_code VARCHAR(10);
BEGIN
-- Start transaction (PostgreSQL automatically starts transaction with procedures)

-- Insert the client record
INSERT INTO client (client_name, client_id, client_email, identifier_type, client_phone, record_status, created_by, company_id, created_date, is_enabled)
    VALUES (p_client_name, p_client_id_in, p_client_email, p_identifier_type, p_client_phone, p_status, p_created_by, p_company_id, CURRENT_TIMESTAMP, 0)
        RETURNING id INTO v_last_id;

-- Get company code
SELECT code INTO v_company_code
FROM companies
WHERE id = p_company_id;

-- Generate client_id only if p_client_id_in is null or empty
IF p_client_id_in IS NULL OR p_client_id_in = '' THEN
        p_client_id := CONCAT(v_company_code, LPAD(v_last_id::TEXT, 6, '0'));

        -- Update the record
UPDATE client
SET client_id = p_client_id
WHERE id = v_last_id;
ELSE
        p_client_id := p_client_id_in;
END IF;

    -- PostgreSQL automatically commits when procedure completes successfully
END;
$procedure$
;