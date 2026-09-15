package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ChartOfAccountsTemplatesResponse {
    private UUID templateAccountId;
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
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private UUID accountId;
    private UUID companyId;

    // Sekarang List ini menampung dirinya sendiri dengan nama CoaResponse
    private List<ChartOfAccountsTemplatesResponse> children = new ArrayList<>();

    public ChartOfAccountsTemplatesResponse(ChartOfAccountsTemplates coa) {
        this.templateAccountId = coa.getTemplateAccountId();
        this.accountCode = coa.getAccountCode();
        this.accountName = coa.getAccountName();
        this.accountType = coa.getAccountType();
        this.normalBalance = coa.getNormalBalance();
        this.parentTemplateId = coa.getParentTemplateId();
        this.accountLevel = coa.getAccountLevel();
        this.isHeader = coa.getIsHeader();
        this.isPostable = coa.getIsPostable();
        this.description = coa.getDescription();
        this.sortOrder = coa.getSortOrder();
        this.createdAt = coa.getCreatedAt();
        this.updatedAt = coa.getUpdatedAt();
    }

    public List<ChartOfAccountsTemplatesResponse> getChildren() {
        return children;
    }

}
