package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

import lombok.Data;

@Data
public class BankAccounts {
    private UUID bankAccountId;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String bankName;
    private String bankBranch;
    private String accountNumber;
    private String accountHolder;
    private String currency = "IDR";
    private BigDecimal openingBalance = BigDecimal.ZERO;
    private Boolean isDefault = false;
    private Boolean isActive = true;
    private UUID companyId;
    
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID userId;
    private OffsetDateTime deletedAt;


    // relasi ke tabel joa
    private UUID accountId;
    // hasil json
    private  ChartOfAccounts coa;
}
