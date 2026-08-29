package com.logikaintermedia.erp.jwt;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenResolver {

    // mengambil JWT token dari request (header atau cookie)
    public String resolveToken(HttpServletRequest request) {

        /**
         * 1. cek header (untuk mobile / API)
         * mengambil JWT token dari request (header atau cookie)KESIMPULAN BAGIAN 1
         * ✔ dipakai untuk:
         * mobile app
         * Postman
         * API eksternal
         */
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        /**
         * 2. cek cookie (untuk web)
         * KESIMPULAN BAGIAN 2
         * ✔ dipakai untuk:
         * web browser
         * HttpOnly cookie
         * auto kirim tanpa JS
         */

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
