package com.logikaintermedia.erp.modules.coa;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CoaRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CoaRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Coa> fetchPage(UUID companyId) {

        String sql = """
                    SELECT coa_id, coa_code, name, account_category, parent_id, is_header, normal_balance
                    FROM chart_of_accounts
                    WHERE company_id = :companyId
                    ORDER BY coa_code ASC
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        return namedParameterJdbcTemplate.query(sql, params, new CoaMapper());

    }
}
