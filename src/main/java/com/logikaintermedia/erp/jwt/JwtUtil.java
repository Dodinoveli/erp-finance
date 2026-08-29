package com.logikaintermedia.erp.jwt;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // =========================
    // 🔑 ACCESS TOKEN
    // Jwts.builder() = tempat kamu “menyusun isi token”
    // .claim() = tempat kamu simpan data tambahan di JWT
    // =========================
    public String generateAccessToken(UUID userId, String username, UUID companyId, List<String> roles) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtConfig.getAccessExpiration());

        String token = Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username) // Sedikit koreksi: harusnya username, bukan userId lagi
                .claim("companyId", companyId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        // 🔥 LOG DI SINI: Pantau kapan token ini berakhir
        log.info("[JWT] Access Token created for user: {}. Expired at: {}", username, expiryDate);

        return token;
    }

    // =========================
    // 🔄 REFRESH TOKEN cukup pakai userId dan deviceId
    // ========================

    // Sekarang kita rapihin biar konsisten & paling benar untuk use case kamu
    // (multi-device + logout). pakai deviceId
    public String generateRefreshToken(UUID userId, String deviceId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("deviceId", deviceId) // 🔥 WAJIB
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        // return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        return Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // =========================
    // 📌 EXTRACT USERNAME, userId, companyId, roles, deviceId
    // =========================

    public UUID extractUserId(String token) {
        String userId = getClaims(token).getSubject();
        return UUID.fromString(userId);
    }

    public String extractUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public UUID extractCompanyId(String token) {
        String companyId = getClaims(token).get("companyId", String.class);
        return UUID.fromString(companyId);
    }

    public List<String> extractRoles(String token) {
        Object roles = getClaims(token).get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }

    public String extractDeviceId(String token) {
        return getClaims(token).get("deviceId", String.class);
    }

    // =========================
    // ✅ VALIDATE TOKEN
    // =========================
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            log.debug("[JWT] Token is valid");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("[JWT] Token EXPIRED! Mati pada: {}", e.getClaims().getExpiration());
        } catch (SignatureException e) {
            log.error("[JWT] Signature Key tidak cocok! Cek secret key di application.yml");
        } catch (Exception e) {
            log.error("[JWT] Token INVALID: {}", e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
