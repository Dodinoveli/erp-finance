package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.logikaintermedia.erp.modules.bankaccounts.BankAccounts;
import com.logikaintermedia.erp.modules.bankaccounts.BankAccountsService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BankAccountPageController {
    private final BankAccountsService service;

    public BankAccountPageController(BankAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount")
    public String bankaccount(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Daftar Kas & Bank";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/bankaccount/list :: content";
        }
        return "content/bankaccount/list";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount/new")
    public String bankaccountNew(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Kas & Bank Baru";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/bankaccount/new :: content";
        }
        return "content/bankaccount/new";
    }

    // @PreAuthorize("hasRole('Owner')")
    // @GetMapping("/bankaccount/edit/{id}")
    // public String edit(@PathVariable UUID id, Model model,
    // @RequestHeader(value = "HX-Request", required = false) String hxRequest,
    // HttpServletResponse response) {
    // String title = "Edit Kas & Bank";
    // BankAccounts accounts = service.findDetailById(id);
    // model.addAttribute("pageTitle", title);
    // model.addAttribute("accounts", accounts);
    // if ("true".equals(hxRequest)) {
    // response.setHeader("X-Page-Title", title);
    // return "content/bankaccount/edit :: content";
    // }
    // return "content/bankaccount/edit";
    // }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/bankaccount/detail/{id}")
    public String detail(@PathVariable UUID id, @AuthenticationPrincipal AuthUserPrincipal principal, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        BankAccounts accounts = service.findDetailById(id, principal.getCompanyId());
        String title = "Detail Kas & Bank";
        model.addAttribute("accounts", accounts);
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/bankaccount/detail :: content";
        }
        return "content/bankaccount/detail";
    }
}
