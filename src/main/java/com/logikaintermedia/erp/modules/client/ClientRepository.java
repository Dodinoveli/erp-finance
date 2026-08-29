package com.logikaintermedia.erp.modules.client;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.encryption.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClientRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ClientRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public String generateClientCode(UUID companyId) {
        String lastCode = null;

        // 1. Ambil kode terakhir dengan LOCK (FOR UPDATE)
        String sqlGetLast = """
                SELECT client_code FROM clients
                WHERE company_id = :companyId
                ORDER BY client_code DESC
                LIMIT 1 FOR UPDATE
                """;
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("companyId", companyId);
            lastCode = namedParameterJdbcTemplate.queryForObject(sqlGetLast, data, String.class);
        } catch (EmptyResultDataAccessException e) {
            // Biarkan lastCode tetap null jika belum ada data sama sekali
        }

        // 2. Logika increment nomor urut
        int newNumber = 1;
        if (lastCode != null && lastCode.startsWith("CL-")) {
            try {
                // Mengambil angka setelah "CL-"
                newNumber = Integer.parseInt(lastCode.substring(3)) + 1;
            } catch (NumberFormatException e) {
                newNumber = 1; // Fallback jika format kode rusak
            }
        }

        return String.format("CL-%05d", newNumber);
    }

    public String code(UUID companyId) {
        String sql = """
                SELECT client_code
                FROM clients
                WHERE company_id = :companyId
                ORDER BY client_code DESC
                LIMIT 1
                """;

        try {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("companyId", companyId);
            String lastCode = namedParameterJdbcTemplate.queryForObject(sql, params, String.class);

            if (lastCode != null && lastCode.startsWith("CL-")) {
                int number = Integer.parseInt(lastCode.substring(3));
                number++;
                return String.format("CL-%05d", number);
            }
        } catch (EmptyResultDataAccessException e) {
            return "CL-00001";
        } catch (Exception e) {
            System.err.println("Error generating code: " + e.getMessage());
        }
        return "CL-00001";
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
        return namedParameterJdbcTemplate.update(sql, toParams(model));
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

        return namedParameterJdbcTemplate.update(sql, toParams(model));
    }

    public List<Client> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt, UUID lastId,
            int limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        params.put("limit", limit);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            // params.put("keyword", "%" + keyword + "%");
            params.put("keyword", keyword + "%");
        }

        String sql = """
                SELECT *
                FROM public.clients
                      WHERE company_id = :companyId
                       """;

        if (hasKeyword) {
            // query ini hanya lambat jika datanya sudah 100.000 baris di bawah itu cepat
            // AND (supplier_name ILIKE CONCAT('%', :keyword, '%'))
            sql += " AND (client_name) ILIKE CONCAT('%', :keyword, '%')";
        }

        // 🔥 keyset filter (optional)
        if (lastCreatedAt != null && lastId != null) {
            sql += """
                        AND (
                            client_created_at < :lastCreatedAt
                            OR (client_created_at = :lastCreatedAt AND client_id < :lastId)
                        )
                    """;
            params.put("lastCreatedAt", lastCreatedAt);
            params.put("lastId", lastId);
        }
        sql += " ORDER BY client_created_at DESC, client_id DESC LIMIT :limit ";

        try {
            List<Client> result = namedParameterJdbcTemplate.query(sql, params, new ClientMapper());
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
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("clientId", clientId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, new ClientMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengambil clients dengan ID: " + clientId, e);
        }
    }

}
