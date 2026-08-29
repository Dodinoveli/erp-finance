package com.logikaintermedia.erp.modules.coa;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;

@RestController
@RequestMapping("api/v1/coa")
public class CoaController {
    private final CoaService coaService;

    public CoaController(CoaService coaService) {
        this.coaService = coaService;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CoaResponse>>> getTree(@AuthenticationPrincipal AuthUserPrincipal user) {
        UUID companyId = user.getCompanyId();
        List<CoaResponse> response = coaService.getCoaTree(companyId);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }
}
