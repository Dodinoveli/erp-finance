package com.logikaintermedia.erp.modules.refreshtoken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.jwt.AuthResult;
import com.logikaintermedia.erp.jwt.JwtConfig;
import com.logikaintermedia.erp.jwt.JwtUtil;
import com.logikaintermedia.erp.modules.user.User;
import com.logikaintermedia.erp.modules.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private RefreshTokenRepository repository;
    private UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JwtConfig config;

    public AuthResult refresh(String refreshToken,
            String deviceId,
            String ipAddress,
            String userAgent) {

        // 1. Validasi JWT
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token tidak valid");
        }

        // 2. cek db
        String hashed = DigestUtils.sha256Hex(refreshToken);
        RefreshToken tokenDb = repository.findByToken(hashed);
        if (tokenDb == null) {
            throw new RuntimeException("Refresh token tidak ditemukan");
        }

        // 3. Cek expired DB
        if (tokenDb.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        // 4. cek device dari request
        if (!tokenDb.getDeviceId().equals(deviceId)) {
            throw new RuntimeException("Device tidak dikenal");
        }

        // 5. ambil user dari jwt
        UUID userId = jwtUtil.extractUserId(refreshToken);
        String username = jwtUtil.extractUsername(refreshToken);
        UUID companyId = jwtUtil.extractCompanyId(refreshToken);
        List<String> roles = jwtUtil.extractRoles(refreshToken);

        // 6. ambil user dari DB
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("user tidak ditemukan");
        }

        // 7. hapus token lama (ROTATION)
        repository.deleteByToken(hashed);

        // 8. generate token baru
        String newAccessToken = jwtUtil.generateAccessToken(userId, username, companyId, roles);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, deviceId);

        // 9. simpan token baru
        String newHashed = DigestUtils.sha256Hex(newRefreshToken);
        RefreshToken newToken = new RefreshToken();
        newToken.setId(Generators.timeBasedEpochRandomGenerator().generate());
        newToken.setUserId(userId);
        newToken.setToken(newHashed);
        newToken.setExpiredAt(
                LocalDateTime.now().plusSeconds(config.getRefreshExpiration() / 1000));
        newToken.setIpAddress(ipAddress);
        newToken.setUserAgent(userAgent);
        newToken.setDeviceId(deviceId);
        newToken.setCreatedAt(LocalDateTime.now());
        repository.save(newToken);

        return new AuthResult(newAccessToken, newRefreshToken);
    }

}
