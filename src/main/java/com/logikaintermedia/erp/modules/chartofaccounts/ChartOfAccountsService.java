package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.List;
import java.util.UUID;

public interface ChartOfAccountsService {
    // List<ChartOfAccounts> findAll();

    // Mengambil seluruh data COA berdasarkan perusahaan
    List<ChartOfAccountsResponse> findAllByCompanyId(UUID companyId);

    // Mengambil data COA berdasarkan ID akun induk (parent)
    // Digunakan untuk mendapatkan daftar akun anak (child)
    List<ChartOfAccounts> findByParentId(UUID parentId);

    // Menginisialisasi COA perusahaan dengan menyalin data
    // dari COA template ke perusahaan yang bersangkutan
    int initializeCompanyCoa(UUID companyId);

    int save(ChartOfAccounts account);

    int update(ChartOfAccounts account);
}