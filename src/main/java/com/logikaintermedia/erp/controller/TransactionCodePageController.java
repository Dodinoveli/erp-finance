package com.logikaintermedia.erp.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.logikaintermedia.erp.modules.transactioncode.TransactionCodeService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TransactionCodePageController {

    private final TransactionCodeService service;

    public TransactionCodePageController(TransactionCodeService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/transactioncode")
    public String project(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = " Kode Transaksi";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/transactioncode/list :: content";
        }
        return "content/transactioncode/list";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/transactioncode/edit/{id}")
    public String edit(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Edit Kode Transaksi";
        Optional<Map<String, Object>> result = service.companyCodeById(id);
        model.addAttribute("pageTitle", title);
        model.addAttribute("trx", result.get());

        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/transactioncode/edit :: content";
        }
        return "content/transactioncode/edit";
    }
}
