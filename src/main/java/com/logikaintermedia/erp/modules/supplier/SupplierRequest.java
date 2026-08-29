package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SupplierRequest {
        private UUID supplierId;

        @NotBlank(message = "supplierCode wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierCode;

        @NotBlank(message = "Nama wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierName;

        @NotBlank(message = "Tipe wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierType;

        @Pattern(regexp = "^\\d{15,16}$", message = "NPWP harus berupa 15 atau 16 digit angka ", groups = {
                        OnCreate.class, OnUpdate.class })
        private String supplierNpwp;

        @NotBlank(message = "Alamat wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierAddress;

        @NotBlank(message = "Kota wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierCity;

        @NotBlank(message = "Propinsi wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierProvince;

        @NotBlank(message = "Kode Pos wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierPostalCode;

        @NotBlank(message = "Negara wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierCountry;

        @Email(message = "Format email tidak valid", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email tidak boleh dimulai dengan simbol", groups = {
                        OnCreate.class, OnUpdate.class })
        private String supplierEmail;

        @NotBlank(message = "Nama Kontak wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String supplierContactPerson;

        @NotBlank(message = "Telp wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^\\d{10,15}", message = "No Telp harus angka 10-15 digit", groups = { OnCreate.class,
                        OnUpdate.class })
        private String supplierContactPhone;

        private String supplierPaymentTermDays;
        private BigDecimal creditLimit = BigDecimal.ZERO;
        private String supplierBankName;

        @Pattern(regexp = "^\\d{10,20}$", message = "Nomor rekening harus berupa angka dengan panjang 10-20 digit", groups = {
                        OnCreate.class, OnUpdate.class })
        private String supplierBankAccountNumber;

        private String supplierBankAccountName;

        @NotNull(message = "Pkp wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private Boolean supplierPkp;

        private Boolean supplierIsActive;
        private UUID companyId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime supplierCreatedAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime supplierUpdatedAt;

        UUID userId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime supplierDeletedAt;

        public void setSupplierNpwp(String supplierNpwp) {
                this.supplierNpwp = (supplierNpwp == null || supplierNpwp.isBlank()) ? null : supplierNpwp;
        }

        public void setSupplierBankAccountNumber(String supplierBankAccountNumber) {
                this.supplierBankAccountNumber = (supplierBankAccountNumber == null
                                || supplierBankAccountNumber.isBlank()) ? null
                                                : supplierBankAccountNumber;
        }

        public void setSupplierEmail(String supplierEmail) {
                this.supplierEmail = (supplierEmail == null || supplierEmail.isBlank()) ? null : supplierEmail;
        }

}
