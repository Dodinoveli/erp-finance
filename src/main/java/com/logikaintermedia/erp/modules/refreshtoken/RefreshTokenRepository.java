package com.logikaintermedia.erp.modules.refreshtoken;

import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RefreshTokenRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public RefreshTokenRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // CREATE INDEX idx_refresh_user ON refresh_token(user_id);
    // CREATE INDEX idx_refresh_token ON refresh_token(token);

    public void save(RefreshToken model) {

        String sql = """
                    INSERT INTO public.refresh_token(
                        id, user_id, token, expired_at, ip_address, user_agent, device_id,created_at
                    )
                    VALUES (
                        :id, :userId, :token, :expiredAt, :ipAddress, :userAgent, :deviceId, :createdAt
                    )
                """;

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", model.getId())
                .addValue("userId", model.getUserId())
                .addValue("token", model.getToken())
                .addValue("expiredAt", model.getExpiredAt())
                .addValue("ipAddress", model.getIpAddress())
                .addValue("userAgent", model.getUserAgent())
                .addValue("deviceId", model.getDeviceId())
                .addValue("createdAt", model.getCreatedAt());
        int affected = jdbc.update(sql, param);
        System.out.println("refresh_token rows added: " + affected);
    }

    // buat logout
    public void deleteByUserIdAndDeviceId(UUID userId, String deviceId) {
        String sql = """
                delete from public.refresh_token
                where user_id = :userId and device_id = :deviceId ;
                """;
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("deviceId", deviceId);
        int affected = jdbc.update(sql, param);
        System.out.println("Deleted rows: " + affected);
    }

    // untuk refresh
    public void deleteByToken(String token) {
        String sql = """
                delete from refresh_token
                where token = :token
                """;

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("token", token);

        int affected = jdbc.update(sql, param);
        log.info("Deleted refresh tokens: {}", affected);
    }

    //
    public RefreshToken findByToken(String token) {
        String sql = "SELECT * FROM refresh_token WHERE token = :token";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("token", token);

        try {
            return jdbc.queryForObject(
                    sql,
                    param,
                    new BeanPropertyRowMapper<>(RefreshToken.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // atau Optional lebih bagus
        }
    }
}
