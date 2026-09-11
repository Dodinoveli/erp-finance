package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.logikaintermedia.erp.modules.project.Proyek;
import com.logikaintermedia.erp.modules.project.ProyekService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProjectPageController {
    private final ProyekService service;

    public ProjectPageController(ProyekService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/project")
    public String project(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Daftar Proyek";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/project/list :: content";
        }
        return "content/project/list";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/new")
    public String newProyek(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Proyek Baru";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/project/new :: content";
        }
        return "content/project/new";

    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/edit/{id}")
    public String edit(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        Proyek project = service.detailById(id);
        String title = "Edit Proyek";

        model.addAttribute("pageTitle", title);
        model.addAttribute("project", project);

        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/project/edit :: content";
        }
        return "content/project/edit";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/detail/{id}")
    public String detail(@PathVariable UUID id, Model model,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        Proyek project = service.detailById(id);
        String title = "Detail Proyek";
        model.addAttribute("pageTitle", title);
        model.addAttribute("project", project);

        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/project/detail :: content";
        }
        return "content/project/detail";
    }
}
