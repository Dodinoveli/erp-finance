package com.logikaintermedia.erp.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
/**
 * membaca JWT → mengubah jadi user login
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenResolver resolver;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenResolver resolver) {
        this.jwtUtil = jwtUtil;
        this.resolver = resolver;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolver.resolveToken(request);
        log.info("[JWT] {} tokenPresent={}",
                request.getRequestURI(),
                token != null);
        log.info("[JWT] FILTER MASUK: {}", request.getRequestURI());

        try {
            // 1. kalau ada token → validasi
            if (token != null && jwtUtil.validateToken(token)) {
                log.info("[JWT] VALID path={}",
                        request.getRequestURI());
                // 2 ambil username dari JWT
                UUID userId = jwtUtil.extractUserId(token);
                UUID companyId = jwtUtil.extractCompanyId(token);
                String username = jwtUtil.extractUsername(token);
                String deviceId = jwtUtil.extractDeviceId(token);

                List<String> roles = jwtUtil.extractRoles(token);
                if (roles == null) {
                    roles = new ArrayList<>();
                }

                // 3. ambil tokenya masukan ke userPrincipal
                AuthUserPrincipal principal = new AuthUserPrincipal(userId, username, companyId, roles, deviceId);

               
                // 4. WAJIB: CONVERT ROLES → AUTHORITIES
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authorities);

                // System.out.println("ROLES DARI JWT: " + roles);
                // System.out.println("AUTHORITIES: " + authorities);
                // 5. set ke security context (INI INTINYA)
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("[JWT] AUTH SET user={} roles={} path={}",
                            username,
                            roles,
                            request.getRequestURI());
                }
            }
        } catch (Exception e) {
            //logger.warn("JWT error silakan periksa ya: " + e.getMessage());
            log.warn("[JWT] Invalid token path={} message={}",
            request.getRequestURI(),
            e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
