-- 1. Table: public.accounts

-- DROP TABLE IF EXISTS public.accounts;

CREATE TABLE IF NOT EXISTS public.accounts
(
    account_id uuid NOT NULL,
    company_id uuid NOT NULL,
    account_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    account_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    account_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    normal_balance character varying(10) COLLATE pg_catalog."default" NOT NULL,
    parent_id uuid,
    account_level smallint NOT NULL,
    is_header boolean NOT NULL DEFAULT false,
    is_postable boolean NOT NULL DEFAULT false,
    is_active boolean NOT NULL DEFAULT true,
    description text COLLATE pg_catalog."default",
    sort_order integer NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT accounts_pkey PRIMARY KEY (account_id),
    CONSTRAINT uq_accounts_company_code UNIQUE (company_id, account_code),
    CONSTRAINT fk_accounts_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_accounts_parent FOREIGN KEY (parent_id)
        REFERENCES public.accounts (account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_accounts_level CHECK (account_level >= 1 AND account_level <= 4),
    CONSTRAINT chk_accounts_type CHECK (account_type::text = ANY (ARRAY['ASSET'::character varying, 'LIABILITY'::character varying, 'EQUITY'::character varying, 'REVENUE'::character varying, 'EXPENSE'::character varying]::text[])),
    CONSTRAINT chk_accounts_normal_balance CHECK (normal_balance::text = ANY (ARRAY['DEBIT'::character varying, 'CREDIT'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.accounts
    OWNER to postgres;
-- Index: idx_accounts_company_id

-- DROP INDEX IF EXISTS public.idx_accounts_company_id;

CREATE INDEX IF NOT EXISTS idx_accounts_company_id
    ON public.accounts USING btree
    (company_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_accounts_company_postable

-- DROP INDEX IF EXISTS public.idx_accounts_company_postable;

CREATE INDEX IF NOT EXISTS idx_accounts_company_postable
    ON public.accounts USING btree
    (company_id ASC NULLS LAST, is_postable ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_accounts_parent_id

-- DROP INDEX IF EXISTS public.idx_accounts_parent_id;

CREATE INDEX IF NOT EXISTS idx_accounts_parent_id
    ON public.accounts USING btree
    (parent_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- 2. Table: public.bank_accounts

-- DROP TABLE IF EXISTS public.bank_accounts;

CREATE TABLE IF NOT EXISTS public.bank_accounts
(
    bank_account_id uuid NOT NULL DEFAULT gen_random_uuid(),
    account_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    account_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    account_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    bank_name character varying(100) COLLATE pg_catalog."default",
    bank_branch character varying(100) COLLATE pg_catalog."default",
    account_number character varying(50) COLLATE pg_catalog."default",
    account_holder character varying(150) COLLATE pg_catalog."default",
    currency character varying(10) COLLATE pg_catalog."default" DEFAULT 'IDR'::character varying,
    opening_balance numeric(18,2) DEFAULT 0,
    is_default boolean DEFAULT false,
    is_active boolean DEFAULT true,
    company_id uuid NOT NULL,
    account_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    user_id uuid,
    deleted_at timestamp without time zone,
    CONSTRAINT bank_accounts_pkey PRIMARY KEY (bank_account_id),
    CONSTRAINT fk_bank_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_account_type CHECK (account_type::text = ANY (ARRAY['CASH'::character varying, 'BANK'::character varying, 'EWALLET'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.bank_accounts
    OWNER to postgres;
-- Index: idx_bank_coa

-- DROP INDEX IF EXISTS public.idx_bank_coa;

CREATE INDEX IF NOT EXISTS idx_bank_coa
    ON public.bank_accounts USING btree
    (account_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_bank_company

-- DROP INDEX IF EXISTS public.idx_bank_company;

CREATE INDEX IF NOT EXISTS idx_bank_company
    ON public.bank_accounts USING btree
    (company_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- 3. Table: public.chart_of_accounts

-- DROP TABLE IF EXISTS public.chart_of_accounts;

CREATE TABLE IF NOT EXISTS public.chart_of_accounts
(
    account_id uuid NOT NULL,
    account_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    account_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    account_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    normal_balance character varying(10) COLLATE pg_catalog."default" NOT NULL,
    parent_id uuid,
    account_level smallint NOT NULL,
    is_header boolean NOT NULL DEFAULT false,
    is_postable boolean NOT NULL DEFAULT false,
    description text COLLATE pg_catalog."default",
    sort_order integer NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    company_id uuid,
    template_account_id uuid,
    CONSTRAINT chart_of_accounts_new_pkey PRIMARY KEY (account_id),
    CONSTRAINT chart_of_accounts_new_account_code_key UNIQUE (account_code),
    CONSTRAINT uk_coa_company_code UNIQUE (company_id, account_code),
    CONSTRAINT fk_chart_of_accounts_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_account_templates_level CHECK (account_level >= 1 AND account_level <= 4),
    CONSTRAINT chk_account_templates_normal_balance CHECK (normal_balance::text = ANY (ARRAY['DEBIT'::character varying, 'CREDIT'::character varying]::text[])),
    CONSTRAINT chk_account_templates_type CHECK (account_type::text = ANY (ARRAY['ASSET'::character varying, 'CONTRA_ASSET'::character varying, 'LIABILITY'::character varying, 'EQUITY'::character varying, 'REVENUE'::character varying, 'EXPENSE'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.chart_of_accounts
    OWNER to postgres;


-- Table: public.chart_of_accounts_templates

-- 4. DROP TABLE IF EXISTS public.chart_of_accounts_templates;

CREATE TABLE IF NOT EXISTS public.chart_of_accounts_templates
(
    template_account_id uuid NOT NULL,
    account_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    account_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    account_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    normal_balance character varying(10) COLLATE pg_catalog."default" NOT NULL,
    parent_template_id uuid,
    account_level smallint NOT NULL,
    is_header boolean NOT NULL DEFAULT false,
    is_postable boolean NOT NULL DEFAULT false,
    description text COLLATE pg_catalog."default",
    sort_order integer NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_templates_pkey PRIMARY KEY (template_account_id),
    CONSTRAINT uq_account_templates_code UNIQUE (account_code),
    CONSTRAINT fk_account_templates_parent FOREIGN KEY (parent_template_id)
        REFERENCES public.chart_of_accounts_templates (template_account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_account_templates_level CHECK (account_level >= 1 AND account_level <= 4),
    CONSTRAINT chk_account_templates_normal_balance CHECK (normal_balance::text = ANY (ARRAY['DEBIT'::character varying, 'CREDIT'::character varying]::text[])),
    CONSTRAINT chk_account_templates_type CHECK (account_type::text = ANY (ARRAY['ASSET'::character varying, 'CONTRA_ASSET'::character varying, 'LIABILITY'::character varying, 'EQUITY'::character varying, 'REVENUE'::character varying, 'EXPENSE'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.chart_of_accounts_templates
    OWNER to postgres;



-- Table: public.clients

-- 5. DROP TABLE IF EXISTS public.clients;

CREATE TABLE IF NOT EXISTS public.clients
(
    client_id uuid NOT NULL,
    client_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    client_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    client_type character varying(35) COLLATE pg_catalog."default",
    client_npwp text COLLATE pg_catalog."default",
    client_nik text COLLATE pg_catalog."default",
    client_address text COLLATE pg_catalog."default",
    client_city character varying(100) COLLATE pg_catalog."default",
    client_province character varying(100) COLLATE pg_catalog."default",
    client_postal_code character varying(10) COLLATE pg_catalog."default",
    client_country character varying(50) COLLATE pg_catalog."default" DEFAULT 'Indonesia'::character varying,
    client_email text COLLATE pg_catalog."default",
    client_contact_person character varying(50) COLLATE pg_catalog."default",
    client_contact_phone text COLLATE pg_catalog."default",
    client_is_active boolean DEFAULT true,
    company_id uuid NOT NULL,
    client_created_at timestamp without time zone,
    user_id uuid NOT NULL,
    client_updated_at timestamp without time zone,
    client_deleted_at timestamp without time zone,
    client_bank_name character varying(30) COLLATE pg_catalog."default",
    client_account_number text COLLATE pg_catalog."default",
    client_account_name character varying(50) COLLATE pg_catalog."default",
    client_is_pkp boolean DEFAULT false,
    client_nitku text COLLATE pg_catalog."default",
    CONSTRAINT clients_pkey PRIMARY KEY (client_id),
    CONSTRAINT clients_client_code_key UNIQUE (client_code),
    CONSTRAINT fk_clients_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_clients_users FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.clients
    OWNER to postgres;
-- Index: idx_unique_active_nik_clients

-- DROP INDEX IF EXISTS public.idx_unique_active_nik_clients;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_active_nik_clients
    ON public.clients USING btree
    (company_id ASC NULLS LAST, client_nik COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE client_is_active = true AND client_nik IS NOT NULL AND length(TRIM(BOTH FROM client_nik)) > 0;
-- Index: idx_unique_active_npwp_clients

-- DROP INDEX IF EXISTS public.idx_unique_active_npwp_clients;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_active_npwp_clients
    ON public.clients USING btree
    (company_id ASC NULLS LAST, client_npwp COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE client_is_active = true AND client_npwp IS NOT NULL AND length(TRIM(BOTH FROM client_npwp)) > 0;
-- Index: idx_unique_client_code

-- DROP INDEX IF EXISTS public.idx_unique_client_code;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_client_code
    ON public.clients USING btree
    (company_id ASC NULLS LAST, client_code COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;


-- 6. Table: public.coa_mapping

-- DROP TABLE IF EXISTS public.coa_mapping;

CREATE TABLE IF NOT EXISTS public.coa_mapping
(
    mapping_id uuid NOT NULL DEFAULT gen_random_uuid(),
    transaction_type character varying(50) COLLATE pg_catalog."default",
    payment_type character varying(20) COLLATE pg_catalog."default",
    debit_coa_id uuid NOT NULL,
    credit_coa_id uuid NOT NULL,
    description text COLLATE pg_catalog."default",
    company_id uuid,
    CONSTRAINT coa_mapping_pkey PRIMARY KEY (mapping_id),
    CONSTRAINT unique_transaction_payment_company UNIQUE (transaction_type, payment_type, company_id),
    CONSTRAINT fk_coa_mapping_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.coa_mapping
    OWNER to postgres;


-- 7. Table: public.coa_mapping_lines

-- DROP TABLE IF EXISTS public.coa_mapping_lines;

CREATE TABLE IF NOT EXISTS public.coa_mapping_lines
(
    mapping_line_id uuid NOT NULL DEFAULT gen_random_uuid(),
    mapping_id uuid NOT NULL,
    coa_id uuid NOT NULL,
    "position" character varying(10) COLLATE pg_catalog."default" NOT NULL,
    line_order integer DEFAULT 0,
    amount_type character varying(20) COLLATE pg_catalog."default",
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT coa_mapping_lines_pkey PRIMARY KEY (mapping_line_id),
    CONSTRAINT fk_mapping FOREIGN KEY (mapping_id)
        REFERENCES public.coa_mapping (mapping_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.coa_mapping_lines
    OWNER to postgres;


-- 8. Table: public.company

-- DROP TABLE IF EXISTS public.company;

CREATE TABLE IF NOT EXISTS public.company
(
    company_id uuid NOT NULL DEFAULT gen_random_uuid(),
    legal_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    company_code character varying(20) COLLATE pg_catalog."default",
    npwp character varying(30) COLLATE pg_catalog."default",
    nib character varying(30) COLLATE pg_catalog."default",
    address text COLLATE pg_catalog."default",
    city character varying(100) COLLATE pg_catalog."default",
    province character varying(100) COLLATE pg_catalog."default",
    postal_code character varying(10) COLLATE pg_catalog."default",
    country character varying(100) COLLATE pg_catalog."default",
    phone character varying(30) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default",
    company_type character varying(50) COLLATE pg_catalog."default",
    pkp boolean DEFAULT false,
    base_currency character varying(10) COLLATE pg_catalog."default",
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    CONSTRAINT company_pkey PRIMARY KEY (company_id),
    CONSTRAINT company_company_code_key UNIQUE (company_code)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.company
    OWNER to postgres;


-- 9. Table: public.employees

-- DROP TABLE IF EXISTS public.employees;

CREATE TABLE IF NOT EXISTS public.employees
(
    employee_id uuid NOT NULL,
    employee_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    employee_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    employee_nik text COLLATE pg_catalog."default",
    employee_npwp text COLLATE pg_catalog."default",
    employee_gender character varying(10) COLLATE pg_catalog."default",
    employee_birth_date date,
    employee_phone text COLLATE pg_catalog."default",
    employee_email character varying(100) COLLATE pg_catalog."default",
    employee_address text COLLATE pg_catalog."default",
    employee_department character varying(100) COLLATE pg_catalog."default",
    employee_job_position character varying(100) COLLATE pg_catalog."default",
    employee_employment_type character varying(50) COLLATE pg_catalog."default",
    employee_join_date date,
    employee_salary_type character varying(20) COLLATE pg_catalog."default",
    employee_basic_salary numeric(18,2) DEFAULT 0,
    employee_daily_wage numeric(18,2) DEFAULT 0,
    employee_bank_name character varying(100) COLLATE pg_catalog."default",
    employee_bank_account_number text COLLATE pg_catalog."default",
    employee_bank_account_name character varying(150) COLLATE pg_catalog."default",
    employee_is_active boolean DEFAULT true,
    company_id uuid NOT NULL,
    employee_created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id uuid NOT NULL,
    employee_updated_at timestamp without time zone,
    employee_deleted_at timestamp without time zone,
    CONSTRAINT employees_pkey PRIMARY KEY (employee_id),
    CONSTRAINT employees_employee_code_key UNIQUE (employee_code),
    CONSTRAINT fk_employees_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.employees
    OWNER to postgres;
-- Index: idx_employees_employee_code

-- DROP INDEX IF EXISTS public.idx_employees_employee_code;

CREATE INDEX IF NOT EXISTS idx_employees_employee_code
    ON public.employees USING btree
    (employee_code COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_employees_name

-- DROP INDEX IF EXISTS public.idx_employees_name;

CREATE INDEX IF NOT EXISTS idx_employees_name
    ON public.employees USING btree
    (employee_name COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_employees_pagination

-- DROP INDEX IF EXISTS public.idx_employees_pagination;

CREATE INDEX IF NOT EXISTS idx_employees_pagination
    ON public.employees USING btree
    (company_id ASC NULLS LAST, employee_created_at DESC NULLS FIRST, employee_id DESC NULLS FIRST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;


-- 10. Table: public.journal_entries

-- DROP TABLE IF EXISTS public.journal_entries;

CREATE TABLE IF NOT EXISTS public.journal_entries
(
    journal_id uuid NOT NULL DEFAULT gen_random_uuid(),
    journal_code character varying(30) COLLATE pg_catalog."default" NOT NULL,
    journal_date date NOT NULL,
    reference_type character varying(30) COLLATE pg_catalog."default",
    reference_id uuid,
    description text COLLATE pg_catalog."default",
    status character varying(20) COLLATE pg_catalog."default",
    is_posted boolean DEFAULT false,
    posted_at timestamp without time zone,
    company_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT journal_entries_pkey PRIMARY KEY (journal_id),
    CONSTRAINT journal_entries_journal_code_key UNIQUE (journal_code)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.journal_entries
    OWNER to postgres;

-- 11. Table: public.journal_lines

-- DROP TABLE IF EXISTS public.journal_lines;

CREATE TABLE IF NOT EXISTS public.journal_lines
(
    journal_line_id uuid NOT NULL DEFAULT gen_random_uuid(),
    journal_id uuid NOT NULL,
    coa_id uuid NOT NULL,
    debit numeric(18,2) DEFAULT 0,
    credit numeric(18,2) DEFAULT 0,
    description text COLLATE pg_catalog."default",
    project_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT journal_lines_pkey PRIMARY KEY (journal_line_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.journal_lines
    OWNER to postgres;

-- 12. Table: public.projects

-- DROP TABLE IF EXISTS public.projects;

CREATE TABLE IF NOT EXISTS public.projects
(
    project_id uuid NOT NULL DEFAULT gen_random_uuid(),
    project_code character varying(30) COLLATE pg_catalog."default" NOT NULL,
    project_po character varying(30) COLLATE pg_catalog."default" NOT NULL,
    name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    client_id uuid NOT NULL,
    project_type character varying(50) COLLATE pg_catalog."default",
    contract_value numeric(18,2) DEFAULT 0,
    location text COLLATE pg_catalog."default",
    company_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    user_id uuid NOT NULL,
    vat_rate numeric(5,2),
    po_date date,
    tax_type character varying(10) COLLATE pg_catalog."default" NOT NULL DEFAULT 'NON_PPN'::character varying,
    total_tax numeric(18,2),
    total_amount numeric(18,2),
    dpp numeric(18,2),
    CONSTRAINT projects_pkey PRIMARY KEY (project_id),
    CONSTRAINT projects_unique_code_per_company UNIQUE (company_id, project_code),
    CONSTRAINT fk_project_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT,
    CONSTRAINT fk_projects_client FOREIGN KEY (client_id)
        REFERENCES public.clients (client_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT projects_tax_type_check CHECK (tax_type::text = ANY (ARRAY['NON_PPN'::character varying::text, 'EXCLUSIVE'::character varying::text, 'INCLUSIVE'::character varying::text]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.projects
    OWNER to postgres;
-- Index: project_code_index

-- DROP INDEX IF EXISTS public.project_code_index;

CREATE INDEX IF NOT EXISTS project_code_index
    ON public.projects USING btree
    (project_code COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;


-- 13 Table: public.refresh_token

-- DROP TABLE IF EXISTS public.refresh_token;

CREATE TABLE IF NOT EXISTS public.refresh_token
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    token text COLLATE pg_catalog."default" NOT NULL,
    expired_at timestamp without time zone NOT NULL,
    ip_address character varying(100) COLLATE pg_catalog."default",
    user_agent character varying(255) COLLATE pg_catalog."default",
    device_id character varying(100) COLLATE pg_catalog."default",
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT refresh_token_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.refresh_token
    OWNER to postgres;
-- Index: idx_refresh_token

-- DROP INDEX IF EXISTS public.idx_refresh_token;

CREATE INDEX IF NOT EXISTS idx_refresh_token
    ON public.refresh_token USING btree
    (token COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- 14. Table: public.role_permissions

-- DROP TABLE IF EXISTS public.role_permissions;

CREATE TABLE IF NOT EXISTS public.role_permissions
(
    role_permission_id uuid NOT NULL DEFAULT gen_random_uuid(),
    role_id uuid NOT NULL,
    permission_id uuid NOT NULL,
    CONSTRAINT role_permissions_pkey PRIMARY KEY (role_permission_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.role_permissions
    OWNER to postgres;


-- 15. Table: public.roles

-- DROP TABLE IF EXISTS public.roles;

CREATE TABLE IF NOT EXISTS public.roles
(
    role_id uuid NOT NULL DEFAULT gen_random_uuid(),
    role_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    company_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT roles_pkey PRIMARY KEY (role_id),
    CONSTRAINT roles_role_name_key UNIQUE (role_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.roles
    OWNER to postgres;

-- 16. Table: public.suppliers

-- DROP TABLE IF EXISTS public.suppliers;

CREATE TABLE IF NOT EXISTS public.suppliers
(
    supplier_id uuid NOT NULL DEFAULT gen_random_uuid(),
    supplier_code character varying(20) COLLATE pg_catalog."default" NOT NULL,
    supplier_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    supplier_type character varying(50) COLLATE pg_catalog."default",
    supplier_npwp text COLLATE pg_catalog."default",
    supplier_address text COLLATE pg_catalog."default",
    supplier_city character varying(100) COLLATE pg_catalog."default",
    supplier_province character varying(100) COLLATE pg_catalog."default",
    supplier_postal_code character varying(10) COLLATE pg_catalog."default",
    supplier_country character varying(100) COLLATE pg_catalog."default" DEFAULT 'Indonesia'::character varying,
    supplier_email text COLLATE pg_catalog."default",
    supplier_contact_person character varying(100) COLLATE pg_catalog."default",
    supplier_contact_phone text COLLATE pg_catalog."default",
    supplier_payment_term_days integer DEFAULT 0,
    supplier_credit_limit numeric(18,2) DEFAULT 0,
    supplier_bank_name character varying(100) COLLATE pg_catalog."default",
    supplier_bank_account_number text COLLATE pg_catalog."default",
    supplier_bank_account_name character varying(150) COLLATE pg_catalog."default",
    supplier_pkp boolean,
    supplier_is_active boolean DEFAULT true,
    company_id uuid NOT NULL,
    supplier_created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    supplier_updated_at timestamp without time zone,
    user_id uuid,
    supplier_deleted_at timestamp without time zone,
    CONSTRAINT suppliers_pkey PRIMARY KEY (supplier_id),
    CONSTRAINT suppliers_supplier_code_key UNIQUE (supplier_code),
    CONSTRAINT fk_supplier_company FOREIGN KEY (company_id)
        REFERENCES public.company (company_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_supplier_users FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.suppliers
    OWNER to postgres;
-- Index: idx_supplier_company_code

-- DROP INDEX IF EXISTS public.idx_supplier_company_code;

CREATE INDEX IF NOT EXISTS idx_supplier_company_code
    ON public.suppliers USING btree
    (company_id ASC NULLS LAST, supplier_code COLLATE pg_catalog."default" DESC NULLS FIRST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_suppliers_company_created_id

-- DROP INDEX IF EXISTS public.idx_suppliers_company_created_id;

CREATE INDEX IF NOT EXISTS idx_suppliers_company_created_id
    ON public.suppliers USING btree
    (company_id ASC NULLS LAST, supplier_created_at DESC NULLS FIRST, supplier_id DESC NULLS FIRST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_unique_active_npwp_suppliers

-- DROP INDEX IF EXISTS public.idx_unique_active_npwp_suppliers;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_active_npwp_suppliers
    ON public.suppliers USING btree
    (company_id ASC NULLS LAST, supplier_npwp COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE supplier_is_active = true AND supplier_npwp IS NOT NULL AND length(TRIM(BOTH FROM supplier_npwp)) > 0;
-- Index: idx_unique_supplier_code

-- DROP INDEX IF EXISTS public.idx_unique_supplier_code;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_supplier_code
    ON public.suppliers USING btree
    (company_id ASC NULLS LAST, supplier_code COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;


-- 17. Table: public.user_roles

-- DROP TABLE IF EXISTS public.user_roles;

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_role_id uuid NOT NULL DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    role_id uuid NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_role_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_roles
    OWNER to postgres;

-- 18.Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id uuid NOT NULL DEFAULT gen_random_uuid(),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password text COLLATE pg_catalog."default" NOT NULL,
    full_name character varying(150) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default",
    phone character varying(30) COLLATE pg_catalog."default",
    is_active boolean DEFAULT true,
    company_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    roles character varying(30) COLLATE pg_catalog."default" DEFAULT 'OWNER'::character varying,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT users_username_key UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;