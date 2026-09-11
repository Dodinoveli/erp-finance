package com.logikaintermedia.erp.modules.employees;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class EmployeesResponse {
    private UUID employeeId;
    private String employeeCode;
    private String employeeName;
    private String employeeNik;
    private String employeeNpwp;
    private String employeeGender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employeeBirthDate;

    private String employeePhone;
    private String employeeEmail;
    private String employeeAddress;
    private String employeeDepartment;
    private String employeeJobPosition;
    private String employeeEmploymentType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employeeJoinDate;

    private String employeeSalaryType;
    private BigDecimal employeeBasicSalary = BigDecimal.ZERO;
    private BigDecimal employeeDailyWage = BigDecimal.ZERO;
    private String employeeBankName;
    private String employeeBankAccountNumber;
    private String employeeBankAccountName;
    private Boolean employeeIsActive;

    private OffsetDateTime employeeCreatedAt;
    private OffsetDateTime employeeUpdatedAt;
    private OffsetDateTime employeeDeletedAt;

}
