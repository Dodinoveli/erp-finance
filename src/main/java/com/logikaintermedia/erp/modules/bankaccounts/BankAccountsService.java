package com.logikaintermedia.erp.modules.bankaccounts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;

@Service
public class BankAccountsService {

    private final BankAccountsRepository repository;

    public BankAccountsService(BankAccountsRepository repository) {
        this.repository = repository;
    }

    public BankAccounts findDetailById(UUID id) {
        return repository.findDetailById(id);
    }

    public List<BankAccountsResponse> findBankAccountById(UUID companyId) {
        List<BankAccounts> bankAccountsList = repository.findBankAccountsById(companyId);

        if (bankAccountsList.isEmpty()) {
            return null;
        }

        List<BankAccountsResponse> responseList = new ArrayList<>();
        for (BankAccounts bac : bankAccountsList) {
            BankAccountsResponse response = new BankAccountsResponse();
            response.setBankAccountId(bac.getBankAccountId());
            response.setAccountCode(bac.getAccountCode());
            response.setAccountName(bac.getAccountName());
            response.setAccountType(bac.getAccountType());
            response.setBankName(bac.getBankName());
            response.setBankBranch(bac.getBankBranch());
            response.setAccountNumber(bac.getAccountNumber());
            response.setAccountHolder(bac.getAccountHolder());
            response.setIsActive(bac.getIsActive());
            response.setCoaId(bac.getCoaId());
            response.setName(bac.getName());
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
        accounts.setAccountType(request.getAccountType());
        accounts.setBankName(request.getBankName());
        accounts.setBankBranch(request.getBankBranch());
        accounts.setAccountNumber(request.getAccountNumber());
        accounts.setAccountHolder(request.getAccountHolder());
        accounts.setCurrency(request.getCurrency());
        accounts.setOpeningBalance(request.getOpeningBalance());
        accounts.setIsDefault(request.getIsDefault());
        accounts.setIsActive(request.getIsActive());
        accounts.setCompanyId(companyId);
        accounts.setCoaId(request.getCoaId());
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
        accounts.setAccountType(request.getAccountType());
        accounts.setBankName(request.getBankName());
        accounts.setBankBranch(request.getBankBranch());
        accounts.setAccountNumber(request.getAccountNumber());
        accounts.setAccountHolder(request.getAccountHolder());
        // accounts.setCurrency(request.getCurrency());
        // accounts.setOpeningBalance(request.getOpeningBalance());
        // accounts.setIsDefault(request.getIsDefault());
        accounts.setIsActive(request.getIsActive());

        accounts.setCoaId(request.getCoaId());
        // accounts.setCreatedAt(request.getCreatedAt());
        accounts.setUpdatedAt(request.getUpdatedAt());
        // accounts.setDeletedAt(null);
        accounts.setBankAccountId(bankAccountId);
        accounts.setCompanyId(companyId);
        accounts.setUserId(userId);
        int data = repository.update(accounts);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal mengubah data Bank Accounts, silakan coba kembali");
        }
        return accounts;
    }

    public List<CoaAccountsResponse> findCoaByName(UUID companyId, String keyword) {
        if (keyword.equalsIgnoreCase("CASH")) {
            keyword = "Kas";
        }

        List<CoaAccounts> coaList = repository.findCoaByName(companyId, keyword);
        System.out.println("Keyword pencarian ========== " + keyword);
        List<CoaAccountsResponse> responseList = new ArrayList<>();
        if (keyword.isBlank()) {
            if (coaList.isEmpty()) {
                return Collections.emptyList();
            }
        } else {

            for (CoaAccounts coa : coaList) {
                CoaAccountsResponse response = new CoaAccountsResponse();
                response.setCoaId(coa.getCoaId());
                response.setName(coa.getName());
                responseList.add(response);
            }
        }

        return responseList;
    }

}
