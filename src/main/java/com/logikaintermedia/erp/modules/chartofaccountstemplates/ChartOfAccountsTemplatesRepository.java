package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ChartOfAccountsTemplatesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ChartOfAccountsTemplatesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChartOfAccountsTemplates> findAll() {
        String sql = """
                    SELECT
                         template_account_id,
                         account_code,
                         account_name,
                         account_type,
                         normal_balance,
                         parent_template_id,
                         account_level,
                         is_header,
                         is_postable,
                         description,
                         sort_order,
                         created_at,
                         updated_at
                     FROM chart_of_accounts_templates
                     ORDER BY
                parent_template_id NULLS FIRST,
                sort_order, account_code
                             """;
        try {
            return jdbcTemplate.query(sql, new ChartOfAccountsTemplatesMaper());
        } catch (DataAccessException e) {
            log.error("Error fetching all account templates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch account templates", e);
        }
    }

}
