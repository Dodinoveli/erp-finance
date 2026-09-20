ALTER TABLE company_sequences 
    ADD COLUMN  parent_account_id uuid,
    ADD COLUMN current_sort_order bigint NOT NULL DEFAULT 0;


ALTER TABLE company_sequences
        ADD CONSTRAINT fk_company_sequences_parent
        FOREIGN KEY (parent_account_id)
        REFERENCES public.chart_of_accounts(account_id);
