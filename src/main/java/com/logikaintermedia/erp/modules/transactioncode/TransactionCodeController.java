package com.logikaintermedia.erp.modules.transactioncode;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.company.Company;
import com.logikaintermedia.erp.modules.company.CompanyRequest;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.validation.OnUpdate;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactioncode") 
public class TransactionCodeController {
    private final TransactionCodeService service;

    public TransactionCodeController(TransactionCodeService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping
    public ResponseEntity<?> getByCode(
            @AuthenticationPrincipal AuthUserPrincipal user) {
        UUID companyId = user.getCompanyId();
        System.out.println("companyId = " + companyId);
        Optional<Map<String, Object>> response =
            service.companyCodeById(companyId);
        return ResponseEntity.ok(ApiResponse.success("Response", response));
    }


    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PutMapping
    public ResponseEntity<?> update( @RequestBody  CompanyRequest request,
            @AuthenticationPrincipal AuthUserPrincipal user) {
        service.updateCode(request, user.getCompanyId());
        return ResponseEntity.ok(ApiResponse.success("Kode Berhasil di update", null));
    }

}
