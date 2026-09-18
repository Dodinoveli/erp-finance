package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

import lombok.Data;

@Data
public class BankAccountsResponse {
    private UUID bankAccountId;
    private String accountCode;
    private String accountName;
    private String bankName;
    private String bankBranch;
    private String accountNumber;
    private String accountHolder;
    private String currency = "IDR";
    private BigDecimal openingBalance = BigDecimal.ZERO;
    private Boolean isDefault = false;
    private Boolean isActive;
    // private UUID companyId;
    // private UUID AccountId;
    private  ChartOfAccounts coa;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    // private UUID userId;
    private OffsetDateTime deletedAt;
    private String name;

    private String parentAccountCode;
    private String parentAccountName;
    private String childAccountCode;
    private String childAccountName;
    
}
