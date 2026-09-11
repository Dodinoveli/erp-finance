package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Supplier {
    private UUID supplierId;
    private String supplierCode;
    private String supplierName;
    private String supplierType;
    private String supplierNpwp;
    private String supplierAddress;
    private String supplierCity;
    private String supplierProvince;
    private String supplierPostalCode;
    private String supplierCountry;
    private String supplierEmail;
    private String supplierContactPerson;
    private String supplierContactPhone;
    private Integer supplierPaymentTermDays;
    private BigDecimal supplierCreditLimit = BigDecimal.ZERO;
    private String supplierBankName;
    private String supplierBankAccountNumber;
    private String supplierBankAccountName;
    private Boolean supplierPkp;
    private Boolean supplierIsActive;
    private UUID companyId;

    private OffsetDateTime supplierCreatedAt;
    private OffsetDateTime supplierUpdatedAt;
    private UUID userId;
    private OffsetDateTime supplierDeletedAt;

}
