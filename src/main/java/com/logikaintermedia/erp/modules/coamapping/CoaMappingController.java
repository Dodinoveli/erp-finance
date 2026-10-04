package com.logikaintermedia.erp.modules.coamapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;
import com.logikaintermedia.erp.validation.OnCreate;

@RestController
@RequestMapping("/api/v1/coamapping")
public class CoaMappingController {

    private final CoaMappingService service;

    public CoaMappingController(CoaMappingService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping
    public ResponseEntity<DataTableResponse<CoaMappingResponse>> findCoaMappingById(
            @AuthenticationPrincipal AuthUserPrincipal user,
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String keyword) {
        System.out.println("KEYWORD = [" + keyword + "]");
        DataTableResponse<CoaMappingResponse> response = service.findCoaMappingById(user.getCompanyId(), draw, start,
                length,
                keyword);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody CoaMappingRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        CoaMapping result = service.save(entity, principal.getCompanyId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Data Coa Mapping berhasil disimpan...", result));
    }

}
