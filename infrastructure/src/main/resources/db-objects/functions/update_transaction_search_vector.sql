CREATE OR REPLACE FUNCTION public.update_transaction_search_vector()
              RETURNS trigger
              LANGUAGE plpgsql
              AS $function$
BEGIN
    NEW.search_vector :=
    to_tsvector('english',
        coalesce(NEW.narration, '') || ' ' ||
        coalesce(NEW.bank_tran_ref, '') || ' ' ||
        coalesce(NEW.payer_name, '') || ' ' ||
        coalesce((SELECT client_id FROM client WHERE client.id = NEW.client_id), '') || ' ' ||
        coalesce(NEW.payer_phone, '')
    );
RETURN NEW;
END
$function$
;