package com.logikaintermedia.erp.modules.company;

import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CompanyRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private @NonNull MapSqlParameterSource toParams(Company model) {

        return new MapSqlParameterSource()
                .addValue("companyId", model.getCompanyId())
                .addValue("legalName", model.getLegalName())
                .addValue("companyCode", model.getCompanyCode())
                .addValue("npwp", model.getNpwp())
                .addValue("nib", model.getNib())
                .addValue("address", model.getAddress())
                .addValue("city", model.getCity())
                .addValue("province", model.getProvince())
                .addValue("postalCode", model.getPostalCode())
                .addValue("country", model.getCountry())
                .addValue("phone", model.getPhone())
                .addValue("email", model.getEmail())
                .addValue("companyType", model.getCompanyType())
                .addValue("pkp", Boolean.parseBoolean(String.valueOf(model.getPkp())))
                .addValue("baseCurrency", model.getBaseCurrency())
                .addValue("isActive", Boolean.parseBoolean(String.valueOf(model.getIsActive())))
                .addValue("createdAt", model.getCreatedAt())
                .addValue("updatedAt", model.getUpdatedAt());
    }

    public int insert(Company model) {
        String sql = """
                INSERT INTO public.company(
                	company_id, legal_name, npwp, nib, address, city, province, postal_code, country, phone, email, company_type, pkp, base_currency, is_active, created_at, updated_at)
                	VALUES (
                    :companyId, :legalName, :npwp, :nib, :address, :city, :province, :postalCode, :country, :phone, :email, :companyType, :pkp, :baseCurrency, :isActive, :createdAt, :updatedAt);
                                """;
        return jdbcTemplate.update(sql, toParams(model));
    }

    public int insertCompanySequences(UUID id, UUID companyId) {
        String[] types = { "CLIENT", "SUPPLIER", "PROJECT", "INVOICE", "PAYMENT", "COA" };

        String seqSql = """
                INSERT INTO company_sequences (id, company_id, sequence_type, current_value)
                VALUES (:id, :companyId, :sequenceType, 0)
                """;

        int total = 0;
        for (String type : types) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("companyId", companyId)
                    .addValue("sequenceType", type);
            total += jdbcTemplate.update(seqSql, params);
        }
        return total;
    }

    public String companyCodeById(UUID id) {
        String sql = """
                select
                company_code
                from company
                where company_id = :companyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", id);

        return jdbcTemplate.queryForObject(sql, params, String.class);
    }
}
