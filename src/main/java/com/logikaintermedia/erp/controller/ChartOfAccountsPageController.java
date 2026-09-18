package com.logikaintermedia.erp.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ChartOfAccountsPageController {

    private final ChartOfAccountsService service;

    public ChartOfAccountsPageController(ChartOfAccountsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('admin')")
    @GetMapping("/coa")
    public String coa(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Chart of Accounts";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/chartofaccounts/list :: content";
        }
        return "content/chartofaccounts/list";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/coa/detail/{id}")
    public String detail(@PathVariable UUID id, @AuthenticationPrincipal AuthUserPrincipal principal, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        List<ChartOfAccounts> accounts = service.findByParentId(id, principal.getCompanyId());
        ChartOfAccounts parent = service.findById(id, principal.getCompanyId());
        String title = "Detail Chart Of Accounts";
        model.addAttribute("pageTitle", title);
        model.addAttribute("accounts", accounts);
        model.addAttribute("parent", parent);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/chartofaccounts/detail :: content";
        }
        return "content/chartofaccounts/detail";
    }

}
