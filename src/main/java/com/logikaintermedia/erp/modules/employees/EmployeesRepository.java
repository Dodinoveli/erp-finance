package com.logikaintermedia.erp.modules.employees;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.encryption.EncryptionUtil;

@Repository
public class EmployeesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EmployeesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // params name gunakan variabel versi Java
    private @NonNull MapSqlParameterSource toParams(Employees emp) {
        return new MapSqlParameterSource()
                .addValue("employeeId", emp.getEmployeeId())
                .addValue("employeeCode", emp.getEmployeeCode())
                .addValue("employeeName", emp.getEmployeeName())
                .addValue("employeeNik", EncryptionUtil.encrypt(emp.getEmployeeNik()))
                .addValue("employeeNpwp", EncryptionUtil.encrypt(emp.getEmployeeNpwp()))
                .addValue("employeeGender", emp.getEmployeeGender())
                .addValue("employeeBirthDate", emp.getEmployeeBirthDate())
                .addValue("employeePhone", EncryptionUtil.encrypt(emp.getEmployeePhone()))
                .addValue("employeeEmail", EncryptionUtil.encrypt(emp.getEmployeeEmail()))
                .addValue("employeeAddress", emp.getEmployeeAddress())
                .addValue("employeeDepartment", emp.getEmployeeDepartment())
                .addValue("employeeJobPosition", emp.getEmployeeJobPosition())
                .addValue("employeeEmploymentType", emp.getEmployeeEmploymentType())
                .addValue("employeeJoinDate", emp.getEmployeeJoinDate())
                .addValue("employeeSalaryType", emp.getEmployeeSalaryType())
                .addValue("employeeBasicSalary", emp.getEmployeeBasicSalary())
                .addValue("employeeDailyWage", emp.getEmployeeDailyWage())
                .addValue("employeeBankName", emp.getEmployeeBankName())
                .addValue("employeeBankAccountNumber", EncryptionUtil.encrypt(emp.getEmployeeBankAccountNumber()))
                .addValue("employeeBankAccountName", emp.getEmployeeBankAccountName())
                .addValue("employeeIsActive", emp.getEmployeeIsActive())
                .addValue("companyId", emp.getCompanyId())
                .addValue("userId", emp.getUserId())
                .addValue("employeeCreatedAt", emp.getEmployeeCreatedAt())
                .addValue("employeeUpdatedAt", emp.getEmployeeUpdatedAt())
                .addValue("employeeDeletedAt", emp.getEmployeeDeletedAt());
    }

    public int save(Employees emp) {
        String sql = """
                    INSERT INTO public.employees(
                        employee_id, employee_code, employee_name, employee_nik, employee_npwp,
                        employee_gender, employee_birth_date, employee_phone, employee_email, employee_address,
                        employee_department, employee_job_position, employee_employment_type,
                        employee_join_date,       employee_salary_type,
                        employee_basic_salary, employee_daily_wage, employee_bank_name,
                        employee_bank_account_number, employee_bank_account_name,
                        employee_is_active, company_id, employee_created_at, user_id, employee_updated_at,
                        employee_deleted_at
                    )
                    VALUES (
                        :employeeId, :employeeCode, :employeeName, :employeeNik, :employeeNpwp,
                        :employeeGender, :employeeBirthDate, :employeePhone, :employeeEmail, :employeeAddress,
                        :employeeDepartment, :employeeJobPosition, :employeeEmploymentType,
                        :employeeJoinDate, :employeeSalaryType,
                        :employeeBasicSalary, :employeeDailyWage, :employeeBankName,
                        :employeeBankAccountNumber, :employeeBankAccountName,
                        :employeeIsActive, :companyId, :employeeCreatedAt, :userId, :employeeUpdatedAt,
                        :employeeDeletedAt
                    );
                """;
        return jdbcTemplate.update(sql, toParams(emp));
    }

    public int update(Employees emp) {
        String sql = """
                    UPDATE public.employees SET
                        employee_code = :employeeCode,
                        employee_name = :employeeName,
                        employee_nik = :employeeNik,
                        employee_npwp = :employeeNpwp,
                        employee_gender = :employeeGender,
                        employee_birth_date = :employeeBirthDate,
                        employee_phone = :employeePhone,
                        employee_email = :employeeEmail,
                        employee_address = :employeeAddress,
                        employee_department = :employeeDepartment,
                        employee_job_position = :employeeJobPosition,
                        employee_employment_type = :employeeEmploymentType,
                        employee_join_date = :employeeJoinDate,
                        employee_salary_type = :employeeSalaryType,
                        employee_basic_salary = :employeeBasicSalary,
                        employee_daily_wage = :employeeDailyWage,
                        employee_bank_name = :employeeBankName,
                        employee_bank_account_number = :employeeBankAccountNumber,
                        employee_bank_account_name = :employeeBankAccountName,
                        employee_is_active = :employeeIsActive,
                        employee_updated_at = :employeeUpdatedAt,
                        employee_deleted_at = :employeeDeletedAt
                    WHERE employee_id = :employeeId and user_id = :userId and company_id = :companyId ;
                """;
        return jdbcTemplate.update(sql, toParams(emp));
    }

    public String code(UUID companyId) {
        String sql = """
                SELECT employee_code
                FROM employees
                WHERE company_id = :companyId
                ORDER BY employee_code DESC
                LIMIT 1
                """;

        try {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("companyId", companyId);
            String lastCode = jdbcTemplate.queryForObject(sql, params, String.class);

            if (lastCode != null && lastCode.startsWith("EMP-")) {
                int number = Integer.parseInt(lastCode.substring(4));
                number++;
                return String.format("EMP-%04d", number);
            }
        } catch (EmptyResultDataAccessException e) {
            return "EMP-0001";
        } catch (Exception e) {
            System.err.println("Error generating code: " + e.getMessage());
        }
        return "EMP-0001";
    }

    public String generateEmployeesCode(UUID companyId) {
        String lastCode = null;

        // 1. Ambil kode terakhir dengan LOCK (FOR UPDATE)
        String sqlGetLast = """
                SELECT employee_code FROM employees
                WHERE company_id = :companyId
                ORDER BY employee_code DESC
                LIMIT 1 FOR UPDATE
                """;
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("companyId", companyId);
            lastCode = jdbcTemplate.queryForObject(sqlGetLast, data, String.class);
        } catch (EmptyResultDataAccessException e) {
            // Biarkan lastCode tetap null jika belum ada data sama sekali
        }

        // 2. Logika increment nomor urut
        int newNumber = 1;
        if (lastCode != null && lastCode.startsWith("EMP-")) {
            try {
                // Mengambil angka setelah "EMP-"
                newNumber = Integer.parseInt(lastCode.substring(4)) + 1;
            } catch (NumberFormatException e) {
                newNumber = 1; // Fallback jika format kode rusak
            }
        }

        return String.format("EMP-%04d", newNumber);
    }

    public List<Employees> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt, UUID lastId,
            int limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        params.put("limit", limit);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            params.put("keyword", keyword + "%");
        }

        String sql = """
                SELECT * FROM public.employees
                WHERE company_id = :companyId
                       """;

        if (hasKeyword) {
            sql += " AND (employee_name ILIKE :keyword)";
        }

        // 🔥 keyset filter (optional)
        if (lastCreatedAt != null && lastId != null) {
            sql += """
                        AND (
                                employee_created_at < :lastCreatedAt
                                OR (employee_created_at = :lastCreatedAt AND employee_id < :lastId)
                            )
                    """;
            params.put("lastCreatedAt", lastCreatedAt);
            params.put("lastId", lastId);
        }

        sql += " ORDER BY employee_created_at DESC, employee_id DESC LIMIT :limit ";
        List<Employees> result = jdbcTemplate.query(sql, params, new EmployeesMapper());

        System.out.println("===== QUERY RESULT =====");
        System.out.println("SIZE = " + result.size());

        for (Employees c : result) {
            System.out.println(
                    "ID=" + c.getEmployeeId()
                            + " | CREATED_AT=" + c.getEmployeeCreatedAt());
        }
        return result;
    }

}
