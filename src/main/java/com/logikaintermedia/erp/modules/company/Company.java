package com.logikaintermedia.erp.modules.company;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Company {
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
    private String companyType;
    private Boolean pkp;
    private String baseCurrency;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private UUID createdBy;
    private OffsetDateTime updatedAt;
}
