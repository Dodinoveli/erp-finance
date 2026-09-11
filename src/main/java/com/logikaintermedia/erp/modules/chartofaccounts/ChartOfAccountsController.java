package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;

@RestController
@RequestMapping("/api/v1/accounts")
public class ChartOfAccountsController {

    private final ChartOfAccountsService service;

    public ChartOfAccountsController(ChartOfAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PostMapping("/install")
    public ResponseEntity<ApiResponse<?>> initializeCompanyCoa(@AuthenticationPrincipal AuthUserPrincipal principal) {
        int response = service.initializeCompanyCoa(principal.getCompanyId());
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<ChartOfAccountsResponse>>> getFindAllByCompanyId(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        List<ChartOfAccountsResponse> response = service.findAllByCompanyId(principal.getCompanyId());
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }
}
