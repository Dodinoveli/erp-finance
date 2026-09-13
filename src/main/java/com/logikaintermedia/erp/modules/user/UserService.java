package com.logikaintermedia.erp.modules.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.jwt.AuthException;
import com.logikaintermedia.erp.jwt.AuthResult;
import com.logikaintermedia.erp.jwt.JwtUtil;
import com.logikaintermedia.erp.modules.refreshtoken.RefreshToken;
import com.logikaintermedia.erp.modules.refreshtoken.RefreshTokenRepository;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResult login(String username,
            String password,
            String deviceId,
            String ipAddress,
            String userAgent) {

        // 1. ambil user (tanpa SQL di sini)
        User user = userRepository.findByUsername(username);

        if (user.getUserName().isBlank()) {
            throw new AuthException("USER_NOT_FOUND", "User tidak ditemukan");
        }

        // 2. cek password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("WRONG_PASSWORD", "Password salah");
        }

        // 3. generate token
        String accessToken = jwtUtil.generateAccessToken(
                user.getUserId(),
                user.getUserName(),
                user.getCompanyId(),
                List.of(user.getRoles()));

        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), deviceId);

        String hashToken = DigestUtils.sha256Hex(refreshToken);
        RefreshToken model = new RefreshToken();
        model.setId(Generators.timeBasedEpochGenerator().generate());
        model.setUserId(user.getUserId());
        model.setToken(hashToken);
        model.setDeviceId(deviceId);
        model.setIpAddress(ipAddress);
        model.setUserAgent(userAgent);

        // Set waktu expired (misal 7 hari dari sekarang)
        model.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(model);

        return new AuthResult(accessToken, refreshToken);
    }

    public String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }

    // buat logout
    @Transactional
    public void logout(UUID userId, String deviceId) {
        refreshTokenRepository.deleteByUserIdAndDeviceId(userId, deviceId);
    }
}
