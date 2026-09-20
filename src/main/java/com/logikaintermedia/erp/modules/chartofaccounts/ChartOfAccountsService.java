package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChartOfAccountsService {

    private final ChartOfAccountRepository repository;

    public ChartOfAccountsService(ChartOfAccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ChartOfAccountsResponse> findAllByCompanyId(UUID companyId) {
        // 1. Ambil data flat murni dari Repository
        List<ChartOfAccounts> flatList = repository.findAllByCompanyId(companyId);

        List<ChartOfAccountsResponse> rootNodes = new ArrayList<>();
        Map<UUID, ChartOfAccountsResponse> nodeMap = new LinkedHashMap<>();

        // 2. Langkah Pertama: Masukkan ke Map pembantu menggunakan CoaResponse
        for (ChartOfAccounts coa : flatList) {
            nodeMap.put(coa.getAccountId(), new ChartOfAccountsResponse(coa));
        }

        // 3. Langkah Kedua: Hubungkan struktur pohonnya (Induk -> Anak)
        for (ChartOfAccounts coa : flatList) {
            ChartOfAccountsResponse currentNode = nodeMap.get(coa.getAccountId());
            UUID parentId = coa.getParentId();

            if (parentId != null && nodeMap.containsKey(parentId)) {
                nodeMap.get(parentId).getChildren().add(currentNode);
            } else {
                rootNodes.add(currentNode);
            }
        }
        return rootNodes;
    }

    @Transactional(readOnly = true)
    public List<ChartOfAccounts> findByParentId(UUID parentId, UUID companyId) {
        return repository.findByParentId(parentId, companyId);
    }

    @Transactional(readOnly = true)
    public ChartOfAccounts findById(UUID parentId, UUID companyId) {
        return repository.findById(parentId, companyId);
    }

    @Transactional
    public int initializeCompanyCoa(UUID companyId) {
        return repository.initializeCompanyCoa(companyId);
    }

    // @Transactional
    // public int[] save(List<ChartOfAccounts> accounts, UUID companyId) {

    // }

    @Transactional
    public int[] update(List<ChartOfAccounts> accounts, UUID companyId) {
        accounts.forEach(account -> account.setCompanyId(companyId));
        int[] result = repository.updateBatch(accounts);
        // memastikan setiap row harus berhasil
        for (int rows : result) {
            if (rows <= 0) {
                throw new IllegalArgumentException("Gagal update akun");
            }
        }
        return result;
    }

}
