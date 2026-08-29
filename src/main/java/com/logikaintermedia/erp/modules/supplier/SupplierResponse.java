package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SupplierResponse {
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
    private String supplierPaymentTermDays;
    private BigDecimal supplierCreditLimit = BigDecimal.ZERO;
    private String supplierBankName;
    private String supplierBankAccountNumber;
    private String supplierBankAccountName;
    private Boolean supplierPkp;
    private Boolean supplierIsActive;
    private UUID companyId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime supplierCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime supplierUpdatedAt;

    private UUID userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime supplierDeletedAt;

}
