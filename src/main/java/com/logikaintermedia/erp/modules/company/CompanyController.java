package com.logikaintermedia.erp.modules.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.validation.OnCreate;

@RestController
@RequestMapping("api/company")
public class CompanyController {
    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody CompanyRequest entity) {
        int result = service.insert(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Register Berhasil", result));
    }

}
