package com.logikaintermedia.erp.modules.chartofaccounts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ChartOfAccountsResponse {
    // private UUID templateAccountId;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String normalBalance;
    private UUID parentTemplateId;
    private Short accountLevel;
    private Boolean isHeader;
    private Boolean isPostable;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UUID accountId;
    private UUID companyId;

    private List<ChartOfAccountsResponse> children = new ArrayList<>();

    public ChartOfAccountsResponse(ChartOfAccounts coa) {
        this.accountId = coa.getAccountId();
        this.accountCode = coa.getAccountCode();
        this.accountName = coa.getAccountName();
        this.accountType = coa.getAccountType();
        this.normalBalance = coa.getNormalBalance();
        // this.parentTemplateId = coa.getParentTemplateId();
        this.accountLevel = coa.getAccountLevel();
        this.isHeader = coa.getIsHeader();
        this.isPostable = coa.getIsPostable();
        this.description = coa.getDescription();
        this.sortOrder = coa.getSortOrder();
        this.createdAt = coa.getCreatedAt();
        this.updatedAt = coa.getUpdatedAt();
    }

    public List<ChartOfAccountsResponse> getChildren() {
        return children;
    }

}
