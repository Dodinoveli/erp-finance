package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.logikaintermedia.erp.modules.client.Client;
import com.logikaintermedia.erp.modules.client.ClientService;
import jakarta.servlet.http.HttpServletResponse;

@Controller 
public class ClientPageController {
    private final ClientService service;

    public ClientPageController(ClientService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    //@PreAuthorize("hasRole('Admin')")
    @GetMapping("/client")
    public String list(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        model.addAttribute("pageTitle", "Daftar Client ");
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", "Daftar Client");
            return "content/client/list :: content";
        }
        return "content/client/list";
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/client/new")
    public String newCL(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Client baru";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/client/new :: content";
        }
        return "content/client/new";
    }

   @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/client/edit/{id}")
    public String edit(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Edit Client";
        Client client = service.detailById(id);
        model.addAttribute("pageTitle", title);
        model.addAttribute("client", client);

        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/client/edit :: content";
        }
        return "content/client/edit";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/client/detail/{id}")
    public String detail(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        Client client = service.detailById(id);
        String title = "Detail Client";
        model.addAttribute("client", client);
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/client/detail :: content";
        }
        return "content/client/detail";
    }
}
