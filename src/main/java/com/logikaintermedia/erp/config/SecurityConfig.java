package com.logikaintermedia.erp.config;

import java.util.List;
import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.logikaintermedia.erp.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        // /https://cdnjs.cloudflare.com
        List<String> cspPolicies = List.of(
                        "default-src 'self'",
                        // JavaScript Whitelist
                        "script-src 'self' 'unsafe-inline' " +
                                        "https://cdn.jsdelivr.net " +
                                        "https://cdnjs.cloudflare.com",

                        // CSS & Styles Whitelist
                        "style-src 'self' 'unsafe-inline' " +
                                        "https://cdn.jsdelivr.net " +
                                        "https://cdnjs.cloudflare.com " +
                                        "https://fonts.googleapis.com",

                        // Font Icons Whitelist (FontAwesome & Bootstrap Icons)
                        "font-src 'self' " +
                                        "https://cdnjs.cloudflare.com " +
                                        "https://cdn.jsdelivr.net " +
                                        "https://fonts.gstatic.com",

                        // Image & AJAX Whitelist
                        "img-src 'self' data: https:",
                        "connect-src 'self'",

                        // Menambahkan domain CDN agar file .map tidak diblokir
                        "connect-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com",

                        // Security Restriction
                        "object-src 'none'",
                        "upgrade-insecure-requests");

        String finalPolicy = String.join("; ", cspPolicies);

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        JwtAuthenticationFilter jwtAuthenticationFilter)
                        throws Exception {
                http
                                .headers(header -> header
                                                .contentSecurityPolicy(csp -> csp.policyDirectives(finalPolicy))
                                                // Tambahkan baris ini untuk memastikan tidak ada header CSP ganda
                                                .frameOptions(frame -> frame.sameOrigin()).disable())
                                .csrf(csrf -> csrf.disable())// sementara off aja dulu
                                .sessionManagement(sess -> sess
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // wajib JWT
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**",
                                                                "/access-denied")
                                                .permitAll()
                                                // Endpoint yang dapat diakses tanpa login
                                                .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/company").permitAll()

                                                // Semua endpoint lainnya wajib login
                                                .anyRequest().authenticated())
                                // TAMBAHKAN DI SINI
                                .exceptionHandling(exception -> exception
                                                // Belum login / JWT tidak ada atau tidak valid
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.sendRedirect("/login");
                                                })
                                                // Sudah login tetapi tidak punya permission/role
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.sendRedirect("/access-denied");
                                                }))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .formLogin(form -> form.disable())
                                .httpBasic(httpBasic -> httpBasic.disable()); // dimatikan karena pakai jwt bukan
                                                                              // session

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public LocaleResolver localeResolver() {
                SessionLocaleResolver slr = new SessionLocaleResolver();
                slr.setDefaultLocale(new Locale("in", "ID")); // Set Locale Indonesia
                return slr;
        }
}
