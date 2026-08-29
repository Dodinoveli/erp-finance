package com.logikaintermedia.erp.modules.company;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CompanyResponse {
    private UUID companyId;
    private String legalName;
    private String companyCode;
    private String npwp;
    private String nib;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String phone;
    private String email;
    // private String website;
    private String companyType;
    // private OffsetDateTime establishedDate;
    private Boolean pkp;
    // private String taxNumber;
    private String baseCurrency;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    // private UUID createdBy;
    private OffsetDateTime updatedAt;
    // private UUID updatedBy;
    private OffsetDateTime deletedAt;
}
