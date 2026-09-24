ALTER TABLE company_sequences
DROP CONSTRAINT uq_company_sequence;

-- Lalu buat constraint/index yang sesuai:
CREATE UNIQUE INDEX uq_company_sequence_general
ON company_sequences (company_id, sequence_type)
WHERE parent_account_id IS NULL;

-- Untuk COA:
CREATE UNIQUE INDEX uq_company_sequence_coa
ON company_sequences (
    company_id,
    sequence_type,
    parent_account_id
)
WHERE parent_account_id IS NOT NULL;

