package com.logikaintermedia.erp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyPageController {
    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/perusahaan")
    public String perusahaan() {
        return "perusahaan/perusahaan";
    }
}
