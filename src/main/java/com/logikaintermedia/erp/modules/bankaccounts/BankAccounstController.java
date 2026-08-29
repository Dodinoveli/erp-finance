package com.logikaintermedia.erp.modules.bankaccounts;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.validation.OnCreate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/bankaccounts")
public class BankAccounstController {

    private final BankAccountsService service;

    public BankAccounstController(BankAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CoaAccountsResponse>>> findCoaByName(
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        UUID companyId = principal.getCompanyId();
        List<CoaAccountsResponse> data = service.findCoaByName(companyId, keyword);
        return ResponseEntity.ok(ApiResponse.success("success", data));
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping
    public ResponseEntity<?> findBankAccounts(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        UUID companyId = principal.getCompanyId();
        List<BankAccountsResponse> response = service.findBankAccountById(companyId);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @PreAuthorize("hasRole('Owner')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody BankAccountsRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        BankAccounts response = service.saveBankAccounts(entity, principal.getCompanyId(), principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("data rekening berhasil disimpan", response));
    }

    @SuppressWarnings("null")
    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(OnCreate.class) @RequestBody BankAccountsRequest entity,
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        BankAccounts data = service.updateBankAccounts(entity, id, principal.getCompanyId(),
                principal.getUserId());
        BankAccountsResponse response = new BankAccountsResponse();
        BeanUtils.copyProperties(data, response);
        return ResponseEntity.ok(ApiResponse.success("data rekening berhasil diperbaharui", response));
    }

}
