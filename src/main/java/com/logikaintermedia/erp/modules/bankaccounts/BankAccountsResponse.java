package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class BankAccountsResponse {
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
    private Boolean isActive;
    // private UUID companyId;
    private UUID coaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private UUID userId;
    private LocalDateTime deletedAt;
    private String name;

}
