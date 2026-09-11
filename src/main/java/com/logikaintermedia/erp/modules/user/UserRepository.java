package com.logikaintermedia.erp.modules.user;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private @NonNull MapSqlParameterSource toParams(User model) {
        return new MapSqlParameterSource()
                .addValue("userId", model.getUserId())
                .addValue("userName", model.getUserName())
                .addValue("password", model.getPassword())
                .addValue("fullName", model.getFullName())
                .addValue("email", model.getEmail())
                .addValue("phone", model.getPhone())
                .addValue("isActive", Boolean.parseBoolean(String.valueOf(model.getIsActive())))
                .addValue("companyId", model.getCompanyId())
                .addValue("createdAt", model.getCreatedAt())
                .addValue("updateAt", model.getUpdatedAt())
                .addValue("role", model.getRole());
    }

    public int insert(User model) {
        String sql = """
                INSERT INTO public.users(
                	user_id, username, password, full_name, email,
                    phone, is_active, company_id, created_at, updated_at, role
                    )
                	VALUES
                    (:userId, :userName, :password, :fullName, :email,
                    :phone, :isActive, :companyId, :createdAt, :updateAt, :role);
                                """;
        return namedParameterJdbcTemplate.update(sql, toParams(model));
    }

    public User findByUsername(String username) {
        String sql = """
                    SELECT user_id, username, password, company_id, roles
                    FROM users
                    WHERE username = :username
                """;
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("username", username);

        List<User> result = namedParameterJdbcTemplate.query(sql, param, (rs, rowNum) -> {
            User u = new User();
            u.setUserId(UUID.fromString(rs.getString("user_id")));
            u.setUserName(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setCompanyId(UUID.fromString(rs.getString("company_id")));
            u.setRole(rs.getString("roles"));
            return u;
        });

        if (result.isEmpty()) {
            System.out.println("USER TIDAK KETEMU DI JAVA");
            throw new IllegalArgumentException("Username atau password tidak sesuai.");
        }

        return result.get(0);
    }

    public User findById(UUID id) {
        String sql = """
                    SELECT user_id, username, password, company_id, roles
                    FROM users
                    WHERE username = :username
                """;
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_id", id);

        List<User> result = namedParameterJdbcTemplate.query(sql, param, (rs, rowNum) -> {
            User u = new User();
            u.setUserId(UUID.fromString(rs.getString("user_id")));
            u.setUserName(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setCompanyId(UUID.fromString(rs.getString("company_id")));
            u.setRole(rs.getString("roles"));
            return u;
        });

        if (result.isEmpty()) {
            System.out.println("USER TIDAK KETEMU DI JAVA");
            return null;
        }

        return result.get(0);
    }
}
