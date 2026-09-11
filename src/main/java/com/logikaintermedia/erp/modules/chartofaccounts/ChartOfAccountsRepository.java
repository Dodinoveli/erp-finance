package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.List;
import java.util.UUID;

public interface ChartOfAccountsRepository {
    // List<ChartOfAccounts> findAll();

    // Mengambil seluruh data COA berdasarkan perusahaan
    List<ChartOfAccounts> findAllByCompanyId(UUID companyId);

    // Mengambil data COA berdasarkan ID akun induk (parent)
    // Digunakan untuk mendapatkan daftar akun anak (child)
    List<ChartOfAccounts> findByParentId(UUID parentId);

    // Menginisialisasi COA perusahaan dengan menyalin data
    // dari COA template ke perusahaan yang bersangkutan
    int initializeCompanyCoa(UUID companyId);

    // Menyimpan data COA baru
    int save(ChartOfAccounts account);

    // Memperbarui data COA yang sudah ada
    int update(ChartOfAccounts account);

    // Menghapus data COA berdasarkan ID akun
    // int deleteById(UUID accountId);
}
