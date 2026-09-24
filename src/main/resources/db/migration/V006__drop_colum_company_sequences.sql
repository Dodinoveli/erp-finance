ALTER TABLE company_sequences
DROP COLUMN IF EXISTS parent_account_id,
DROP COLUMN IF EXISTS current_sort_order;