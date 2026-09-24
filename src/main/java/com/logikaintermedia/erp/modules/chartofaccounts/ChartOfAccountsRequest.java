package com.logikaintermedia.erp.modules.chartofaccounts;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ChartOfAccountsRequest {
    private String accountCode;
    private List<String> accountName;
    private String accountType;
    private String normalBalance;
    private UUID parentId;
    private Short accountLevel;
    private Boolean isHeader;
    private Boolean isPostable;
    private String description;
    private Integer sortOrder;
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    private OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

    private UUID accountId;
    private UUID companyId;

}
