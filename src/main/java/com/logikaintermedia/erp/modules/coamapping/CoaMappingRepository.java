package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class CoaMappingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CoaMappingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_SELECT = """
            SELECT mapping_id, transaction_type, payment_type, description, company_id
            FROM coa_mapping
            """;

    public @NonNull MapSqlParameterSource toParams(CoaMapping mapping) {
        return new MapSqlParameterSource()
                .addValue("mappingId", mapping.getMappingId())
                .addValue("transactionType", mapping.getTransactionType().name())
                .addValue("paymentType", mapping.getPaymentType().name())
                .addValue("description", mapping.getDescription())
                .addValue("companyId", mapping.getCompanyId());
    }

    public Optional<CoaMapping> findByTransactionTypeAndPaymentType(String transactionType,
            String paymentType, UUID companyId) {
        String sql = BASE_SELECT + """
                where
                    transaction_type = :transactionType
                    and payment_type= :paymentType
                    and company_id= :companyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("transactionType", transactionType)
                .addValue("paymentType", paymentType)
                .addValue("companyId", companyId);

        return jdbcTemplate.query(sql, params, new CoaMappingMapper()).stream().findFirst();
    }

    public List<CoaMapping> findCoaMappingById(UUID companyId,
            int start,
            int length,
            String keyword) {
        String sql = """
                SELECT  transaction_type,
                count(mapping_id) as total
                    FROM coa_mapping
                WHERE company_id = :companyId
                                    """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        params.addValue("start", start);
        params.addValue("length", length);
        if (keyword != null && !keyword.isBlank()) {
            sql += """
                    AND (
                        transaction_type ILIKE :keyword
                    )
                    """;
            params.addValue("keyword", "%" + keyword.trim() + "%");
        }
        sql += """
                GROUP BY transaction_type
                ORDER BY total ASC
                LIMIT :length OFFSET :start
                """;
        try {
            List<CoaMapping> result = jdbcTemplate.query(sql, params,
                    new BeanPropertyRowMapper<>(CoaMapping.class));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("null")
    public long countAll(UUID companyId) {

        String sql = """
                SELECT  COUNT(DISTINCT transaction_type)
                FROM coa_mapping
                WHERE company_id = :companyId
                """;

        Long result = jdbcTemplate.queryForObject(
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
                SELECT COUNT(DISTINCT transaction_type)
                FROM coa_mapping
                WHERE company_id = :companyId
                """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
                    AND (
                        transaction_type ILIKE :keyword
                    )
                    """);

            params.addValue(
                    "keyword",
                    "%" + keyword.trim() + "%");
        }

        Long result = jdbcTemplate.queryForObject(
                sql.toString(),
                params,
                Long.class);

        return result != null ? result : 0;
    }

    public List<CoaMapping> detail(String keyword, UUID companyId) {
        String sql = BASE_SELECT + """
                where transaction_type = :transactionType
                and company_id = :companyId
                        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("transactionType", keyword)
                .addValue("companyId", companyId);
        try {
            List<CoaMapping> result = jdbcTemplate.query(sql, params,
                    new BeanPropertyRowMapper<>(CoaMapping.class));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int save(CoaMapping model) {
        String sql = """
                    INSERT INTO public.coa_mapping (
                        mapping_id, transaction_type, payment_type, description, company_id
                    ) VALUES (
                        :mappingId, :transactionType, :paymentType, :description, :companyId
                    )
                """;
        return jdbcTemplate.update(sql, toParams(model));
    }

}
