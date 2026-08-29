package com.logikaintermedia.erp.modules.coa;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coa {
    private UUID coaId;
    private UUID companyId;
    private UUID parentId;
    private String coaCode;
    private String name;
    private String accountCategory;
    private boolean isHeader;
    private String normalBalance;
}
