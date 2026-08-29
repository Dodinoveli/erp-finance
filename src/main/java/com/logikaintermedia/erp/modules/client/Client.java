package com.logikaintermedia.erp.modules.client;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime clientCreatedAt;

    private UUID userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime clientUpdatedAt;
    // private UUID updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime clientDeletedAt;

    private String clientBankName;
    private String clientAccountNumber;
    private String clientAccountName;

}
