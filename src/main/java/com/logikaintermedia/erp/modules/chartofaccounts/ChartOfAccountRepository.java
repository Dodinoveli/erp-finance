package com.logikaintermedia.erp.modules.chartofaccounts;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ChartOfAccountRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ChartOfAccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChartOfAccounts> findAllByCompanyId(UUID companyId) {
        String sql = """
                SELECT
                    account_id, account_code, account_name,
                    account_type, normal_balance, parent_id,
                    account_level, is_header, is_postable,
                    description, sort_order, created_at,
                    updated_at, template_account_id
                FROM public.chart_of_accounts
                where company_id = :companyId
                ORDER BY parent_id NULLS FIRST,
                sort_order, account_code;
                """;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        try {
            return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ChartOfAccounts.class));
        } catch (DataAccessException e) {
            log.error("Error fetching all account : {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch account ", e);
        }
    }

    // mengambil coa bedasarkan parentId, untuk menampilkan detail
    public List<ChartOfAccounts> findByParentId(UUID parentId, UUID companyId) {
        String sql = """
                SELECT
                    c.*,
                    p.account_code AS parent_code,
                    p.account_name AS parent_name
                FROM chart_of_accounts c
                LEFT JOIN chart_of_accounts p
                    ON p.account_id = c.parent_id
                WHERE c.parent_id = :parentId
                and c.company_id= :companyId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("parentId", parentId)
                .addValue("companyId", companyId);
        try {
            return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ChartOfAccounts.class));
        } catch (DataAccessException e) {
            log.error("Error fetching all account by parent_id : {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch account parent_id", e);
        }
    }

    // untuk ambil data coa bedasarkan id
    public ChartOfAccounts findById(UUID accountId, UUID companyId) {
        String sql = """
                SELECT
                   *
                FROM chart_of_accounts
                WHERE account_id = :accountId
                and company_id = :companyId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("companyId", companyId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(ChartOfAccounts.class));
        } catch (DataAccessException e) {
            log.error("Error fetching all account by accountId : {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch account accountId", e);
        }
    }

    public int initializeCompanyCoa(UUID companyId) {
        // Cek apakah COA perusahaan sudah pernah di-install
        String checkSql = """
                SELECT COUNT(*)
                FROM public.chart_of_accounts
                WHERE company_id = :companyId
                """;

        MapSqlParameterSource params1 = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        String insertSql = """
                INSERT INTO public.chart_of_accounts (
                    account_id,
                    template_account_id,
                    account_code,
                    account_name,
                    account_type,
                    normal_balance,
                    parent_id,
                    account_level,
                    is_header,
                    is_postable,
                    description,
                    sort_order,
                    created_at,
                    updated_at,
                    company_id
                )
                SELECT
                    gen_random_uuid(),
                    t.template_account_id,
                    t.account_code,
                    t.account_name,
                    t.account_type,
                    t.normal_balance,
                    NULL,
                    t.account_level,
                    t.is_header,
                    t.is_postable,
                    t.description,
                    t.sort_order,
                    NOW(),
                    NOW(),
                    :companyId
                FROM public.chart_of_accounts_templates t
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM public.chart_of_accounts n
                    WHERE n.company_id = :companyId
                      AND n.account_code = t.account_code
                )
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        String updateParentSql = """
                UPDATE public.chart_of_accounts AS child
                SET parent_id = parent.account_id
                FROM public.chart_of_accounts_templates AS t,
                     public.chart_of_accounts AS parent
                WHERE child.template_account_id = t.template_account_id
                  AND parent.template_account_id = t.parent_template_id
                  AND parent.company_id = child.company_id
                  AND child.company_id = :companyId
                """;
        try {
            Integer count = jdbcTemplate.queryForObject(
                    checkSql,
                    params1,
                    Integer.class);

            // Jika sudah ada COA, jangan install lagi
            if (count != null && count > 0) {
                throw new IllegalArgumentException(
                        "Chart of Accounts sudah di-install");
            }

            int insertedRows = jdbcTemplate.update(insertSql, params);
            jdbcTemplate.update(updateParentSql, params);

            return insertedRows;
        } catch (DataAccessException e) {
            log.error("Gagal initialize COA, companyId={}", companyId, e);
            throw new IllegalArgumentException("Gagal membuat Chart of Accounts perusahaan", e);
        }
    }

    public int save(ChartOfAccounts account) {

        return 0;
    }

    public int update(ChartOfAccounts account) {

        return 0;
    }

}
