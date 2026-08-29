package com.logikaintermedia.erp.validation;

import java.beans.PropertyEditorSupport;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class XssURLCleanupAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register editor khusus untuk semua input bertipe String dari URL
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null) {
                    setValue(null);
                } else {
                    // Membersihkan HTML tag dari parameter URL
                    // Misal: ?name=<script>Dodi</script> menjadi "Dodi"
                    String cleanValue = Jsoup.clean(text.trim(), Safelist.none());
                    setValue(cleanValue);
                }
            }
        });
    }
}
