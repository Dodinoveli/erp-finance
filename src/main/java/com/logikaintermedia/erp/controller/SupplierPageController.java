package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.logikaintermedia.erp.modules.supplier.Supplier;
import com.logikaintermedia.erp.modules.supplier.SupplierService;

@Controller
public class SupplierPageController {

    private final SupplierService service;

    public SupplierPageController(SupplierService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/supplier")
    public String supplier(Model model) {
        String title = "Daftar Supplier";
        model.addAttribute("pageTitle", title);
        return "suppliers/supplier";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/supplier/new")
    public String newSup(Model model) {
        String title = "Supplier Baru";
        model.addAttribute("pageTitle", title);
        return "suppliers/supplier_new";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/supplier/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        Supplier supplier = service.detailById(id);
        String title = "Edit Supplier";
        model.addAttribute("pageTitle", title);
        model.addAttribute("supplier", supplier);
        return "suppliers/supplier_edit";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/supplier/detail/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        Supplier supplier = service.detailById(id);
        String title = "Detail Supplier";
        model.addAttribute("pageTitle", title);
        model.addAttribute("supplier", supplier);
        return "suppliers/supplier_detail";
    }

}
