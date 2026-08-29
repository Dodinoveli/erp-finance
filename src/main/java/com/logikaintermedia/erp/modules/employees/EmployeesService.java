package com.logikaintermedia.erp.modules.employees;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.utility.CursorResponse;

@Service
public class EmployeesService {

    private final EmployeesRepository repository;

    public EmployeesService(EmployeesRepository repository) {
        this.repository = repository;
    }

    public String generateCode(UUID companyId) {
        return repository.code(companyId);
    }

    public CursorResponse<EmployeesResponse> getEmployeesById(UUID companyId, String keyword,
            LocalDateTime lastCreatedAt,
            UUID lastId,
            int limit) {

        List<Employees> entities = repository.findById(companyId, keyword, lastCreatedAt, lastId, limit + 1);
        Boolean hasNext = entities.size() > limit;

        List<Employees> currentData = hasNext ? entities.subList(0, limit) : entities;

        List<EmployeesResponse> list = currentData.stream().map(entity -> {
            EmployeesResponse dto = new EmployeesResponse();
            dto.setEmployeeId(entity.getEmployeeId());
            dto.setEmployeeCode(entity.getEmployeeCode());
            dto.setEmployeeName(entity.getEmployeeName());
            dto.setEmployeeNik(entity.getEmployeeNik());
            dto.setEmployeeNpwp(entity.getEmployeeNpwp());
            dto.setEmployeeGender(entity.getEmployeeGender());
            dto.setEmployeeBirthDate(entity.getEmployeeBirthDate());
            dto.setEmployeePhone(entity.getEmployeePhone());
            dto.setEmployeeEmail(entity.getEmployeeEmail());
            dto.setEmployeeAddress(entity.getEmployeeAddress());
            dto.setEmployeeDepartment(entity.getEmployeeDepartment());
            dto.setEmployeeJobPosition(entity.getEmployeeJobPosition());
            dto.setEmployeeEmploymentType(entity.getEmployeeEmploymentType());
            dto.setEmployeeJoinDate(entity.getEmployeeJoinDate());
            dto.setEmployeeSalaryType(entity.getEmployeeSalaryType());
            dto.setEmployeeBasicSalary(entity.getEmployeeBasicSalary());
            dto.setEmployeeDailyWage(entity.getEmployeeDailyWage());
            dto.setEmployeeBankName(entity.getEmployeeBankName());
            dto.setEmployeeBankAccountNumber(entity.getEmployeeBankAccountNumber());
            dto.setEmployeeBankAccountName(entity.getEmployeeBankAccountName());
            dto.setEmployeeIsActive(entity.getEmployeeIsActive());
            // dto.setCompanyId(companyId);
            dto.setEmployeeCreatedAt(entity.getEmployeeCreatedAt());
            // dto.setUserId(userId);
            dto.setEmployeeUpdatedAt(null);
            dto.setEmployeeDeletedAt(null);
            return dto;
        }).toList();

        // Ambil "Kunci" dari data terakhir (Data ke-10) untuk ambil data 11-20 nanti
        LocalDateTime nextCreatedAt = null;

        UUID nextId = null;

        if (!list.isEmpty()) {
            EmployeesResponse last = list.get(list.size() - 1);
            nextCreatedAt = last.getEmployeeCreatedAt();
            nextId = last.getEmployeeId();
        }
        return new CursorResponse<>(list, nextCreatedAt, nextId, hasNext);
    }

    @Transactional
    public Employees createEmployees(EmployeesRequest emp, UUID companyId, UUID userId) {
        Employees mdl = new Employees();
        mdl.setEmployeeId(Generators.timeBasedEpochRandomGenerator().generate());
        // mdl.setEmployeeCode(emp.getEmployeeCode());
        mdl.setEmployeeName(emp.getEmployeeName());
        mdl.setEmployeeNik(emp.getEmployeeNik());
        mdl.setEmployeeNpwp(emp.getEmployeeNpwp());
        mdl.setEmployeeGender(emp.getEmployeeGender());
        mdl.setEmployeeBirthDate(emp.getEmployeeBirthDate());
        mdl.setEmployeePhone(emp.getEmployeePhone());
        mdl.setEmployeeEmail(emp.getEmployeeEmail());
        mdl.setEmployeeAddress(emp.getEmployeeAddress());
        mdl.setEmployeeDepartment(emp.getEmployeeDepartment());
        mdl.setEmployeeJobPosition(emp.getEmployeeJobPosition());
        mdl.setEmployeeEmploymentType(emp.getEmployeeEmploymentType());
        mdl.setEmployeeJoinDate(emp.getEmployeeJoinDate());
        mdl.setEmployeeSalaryType(emp.getEmployeeSalaryType());
        mdl.setEmployeeBasicSalary(emp.getEmployeeBasicSalary());
        mdl.setEmployeeDailyWage(emp.getEmployeeDailyWage());
        mdl.setEmployeeBankName(emp.getEmployeeBankName());
        mdl.setEmployeeBankAccountNumber(emp.getEmployeeBankAccountNumber());
        mdl.setEmployeeBankAccountName(emp.getEmployeeBankAccountName());
        mdl.setEmployeeIsActive(true);
        mdl.setCompanyId(companyId);
        mdl.setEmployeeCreatedAt(LocalDateTime.now());
        mdl.setUserId(userId);
        mdl.setEmployeeUpdatedAt(null);
        mdl.setEmployeeDeletedAt(null);
        String code = repository.generateEmployeesCode(mdl.getCompanyId());
        mdl.setEmployeeCode(code);

        int data = repository.save(mdl);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return mdl;
    }

    @Transactional
    public Employees updateEmployees(EmployeesRequest emp, UUID employeeId, UUID userId, UUID companyId) {
        Employees mdl = new Employees();
        mdl.setEmployeeCode(emp.getEmployeeCode());
        mdl.setEmployeeName(emp.getEmployeeName());
        mdl.setEmployeeNik(emp.getEmployeeNik());
        mdl.setEmployeeNpwp(emp.getEmployeeNpwp());
        mdl.setEmployeeGender(emp.getEmployeeGender());
        mdl.setEmployeeBirthDate(emp.getEmployeeBirthDate());
        mdl.setEmployeePhone(emp.getEmployeePhone());
        mdl.setEmployeeEmail(emp.getEmployeeEmail());
        mdl.setEmployeeAddress(emp.getEmployeeAddress());
        mdl.setEmployeeDepartment(emp.getEmployeeDepartment());
        mdl.setEmployeeJobPosition(emp.getEmployeeJobPosition());
        mdl.setEmployeeEmploymentType(emp.getEmployeeEmploymentType());
        mdl.setEmployeeJoinDate(emp.getEmployeeJoinDate());
        mdl.setEmployeeSalaryType(emp.getEmployeeSalaryType());
        mdl.setEmployeeBasicSalary(emp.getEmployeeBasicSalary());
        mdl.setEmployeeDailyWage(emp.getEmployeeDailyWage());
        mdl.setEmployeeBankName(emp.getEmployeeBankName());
        mdl.setEmployeeBankAccountNumber(emp.getEmployeeBankAccountNumber());
        mdl.setEmployeeBankAccountName(emp.getEmployeeBankAccountName());
        mdl.setEmployeeIsActive(true);

        mdl.setEmployeeUpdatedAt(LocalDateTime.now());
        mdl.setEmployeeDeletedAt(null);
        // where
        mdl.setEmployeeId(employeeId);
        mdl.setUserId(userId);
        mdl.setCompanyId(companyId);

        int data = repository.update(mdl);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal mengubah data, silakan coba kembali");
        }
        return mdl;
    }

}
