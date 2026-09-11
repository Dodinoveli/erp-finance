package com.logikaintermedia.erp.modules.client;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.encryption.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClientRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ClientRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Generator Urutan Bilangan Bulat
     * 
     * @param companyId
     * @return
     */
    public long IntSequenceGenerator(UUID companyId) {
        String sql = """
                     UPDATE company_sequences
                         SET current_value = current_value + 1
                     WHERE company_id = :companyId
                         AND sequence_type = 'CLIENT'
                         RETURNING current_value;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);

        Long result = jdbc.queryForObject(sql, params, Long.class);
        if (result == null) {
            throw new IllegalStateException("Gagal mendapatkan nomor client untuk company: " + companyId);
        }
        return result;
    }

    private @NonNull MapSqlParameterSource toParams(Client model) {
        return new MapSqlParameterSource()
                .addValue("clientId", model.getClientId())
                .addValue("clientCode", model.getClientCode())
                .addValue("clientName", model.getClientName())
                .addValue("clientType", model.getClientType())
                .addValue("clientNpwp", EncryptionUtil.encrypt(model.getClientNpwp()))
                .addValue("clientNik", EncryptionUtil.encrypt(model.getClientNik()))
                .addValue("clientAddress", model.getClientAddress())
                .addValue("clientCity", model.getClientCity())
                .addValue("clientProvince", model.getClientProvince())
                .addValue("clientPostalCode", model.getClientPostalCode())
                .addValue("clientCountry", model.getClientCountry())
                .addValue("clientEmail", EncryptionUtil.encrypt(model.getClientEmail()))
                .addValue("clientContactPerson", model.getClientContactPerson())
                .addValue("clientContactPhone", EncryptionUtil.encrypt(model.getClientContactPhone()))
                .addValue("clientIsActive", model.getClientIsActive())
                .addValue("companyId", model.getCompanyId())
                .addValue("clientCreatedAt", model.getClientCreatedAt())
                .addValue("userId", model.getUserId())
                .addValue("clientUpdatedAt", model.getClientUpdatedAt())
                .addValue("clientDeletedAt", model.getClientDeletedAt())
                .addValue("clientBankName", model.getClientBankName())
                .addValue("clientAccountNumber", EncryptionUtil.encrypt(model.getClientAccountNumber()))
                .addValue("clientAccountName", model.getClientAccountName())
                .addValue("clientNitku", EncryptionUtil.encrypt(model.getClientNitku()))
                .addValue("clientIsPkp", model.getClientIsPkp());
    }

    public int save(Client model) {

        String sql = """
                    INSERT INTO public.clients (
                        client_id, client_code, client_name, client_type, client_npwp, client_nik,
                        client_address, client_city, client_province, client_postal_code, client_country,
                        client_email, client_contact_person, client_contact_phone,
                        client_is_active, company_id,
                        client_created_at, user_id, client_updated_at, client_deleted_at,
                        client_bank_name, client_account_number, client_account_name, client_nitku, client_is_pkp
                    ) VALUES (
                        :clientId, :clientCode, :clientName, :clientType, :clientNpwp, :clientNik,
                        :clientAddress, :clientCity, :clientProvince, :clientPostalCode, :clientCountry,
                        :clientEmail, :clientContactPerson, :clientContactPhone,
                        :clientIsActive, :companyId,
                        :clientCreatedAt, :userId, :clientUpdatedAt, :clientDeletedAt,
                        :clientBankName, :clientAccountNumber, :clientAccountName, :clientNitku, :clientIsPkp
                    )
                """;
        return jdbc.update(sql, toParams(model));
    }

    public int update(Client model) {

        String sql = """
                    UPDATE clients
                    SET
                        client_code = :clientCode,
                        client_name = :clientName,
                        client_type = :clientType,
                        client_npwp = :clientNpwp,
                        client_nik = :clientNik,
                        client_address = :clientAddress,
                        client_city = :clientCity,
                        client_province = :clientProvince,
                        client_postal_code = :clientPostalCode,
                        client_country = :clientCountry,
                        client_email = :clientEmail,
                        client_contact_person = :clientContactPerson,
                        client_contact_phone = :clientContactPhone,
                        client_is_active = :clientIsActive,
                        client_updated_at = :clientUpdatedAt,
                        client_bank_name = :clientBankName,
                        client_account_number = :clientAccountNumber,
                        client_account_name = :clientAccountName,
                        client_nitku = :clientNitku,
                        client_is_pkp = :clientIsPkp
                    WHERE client_id = :clientId and company_id = :companyId
                """;

        return jdbc.update(sql, toParams(model));
    }

    public List<Client> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt, UUID lastId,
            int limit) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        params.addValue("limit", limit);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            params.addValue("keyword", keyword + "%");
        }

        String sql = """
                SELECT *
                FROM public.clients
                      WHERE company_id = :companyId
                       """;

        if (hasKeyword) {
            // query ini hanya lambat jika datanya sudah 100.000 baris di bawah itu cepat
            // AND (supplier_name ILIKE CONCAT('%', :keyword, '%'))
            sql += " AND client_name ILIKE :keyword";
        }

        // 🔥 keyset filter (optional)
        if (lastCreatedAt != null && lastId != null) {
            sql += """
                        AND (
                            client_created_at < :lastCreatedAt
                            OR (client_created_at = :lastCreatedAt AND client_id < :lastId)
                        )
                    """;
             params.addValue("lastCreatedAt", lastCreatedAt);
             params.addValue("lastId", lastId);
        }
        sql += " ORDER BY client_created_at DESC, client_id DESC LIMIT :limit ";

        try {
            List<Client> result = jdbc.query(sql, params, new ClientMapper());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Client detailById(UUID clientId) {
        String sql = """
                SELECT *
                    FROM public.clients
                WHERE client_id = :clientId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("clientId", clientId);
        try {
            return jdbc.queryForObject(sql, params, new ClientMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengambil clients dengan ID: " + clientId, e);
        }
    }

    public List<Client> findClientByCompanyId(UUID companyId,
            int start,
            int length,
            String keyword) {
        String sql = """
                SELECT
                client_id,
                client_name,
                client_code,
                client_contact_person,
                client_contact_phone,
                client_email,
                client_is_active
                    FROM public.clients
                WHERE company_id = :companyId
                    """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        params.addValue("start", start);
        params.addValue("length", length);
        if (keyword != null && !keyword.isBlank()) {
            sql += """
                    AND (
                        client_name ILIKE :keyword
                        OR client_code ILIKE :keyword
                        OR client_contact_person ILIKE :keyword
                        OR client_email ILIKE :keyword
                    )
                    """;
            params.addValue("keyword", "%" + keyword.trim() + "%");
        }
        sql += """
                ORDER BY client_code DESC
                LIMIT :length OFFSET :start
                """;

        try {
            List<Client> result = jdbc.query(sql, params,
                    new BeanPropertyRowMapper<>(Client.class));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("null")
    public long countAll(UUID companyId) {

        String sql = """
                SELECT COUNT(*)
                FROM clients
                WHERE company_id = :companyId
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result : 0;
    }

    @SuppressWarnings("null")
    public long countFiltered(
            UUID companyId,
            String keyword) {

        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM clients
                WHERE company_id = :companyId
                """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
                    AND (
                        client_name ILIKE :keyword
                        OR client_code ILIKE :keyword
                        OR client_contact_person ILIKE :keyword
                        OR client_email ILIKE :keyword
                    )
                    """);

            params.addValue(
                    "keyword",
                    "%" + keyword.trim() + "%");
        }

        Long result = jdbc.queryForObject(
                sql.toString(),
                params,
                Long.class);

        return result != null ? result : 0;
    }

    // untuk dashboard client
    @SuppressWarnings("null")
    public int countTotalByCompanyId(UUID companyId) {

        String sql = """
                SELECT COUNT(*) as count
                FROM clients
                WHERE company_id = :companyId
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result.intValue() : 0;
    }

    //
    @SuppressWarnings("null")
    public int countTotalActiveByCompanyId(UUID companyId) {

        String sql = """
                SELECT COUNT(*) as count_active
                    FROM clients
                where client_is_active=true
                and company_id = :companyId
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result.intValue() : 0;
    }

    @SuppressWarnings("null")
    public int countTotalInactiveByCompanyId(UUID companyId) {

        String sql = """
                SELECT COUNT(*) as count_active
                    FROM clients
                where client_is_active=false
                and company_id = :companyId
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result.intValue() : 0;
    }

    @SuppressWarnings("null")
    public int countTotalNewByCompanyId(UUID companyId) {

        String sql = """
                SELECT COUNT(*) as total_new
                    FROM clients
                WHERE company_id = :companyId
                    and client_created_at  >= CURRENT_TIMESTAMP - INTERVAL '7 days'
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result.intValue() : 0;
    }
}
