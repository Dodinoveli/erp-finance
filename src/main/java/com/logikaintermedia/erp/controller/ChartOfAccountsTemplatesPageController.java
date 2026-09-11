package com.logikaintermedia.erp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartOfAccountsTemplatesPageController {

    @PreAuthorize("hasRole('Owner') or hasRole('admin')")
    @GetMapping("/chartofaccountstemplates")
    public String accountTemplates(Model model) {
        String title = "Daftar Template Akun Coa/ Chart of Accounts (COA)";
        model.addAttribute("pageTitle", title);
        return "accounttemplates/accounttemplates";
    }
}
