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
    private int total;

    /**
     * Factory method untuk mengonversi data CoaMapping
     * menjadi CoaMappingResponse sebagai response API.
     */
    public static CoaMappingResponse from(CoaMapping mapping) {
        CoaMappingResponse response = new CoaMappingResponse();
        response.setMappingId(mapping.getMappingId());
        response.setTransactionType(mapping.getTransactionType());
        response.setPaymentType(mapping.getPaymentType());
        response.setDescription(mapping.getDescription());
        response.setCompanyId(mapping.getCompanyId());
        response.setTotal(mapping.getTotal());
        return response;
    }
}
