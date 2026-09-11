package com.logikaintermedia.erp.modules.client;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Client {
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

    private OffsetDateTime clientCreatedAt;
    private UUID userId;
    private OffsetDateTime clientUpdatedAt;
    private OffsetDateTime clientDeletedAt;

    private String clientBankName;
    private String clientAccountNumber;
    private String clientAccountName;

}
