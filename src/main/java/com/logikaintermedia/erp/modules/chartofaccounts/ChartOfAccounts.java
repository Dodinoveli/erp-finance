package com.logikaintermedia.erp.modules.chartofaccounts;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ChartOfAccounts {
    private String accountCode;
    private String accountName;
    private String accountType;
    private String normalBalance;
    private UUID parentId;
    private Short accountLevel;
    private Boolean isHeader;
    private Boolean isPostable;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UUID accountId;
    private UUID companyId;

    // parent
    private String parentAccountCode;
    private String parentAccountName;

    private UUID childAccountId;
    private String childAccountCode;
    private String childAccountName;

}
