package com.logikaintermedia.erp.modules.shared.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitingService {
    // Map untuk menampung ember-ember berdasarkan IP/Key
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Fungsi utama: Meminta jatah (consume)
    public boolean isAllowed(String key) {
        // Ambil atau buat bucket baru jika belum ada
        Bucket bucket = buckets.computeIfAbsent(key, this::createNewLoginBucket);

        // Coba ambil 1 token, kembalikan true jika sukses, false jika habis
        return bucket.tryConsume(1);
    }

    private Bucket createNewLoginBucket(String key) {

        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(5) // Jatah maksimal 5 kali
                        .refillIntervally(5, Duration.ofMinutes(1)) // Reset isi 5 token tiap 1 menit
                        .build())
                .build();

    }
}
