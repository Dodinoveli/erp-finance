package com.logikaintermedia.erp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ChartOfAccountsTemplatesPageController {

    @PreAuthorize("hasRole('Owner') or hasRole('admin')")
    @GetMapping("/coatemplates")
    public String accountTemplates(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Chart of Accounts (COA) Templates";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/chartofaccountstemplates/list :: content";
        }
        return "content/chartofaccountstemplates/list";
    }
}
