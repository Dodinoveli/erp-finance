package com.logikaintermedia.erp.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsBatchRequest;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsService;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@lombok.extern.slf4j.Slf4j
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

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/coa/edit/{id}")
    public String edit(@PathVariable UUID id, @AuthenticationPrincipal AuthUserPrincipal principal, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {

        List<ChartOfAccounts> accounts = service.findByParentId(id, principal.getCompanyId());
        ChartOfAccounts parent = service.findById(id, principal.getCompanyId());
        String title = "Edit Chart Of Accounts";
        model.addAttribute("pageTitle", title);
        model.addAttribute("accounts", accounts);
        model.addAttribute("parent", parent);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/chartofaccounts/edit :: content";
        }
        return "content/chartofaccounts/edit";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/coa/add/{id}")
    public String add(@PathVariable UUID id, @AuthenticationPrincipal AuthUserPrincipal principal, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        List<ChartOfAccounts> accounts = service.findByParentId(id, principal.getCompanyId());
        ChartOfAccounts parent = service.findById(id, principal.getCompanyId());
        String title = "Tambah Chart Of Accounts";
        model.addAttribute("pageTitle", title);
        model.addAttribute("accounts", accounts);
        model.addAttribute("parent", parent);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/chartofaccounts/add :: content";
        }
        return "content/chartofaccounts/add";
    }

    // untuk update dengan form langsung tanpa api
    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @PostMapping("coa/update")
    public String updateBatch(@ModelAttribute ChartOfAccountsBatchRequest request,
            @AuthenticationPrincipal AuthUserPrincipal principal, RedirectAttributes redirectAttributes, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        service.update(request.getAccounts(), principal.getCompanyId());

        UUID parentId = request.getParentId();
        List<ChartOfAccounts> accounts = service.findByParentId(parentId, principal.getCompanyId());
        ChartOfAccounts parent = service.findById(parentId, principal.getCompanyId());

        String title = "Edit Chart Of Accounts";
        model.addAttribute("pageTitle", title);
        model.addAttribute("accounts", accounts);
        model.addAttribute("parent", parent);
        response.setHeader("X-Page-Title", title);
        log.info("Parentid " + parentId);
        // Kirim event ke HTMX
        response.setHeader(
                "HX-Trigger",
                "coaUpdated");
        return "content/chartofaccounts/edit :: content";
    }

}
