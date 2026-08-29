package com.logikaintermedia.erp.validation;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app.error-messages")
@Getter
@Setter
public class ErrorConfig {

    // 1. Variabel penampung (Pastikan letaknya DI LUAR inner class)
    private Database database = new Database();
    private General general = new General();

    // 2. Definisi Tipe Data / Inner Class (Gunakan static agar bisa dikenali)
    @Getter
    @Setter
    public static class Database {
        private Map<String, String> uniqueConstraints;
        private String defaultUnique = "Data duplikat terdeteksi.";
    }

    @Getter
    @Setter
    public static class General {
        private String internalError = "Error sistem.";
        private String notFound = "Data tidak ditemukan.";
    }
}