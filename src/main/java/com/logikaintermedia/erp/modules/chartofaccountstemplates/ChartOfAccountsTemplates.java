package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountsTemplates {
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UUID accountId;
    private UUID companyId;

}
