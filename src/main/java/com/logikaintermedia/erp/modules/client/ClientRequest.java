package com.logikaintermedia.erp.modules.client;

import java.util.UUID;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientRequest {
        private UUID clientId;

        @NotBlank(message = "Kode client wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientCode;

        @NotBlank(message = "Nama wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientName;

        @NotBlank(message = "Tipe Klien wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientType;

        @Pattern(regexp = "^\\d{15,16}$", message = "NPWP harus berupa 15 atau 16 digit angka ", groups = {
                        OnCreate.class, OnUpdate.class })
        private String clientNpwp;

        @Pattern(regexp = "^\\d{16}$", message = "Nik harus berupa 16 angka ", groups = { OnCreate.class,
                        OnUpdate.class })
        private String clientNik;

        @Pattern(regexp = "^\\d{22}$", message = "Nik harus berupa 22 angka ", groups = { OnCreate.class,
                        OnUpdate.class })
        private String clientNitku;

        @NotNull(message = "Pkp wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private Boolean clientIsPkp;

        @NotBlank(message = "Alamat wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientAddress;

        @NotBlank(message = "Kota wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientCity;

        @NotBlank(message = "Propinsi wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientProvince;

        @NotBlank(message = "Kode Pos wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^\\d{5}", message = "Kode Pos Wajib Angka 5 digit", groups = { OnCreate.class,
                        OnUpdate.class })
        private String clientPostalCode;

        @NotBlank(message = "Negara wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientCountry;

        @Email(message = "Format email tidak valid", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email tidak boleh dimulai dengan simbol", groups = {
                        OnCreate.class, OnUpdate.class })
        private String clientEmail;

        @NotBlank(message = "Nama Kontak wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String clientContactPerson;

        @Pattern(regexp = "^\\d{10,15}", message = "No Telp harus angka 10-15 digit", groups = { OnCreate.class,
                        OnUpdate.class })
        private String clientContactPhone;

        private Boolean clientIsActive;
        private String clientBankName;

        @Pattern(regexp = "^\\d{10,20}$", message = "Nomor rekening harus berupa angka dengan panjang 10-20 digit", groups = {
                        OnCreate.class, OnUpdate.class })
        private String clientAccountNumber;

        private String clientAccountName;

        public void setClientNik(String clientNik) {
                // Logika: Jika dari FE dikirim "" (kosong), ubah jadi NULL
                this.clientNik = (clientNik == null || clientNik.isBlank()) ? null : clientNik;
        }

        public void setClientNpwp(String clientNpwp) {
                this.clientNpwp = (clientNpwp == null || clientNpwp.isBlank()) ? null : clientNpwp;
        }

        public void setClientNitku(String clientNitku) {
                this.clientNitku = (clientNitku == null || clientNitku.isBlank()) ? null : clientNitku;
        }

        public void setClientAccountNumber(String clientAccountNumber) {
                this.clientAccountNumber = (clientAccountNumber == null || clientAccountNumber.isBlank()) ? null
                                : clientAccountNumber;
        }

        public void setClientEmail(String clientEmail) {
                this.clientEmail = (clientEmail == null || clientEmail.isBlank()) ? null : clientEmail;
        }

        public void setClientContactPhone(String clientContactPhone) {
                this.clientContactPhone = (clientContactPhone == null || clientContactPhone.trim().isEmpty()) ? null
                                : clientContactPhone;
        }

}
