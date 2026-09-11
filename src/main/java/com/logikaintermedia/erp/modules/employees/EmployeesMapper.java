package com.logikaintermedia.erp.modules.employees;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.logikaintermedia.erp.encryption.EncryptionUtil;

public class EmployeesMapper implements RowMapper<Employees> {

    @Override
    public Employees mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Employees emp = new Employees();
        emp.setEmployeeId(rs.getObject("employee_id", UUID.class));
        emp.setEmployeeCode(rs.getString("employee_code"));
        emp.setEmployeeName(rs.getString("employee_name"));
        emp.setEmployeeNik(EncryptionUtil.decrypt(rs.getString("employee_nik")));
        emp.setEmployeeNpwp(EncryptionUtil.decrypt(rs.getString("employee_npwp")));
        emp.setEmployeeGender(rs.getString("employee_gender"));
        emp.setEmployeeBirthDate(rs.getObject("employee_birth_date", LocalDate.class));
        emp.setEmployeePhone(EncryptionUtil.decrypt(rs.getString("employee_phone")));
        emp.setEmployeeEmail(EncryptionUtil.decrypt(rs.getString("employee_email")));
        emp.setEmployeeAddress(rs.getString("employee_address"));
        emp.setEmployeeDepartment(rs.getString("employee_department"));
        emp.setEmployeeJobPosition(rs.getString("employee_job_position"));
        emp.setEmployeeEmploymentType(rs.getString("employee_employment_type"));
        emp.setEmployeeJoinDate(rs.getObject("employee_join_date", LocalDate.class));
        emp.setEmployeeSalaryType(rs.getString("employee_salary_type"));
        emp.setEmployeeBasicSalary(rs.getBigDecimal("employee_basic_salary"));
        emp.setEmployeeDailyWage(rs.getBigDecimal("employee_daily_wage"));
        emp.setEmployeeBankName(rs.getString("employee_bank_name"));
        emp.setEmployeeBankAccountNumber(EncryptionUtil.decrypt(rs.getString("employee_bank_account_number")));
        emp.setEmployeeBankAccountName(rs.getString("employee_bank_account_name"));
        emp.setEmployeeIsActive(rs.getBoolean("employee_is_active"));
        emp.setCompanyId(rs.getObject("company_id", UUID.class));
        emp.setEmployeeCreatedAt(rs.getObject("employee_created_at", OffsetDateTime.class));
        emp.setEmployeeUpdatedAt(rs.getObject("employee_updated_at", OffsetDateTime.class));
        emp.setEmployeeDeletedAt(rs.getObject("employee_deleted_at", OffsetDateTime.class));
        emp.setUserId(rs.getObject("user_id", UUID.class));
        return emp;
    }

}
