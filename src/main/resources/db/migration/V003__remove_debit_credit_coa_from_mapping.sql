ALTER TABLE public.coa_mapping
    DROP COLUMN IF EXISTS debit_coa_id,
    DROP COLUMN IF EXISTS credit_coa_id;