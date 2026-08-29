package com.logikaintermedia.erp.modules.coa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoaResponse {
    private UUID coaId;
    private UUID parentId;
    private String coaCode;
    private String name;
    private String accountCategory;
    private boolean isHeader;
    private String normalBalance;

    // Sekarang List ini menampung dirinya sendiri dengan nama CoaResponse
    private List<CoaResponse> children = new ArrayList<>();

    public CoaResponse(Coa coa) {
        this.coaId = coa.getCoaId();
        this.coaCode = coa.getCoaCode();
        this.name = coa.getName();
        this.accountCategory = coa.getAccountCategory();
        this.parentId = coa.getParentId();
        this.isHeader = coa.isHeader();
        this.normalBalance = coa.getNormalBalance();
    }
}
