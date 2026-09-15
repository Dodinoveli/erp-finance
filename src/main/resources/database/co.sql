INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '11701',
    'PPN Masukan',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '11700'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '11702',
    'PPh 22 Dibayar Dimuka',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '11700'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '11703',
    'PPh 23 Dibayar Dimuka',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '11700'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
),
(
    gen_random_uuid(),
    '11704',
    'PPh 25 Dibayar Dimuka',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '11700'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    4
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES (
    gen_random_uuid(),
    '12100',
    'ASET TETAP',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);



INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '12101',
    'Tanah',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '12102',
    'Bangunan',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '12103',
    'Kendaraan',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
),
(
    gen_random_uuid(),
    '12104',
    'Mesin dan Peralatan',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    4
),
(
    gen_random_uuid(),
    '12105',
    'Peralatan Proyek',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    5
),
(
    gen_random_uuid(),
    '12106',
    'Peralatan Kantor',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    6
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES (
    gen_random_uuid(),
    '12200',
    'AKUMULASI PENYUSUTAN',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    2
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '12201',
    'Akumulasi Penyusutan Bangunan',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '12202',
    'Akumulasi Penyusutan Kendaraan',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '12203',
    'Akumulasi Penyusutan Mesin dan Peralatan',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
),
(
    gen_random_uuid(),
    '12204',
    'Akumulasi Penyusutan Peralatan Proyek',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    4
),
(
    gen_random_uuid(),
    '12205',
    'Akumulasi Penyusutan Peralatan Kantor',
    'CONTRA_ASSET',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    5
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES (
    gen_random_uuid(),
    '12300',
    'ASET TAKBERWUJUD',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    3
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '12301',
    'Perangkat Lunak',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12300'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '12302',
    'Lisensi',
    'ASSET',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '12300'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21000',
    'LIABILITAS JANGKA PENDEK',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '20000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    1
);



INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21100',
    'UTANG USAHA',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21101',
    'Utang Supplier',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '21102',
    'Utang Subkontraktor',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21200',
    'UTANG PAJAK',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21201',
    'Utang PPN',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '21202',
    'Utang PPH 21',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '21203',
    'Utang PPH 22',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
),
(
    gen_random_uuid(),
    '21204',
    'Utang PPH 23',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    4
),
(
    gen_random_uuid(),
    '21205',
    'Utang PPh 4 Ayat 2',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    5
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21300',
    'UANG MUKA PELANGGAN',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    3
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21301',
    'Uang Muka Proyek',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21300'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21400',
    'BIAYA MASIH HARUS DIBAYAR',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    4
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21401',
    'Biaya Masih Harus Dibayar',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21400'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21500',
    'UTANG GAJI',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    5
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21501',
    'Utang Gaji',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21500'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21600',
    'UTANG BPJS',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    6
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '21601',
    'Utang Bpjs',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '21600'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '22000',
    'LIABILITAS JANGKA PANJANG',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '20000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '22100',
    'UTANG BANK',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '22101',
    'Utang Bank Bni',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '22102',
    'Utang Bank Bca',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '22103',
    'Utang Bank Mandiri',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '22200',
    'UTANG LEASING',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '22201',
    'Utang Leasing Kendaraan',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '22202',
    'Utang Leasing Peralatan',
    'LIABILITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '22200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
);

// lanjut 
INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '31000',
    'Modal',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '30000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '31100',
    'Modal Pemilik',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '31000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '31101',
    'Modal Pemilik',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '31100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '32000',
    'Laba Ditahan',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '30000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    2
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '32100',
    'Laba Ditahan',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '32000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '32101',
    'Laba Ditahan',
    'EQUITY',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '32100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '33000',
    'Prive',
    'EQUITY',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '30000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    3
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '33100',
    'Prive Pemilik',
    'EQUITY',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '33000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '33101',
    'Prive Pemilik',
    'EQUITY',
    'DEBIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '33100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '41000',
    'PENDAPATAN USAHA',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '40000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    1
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '41100',
    'PENDAPATAN PROYEK',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '41101',
    'Pendapatan Konstruksi',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
),
(
    gen_random_uuid(),
    '41102',
    'Pendapatan Instalasi',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    2
),
(
    gen_random_uuid(),
    '41103',
    'Pendapatan Pemeliharaan',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    3
);

INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '41200',
    'PENDAPATAN JASA',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '41201',
    'Pendapatan Jasa',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '41200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '42000',
    'PENDAPATAN LAIN-LAIN',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '40000'
    ),
    2,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '42100',
    'PENDAPATAN BUNGA',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '42000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '42101',
    'Pendapatan Bunga',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '42100'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '42200',
    'PENDAPATAN LAINNYA',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '42000'
    ),
    3,
    TRUE,
    FALSE,
    NULL,
    2
);


INSERT INTO public.chart_of_accounts_templates (
    template_account_id,
    account_code,
    account_name,
    account_type,
    normal_balance,
    parent_template_id,
    account_level,
    is_header,
    is_postable,
    description,
    sort_order
)
VALUES
(
    gen_random_uuid(),
    '42201',
    'Pendapatan Lainnya',
    'REVENUE',
    'CREDIT',
    (
        SELECT template_account_id
        FROM public.chart_of_accounts_templates
        WHERE account_code = '42200'
    ),
    4,
    FALSE,
    TRUE,
    NULL,
    1
);



