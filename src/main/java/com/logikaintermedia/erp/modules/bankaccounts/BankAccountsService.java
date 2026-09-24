package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsResponse;

@Service
public class BankAccountsService {

    private final BankAccountsRepository repository;

    public BankAccountsService(BankAccountsRepository repository) {
        this.repository = repository;
    }

    public BankAccounts findDetailById(UUID bankAccountId, UUID companyId) {
        return repository.findDetailById(bankAccountId, companyId);
    }

    @Transactional
    public List<BankAccountsResponse> findBankAccountsByCompanyId(UUID companyId) {
        List<BankAccounts> bankAccountsList = repository.findBankAccountsByCompanyId(companyId);

        if (bankAccountsList.isEmpty()) {
            return null;
        }

        List<BankAccountsResponse> responseList = new ArrayList<>();
        for (BankAccounts bac : bankAccountsList) {
            BankAccountsResponse response = new BankAccountsResponse();
            ChartOfAccounts accounts = new ChartOfAccounts();
            response.setBankAccountId(bac.getBankAccountId());
            response.setAccountCode(bac.getAccountCode());
            response.setAccountName(bac.getAccountName());
            response.setOpeningBalance(bac.getOpeningBalance());
            response.setBankName(bac.getBankName());
            response.setBankBranch(bac.getBankBranch());
            response.setAccountNumber(bac.getAccountNumber());
            response.setAccountHolder(bac.getAccountHolder());
            // ba.bank_name,
            // ba.bank_branch,
            // ba.account_number,
            // ba.account_holder,
            accounts.setAccountId(bac.getAccountId());
            accounts.setAccountName(bac.getAccountName());
            accounts.setParentAccountCode(bac.getParentAccountCode());
            response.setCoa(bac.getCoa());
            responseList.add(response);
        }
        return responseList;
    }

    @Transactional
    public BankAccounts saveBankAccounts(BankAccountsRequest request, UUID companyId, UUID userId) {
        BankAccounts accounts = new BankAccounts();
        accounts.setBankAccountId(Generators.timeBasedEpochRandomGenerator().generate());
        accounts.setAccountCode(request.getAccountCode());
        accounts.setAccountName(request.getAccountName());
        accounts.setBankName(request.getBankName());
        accounts.setBankBranch(request.getBankBranch());
        accounts.setAccountNumber(request.getAccountNumber());
        accounts.setAccountHolder(request.getAccountHolder());
        accounts.setCurrency(request.getCurrency());
        accounts.setOpeningBalance(request.getOpeningBalance());
        accounts.setIsDefault(request.getIsDefault());
        accounts.setIsActive(request.getIsActive());
        accounts.setCompanyId(companyId);
        accounts.setAccountId(request.getAccountId());
        accounts.setCreatedAt(request.getCreatedAt());
        accounts.setUpdatedAt(null);
        accounts.setUserId(userId);
        accounts.setDeletedAt(null);
        int data = repository.save(accounts);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data Bank Accounts , silakan coba kembali");
        }
        return accounts;
    }

    @Transactional
    public BankAccounts updateBankAccounts(BankAccountsRequest request, UUID bankAccountId, UUID companyId,
            UUID userId) {
        BankAccounts accounts = new BankAccounts();
        accounts.setAccountCode(request.getAccountCode());
        accounts.setAccountName(request.getAccountName());
        accounts.setBankName(request.getBankName());
        accounts.setBankBranch(request.getBankBranch());
        accounts.setAccountNumber(request.getAccountNumber());
        accounts.setAccountHolder(request.getAccountHolder());
        accounts.setOpeningBalance(BigDecimal.ZERO);
        accounts.setIsActive(true);
        accounts.setAccountId(request.getAccountId());
        accounts.setUpdatedAt(request.getUpdatedAt());
        accounts.setBankAccountId(bankAccountId);
        accounts.setCompanyId(companyId);
        accounts.setUserId(userId);
        int data = repository.update(accounts);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal mengubah data Bank Accounts, silakan coba kembali");
        }
        return accounts;
    }

    // mengambil dan menampilkan kategori akun Kas dan bank bedasarkan companies id
    @Transactional
    public List<ChartOfAccountsResponse> findCashAndBankAccountsByCompanyId(UUID companyId) {
        List<ChartOfAccounts> coaList = repository.findCashAndBankAccountsByCompanyId(companyId);
        List<ChartOfAccountsResponse> responseList = new ArrayList<>();
        for (ChartOfAccounts coa : coaList) {
            ChartOfAccountsResponse response = new ChartOfAccountsResponse(coa);
            responseList.add(response);
        }
        return responseList;
    }

    public List<ChartOfAccounts> findCoaForBankAccount(UUID companyId,
            UUID currentAccountId) {
        return repository.findCoaForBankAccount(companyId, currentAccountId);
    }

    // public List<ChartOfAccountsResponse> findCoaByparentId(UUID companyId, UUID
    // parentId) {

    // List<ChartOfAccounts> coaList = repository.findCoaByparentId(companyId,
    // parentId);
    // System.out.println("Keyword pencarian ========== " + parentId);
    // List<ChartOfAccountsResponse> responseList = new ArrayList<>();
    // if (parentId == null) {
    // if (coaList.isEmpty()) {
    // return Collections.emptyList();
    // }
    // } else {

    // for (ChartOfAccounts coa : coaList) {
    // ChartOfAccountsResponse response = new ChartOfAccountsResponse(coa);
    // responseList.add(response);
    // }
    // }

    // return responseList;
    // }

}
