package com.logikaintermedia.erp.modules.coamapping;

import java.util.UUID;
import lombok.Data;

@Data
public class CoaMappingResponse {
    private UUID mappingId;
    private TransactionType transactionType;
    private PaymentType paymentType;
    private String description;
    private UUID companyId;
}
