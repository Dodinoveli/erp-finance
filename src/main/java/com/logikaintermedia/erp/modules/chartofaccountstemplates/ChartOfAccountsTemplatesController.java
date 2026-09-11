package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsResponse;
import com.logikaintermedia.erp.utility.ApiResponse;

@RestController
@RequestMapping("/api/v1/accounttemplates")
public class ChartOfAccountsTemplatesController {

    private final ChartOfAccountsTemplatesService service;

    public ChartOfAccountsTemplatesController(ChartOfAccountsTemplatesService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<ChartOfAccountsTemplatesResponse>>> getFindAll() {
        List<ChartOfAccountsTemplatesResponse> response = service.getFindAll();
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

}
