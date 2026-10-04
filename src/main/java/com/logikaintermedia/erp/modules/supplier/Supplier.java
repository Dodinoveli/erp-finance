package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import com.fasterxml.uuid.Generators;
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

    /**
     * Membuat Supplier baru dari SupplierRequest.
     * 
     * @param request   data Supplier
     * @param code      kode Supplier
     * @param companyId ID perusahaan
     * @param userId    ID user pembuat data
     * @return Supplier baru
     */
    public static Supplier from(SupplierRequest dto, UUID companyId, UUID userId, String code) {
        Supplier model = new Supplier();
        model.setSupplierId(Generators.timeBasedEpochRandomGenerator().generate());
        model.setSupplierCode(code);
        model.setSupplierName(dto.getSupplierName());
        model.setSupplierType(dto.getSupplierType());
        model.setSupplierNpwp(dto.getSupplierNpwp());
        model.setSupplierAddress(dto.getSupplierAddress());
        model.setSupplierCity(dto.getSupplierCity());
        model.setSupplierProvince(dto.getSupplierProvince());
        model.setSupplierPostalCode(dto.getSupplierPostalCode());
        model.setSupplierCountry(dto.getSupplierCountry());
        model.setSupplierEmail(dto.getSupplierEmail());
        model.setSupplierContactPerson(dto.getSupplierContactPerson());
        model.setSupplierContactPhone(dto.getSupplierContactPhone());
        model.setSupplierPaymentTermDays(0);
        model.setSupplierCreditLimit(BigDecimal.ZERO);
        model.setSupplierBankName(dto.getSupplierBankName());
        model.setSupplierBankAccountNumber(dto.getSupplierBankAccountNumber());
        model.setSupplierBankAccountName(dto.getSupplierBankAccountName());
        model.setSupplierPkp(dto.getSupplierPkp());
        model.setSupplierIsActive(true);
        model.setCompanyId(companyId);
        model.setSupplierCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setSupplierUpdatedAt(null);
        model.setSupplierDeletedAt(null);
        model.setUserId(userId);
        return model;
    }

    /**
     * Membuat perubahan data Supplier berdasarkan SupplierRequest.
     *
     * @param dto        data Supplier yang diperbarui
     * @param SupplierId ID Supplier yang diperbarui
     * @param companyId  ID perusahaan pemilik data
     * @return Supplier dengan data yang telah diperbarui
     */
    public static Supplier updateFrom(SupplierRequest dto, UUID supplierId, UUID companyId) {
        Supplier model = new Supplier();
        model.setSupplierCode(dto.getSupplierCode());
        model.setSupplierName(dto.getSupplierName());
        model.setSupplierType(dto.getSupplierType());
        model.setSupplierNpwp(dto.getSupplierNpwp());
        model.setSupplierAddress(dto.getSupplierAddress());
        model.setSupplierCity(dto.getSupplierCity());
        model.setSupplierProvince(dto.getSupplierProvince());
        model.setSupplierPostalCode(dto.getSupplierPostalCode());
        model.setSupplierCountry(dto.getSupplierCountry());
        model.setSupplierEmail(dto.getSupplierEmail());
        model.setSupplierContactPerson(dto.getSupplierContactPerson());
        model.setSupplierContactPhone(dto.getSupplierContactPhone());
        model.setSupplierPaymentTermDays(0);
        model.setSupplierCreditLimit(BigDecimal.ZERO);
        model.setSupplierBankName(dto.getSupplierBankName());
        model.setSupplierBankAccountNumber(dto.getSupplierBankAccountNumber());
        model.setSupplierBankAccountName(dto.getSupplierBankAccountName());
        model.setSupplierPkp(dto.getSupplierPkp());
        model.setSupplierIsActive(dto.getSupplierIsActive() != null ? dto.getSupplierIsActive() : true);
        model.setSupplierUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setSupplierId(supplierId);
        model.setCompanyId(companyId);
        return model;
    }
}
