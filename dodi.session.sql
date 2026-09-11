CREATE TABLE account_templates (
    template_account_id UUID PRIMARY KEY,

    account_code VARCHAR(20) NOT NULL,
    account_name VARCHAR(150) NOT NULL,

    account_type VARCHAR(20) NOT NULL,
    normal_balance VARCHAR(10) NOT NULL,

    parent_template_id UUID NULL,

    account_level SMALLINT NOT NULL,

    is_header BOOLEAN NOT NULL DEFAULT FALSE,
    is_postable BOOLEAN NOT NULL DEFAULT FALSE,

    description TEXT,

    sort_order INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_templates_parent
        FOREIGN KEY (parent_template_id)
        REFERENCES account_templates(template_account_id),

    CONSTRAINT uq_account_templates_code
        UNIQUE (account_code),

    CONSTRAINT chk_account_templates_level
        CHECK (account_level BETWEEN 1 AND 4),

    CONSTRAINT chk_account_templates_type
        CHECK (
            account_type IN (
                'ASSET',
                'LIABILITY',
                'EQUITY',
                'REVENUE',
                'EXPENSE'
            )
        ),

    CONSTRAINT chk_account_templates_normal_balance
        CHECK (
            normal_balance IN ('DEBIT', 'CREDIT')
        )
);



CREATE TABLE accounts (
    account_id UUID PRIMARY KEY,

    company_id UUID NOT NULL,

    account_code VARCHAR(20) NOT NULL,
    account_name VARCHAR(150) NOT NULL,

    account_type VARCHAR(20) NOT NULL,
    normal_balance VARCHAR(10) NOT NULL,

    parent_id UUID NULL,

    account_level SMALLINT NOT NULL,

    is_header BOOLEAN NOT NULL DEFAULT FALSE,
    is_postable BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    description TEXT,

    sort_order INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_accounts_company
        FOREIGN KEY (company_id)
        REFERENCES company(company_id),

    CONSTRAINT fk_accounts_parent
        FOREIGN KEY (parent_id)
        REFERENCES accounts(account_id),

    CONSTRAINT uq_accounts_company_code
        UNIQUE (company_id, account_code),

    CONSTRAINT chk_accounts_level
        CHECK (account_level BETWEEN 1 AND 4),

    CONSTRAINT chk_accounts_type
        CHECK (
            account_type IN (
                'ASSET',
                'LIABILITY',
                'EQUITY',
                'REVENUE',
                'EXPENSE'
            )
        ),

    CONSTRAINT chk_accounts_normal_balance
        CHECK (
            normal_balance IN ('DEBIT', 'CREDIT')
        )
);


CREATE INDEX idx_accounts_company_id
ON accounts(company_id);

CREATE INDEX idx_accounts_parent_id
ON accounts(parent_id);

CREATE INDEX idx_accounts_company_postable
ON accounts(company_id, is_postable);




INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
VALUES
(gen_random_uuid(), '10000', 'ASET', 'ASSET', 'DEBIT',
 NULL, 1, TRUE, FALSE, 100),

(gen_random_uuid(), '20000', 'LIABILITAS', 'LIABILITY', 'CREDIT',
 NULL, 1, TRUE, FALSE, 200),

(gen_random_uuid(), '30000', 'EKUITAS', 'EQUITY', 'CREDIT',
 NULL, 1, TRUE, FALSE, 300),

(gen_random_uuid(), '40000', 'PENDAPATAN', 'REVENUE', 'CREDIT',
 NULL, 1, TRUE, FALSE, 400),

(gen_random_uuid(), '50000', 'BEBAN', 'EXPENSE', 'DEBIT',
 NULL, 1, TRUE, FALSE, 500);


 select * from account_templates

 INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 2, TRUE, FALSE, v.sort_order
FROM (
    VALUES
    ('11000', 'ASET LANCAR', 'ASSET', 'DEBIT', 110),
    ('12000', 'ASET TIDAK LANCAR', 'ASSET', 'DEBIT', 120),
    ('21000', 'LIABILITAS JANGKA PENDEK', 'LIABILITY', 'CREDIT', 210),
    ('22000', 'LIABILITAS JANGKA PANJANG', 'LIABILITY', 'CREDIT', 220),
    ('31000', 'MODAL', 'EQUITY', 'CREDIT', 310),
    ('32000', 'LABA DITAHAN', 'EQUITY', 'CREDIT', 320),
    ('33000', 'PRIVE', 'EQUITY', 'DEBIT', 330),
    ('41000', 'PENDAPATAN USAHA', 'REVENUE', 'CREDIT', 410),
    ('42000', 'PENDAPATAN LAIN-LAIN', 'REVENUE', 'CREDIT', 420),
    ('51000', 'BEBAN PROYEK', 'EXPENSE', 'DEBIT', 510),
    ('52000', 'BEBAN OPERASIONAL', 'EXPENSE', 'DEBIT', 520)
) AS v(code, name, type, balance, sort_order)
JOIN account_templates p
    ON p.account_code = CASE
        WHEN v.code LIKE '1%' THEN '10000'
        WHEN v.code LIKE '2%' THEN '20000'
        WHEN v.code LIKE '3%' THEN '30000'
        WHEN v.code LIKE '4%' THEN '40000'
        WHEN v.code LIKE '5%' THEN '50000'
    END;



INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 3, TRUE, FALSE, v.sort_order
FROM (
    VALUES

    -- ASET LANCAR
    ('11100', 'KAS', 'ASSET', 'DEBIT', 111),
    ('11200', 'BANK', 'ASSET', 'DEBIT', 112),
    ('11300', 'PIUTANG', 'ASSET', 'DEBIT', 113),
    ('11400', 'PERSEDIAAN', 'ASSET', 'DEBIT', 114),
    ('11500', 'UANG MUKA', 'ASSET', 'DEBIT', 115),
    ('11600', 'BIAYA DIBAYAR DIMUKA', 'ASSET', 'DEBIT', 116),
    ('11700', 'PAJAK DIBAYAR DIMUKA', 'ASSET', 'DEBIT', 117),

    -- ASET TIDAK LANCAR
    ('12100', 'ASET TETAP', 'ASSET', 'DEBIT', 121),
    ('12200', 'AKUMULASI PENYUSUTAN', 'CONTRA_ASSET', 'CREDIT', 122),
    ('12300', 'ASET TAKBERWUJUD', 'ASSET', 'DEBIT', 123),

    -- LIABILITAS JANGKA PENDEK
    ('21100', 'UTANG USAHA', 'LIABILITY', 'CREDIT', 211),
    ('21200', 'UTANG PAJAK', 'LIABILITY', 'CREDIT', 212),
    ('21300', 'UANG MUKA PELANGGAN', 'LIABILITY', 'CREDIT', 213),
    ('21400', 'BIAYA MASIH HARUS DIBAYAR', 'LIABILITY', 'CREDIT', 214),
    ('21500', 'UTANG GAJI', 'LIABILITY', 'CREDIT', 215),
    ('21600', 'UTANG BPJS', 'LIABILITY', 'CREDIT', 216),

    -- LIABILITAS JANGKA PANJANG
    ('22100', 'UTANG BANK', 'LIABILITY', 'CREDIT', 221),
    ('22200', 'UTANG LEASING', 'LIABILITY', 'CREDIT', 222),

    -- EKUITAS
    ('31100', 'MODAL PEMILIK', 'EQUITY', 'CREDIT', 311),
    ('32100', 'LABA DITAHAN', 'EQUITY', 'CREDIT', 321),
    ('33100', 'PRIVE PEMILIK', 'EQUITY', 'DEBIT', 331),

    -- PENDAPATAN
    ('41100', 'PENDAPATAN PROYEK', 'REVENUE', 'CREDIT', 411),
    ('41200', 'PENDAPATAN JASA', 'REVENUE', 'CREDIT', 412),
    ('42100', 'PENDAPATAN BUNGA', 'REVENUE', 'CREDIT', 421),
    ('42200', 'PENDAPATAN LAINNYA', 'REVENUE', 'CREDIT', 422),

    -- BEBAN PROYEK
    ('51100', 'BEBAN MATERIAL', 'EXPENSE', 'DEBIT', 511),
    ('51200', 'BEBAN TENAGA KERJA', 'EXPENSE', 'DEBIT', 512),
    ('51300', 'BEBAN SUBKONTRAKTOR', 'EXPENSE', 'DEBIT', 513),
    ('51400', 'BEBAN SEWA ALAT', 'EXPENSE', 'DEBIT', 514),
    ('51500', 'BEBAN TRANSPORTASI PROYEK', 'EXPENSE', 'DEBIT', 515),
    ('51600', 'BEBAN PROYEK LAINNYA', 'EXPENSE', 'DEBIT', 516),

    -- BEBAN OPERASIONAL
    ('52100', 'BEBAN GAJI', 'EXPENSE', 'DEBIT', 521),
    ('52200', 'BEBAN LISTRIK DAN AIR', 'EXPENSE', 'DEBIT', 522),
    ('52300', 'BEBAN TELEKOMUNIKASI', 'EXPENSE', 'DEBIT', 523),
    ('52400', 'BEBAN KANTOR', 'EXPENSE', 'DEBIT', 524),
    ('52500', 'BEBAN PENYUSUTAN', 'EXPENSE', 'DEBIT', 525),
    ('52600', 'BEBAN PERJALANAN DINAS', 'EXPENSE', 'DEBIT', 526),
    ('52700', 'BEBAN OPERASIONAL LAINNYA', 'EXPENSE', 'DEBIT', 527)

) AS v(code, name, type, balance, sort_order)
JOIN account_templates p
    ON p.account_code = CASE
        WHEN v.code LIKE '11%' THEN '11000'
        WHEN v.code LIKE '12%' THEN '12000'
        WHEN v.code LIKE '21%' THEN '21000'
        WHEN v.code LIKE '22%' THEN '22000'
        WHEN v.code LIKE '31%' THEN '31000'
        WHEN v.code LIKE '32%' THEN '32000'
        WHEN v.code LIKE '33%' THEN '33000'
        WHEN v.code LIKE '41%' THEN '41000'
        WHEN v.code LIKE '42%' THEN '42000'
        WHEN v.code LIKE '51%' THEN '51000'
        WHEN v.code LIKE '52%' THEN '52000'
    END;


INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 4, FALSE, TRUE, v.sort_order
FROM (
    VALUES

    -- KAS
    ('11101', 'Kas Operasional', 'ASSET', 'DEBIT', 11101, '11100'),
    ('11102', 'Kas Kecil', 'ASSET', 'DEBIT', 11102, '11100'),
    ('11103', 'Kas Proyek', 'ASSET', 'DEBIT', 11103, '11100'),
    ('11104', 'Kas Kantor', 'ASSET', 'DEBIT', 11104, '11100'),

    -- BANK
    ('11201', 'Bank BNI', 'ASSET', 'DEBIT', 11201, '11200'),
    ('11202', 'Bank BCA', 'ASSET', 'DEBIT', 11202, '11200'),
    ('11203', 'Bank Mandiri', 'ASSET', 'DEBIT', 11203, '11200'),

    -- PIUTANG
    ('11301', 'Piutang Usaha', 'ASSET', 'DEBIT', 11301, '11300'),
    ('11302', 'Piutang Termin', 'ASSET', 'DEBIT', 11302, '11300'),
    ('11303', 'Piutang Retensi', 'ASSET', 'DEBIT', 11303, '11300'),
    ('11304', 'Piutang Lainnya', 'ASSET', 'DEBIT', 11304, '11300'),

    -- PERSEDIAAN
    ('11401', 'Persediaan Material', 'ASSET', 'DEBIT', 11401, '11400'),
    ('11402', 'Persediaan Material Umum', 'ASSET', 'DEBIT', 11402, '11400'),
    ('11403', 'Persediaan Material Lainnya', 'ASSET', 'DEBIT', 11403, '11400'),

    -- UANG MUKA
    ('11501', 'Uang Muka Pembelian', 'ASSET', 'DEBIT', 11501, '11500'),
    ('11502', 'Uang Muka Subkontraktor', 'ASSET', 'DEBIT', 11502, '11500'),
    ('11503', 'Uang Muka Karyawan', 'ASSET', 'DEBIT', 11503, '11500'),

    -- BIAYA DIBAYAR DIMUKA
    ('11601', 'Sewa Dibayar Dimuka', 'ASSET', 'DEBIT', 11601, '11600'),
    ('11602', 'Asuransi Dibayar Dimuka', 'ASSET', 'DEBIT', 11602, '11600'),

    -- PAJAK DIBAYAR DIMUKA
    ('11701', 'PPN Masukan', 'ASSET', 'DEBIT', 11701, '11700'),
    ('11702', 'PPh 22 Dibayar Dimuka', 'ASSET', 'DEBIT', 11702, '11700'),
    ('11703', 'PPh 23 Dibayar Dimuka', 'ASSET', 'DEBIT', 11703, '11700'),
    ('11704', 'PPh 25 Dibayar Dimuka', 'ASSET', 'DEBIT', 11704, '11700'),

    -- ASET TETAP
    ('12101', 'Tanah', 'ASSET', 'DEBIT', 12101, '12100'),
    ('12102', 'Bangunan', 'ASSET', 'DEBIT', 12102, '12100'),
    ('12103', 'Kendaraan', 'ASSET', 'DEBIT', 12103, '12100'),
    ('12104', 'Mesin dan Peralatan', 'ASSET', 'DEBIT', 12104, '12100'),
    ('12105', 'Peralatan Proyek', 'ASSET', 'DEBIT', 12105, '12100'),
    ('12106', 'Peralatan Kantor', 'ASSET', 'DEBIT', 12106, '12100'),

    -- AKUMULASI PENYUSUTAN
    ('12201', 'Akumulasi Penyusutan Bangunan', 'CONTRA_ASSET', 'CREDIT', 12201, '12200'),
    ('12202', 'Akumulasi Penyusutan Kendaraan', 'CONTRA_ASSET', 'CREDIT', 12202, '12200'),
    ('12203', 'Akumulasi Penyusutan Mesin dan Peralatan', 'CONTRA_ASSET', 'CREDIT', 12203, '12200'),
    ('12204', 'Akumulasi Penyusutan Peralatan Proyek', 'CONTRA_ASSET', 'CREDIT', 12204, '12200'),
    ('12205', 'Akumulasi Penyusutan Peralatan Kantor', 'CONTRA_ASSET', 'CREDIT', 12205, '12200'),

    -- ASET TAKBERWUJUD
    ('12301', 'Perangkat Lunak', 'ASSET', 'DEBIT', 12301, '12300'),
    ('12302', 'Lisensi', 'ASSET', 'DEBIT', 12302, '12300'),

    -- UTANG USAHA
    ('21101', 'Utang Supplier', 'LIABILITY', 'CREDIT', 21101, '21100'),
    ('21102', 'Utang Subkontraktor', 'LIABILITY', 'CREDIT', 21102, '21100'),

    -- UTANG PAJAK
    ('21201', 'Utang PPN', 'LIABILITY', 'CREDIT', 21201, '21200'),
    ('21202', 'Utang PPh 21', 'LIABILITY', 'CREDIT', 21202, '21200'),
    ('21203', 'Utang PPh 22', 'LIABILITY', 'CREDIT', 21203, '21200'),
    ('21204', 'Utang PPh 23', 'LIABILITY', 'CREDIT', 21204, '21200'),
    ('21205', 'Utang PPh 4 Ayat 2', 'LIABILITY', 'CREDIT', 21205, '21200'),

    -- UANG MUKA PELANGGAN
    ('21301', 'Uang Muka Proyek', 'LIABILITY', 'CREDIT', 21301, '21300'),

    -- BIAYA MASIH HARUS DIBAYAR
    ('21401', 'Biaya Masih Harus Dibayar', 'LIABILITY', 'CREDIT', 21401, '21400'),

    -- UTANG GAJI
    ('21501', 'Utang Gaji', 'LIABILITY', 'CREDIT', 21501, '21500'),

    -- UTANG BPJS
    ('21601', 'Utang BPJS', 'LIABILITY', 'CREDIT', 21601, '21600'),

    -- UTANG BANK
    ('22101', 'Utang Bank BNI', 'LIABILITY', 'CREDIT', 22101, '22100'),
    ('22102', 'Utang Bank BCA', 'LIABILITY', 'CREDIT', 22102, '22100'),
    ('22103', 'Utang Bank Mandiri', 'LIABILITY', 'CREDIT', 22103, '22100'),

    -- UTANG LEASING
    ('22201', 'Utang Leasing Kendaraan', 'LIABILITY', 'CREDIT', 22201, '22200'),
    ('22202', 'Utang Leasing Peralatan', 'LIABILITY', 'CREDIT', 22202, '22200'),

    -- MODAL
    ('31101', 'Modal Pemilik', 'EQUITY', 'CREDIT', 31101, '31100'),

    -- LABA DITAHAN
    ('32101', 'Laba Ditahan', 'EQUITY', 'CREDIT', 32101, '32100'),

    -- PRIVE
    ('33101', 'Prive Pemilik', 'EQUITY', 'DEBIT', 33101, '33100'),

    -- PENDAPATAN PROYEK
    ('41101', 'Pendapatan Konstruksi', 'REVENUE', 'CREDIT', 41101, '41100'),
    ('41102', 'Pendapatan Instalasi', 'REVENUE', 'CREDIT', 41102, '41100'),
    ('41103', 'Pendapatan Pemeliharaan', 'REVENUE', 'CREDIT', 41103, '41100'),

    -- PENDAPATAN JASA
    ('41201', 'Pendapatan Jasa', 'REVENUE', 'CREDIT', 41201, '41200'),

    -- PENDAPATAN LAIN
    ('42101', 'Pendapatan Bunga', 'REVENUE', 'CREDIT', 42101, '42100'),
    ('42201', 'Pendapatan Lainnya', 'REVENUE', 'CREDIT', 42201, '42200'),

    -- BEBAN PROYEK
    ('51101', 'Beban Material Proyek', 'EXPENSE', 'DEBIT', 51101, '51100'),
    ('51201', 'Beban Tenaga Kerja Proyek', 'EXPENSE', 'DEBIT', 51201, '51200'),
    ('51301', 'Beban Subkontraktor', 'EXPENSE', 'DEBIT', 51301, '51300'),
    ('51401', 'Beban Sewa Alat', 'EXPENSE', 'DEBIT', 51401, '51400'),
    ('51501', 'Beban Transportasi Proyek', 'EXPENSE', 'DEBIT', 51501, '51500'),
    ('51601', 'Beban Proyek Lainnya', 'EXPENSE', 'DEBIT', 51601, '51600'),

    -- BEBAN OPERASIONAL
    ('52101', 'Beban Gaji', 'EXPENSE', 'DEBIT', 52101, '52100'),
    ('52201', 'Beban Listrik dan Air', 'EXPENSE', 'DEBIT', 52201, '52200'),
    ('52301', 'Beban Telekomunikasi', 'EXPENSE', 'DEBIT', 52301, '52300'),
    ('52401', 'Beban Kantor', 'EXPENSE', 'DEBIT', 52401, '52400'),
    ('52501', 'Beban Penyusutan', 'EXPENSE', 'DEBIT', 52501, '52500'),
    ('52601', 'Beban Perjalanan Dinas', 'EXPENSE', 'DEBIT', 52601, '52600'),
    ('52701', 'Beban Operasional Lainnya', 'EXPENSE', 'DEBIT', 52701, '52700')

) AS v(code, name, type, balance, sort_order, parent_code)
JOIN account_templates p
    ON p.account_code = v.parent_code;


SELECT
    account_code,
    account_name,
    account_type,
    normal_balance,
    account_level,
    is_header,
    is_postable,
    sort_order
FROM account_templates
ORDER BY sort_order, account_code;

SELECT *
FROM account_templates
ORDER BY sort_order;


-- ============================================================
-- COA TEMPLATE
-- Maksimal 4 Level
-- Level 1-3 = Header
-- Level 4   = Bisa Jurnal
-- ============================================================

-- ============================================================
-- LEVEL 1
-- ============================================================

INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
VALUES
(gen_random_uuid(), '10000', 'ASET', 'ASSET', 'DEBIT',
 NULL, 1, TRUE, FALSE, 100),

(gen_random_uuid(), '20000', 'LIABILITAS', 'LIABILITY', 'CREDIT',
 NULL, 1, TRUE, FALSE, 200),

(gen_random_uuid(), '30000', 'EKUITAS', 'EQUITY', 'CREDIT',
 NULL, 1, TRUE, FALSE, 300),

(gen_random_uuid(), '40000', 'PENDAPATAN', 'REVENUE', 'CREDIT',
 NULL, 1, TRUE, FALSE, 400),

(gen_random_uuid(), '50000', 'BEBAN', 'EXPENSE', 'DEBIT',
 NULL, 1, TRUE, FALSE, 500);


-- ============================================================
-- LEVEL 2
-- ============================================================

INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 2, TRUE, FALSE, v.sort_order
FROM (
    VALUES
    ('11000', 'ASET LANCAR', 'ASSET', 'DEBIT', 110),
    ('12000', 'ASET TIDAK LANCAR', 'ASSET', 'DEBIT', 120),
    ('21000', 'LIABILITAS JANGKA PENDEK', 'LIABILITY', 'CREDIT', 210),
    ('22000', 'LIABILITAS JANGKA PANJANG', 'LIABILITY', 'CREDIT', 220),
    ('31000', 'MODAL', 'EQUITY', 'CREDIT', 310),
    ('32000', 'LABA DITAHAN', 'EQUITY', 'CREDIT', 320),
    ('33000', 'PRIVE', 'EQUITY', 'DEBIT', 330),
    ('41000', 'PENDAPATAN USAHA', 'REVENUE', 'CREDIT', 410),
    ('42000', 'PENDAPATAN LAIN-LAIN', 'REVENUE', 'CREDIT', 420),
    ('51000', 'BEBAN PROYEK', 'EXPENSE', 'DEBIT', 510),
    ('52000', 'BEBAN OPERASIONAL', 'EXPENSE', 'DEBIT', 520)
) AS v(code, name, type, balance, sort_order)
JOIN account_templates p
    ON p.account_code = CASE
        WHEN v.code LIKE '1%' THEN '10000'
        WHEN v.code LIKE '2%' THEN '20000'
        WHEN v.code LIKE '3%' THEN '30000'
        WHEN v.code LIKE '4%' THEN '40000'
        WHEN v.code LIKE '5%' THEN '50000'
    END;


-- ============================================================
-- LEVEL 3
-- ============================================================

INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 3, TRUE, FALSE, v.sort_order
FROM (
    VALUES

    -- ASET LANCAR
    ('11100', 'KAS', 'ASSET', 'DEBIT', 111),
    ('11200', 'BANK', 'ASSET', 'DEBIT', 112),
    ('11300', 'PIUTANG', 'ASSET', 'DEBIT', 113),
    ('11400', 'PERSEDIAAN', 'ASSET', 'DEBIT', 114),
    ('11500', 'UANG MUKA', 'ASSET', 'DEBIT', 115),
    ('11600', 'BIAYA DIBAYAR DIMUKA', 'ASSET', 'DEBIT', 116),
    ('11700', 'PAJAK DIBAYAR DIMUKA', 'ASSET', 'DEBIT', 117),

    -- ASET TIDAK LANCAR
    ('12100', 'ASET TETAP', 'ASSET', 'DEBIT', 121),
    ('12200', 'AKUMULASI PENYUSUTAN', 'CONTRA_ASSET', 'CREDIT', 122),
    ('12300', 'ASET TAKBERWUJUD', 'ASSET', 'DEBIT', 123),

    -- LIABILITAS JANGKA PENDEK
    ('21100', 'UTANG USAHA', 'LIABILITY', 'CREDIT', 211),
    ('21200', 'UTANG PAJAK', 'LIABILITY', 'CREDIT', 212),
    ('21300', 'UANG MUKA PELANGGAN', 'LIABILITY', 'CREDIT', 213),
    ('21400', 'BIAYA MASIH HARUS DIBAYAR', 'LIABILITY', 'CREDIT', 214),
    ('21500', 'UTANG GAJI', 'LIABILITY', 'CREDIT', 215),
    ('21600', 'UTANG BPJS', 'LIABILITY', 'CREDIT', 216),

    -- LIABILITAS JANGKA PANJANG
    ('22100', 'UTANG BANK', 'LIABILITY', 'CREDIT', 221),
    ('22200', 'UTANG LEASING', 'LIABILITY', 'CREDIT', 222),

    -- EKUITAS
    ('31100', 'MODAL PEMILIK', 'EQUITY', 'CREDIT', 311),
    ('32100', 'LABA DITAHAN', 'EQUITY', 'CREDIT', 321),
    ('33100', 'PRIVE PEMILIK', 'EQUITY', 'DEBIT', 331),

    -- PENDAPATAN
    ('41100', 'PENDAPATAN PROYEK', 'REVENUE', 'CREDIT', 411),
    ('41200', 'PENDAPATAN JASA', 'REVENUE', 'CREDIT', 412),
    ('42100', 'PENDAPATAN BUNGA', 'REVENUE', 'CREDIT', 421),
    ('42200', 'PENDAPATAN LAINNYA', 'REVENUE', 'CREDIT', 422),

    -- BEBAN PROYEK
    ('51100', 'BEBAN MATERIAL', 'EXPENSE', 'DEBIT', 511),
    ('51200', 'BEBAN TENAGA KERJA', 'EXPENSE', 'DEBIT', 512),
    ('51300', 'BEBAN SUBKONTRAKTOR', 'EXPENSE', 'DEBIT', 513),
    ('51400', 'BEBAN SEWA ALAT', 'EXPENSE', 'DEBIT', 514),
    ('51500', 'BEBAN TRANSPORTASI PROYEK', 'EXPENSE', 'DEBIT', 515),
    ('51600', 'BEBAN PROYEK LAINNYA', 'EXPENSE', 'DEBIT', 516),

    -- BEBAN OPERASIONAL
    ('52100', 'BEBAN GAJI', 'EXPENSE', 'DEBIT', 521),
    ('52200', 'BEBAN LISTRIK DAN AIR', 'EXPENSE', 'DEBIT', 522),
    ('52300', 'BEBAN TELEKOMUNIKASI', 'EXPENSE', 'DEBIT', 523),
    ('52400', 'BEBAN KANTOR', 'EXPENSE', 'DEBIT', 524),
    ('52500', 'BEBAN PENYUSUTAN', 'EXPENSE', 'DEBIT', 525),
    ('52600', 'BEBAN PERJALANAN DINAS', 'EXPENSE', 'DEBIT', 526),
    ('52700', 'BEBAN OPERASIONAL LAINNYA', 'EXPENSE', 'DEBIT', 527)

) AS v(code, name, type, balance, sort_order)
JOIN account_templates p
    ON p.account_code = CASE
        WHEN v.code LIKE '11%' THEN '11000'
        WHEN v.code LIKE '12%' THEN '12000'
        WHEN v.code LIKE '21%' THEN '21000'
        WHEN v.code LIKE '22%' THEN '22000'
        WHEN v.code LIKE '31%' THEN '31000'
        WHEN v.code LIKE '32%' THEN '32000'
        WHEN v.code LIKE '33%' THEN '33000'
        WHEN v.code LIKE '41%' THEN '41000'
        WHEN v.code LIKE '42%' THEN '42000'
        WHEN v.code LIKE '51%' THEN '51000'
        WHEN v.code LIKE '52%' THEN '52000'
    END;


-- ============================================================
-- LEVEL 4
-- ============================================================

INSERT INTO account_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    sort_order
)
SELECT
    gen_random_uuid(), v.code, v.name, v.type, v.balance,
    p.template_account_id, 4, FALSE, TRUE, v.sort_order
FROM (
    VALUES

    -- KAS
    ('11101', 'Kas Operasional', 'ASSET', 'DEBIT', 11101, '11100'),
    ('11102', 'Kas Kecil', 'ASSET', 'DEBIT', 11102, '11100'),
    ('11103', 'Kas Proyek', 'ASSET', 'DEBIT', 11103, '11100'),
    ('11104', 'Kas Kantor', 'ASSET', 'DEBIT', 11104, '11100'),

    -- BANK
    ('11201', 'Bank BNI', 'ASSET', 'DEBIT', 11201, '11200'),
    ('11202', 'Bank BCA', 'ASSET', 'DEBIT', 11202, '11200'),
    ('11203', 'Bank Mandiri', 'ASSET', 'DEBIT', 11203, '11200'),

    -- PIUTANG
    ('11301', 'Piutang Usaha', 'ASSET', 'DEBIT', 11301, '11300'),
    ('11302', 'Piutang Termin', 'ASSET', 'DEBIT', 11302, '11300'),
    ('11303', 'Piutang Retensi', 'ASSET', 'DEBIT', 11303, '11300'),
    ('11304', 'Piutang Lainnya', 'ASSET', 'DEBIT', 11304, '11300'),

    -- PERSEDIAAN
    ('11401', 'Persediaan Material', 'ASSET', 'DEBIT', 11401, '11400'),
    ('11402', 'Persediaan Material Umum', 'ASSET', 'DEBIT', 11402, '11400'),
    ('11403', 'Persediaan Material Lainnya', 'ASSET', 'DEBIT', 11403, '11400'),

    -- UANG MUKA
    ('11501', 'Uang Muka Pembelian', 'ASSET', 'DEBIT', 11501, '11500'),
    ('11502', 'Uang Muka Subkontraktor', 'ASSET', 'DEBIT', 11502, '11500'),
    ('11503', 'Uang Muka Karyawan', 'ASSET', 'DEBIT', 11503, '11500'),

    -- BIAYA DIBAYAR DIMUKA
    ('11601', 'Sewa Dibayar Dimuka', 'ASSET', 'DEBIT', 11601, '11600'),
    ('11602', 'Asuransi Dibayar Dimuka', 'ASSET', 'DEBIT', 11602, '11600'),

    -- PAJAK DIBAYAR DIMUKA
    ('11701', 'PPN Masukan', 'ASSET', 'DEBIT', 11701, '11700'),
    ('11702', 'PPh 22 Dibayar Dimuka', 'ASSET', 'DEBIT', 11702, '11700'),
    ('11703', 'PPh 23 Dibayar Dimuka', 'ASSET', 'DEBIT', 11703, '11700'),
    ('11704', 'PPh 25 Dibayar Dimuka', 'ASSET', 'DEBIT', 11704, '11700'),

    -- ASET TETAP
    ('12101', 'Tanah', 'ASSET', 'DEBIT', 12101, '12100'),
    ('12102', 'Bangunan', 'ASSET', 'DEBIT', 12102, '12100'),
    ('12103', 'Kendaraan', 'ASSET', 'DEBIT', 12103, '12100'),
    ('12104', 'Mesin dan Peralatan', 'ASSET', 'DEBIT', 12104, '12100'),
    ('12105', 'Peralatan Proyek', 'ASSET', 'DEBIT', 12105, '12100'),
    ('12106', 'Peralatan Kantor', 'ASSET', 'DEBIT', 12106, '12100'),

    -- AKUMULASI PENYUSUTAN
    ('12201', 'Akumulasi Penyusutan Bangunan', 'CONTRA_ASSET', 'CREDIT', 12201, '12200'),
    ('12202', 'Akumulasi Penyusutan Kendaraan', 'CONTRA_ASSET', 'CREDIT', 12202, '12200'),
    ('12203', 'Akumulasi Penyusutan Mesin dan Peralatan', 'CONTRA_ASSET', 'CREDIT', 12203, '12200'),
    ('12204', 'Akumulasi Penyusutan Peralatan Proyek', 'CONTRA_ASSET', 'CREDIT', 12204, '12200'),
    ('12205', 'Akumulasi Penyusutan Peralatan Kantor', 'CONTRA_ASSET', 'CREDIT', 12205, '12200'),

    -- ASET TAKBERWUJUD
    ('12301', 'Perangkat Lunak', 'ASSET', 'DEBIT', 12301, '12300'),
    ('12302', 'Lisensi', 'ASSET', 'DEBIT', 12302, '12300'),

    -- UTANG USAHA
    ('21101', 'Utang Supplier', 'LIABILITY', 'CREDIT', 21101, '21100'),
    ('21102', 'Utang Subkontraktor', 'LIABILITY', 'CREDIT', 21102, '21100'),

    -- UTANG PAJAK
    ('21201', 'Utang PPN', 'LIABILITY', 'CREDIT', 21201, '21200'),
    ('21202', 'Utang PPh 21', 'LIABILITY', 'CREDIT', 21202, '21200'),
    ('21203', 'Utang PPh 22', 'LIABILITY', 'CREDIT', 21203, '21200'),
    ('21204', 'Utang PPh 23', 'LIABILITY', 'CREDIT', 21204, '21200'),
    ('21205', 'Utang PPh 4 Ayat 2', 'LIABILITY', 'CREDIT', 21205, '21200'),

    -- UANG MUKA PELANGGAN
    ('21301', 'Uang Muka Proyek', 'LIABILITY', 'CREDIT', 21301, '21300'),

    -- BIAYA MASIH HARUS DIBAYAR
    ('21401', 'Biaya Masih Harus Dibayar', 'LIABILITY', 'CREDIT', 21401, '21400'),

    -- UTANG GAJI
    ('21501', 'Utang Gaji', 'LIABILITY', 'CREDIT', 21501, '21500'),

    -- UTANG BPJS
    ('21601', 'Utang BPJS', 'LIABILITY', 'CREDIT', 21601, '21600'),

    -- UTANG BANK
    ('22101', 'Utang Bank BNI', 'LIABILITY', 'CREDIT', 22101, '22100'),
    ('22102', 'Utang Bank BCA', 'LIABILITY', 'CREDIT', 22102, '22100'),
    ('22103', 'Utang Bank Mandiri', 'LIABILITY', 'CREDIT', 22103, '22100'),

    -- UTANG LEASING
    ('22201', 'Utang Leasing Kendaraan', 'LIABILITY', 'CREDIT', 22201, '22200'),
    ('22202', 'Utang Leasing Peralatan', 'LIABILITY', 'CREDIT', 22202, '22200'),

    -- MODAL
    ('31101', 'Modal Pemilik', 'EQUITY', 'CREDIT', 31101, '31100'),

    -- LABA DITAHAN
    ('32101', 'Laba Ditahan', 'EQUITY', 'CREDIT', 32101, '32100'),

    -- PRIVE
    ('33101', 'Prive Pemilik', 'EQUITY', 'DEBIT', 33101, '33100'),

    -- PENDAPATAN PROYEK
    ('41101', 'Pendapatan Konstruksi', 'REVENUE', 'CREDIT', 41101, '41100'),
    ('41102', 'Pendapatan Instalasi', 'REVENUE', 'CREDIT', 41102, '41100'),
    ('41103', 'Pendapatan Pemeliharaan', 'REVENUE', 'CREDIT', 41103, '41100'),

    -- PENDAPATAN JASA
    ('41201', 'Pendapatan Jasa', 'REVENUE', 'CREDIT', 41201, '41200'),

    -- PENDAPATAN LAIN
    ('42101', 'Pendapatan Bunga', 'REVENUE', 'CREDIT', 42101, '42100'),
    ('42201', 'Pendapatan Lainnya', 'REVENUE', 'CREDIT', 42201, '42200'),

    -- BEBAN PROYEK
    ('51101', 'Beban Material Proyek', 'EXPENSE', 'DEBIT', 51101, '51100'),
    ('51201', 'Beban Tenaga Kerja Proyek', 'EXPENSE', 'DEBIT', 51201, '51200'),
    ('51301', 'Beban Subkontraktor', 'EXPENSE', 'DEBIT', 51301, '51300'),
    ('51401', 'Beban Sewa Alat', 'EXPENSE', 'DEBIT', 51401, '51400'),
    ('51501', 'Beban Transportasi Proyek', 'EXPENSE', 'DEBIT', 51501, '51500'),
    ('51601', 'Beban Proyek Lainnya', 'EXPENSE', 'DEBIT', 51601, '51600'),

    -- BEBAN OPERASIONAL
    ('52101', 'Beban Gaji', 'EXPENSE', 'DEBIT', 52101, '52100'),
    ('52201', 'Beban Listrik dan Air', 'EXPENSE', 'DEBIT', 52201, '52200'),
    ('52301', 'Beban Telekomunikasi', 'EXPENSE', 'DEBIT', 52301, '52300'),
    ('52401', 'Beban Kantor', 'EXPENSE', 'DEBIT', 52401, '52400'),
    ('52501', 'Beban Penyusutan', 'EXPENSE', 'DEBIT', 52501, '52500'),
    ('52601', 'Beban Perjalanan Dinas', 'EXPENSE', 'DEBIT', 52601, '52600'),
    ('52701', 'Beban Operasional Lainnya', 'EXPENSE', 'DEBIT', 52701, '52700')

) AS v(code, name, type, balance, sort_order, parent_code)
JOIN account_templates p
    ON p.account_code = v.parent_code;