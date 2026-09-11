package com.logikaintermedia.erp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsService;

@Controller
public class ChartOfAccountsPageController {

    private final ChartOfAccountsService service;

    public ChartOfAccountsPageController(ChartOfAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('admin')")
    @GetMapping("/chartofaccounts")
    public String coa(Model model) {
        String title = "Daftar Akun/ Chart of Accounts (COA)";
        model.addAttribute("pageTitle", title);
        return "coa/coa";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/chartofaccounts/detail/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        List<ChartOfAccounts> accounts = service.findByParentId(id);
        String title = "Detail Chart Of Accounts";
        model.addAttribute("pageTitle", title);
        model.addAttribute("chartofaccounts", accounts);
        return "coa/detail_coa";
    }

}
