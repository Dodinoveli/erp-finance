package com.logikaintermedia.erp.modules.supplier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.CursorResponse;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

    @PreAuthorize("hasRole('Owner')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody SupplierRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Supplier result = service.createSupplier(entity, user.getCompanyId(), user.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Pemasok berhasil disimpan", result));
    }

    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody SupplierRequest entity,
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Supplier result = Objects.requireNonNull(service.updateSupplier(entity, id, user.getCompanyId()));
        SupplierResponse data = new SupplierResponse();
        BeanUtils.copyProperties(result, data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Pemasok berhasil diubah", data));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/generate-code")
    public ResponseEntity<?> generateCode(@AuthenticationPrincipal AuthUserPrincipal user) {
        String supplierCode = service.generateCode(user.getCompanyId());
        Map<String, String> data = new HashMap<>();
        data.put("supplierCode", supplierCode);
        return ResponseEntity.ok(ApiResponse.success("Berhasil", data));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorResponse<SupplierResponse>>> getById(
            @AuthenticationPrincipal AuthUserPrincipal user,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(required = false) UUID lastId,
            @RequestParam(defaultValue = "15") int limit) {
        UUID companyId = user.getCompanyId();
        System.out.println("lastCreatedAt = " + lastCreatedAt);
        System.out.println("lastId = " + lastId);
        System.out.println("companyId = " + companyId);
        CursorResponse<SupplierResponse> response = service.getSupplierById(companyId, keyword, lastCreatedAt, lastId,
                limit);
        return ResponseEntity.ok(ApiResponse.success("Berhasil", response));
    }

}
