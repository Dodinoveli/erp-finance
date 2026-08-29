package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.logikaintermedia.erp.modules.bankaccounts.BankAccounts;
import com.logikaintermedia.erp.modules.bankaccounts.BankAccountsService;

@Controller
public class BankAccountPageController {
    private final BankAccountsService service;

    public BankAccountPageController(BankAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount")
    public String bankaccount(Model model) {
        String title = "Daftar Kas & Bank";
        model.addAttribute("pageTitle", title);
        return "bankaccount/bankaccount";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount/new")
    public String bankaccountNew(Model model) {
        String title = "Kas & Bank Baru";
        model.addAttribute("pageTitle", title);
        return "bankaccount/bankaccount_new";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        String title = "Edit Kas & Bank";
        BankAccounts accounts = service.findDetailById(id);
        model.addAttribute("pageTitle", title);
        model.addAttribute("accounts", accounts);
        return "bankaccount/bankaccount_edit";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount/detail/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        BankAccounts accounts = service.findDetailById(id);
        String title = "Detail Kas & Bank";
        model.addAttribute("accounts", accounts);
        model.addAttribute("pageTitle", title);
        return "bankaccount/bankaccount_detail";
    }
}
