package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public long generateSupplierCode(UUID companyId) {
        String sql = """
                     UPDATE company_sequences
                         SET current_value = current_value + 1
                     WHERE company_id = :companyId
                         AND sequence_type = 'SUPPLIER'
                         RETURNING current_value;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);

        Long result = jdbc.queryForObject(sql, params, Long.class);
        if (result == null) {
            throw new IllegalStateException("Gagal mendapatkan nomor SUPPLIER untuk company: " + companyId);
        }
        return result;
    }

    public List<Supplier> findSupplierByCompanyId(UUID companyId,
            int start,
            int length,
            String keyword) {
        String sql = """
                select * from suppliers where company_id = :companyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        params.addValue("start", start);
        params.addValue("length", length);
        if (keyword != null && !keyword.isBlank()) {
            sql += """
                    AND (
                            supplier_name ILIKE :keyword
                            OR supplier_code ILIKE :keyword
                            OR supplier_contact_person ILIKE :keyword
                            OR supplier_email ILIKE :keyword
                        )
                    """;
            params.addValue("keyword", "%" + keyword.trim() + "%");
        }
        sql += """
                ORDER BY supplier_code DESC
                LIMIT :length OFFSET :start
                """;
        try {
            List<Supplier> result = jdbc.query(sql, params, new SupplierMapper());
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
                FROM suppliers
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
                FROM suppliers
                WHERE company_id = :companyId
                """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
                    AND (
                            supplier_name ILIKE :keyword
                            OR supplier_code ILIKE :keyword
                            OR supplier_contact_person ILIKE :keyword
                            OR supplier_email ILIKE :keyword
                        )
                    """);

            params.addValue("keyword", "%" + keyword.trim() + "%");
        }

        Long result = jdbc.queryForObject(
                sql.toString(),
                params,
                Long.class);

        return result != null ? result : 0;
    }

    // untuk dashboard
    @SuppressWarnings("null")
    public int countTotalByCompanyId(UUID companyId) {

        String sql = """
                SELECT COUNT(*) as count
                FROM suppliers
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
                    FROM suppliers
                where supplier_is_active = true
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
                    FROM suppliers
                where supplier_is_active = false
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
                    FROM suppliers
                WHERE company_id = :companyId
                    and supplier_created_at  >= CURRENT_TIMESTAMP - INTERVAL '7 days'
                """;

        Long result = jdbc.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result.intValue() : 0;
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
