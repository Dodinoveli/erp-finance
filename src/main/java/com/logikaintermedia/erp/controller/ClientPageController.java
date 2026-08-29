package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.logikaintermedia.erp.modules.client.Client;
import com.logikaintermedia.erp.modules.client.ClientService;

@Controller
public class ClientPageController {
    private final ClientService service;

    public ClientPageController(ClientService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/client")
    public String client(Model model) {
        String title = "Daftar Client";
        model.addAttribute("pageTitle", title);
        return "client/client";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/client/new")
    public String tambah(Model model) {
        String title = "Client baru";
        model.addAttribute("pageTitle", title);
        return "client/tambah_client";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/client/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        String title = "Edit Client";
        Client client = service.detailById(id);
        model.addAttribute("pageTitle", title);
        model.addAttribute("client", client);
        return "client/edit_client";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/client/detail/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        Client client = service.detailById(id);
        String title = "Detail Client";
        model.addAttribute("client", client);
        model.addAttribute("pageTitle", title);
        return "client/detail_client";
    }
}
