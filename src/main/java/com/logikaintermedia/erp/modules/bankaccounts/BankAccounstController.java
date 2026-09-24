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
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsResponse;
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

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping
    public ResponseEntity<?> findBankAccountsByCompanyId(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        UUID companyId = principal.getCompanyId();
        List<BankAccountsResponse> response = service.findBankAccountsByCompanyId(companyId);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    // mengambil dan menampilkan kategori akun Kas dan bank bedasarkan companies id
    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/find-cash-and-bank-accounts-by-company-id")
    public ResponseEntity<?> findCashAndBankAccountsByCompanyId(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        UUID companyId = principal.getCompanyId();
        List<ChartOfAccountsResponse> response = service.findCashAndBankAccountsByCompanyId(companyId);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    // @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    // @GetMapping("/find-coa-byparent-id/search")
    // public ResponseEntity<ApiResponse<List<ChartOfAccountsResponse>>>
    // findCoaByparentId(
    // @RequestParam(required = false) UUID parentId,
    // @AuthenticationPrincipal AuthUserPrincipal principal) {
    // UUID companyId = principal.getCompanyId();
    // List<ChartOfAccountsResponse> data = service.findCoaByparentId(companyId,
    // parentId);
    // return ResponseEntity.ok(ApiResponse.success("success", data));
    // }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody BankAccountsRequest entity,
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        BankAccounts response = service.saveBankAccounts(entity, principal.getCompanyId(), principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("data rekening berhasil disimpan", response));
    }

    @SuppressWarnings("null")
    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
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
