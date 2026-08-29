package com.logikaintermedia.erp.modules.project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.client.ClientResponse;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.CursorResponse;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;

@RestController
@RequestMapping("/api/v1/project")
public class ProyekController {
    private final ProyekService service;

    public ProyekController(ProyekService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorResponse<ProyekResponse>>> getById(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(required = false) UUID lastId,
            @RequestParam(defaultValue = "13") int limit) {
        CursorResponse<ProyekResponse> response = service.getProjectById(
                principal.getCompanyId(), keyword, lastCreatedAt, lastId,
                limit);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/clients")
    public ResponseEntity<?> getClientByCompanyId(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        List<ClientResponse> response = service.findClientByCompanyId(principal.getCompanyId());
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody ProyekRequest request,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        Proyek response = service.saveProyek(request, principal.getCompanyId(), principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Data Proyek berhasil disimpan", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody ProyekRequest entity,
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        Proyek data = Objects
                .requireNonNull(service.updateProyek(entity, id, principal.getCompanyId(), principal.getUserId()));
        // ProyekRequest request, UUID projectId, UUID companyId, UUID userId
        ProyekResponse result = new ProyekResponse();
        BeanUtils.copyProperties(data, result);
        return ResponseEntity.ok(ApiResponse.success("Data Proyek berhasil di perbaharui...", result));
    }
}
