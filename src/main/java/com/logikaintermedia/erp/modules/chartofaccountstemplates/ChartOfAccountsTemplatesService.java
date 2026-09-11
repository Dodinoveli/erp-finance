package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountsTemplatesService {

    private final ChartOfAccountsTemplatesRepository repository;

    public ChartOfAccountsTemplatesService(ChartOfAccountsTemplatesRepository repository) {
        this.repository = repository;
    }

    public List<ChartOfAccountsTemplatesResponse> getFindAll() {
        // 1. Ambil data flat murni dari Repository
        List<ChartOfAccountsTemplates> flatList = repository.findAll();
        // System.out.println("=== DEBUG SERVICE ===");
        // System.out.println("=== DEBUG COMPANY ID ===" + companyId);
        // System.out.println("1. Jumlah data mentah dari DB: " + (flatList != null ?
        // flatList.size() : "NULL"));

        List<ChartOfAccountsTemplatesResponse> rootNodes = new ArrayList<>();
        Map<UUID, ChartOfAccountsTemplatesResponse> nodeMap = new LinkedHashMap<>();

        // 2. Langkah Pertama: Masukkan ke Map pembantu menggunakan CoaResponse
        for (ChartOfAccountsTemplates coa : flatList) {
            nodeMap.put(coa.getTemplateAccountId(), new ChartOfAccountsTemplatesResponse(coa));
        }

        // 3. Langkah Kedua: Hubungkan struktur pohonnya (Induk -> Anak)
        for (ChartOfAccountsTemplates coa : flatList) {
            ChartOfAccountsTemplatesResponse currentNode = nodeMap.get(coa.getTemplateAccountId());
            UUID parentId = coa.getParentTemplateId();

            if (parentId != null && nodeMap.containsKey(parentId)) {
                nodeMap.get(parentId).getChildren().add(currentNode);
            } else {
                rootNodes.add(currentNode);
            }
        }
        return rootNodes;
    }

}
