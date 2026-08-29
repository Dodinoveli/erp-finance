package com.logikaintermedia.erp.modules.user;

import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthResult;
import com.logikaintermedia.erp.jwt.CookieUtil;
import com.logikaintermedia.erp.jwt.JwtConfig;
import com.logikaintermedia.erp.jwt.JwtUtil;
import com.logikaintermedia.erp.jwt.TokenResolver;
import com.logikaintermedia.erp.modules.refreshtoken.RefreshTokenService;
import com.logikaintermedia.erp.modules.shared.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class UserController {
        private final RefreshTokenService refreshTokenService;
        private final JwtConfig jwtConfig;
        private final JwtUtil jwtUtil;
        private final UserService service;
        private final TokenResolver resolver;
        private final RateLimitingService limitingService;

        public UserController(UserService service, TokenResolver resolver, JwtUtil jwtUtil,
                        RateLimitingService limitingService, JwtConfig jwtConfig,
                        RefreshTokenService refreshTokenService) {
                this.service = service;
                this.resolver = resolver;
                this.jwtUtil = jwtUtil;
                this.limitingService = limitingService;
                this.jwtConfig = jwtConfig;
                this.refreshTokenService = refreshTokenService;
        }

        /**
         * Controller → HTTP (cookie, request, response)
         * Service → Logic (login, validasi, generate token)
         * Repository → Database
         * Filter → Security (baca token)
         */
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody UserRequest req,
                        HttpServletRequest request, HttpServletResponse response) {
                // ambil IP (proxy aware)
                String ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress != null && !ipAddress.isBlank()) {
                        ipAddress = ipAddress.split(",")[0].trim();
                } else {
                        ipAddress = request.getRemoteAddr();
                }

                String userAgent = request.getHeader("User-Agent");

                // device id (lebih aman)
                String rawDevice = userAgent + "|" + ipAddress;

                String deviceId = Optional.ofNullable(request.getHeader("X-Device-Id"))
                                .orElse(DigestUtils.sha256Hex(rawDevice));

                AuthResult result = service.login(req.getUserName(), req.getPassword(), deviceId, ipAddress, userAgent);

                int accessExpireInSecond = (int) (jwtConfig.getAccessExpiration() / 1000);

                CookieUtil.setCookie(response, "access_token",
                                result.getAccessToken(), accessExpireInSecond);

                int refreshExpireInSecond = (int) (jwtConfig.getRefreshExpiration() / 1000);
                CookieUtil.setCookie(response, "refresh_token",
                                result.getRefreshToken(), refreshExpireInSecond);

                return ResponseEntity.ok("Login sukses");
        }

        @PostMapping("/refresh")
        public ResponseEntity<?> refreshToken(HttpServletRequest request,
                        HttpServletResponse response) {
                // 1. ambil refresh token dari cookie
                String refreshToken = CookieUtil.getCookie(request, "refresh_token");
                if (refreshToken == null) {
                        return ResponseEntity.status(401).body("Refresh token tidak ada");
                }

                // 2. ambil IP (proxy aware)
                String ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress != null && !ipAddress.isBlank()) {
                        ipAddress = ipAddress.split(",")[0].trim();
                } else {
                        ipAddress = request.getRemoteAddr();
                }

                // 3. user agent
                String userAgent = request.getHeader("User-Agent");

                // 4. deviceId (harus sama kayak login)
                String rawDevice = userAgent + "|" + ipAddress;

                String deviceId = Optional.ofNullable(request.getHeader("X-Device-Id"))
                                .orElse(DigestUtils.sha256Hex(rawDevice));

                // 5. panggil service
                AuthResult result = refreshTokenService.refresh(
                                refreshToken,
                                deviceId,
                                ipAddress,
                                userAgent);

                // 6. set cookie baru
                int accessExpire = (int) (jwtConfig.getAccessExpiration() / 1000);
                CookieUtil.setCookie(response, "access_token",
                                result.getAccessToken(), accessExpire);

                int refreshExpire = (int) (jwtConfig.getRefreshExpiration() / 1000);
                CookieUtil.setCookie(response, "refresh_token",
                                result.getRefreshToken(), refreshExpire);

                return ResponseEntity.ok("Token berhasil di-refresh");
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) {
                String refreshToken = resolver.resolveRefreshToken(request);
                try {
                        if (refreshToken != null) {
                                UUID userId = jwtUtil.extractUserId(refreshToken);
                                String deviceId = jwtUtil.extractDeviceId(refreshToken);
                                System.out.println("Mencoba logout user: " + userId); // Debug
                                service.logout(userId, deviceId);
                                System.out.println("DEBUG: UserID=" + userId + " | DeviceID='" + deviceId + "'");
                                System.out.println("Service logout berhasil dipanggil"); // Debug
                        } else {
                                System.out.println("Refresh token tidak ditemukan di request!");
                        }
                } catch (Exception e) {
                        System.err.println("Gagal logout karena: " + e.getMessage());
                }
                CookieUtil.deleteCookie(response, "access_token");
                CookieUtil.deleteCookie(response, "refresh_token");
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok("logout sukses");
        }
}
