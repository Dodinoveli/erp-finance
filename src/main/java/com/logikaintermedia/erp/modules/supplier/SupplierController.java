package com.logikaintermedia.erp.modules.supplier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.DataCountResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping
    public ResponseEntity<DataTableResponse<SupplierResponse>> getById(
            @AuthenticationPrincipal AuthUserPrincipal user,
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String keyword) {
        UUID companyId = user.getCompanyId();
        System.out.println("companyId = " + companyId);
        DataTableResponse<SupplierResponse> response = service.getSupplierById(companyId, draw, start, length, keyword);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/total-new")
    public ResponseEntity<DataCountResponse<Supplier>> getTotal(
            @AuthenticationPrincipal AuthUserPrincipal user) {
        DataCountResponse<Supplier> response = service.getTotal(user.getCompanyId());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody SupplierRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Supplier result = service.createSupplier(entity, user.getCompanyId(), user.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Supplier berhasil disimpan", result));
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody SupplierRequest entity,
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Supplier result = Objects.requireNonNull(service.updateSupplier(entity, id, user.getCompanyId()));
        SupplierResponse data = new SupplierResponse();
        BeanUtils.copyProperties(result, data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Supplier berhasil diubah", data));
    }

}
