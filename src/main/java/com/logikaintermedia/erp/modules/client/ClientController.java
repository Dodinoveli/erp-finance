package com.logikaintermedia.erp.modules.client;

import org.springframework.web.bind.annotation.RequestMapping;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorResponse<ClientResponse>>> findById(
            @AuthenticationPrincipal AuthUserPrincipal user,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(required = false) UUID lastId,
            @RequestParam(defaultValue = "13") int limit) {
        UUID companyId = user.getCompanyId();
        System.out.println("keyword = " + keyword);
        System.out.println("lastCreatedAt = " + lastCreatedAt);
        System.out.println("lastId = " + lastId);
        System.out.println("companyId = " + companyId);
        CursorResponse<ClientResponse> response = service.findById(companyId, keyword, lastCreatedAt, lastId, limit);

        return ResponseEntity.ok(ApiResponse.success("Berhasil", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/generate-code")
    public ResponseEntity<?> generateCode(@AuthenticationPrincipal AuthUserPrincipal user) {
        String clientCode = service.generateCode(user.getCompanyId());
        Map<String, String> data = new HashMap<>();
        data.put("clientCode", clientCode);
        return ResponseEntity.ok(ApiResponse.success("Berhasil", data));
    }

    @PreAuthorize("hasRole('Owner')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody ClientRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Client result = service.createClient(entity, user.getCompanyId(), user.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Client berhasil disimpan...", result));
    }

    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody ClientRequest entity, @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        Client data = Objects.requireNonNull(service.updateClient(entity, id, user.getCompanyId()));
        ClientResponse result = new ClientResponse();
        BeanUtils.copyProperties(data, result);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Client berhasil diperbaharui...", result));
    }

}
