package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ChartOfAccountsBatchRequest {
    private UUID parentId;
    private List<ChartOfAccounts> accounts;
}
