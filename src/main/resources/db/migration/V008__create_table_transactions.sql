CREATE TABLE transactions(
	transaction_id uuid not null ,
	company_id uuid not null,
	transaction_number character varying(15),
	transaction_type character varying(25),
	transaction_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
	project_id uuid not null,
	payment_type character varying(15),
	amount numeric(19,2),
	description text,
	status boolean default false,
	created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
	user_id uuid not null,
	CONSTRAINT pk_transaction PRIMARY KEY(transaction_id),
	CONSTRAINT fk_company FOREIGN KEY (company_id)
	REFERENCES company(company_id),
	CONSTRAINT fk_project FOREIGN KEY (project_id)
	REFERENCES projects(project_id),
	CONSTRAINT fk_users FOREIGN KEY (user_id)
	REFERENCES users(user_id)
);