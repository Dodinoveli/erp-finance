package com.logikaintermedia.erp.modules.transactioncode;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.logikaintermedia.erp.modules.company.Company;

import org.springframework.dao.EmptyResultDataAccessException;
@Repository
public class TransactionCodeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TransactionCodeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public  Optional<Map<String, Object>> companyById(UUID id) {
        String sql = """
                select
                company_id as companyId,
                company_code as companyCode
                from company
                where company_id = :companyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", id);

        try {
            return Optional.of(jdbcTemplate.queryForMap(sql, params));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int updateCode(Company model){
        String sql ="""
                update company 
                    set company_code = :companyCode 
                where company_id = :companyId
                """;
                MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyCode", model.getCompanyCode());
        params.addValue("companyId", model.getCompanyId());
                return  jdbcTemplate.update(sql, params);
    }
}
