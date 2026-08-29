package com.logikaintermedia.erp.modules.employees;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeesRequest {
        private UUID employeeId;
        private String employeeCode;

        @NotBlank(message = "Nama wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeName;

        @Pattern(regexp = "^\\d{16}$", message = "Nik harus berupa 16 angka ", groups = { OnCreate.class,
                        OnUpdate.class })
        private String employeeNik;

        @Pattern(regexp = "^\\d{15,16}$", message = "NPWP harus berupa 15 atau 16 digit angka ", groups = {
                        OnCreate.class, OnUpdate.class })
        private String employeeNpwp;

        @NotBlank(message = "Jenis kelamin wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeGender;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate employeeBirthDate;

        @NotBlank(message = "No Telp wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^\\d{10,15}", message = "No Telp harus angka 10-15 digit", groups = { OnCreate.class,
                        OnUpdate.class })
        private String employeePhone;

        @Email(message = "Format email tidak valid", groups = { OnCreate.class, OnUpdate.class })
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email tidak boleh dimulai dengan simbol", groups = {
                        OnCreate.class, OnUpdate.class })
        private String employeeEmail;

        @NotBlank(message = "Alamat wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeAddress;

        @NotBlank(message = "Department wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeDepartment;

        @NotBlank(message = "Jabatan wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeJobPosition;

        @NotBlank(message = "Status Karyawan wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeEmploymentType;

        @NotNull(message = "tanggal join wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate employeeJoinDate;

        @NotBlank(message = "Tipe Gaji wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeSalaryType;

        @Digits(integer = 18, fraction = 2, message = "Format angka tidak valid")
        @DecimalMin(value = "0.0", inclusive = false, message = "Gaji harus lebih dari 0")
        private BigDecimal employeeBasicSalary;

        @Digits(integer = 18, fraction = 2, message = "Format angka tidak valid")
        @DecimalMin(value = "0.0", inclusive = false, message = "Gaji harian harus lebih dari 0")
        private BigDecimal employeeDailyWage;

        @NotBlank(message = "Nama Bank wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeBankName;

        @Pattern(regexp = "^\\d{10,20}$", message = "Nomor rekening harus berupa angka dengan panjang 10-20 digit", groups = {
                        OnCreate.class, OnUpdate.class })
        private String employeeBankAccountNumber;

        @NotBlank(message = "Nama Pemilik wajib diisi", groups = { OnCreate.class, OnUpdate.class })
        private String employeeBankAccountName;

        private Boolean employeeIsActive;
        private UUID companyId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime employeeCreatedAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime employeeUpdatedAt;

        private UUID userId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        private LocalDateTime employeeDeletedAt;

        public void setEmployeeBirthDate(LocalDate employeeBirthDate) {
                this.employeeBirthDate = employeeBirthDate;
        }

        public void setEmployeeNpwp(String npwp) {
                this.employeeNpwp = (employeeNpwp == null || employeeNpwp.isBlank()) ? null : employeeNpwp;
        }

        public void setEmployeeNik(String employeeNik) {
                this.employeeNik = (employeeNik == null || employeeNik.isBlank()) ? null : employeeNik;
        }

        public void setEmployeePhone(String employeePhone) {
                this.employeePhone = (employeePhone == null || employeePhone.isBlank()) ? null : employeePhone;
        }

        public void setEmployeeEmail(String employeeEmail) {
                this.employeeEmail = (employeeEmail == null || employeeEmail.isBlank()) ? null : employeeEmail;
        }

        public void setEmployeeBankAccountNumber() {
                this.employeeBankAccountNumber = (employeeBankAccountNumber == null
                                || employeeBankAccountNumber.isBlank())
                                                ? null
                                                : employeeBankAccountNumber;
        }

        // kalau karyawan tetap dan kontrak maka harus pilih gaji bulanan selain itu
        // harian

        @AssertTrue(message = "status karyawan tetap, kontrak gunakan gaji bulanan dan selain itu gaji harian", groups = {
                        OnCreate.class,
                        OnUpdate.class })
        public boolean isEmployeeSalaryType() {
                if (this.employeeEmploymentType == null || this.employeeSalaryType == null) {
                        return true;
                }

                boolean isTetap = this.employeeEmploymentType.equalsIgnoreCase("Tetap");
                boolean isKontrak = this.employeeEmploymentType.equalsIgnoreCase("Kontrak");
                boolean isLepas = this.employeeEmploymentType.equalsIgnoreCase("Lepas");

                if (isTetap) {
                        this.employeeDailyWage = BigDecimal.ZERO;
                        return this.employeeSalaryType.equalsIgnoreCase("Bulanan");
                }

                if (isKontrak) {
                        this.employeeDailyWage = BigDecimal.ZERO;
                        return this.employeeSalaryType.equalsIgnoreCase("Bulanan");
                }

                if (isLepas) {
                        this.employeeBasicSalary = BigDecimal.ZERO;
                        return this.employeeSalaryType.equalsIgnoreCase("Harian");
                }
                return true;

        }

        @AssertTrue(message = "Gaji pokok wajib diisi untuk karyawan bulanan", groups = { OnCreate.class,
                        OnUpdate.class })
        public boolean isBasicSalaryValid() {
                if (employeeSalaryType == null)
                        return true;

                if ("Bulanan".equalsIgnoreCase(employeeSalaryType)) {
                        return employeeBasicSalary != null && employeeBasicSalary.compareTo(BigDecimal.ZERO) > 0;
                }

                return true;
        }

        @AssertTrue(message = "Gaji harian wajib diisi untuk karyawan harian", groups = { OnCreate.class,
                        OnUpdate.class })
        public boolean isDailyWageValid() {
                if (employeeSalaryType == null)
                        return true;

                if ("Harian".equalsIgnoreCase(employeeSalaryType)) {
                        return employeeDailyWage != null && employeeDailyWage.compareTo(BigDecimal.ZERO) > 0;

                }

                return true;
        }

}
