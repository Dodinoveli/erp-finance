package com.logikaintermedia.erp.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.logikaintermedia.erp.modules.project.Proyek;
import com.logikaintermedia.erp.modules.project.ProyekService;

@Controller
public class ProjectPageController {
    private final ProyekService service;

    public ProjectPageController(ProyekService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project")
    public String project(Model model) {
        String title = "Daftar Proyek";
        model.addAttribute("pageTitle", title);
        return "project/project";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/new")
    public String newProyek(Model model) {
        String title = "Proyek Baru";
        model.addAttribute("pageTitle", title);
        return "project/project_new";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        Proyek project = service.detailById(id);
        String title = "Edit Proyek";
        model.addAttribute("pageTitle", title);
        model.addAttribute("project", project);
        return "project/project_edit";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/project/detail/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        Proyek project = service.detailById(id);
        String title = "Detail Proyek";
        model.addAttribute("pageTitle", title);
        model.addAttribute("project", project);
        return "project/project_detail";
    }
}
