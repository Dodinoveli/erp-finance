package com.logikaintermedia.erp.modules.bankaccounts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        BankAccounts accounts = BankAccounts.from(request, userId, companyId, userId);
        int data = repository.save(accounts);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data Bank Accounts , silakan coba kembali");
        }
        return accounts;
    }

    @Transactional
    public BankAccounts updateBankAccounts(BankAccountsRequest request, UUID bankAccountId, UUID companyId,
            UUID userId) {
        BankAccounts accounts = BankAccounts.updateFrom(request, bankAccountId, companyId, userId);
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

}
