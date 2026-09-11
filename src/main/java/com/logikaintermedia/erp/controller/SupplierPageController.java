package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.logikaintermedia.erp.modules.supplier.Supplier;
import com.logikaintermedia.erp.modules.supplier.SupplierService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SupplierPageController {

    private final SupplierService service;

    public SupplierPageController(SupplierService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/supplier")
    public String list(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Daftar Supplier";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title); 
            return "content/suppliers/list :: content";
        }
        return "content/suppliers/list";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/supplier/new")
    public String newSup(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Supplier Baru";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/suppliers/new :: content";
        }
        return "content/suppliers/new";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/supplier/edit/{id}")
    public String edit(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        Supplier supplier = service.detailById(id);
        String title = "Edit Supplier";
        model.addAttribute("pageTitle", title);
        model.addAttribute("supplier", supplier);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/suppliers/edit :: content";
        }
        return "content/suppliers/edit";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/supplier/detail/{id}")
    public String detail(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        Supplier supplier = service.detailById(id);
        String title = "Detail Supplier";
        model.addAttribute("supplier", supplier);
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);  
            return "content/suppliers/detail :: content"; 
        }
        return "content/suppliers/detail";
    }

}
