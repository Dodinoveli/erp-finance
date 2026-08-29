package com.logikaintermedia.erp.modules.client;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ClientResponse {
    private UUID clientId;
    private String clientCode;
    private String clientName;
    private String clientType;

    private String clientNpwp;
    private String clientNik;
    private String clientNitku;
    private Boolean clientIsPkp;

    private String clientAddress;
    private String clientCity;
    private String clientProvince;
    private String clientPostalCode;
    private String clientCountry;
    private String clientEmail;

    private String clientContactPerson;
    private String clientContactPhone;

    private Boolean clientIsActive;

    private UUID companyId;

    private LocalDateTime clientCreatedAt;
    private UUID clientCreatedBy;

    private LocalDateTime clientUpdatedAt;
    private UUID clientUpdatedBy;

    private LocalDateTime clientDeletedAt;

    private String clientBankName;
    private String clientAccountNumber;
    private String clientAccountName;
}
