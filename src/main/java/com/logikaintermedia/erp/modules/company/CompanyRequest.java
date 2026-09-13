package com.logikaintermedia.erp.modules.company;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequest {
    private UUID companyId;

    @NotBlank(message = "Nama Perusahaan wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String legalName;

    @NotBlank(message = "Kode wajib diisi", groups = {
            OnUpdate.class })
    private String companyCode;

    // @NotBlank(message = "Npwp wajib diisi", groups = { OnCreate.class,
    // OnUpdate.class })
    private String npwp;

    // @NotBlank(message = "Nib wajib diisi", groups = { OnCreate.class,
    // OnUpdate.class })
    private String nib;

    @NotBlank(message = "Alamat wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String address;

    @NotBlank(message = "Kota wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String city;

    @NotBlank(message = "Propinsi wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String province;

    @NotBlank(message = "Kode Post wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String postalCode;

    // @NotBlank(message = "Negara wajib diisi", groups = { OnCreate.class,
    // OnUpdate.class })
    private String country;

    // @NotBlank(message = "Telpon wajib diisi", groups = { OnCreate.class,
    // OnUpdate.class })
    private String phone;

    @NotBlank(message = "Email wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String email;

    @NotBlank(message = "Tipe Perusahaan wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String companyType;

    private Boolean pkp;
    private String baseCurrency;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    @NotBlank(message = "username wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String userName;

    @NotBlank(message = "Password wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    @Size(min = 6, message = "Password minimal 6 karakter", groups = { OnCreate.class, OnUpdate.class })
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$", message = "Password minimal 6 karakter dan harus mengandung huruf serta angka", groups = {
            OnCreate.class, OnUpdate.class })
    private String password;
}
