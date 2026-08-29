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
