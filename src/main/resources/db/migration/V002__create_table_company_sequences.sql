CREATE TABLE company_sequences (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    sequence_type VARCHAR(50) NOT NULL,
    current_value BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uq_company_sequence
        UNIQUE (company_id, sequence_type)
);