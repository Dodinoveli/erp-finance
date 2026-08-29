package com.logikaintermedia.erp.modules.coa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CoaService {
    private CoaRepository coaRepository;

    public List<CoaResponse> getCoaTree(UUID companyId) {
        // 1. Ambil data flat murni dari Repository
        List<Coa> flatList = coaRepository.fetchPage(companyId);
        // System.out.println("=== DEBUG SERVICE ===");
        // System.out.println("=== DEBUG COMPANY ID ===" + companyId);
        // System.out.println("1. Jumlah data mentah dari DB: " + (flatList != null ?
        // flatList.size() : "NULL"));

        List<CoaResponse> rootNodes = new ArrayList<>();
        Map<UUID, CoaResponse> nodeMap = new LinkedHashMap<>();

        // 2. Langkah Pertama: Masukkan ke Map pembantu menggunakan CoaResponse
        for (Coa coa : flatList) {
            nodeMap.put(coa.getCoaId(), new CoaResponse(coa));
        }

        // 3. Langkah Kedua: Hubungkan struktur pohonnya (Induk -> Anak)
        for (Coa coa : flatList) {
            CoaResponse currentNode = nodeMap.get(coa.getCoaId());
            UUID parentId = coa.getParentId();

            if (parentId != null && nodeMap.containsKey(parentId)) {
                nodeMap.get(parentId).getChildren().add(currentNode);
            } else {
                rootNodes.add(currentNode);
            }
        }

        return rootNodes;
    }
}
