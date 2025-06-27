create trigger trg_update_transaction_search_vector before
    insert
    or
update
    on
    public.transactions for each row execute function update_transaction_search_vector()