package com.logikaintermedia.erp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CoaMappingPageController {
    
    @PreAuthorize("hasRole('Owner') or hasRole('Admin')")
    @GetMapping("/coamapping")
    public String coamapping(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletResponse response) {
        String title = "Coa Mapping";
        model.addAttribute("pageTitle", title);
        if ("true".equals(hxRequest)) {
            response.setHeader("X-Page-Title", title);
            return "content/coamapping/form-list :: content";
        }
        return "content/coamapping/form-list";
    }
}
