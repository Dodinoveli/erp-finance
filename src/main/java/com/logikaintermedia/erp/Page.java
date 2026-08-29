package com.logikaintermedia.erp;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class Page {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        // String userId = (String) request.getAttribute("userId");
        // if (userId != null && !userId.isEmpty()) {
        // return "redirect:/dashboard";
        // }
        return "index";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request) {
        return "register";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @PreAuthorize("hasRole('Owner')")
    @GetMapping("/dashboard")
    public String index(Model model) {
        String title = "Dashboard";
        // pageTitle
        model.addAttribute("pageTitle", title);
        return "dashboard";
    }

    // public String index(@RequestParam(value = "page", required = false) String
    // page,
    // @RequestHeader(value = "X-Requested-With", required = false) String
    // requestedWith, Model model) {

    // String fragmentPath = null;
    // if ("Klien".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/client_view";
    // } else if ("Pemasok".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/supplier_view";
    // } else if ("Karyawan".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/employees_view";
    // } else if ("Bagan_akun".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/akuntansi/bagan_akun";
    // } else if ("Pemetaan_bagan_akun".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/akuntansi/pemetaan_bagan_akun";
    // }

    // else if ("Rekening".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/bank_accounts_view";
    // } else if ("Proyek".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/proyek/proyek";
    // } else if ("Kategori".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/kategori_view";
    // } else if ("Merk".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/merk_view";
    // } else if ("Satuan".equalsIgnoreCase(page)) {
    // fragmentPath = "fragments/master/satuan_view";
    // } else {
    // fragmentPath = "menu";
    // }

    // // 2. Jika request datang dari AJAX (klik menu) Rekening
    // if ("XMLHttpRequest".equals(requestedWith)) {
    // return fragmentPath + " :: mainContent";
    // }

    // // JIKA REFRESH (Bukan AJAX): Kirim seluruh halaman dashboard
    // model.addAttribute("templatePath", "menu");
    // model.addAttribute("currentPage", page); // Untuk menandai tab mana yang
    // aktif nanti

    // return "dashboard";
    // }

}
