package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChartOfAccountsServiceImpl implements ChartOfAccountsService {

    private final ChartOfAccountsRepository repository;

    public ChartOfAccountsServiceImpl(ChartOfAccountsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
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
    @Override
    public List<ChartOfAccounts> findByParentId(UUID parentId) {
        return repository.findByParentId(parentId);
    }

    @Transactional
    @Override
    public int initializeCompanyCoa(UUID companyId) {
        return repository.initializeCompanyCoa(companyId);
    }

    @Override
    public int save(ChartOfAccounts account) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(ChartOfAccounts account) {
        // TODO Auto-generated method stub
        return 0;
    }

}
