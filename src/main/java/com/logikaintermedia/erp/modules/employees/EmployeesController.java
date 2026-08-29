package com.logikaintermedia.erp.modules.employees;

import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.CursorResponse;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeesController {

    private final EmployeesService service;

    public EmployeesController(EmployeesService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/generate-code")
    public ResponseEntity<?> generateCode(@AuthenticationPrincipal AuthUserPrincipal principal) {
        String employeeCode = service.generateCode(principal.getCompanyId());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("employeeCode", employeeCode);
        return ResponseEntity.ok(ApiResponse.success("success", data));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorResponse<EmployeesResponse>>> getById(
            @AuthenticationPrincipal AuthUserPrincipal user,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(required = false) UUID lastId,
            @RequestParam(defaultValue = "15") int limit) {
        UUID companyId = user.getCompanyId();
        CursorResponse<EmployeesResponse> response = service.getEmployeesById(companyId, keyword, lastCreatedAt, lastId,
                limit);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody EmployeesRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Employees response = service.createEmployees(entity, user.getCompanyId(), user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Data Karyawan berhasil disimpan", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody EmployeesRequest entity,
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Employees data = Objects
                .requireNonNull(service.updateEmployees(entity, id, user.getUserId(), user.getCompanyId()));
        EmployeesResponse result = new EmployeesResponse();
        BeanUtils.copyProperties(data, result);
        return ResponseEntity.ok(ApiResponse.success("Data Karyawan berhasil di perbaharui...", result));
    }

}
