package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.encryption.EncryptionUtil;

@Repository
public class SupplierRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public SupplierRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private @NonNull MapSqlParameterSource toParams(Supplier model) {
        return new MapSqlParameterSource()
                .addValue("supplierId", model.getSupplierId())
                .addValue("supplierCode", model.getSupplierCode())
                .addValue("supplierName", model.getSupplierName())
                .addValue("supplierType", model.getSupplierType())
                .addValue("supplierNpwp", EncryptionUtil.encrypt(model.getSupplierNpwp()))
                .addValue("supplierAddress", model.getSupplierAddress())
                .addValue("supplierCity", model.getSupplierCity())
                .addValue("supplierProvince", model.getSupplierProvince())
                .addValue("supplierPostalCode", model.getSupplierPostalCode())
                .addValue("supplierCountry", model.getSupplierCountry())
                .addValue("supplierEmail", EncryptionUtil.encrypt(model.getSupplierEmail()))
                .addValue("supplierContactPerson", model.getSupplierContactPerson())
                .addValue("supplierContactPhone", EncryptionUtil.encrypt(model.getSupplierContactPhone()))
                .addValue("supplierPaymentTermDays", model.getSupplierPaymentTermDays())
                .addValue("supplierCreditLimit",
                        model.getSupplierCreditLimit() != null ? model.getSupplierCreditLimit() : BigDecimal.ZERO)
                .addValue("supplierBankName", model.getSupplierBankName())
                .addValue("supplierBankAccountNumber", EncryptionUtil.encrypt(model.getSupplierBankAccountNumber()))
                .addValue("supplierBankAccountName", model.getSupplierBankAccountName())
                .addValue("supplierPkp", model.getSupplierPkp())
                .addValue("supplierIsActive", model.getSupplierIsActive())
                .addValue("companyId", model.getCompanyId())
                .addValue("supplierCreatedAt", model.getSupplierCreatedAt())
                .addValue("supplierUpdatedAt", model.getSupplierUpdatedAt())
                .addValue("userId", model.getUserId())
                .addValue("supplierDeletedAt", model.getSupplierDeletedAt());
    }

    public int save(Supplier model) {
        String sql = """
                INSERT INTO public.suppliers (
                    supplier_id, supplier_code, supplier_name, supplier_type, supplier_npwp,
                    supplier_address, supplier_city, supplier_province, supplier_postal_code, supplier_country,
                    supplier_email, supplier_contact_person, supplier_contact_phone, supplier_payment_term_days,
                    supplier_credit_limit, supplier_bank_name, supplier_bank_account_number, supplier_bank_account_name,
                    supplier_pkp, supplier_is_active, company_id, supplier_created_at, supplier_updated_at, user_id, supplier_deleted_at
                ) VALUES (
                    :supplierId, :supplierCode, :supplierName, :supplierType, :supplierNpwp,
                    :supplierAddress, :supplierCity, :supplierProvince, :supplierPostalCode,
                    :supplierCountry, :supplierEmail, :supplierContactPerson,
                    :supplierContactPhone, :supplierPaymentTermDays, :supplierCreditLimit, :supplierBankName,
                    :supplierBankAccountNumber,
                    :supplierBankAccountName, :supplierPkp, :supplierIsActive, :companyId, :supplierCreatedAt, :supplierUpdatedAt, :userId, :supplierDeletedAt
                )
                """;
        try {
            return jdbc.update(sql, toParams(model));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int update(Supplier model) {
        String sql = """
                UPDATE public.suppliers
                SET
                    supplier_code = :supplierCode,
                    supplier_name = :supplierName,
                    supplier_type = :supplierType,
                    supplier_npwp = :supplierNpwp,
                    supplier_address = :supplierAddress,
                    supplier_city = :supplierCity,
                    supplier_province = :supplierProvince,
                    supplier_postal_code = :supplierPostalCode,
                    supplier_country = :supplierCountry,
                    supplier_email = :supplierEmail,
                    supplier_contact_person = :supplierContactPerson,
                    supplier_contact_phone = :supplierContactPhone,
                    supplier_payment_term_days = :supplierPaymentTermDays,
                    supplier_credit_limit = :supplierCreditLimit,
                    supplier_bank_name = :supplierBankName,
                    supplier_bank_account_number = :supplierBankAccountNumber,
                    supplier_bank_account_name = :supplierBankAccountName,
                    supplier_pkp = :supplierPkp,
                    supplier_is_active = :supplierIsActive,
                    supplier_updated_at = :supplierUpdatedAt
                WHERE supplier_id = :supplierId and company_id = :companyId;
                                """;
        try {
            return jdbc.update(sql, toParams(model));
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    public List<Supplier> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt, UUID lastId,
            int limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        params.put("limit", limit);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            params.put("keyword", keyword + "%");
        }
        String sql = """
                SELECT
                    supplier_id,
                    supplier_code,
                    supplier_name,
                    supplier_contact_phone,
                    supplier_email,
                    supplier_is_active,
                    supplier_created_at
                FROM suppliers WHERE company_id = :companyId
                """;

        if (hasKeyword) {
            // query ini hanya lambat jika datanya sudah 100.000 baris di bawah itu cepat
            // AND (supplier_name ILIKE CONCAT('%', :keyword, '%'))
            sql += " AND (supplier_name ILIKE CONCAT('%', :keyword, '%'))";
        }

        if (lastCreatedAt != null && lastId != null) {
            sql += """
                        AND (
                            supplier_created_at < :lastCreatedAt
                            OR (supplier_created_at = :lastCreatedAt AND supplier_id < :lastId)
                        )
                    """;
            params.put("lastCreatedAt", lastCreatedAt);
            params.put("lastId", lastId);
        }

        sql += " ORDER BY supplier_created_at DESC, supplier_id DESC LIMIT :limit ";
        List<Supplier> result = jdbc.query(sql, params, new DataClassRowMapper<>(Supplier.class));
        return result;
    }

    public String generateCode(UUID companyId) {
        String sql = """
                SELECT supplier_code
                FROM suppliers
                WHERE company_id = :companyId
                ORDER BY supplier_code DESC
                LIMIT 1
                """;
        try {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("companyId", companyId);
            String lastCode = jdbc.queryForObject(sql, params, String.class);

            if (lastCode != null && lastCode.startsWith("SP-")) {
                int number = Integer.parseInt(lastCode.substring(3));
                number++;
                return String.format("SP-%05d", number);
            }
        } catch (EmptyResultDataAccessException e) {
            return "SP-00001";
        } catch (Exception e) {
            System.err.println("Error generating code: " + e.getMessage());
        }
        return "SP-00001";
    }

    public String generateSupplierCode(UUID companyId) {
        String lastCode = null;
        // 1. Ambil kode terakhir dengan LOCK (FOR UPDATE)
        String sqlGetLast = """
                SELECT supplier_code FROM suppliers
                WHERE company_id = :companyId
                ORDER BY supplier_code DESC
                LIMIT 1 FOR UPDATE
                """;
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("companyId", companyId);
            lastCode = jdbc.queryForObject(sqlGetLast, data, String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        // 2. Logika increment nomor urut
        int newNumber = 1;
        if (lastCode != null && lastCode.startsWith("SP-")) {
            try {
                // Mengambil angka setelah "SP-"
                newNumber = Integer.parseInt(lastCode.substring(3)) + 1;
            } catch (NumberFormatException e) {
                newNumber = 1; // Fallback jika format kode rusak
            }
        }

        return String.format("SP-%05d", newNumber);
    }

    public Supplier detailById(UUID id) {
        String sql = """
                select * from suppliers where supplier_id = :supplierId
                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("supplierId", id);
        try {
            return jdbc.queryForObject(sql, params, new SupplierMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengambil supplier dengan ID: " + id, e);
        }
    }

}
