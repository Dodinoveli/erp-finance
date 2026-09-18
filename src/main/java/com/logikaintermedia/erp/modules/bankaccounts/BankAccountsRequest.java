package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BankAccountsRequest {

    private UUID bankAccountId;

    @NotBlank(message = "Kode wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String accountCode;

    @NotBlank(message = "Nama Akun wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String accountName;

    private String bankName;

    private String bankBranch;

    private String accountNumber;

    private String accountHolder;

    private String currency = "IDR";

    private BigDecimal openingBalance = BigDecimal.ZERO;

    private Boolean isDefault;
    private Boolean isActive = true;

    @NotNull(message = "Coa wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private UUID accountId;

    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    private OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

    // private UUID userId;

    private OffsetDateTime deletedAt = OffsetDateTime.now(ZoneOffset.UTC);

    // @AssertTrue(message = "Nama Bank wajib untuk tipe akun BANK", groups = {
    // OnCreate.class,
    // OnUpdate.class })
    // public boolean isBankName() {
    // if ("BANK".equalsIgnoreCase(accountType)) {
    // return bankName != null && !bankName.isBlank();
    // }
    // return true;
    // }

    // @AssertTrue(message = "Cabang wajib untuk tipe akun BANK", groups = {
    // OnCreate.class,
    // OnUpdate.class })
    // public boolean isBankBranch() {
    // if ("BANK".equalsIgnoreCase(accountType)) {
    // return bankBranch != null && !bankBranch.isBlank();
    // }
    // return true;
    // }

    // @AssertTrue(message = "Pemilik Rekening wajib untuk tipe akun BANK", groups =
    // { OnCreate.class,
    // OnUpdate.class })
    // public boolean isAccountHolder() {
    // if ("BANK".equalsIgnoreCase(accountType)) {
    // return accountHolder != null && !accountHolder.isBlank();
    // }
    // return true;
    // }

    // @AssertTrue(message = "Nomor rekening wajib diisi, harus angka dan minimal 10
    // digit untuk tipe akun BANK", groups = {
    // OnCreate.class,
    // OnUpdate.class })
    // public boolean isAccountNumber() {
    // if ("BANK".equalsIgnoreCase(accountType)) {
    // return accountNumber != null && !accountNumber.isBlank()
    // && accountNumber.matches("\\d{10,}");
    // }
    // return true;
    // }

}
