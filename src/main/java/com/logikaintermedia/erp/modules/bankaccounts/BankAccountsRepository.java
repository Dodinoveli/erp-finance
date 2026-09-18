package com.logikaintermedia.erp.modules.bankaccounts;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

@Repository
public class BankAccountsRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BankAccountsRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private @NonNull MapSqlParameterSource toParams(BankAccounts acc) {
        return new MapSqlParameterSource()
                .addValue("bankAccountId", acc.getBankAccountId())
                .addValue("accountCode", acc.getAccountCode())
                .addValue("accountName", acc.getAccountName())
                .addValue("bankName", acc.getBankName())
                .addValue("bankBranch", acc.getBankBranch())
                .addValue("accountNumber", acc.getAccountNumber())
                .addValue("accountHolder", acc.getAccountHolder())
                .addValue("currency", acc.getCurrency())
                .addValue("openingBalance", acc.getOpeningBalance())
                .addValue("isDefault", acc.getIsDefault())
                .addValue("isActive", acc.getIsActive())
                .addValue("companyId", acc.getCompanyId())
                .addValue("accountId", acc.getAccountId())
                .addValue("createdAt", acc.getCreatedAt())
                .addValue("updatedAt", acc.getUpdatedAt())
                .addValue("userId", acc.getUserId())
                .addValue("deletedAt", acc.getDeletedAt());
    }

    public int save(BankAccounts accounts) {
        String sql = """
                INSERT INTO bank_accounts(
                	bank_account_id, account_code, account_name,
                    bank_name, bank_branch, account_number, account_holder, currency,
                    opening_balance, is_default, is_active, company_id, account_id,
                    created_at, updated_at, user_id, deleted_at
                    )
                    VALUES(
                        :bankAccountId, :accountCode, :accountName,
                        :bankName, :bankBranch, :accountNumber, :accountHolder, :currency,
                        :openingBalance, :isDefault, :isActive, :companyId, :accountId,
                        :createdAt, :updatedAt, :userId, :deletedAt
                        );
                                """;
        return namedParameterJdbcTemplate.update(sql, toParams(accounts));
    }

    public int update(BankAccounts accounts) {
        String sql = """
                UPDATE bank_accounts SET
                    account_code = :accountCode,
                    account_name = :accountName,
                    account_type = :accountType,
                    bank_name = :bankName,
                    bank_branch = :bankBranch,
                    account_number = :accountNumber,
                    account_holder = :accountHolder,
                    account_id = :accountId,
                    is_active = :isActive,
                    updated_at = :updatedAt,
                    account_id_detail = :accountIdDetail
                where bank_account_id = :bankAccountId
                and company_id = :companyId
                and user_id = :userId
                        """;
        return namedParameterJdbcTemplate.update(sql, toParams(accounts));
    }

    public List<BankAccounts> findBankAccountsByCompanyId(UUID companyId) {
        String sql = """
                SELECT
                    ba.bank_account_id,
                    ba.account_code,
                    ba.account_name,
                    ba.bank_name,
                    ba.bank_branch,
                    ba.account_number,
                    ba.account_holder,
                    ba.currency,
                    ba.opening_balance,
                    ba.is_default,
                    ba.is_active,
                    ba.company_id,
                    ba.account_id,
                    ba.user_id,
                    ba.created_at,
                    ba.updated_at,
                    ba.deleted_at,
                    parent.account_code AS parent_account_code,
                    parent.account_name AS parent_account_name,
                    child.account_code AS child_account_code,
                    child.account_name AS child_account_name
                FROM bank_accounts ba
                LEFT JOIN chart_of_accounts child
                    ON child.account_id = ba.account_id
                LEFT JOIN chart_of_accounts parent
                    ON parent.account_id = child.parent_id
                WHERE ba.company_id = :companyId
                AND ba.deleted_at IS NULL;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);

        return namedParameterJdbcTemplate.query(sql,
                params,
                new BankAccountsMapper());
    }

    public BankAccounts findDetailById(UUID bankAccountId, UUID companyId) {
        String sql = """
                     SELECT
                        ba.bank_account_id,
                        ba.account_code,
                        ba.account_name,
                        ba.bank_name,
                        ba.bank_branch,
                        ba.account_number,
                        ba.account_holder,
                        ba.currency,
                        ba.opening_balance,
                        ba.is_default,
                        ba.is_active,
                        ba.company_id,
                        ba.account_id,
                        ba.user_id,
                        ba.created_at,
                        ba.updated_at,
                        ba.deleted_at,
                        parent.account_code AS parent_account_code,
                        parent.account_name AS parent_account_name,
                        child.account_code AS child_account_code,
                        child.account_name AS child_account_name
                    FROM bank_accounts ba
                    LEFT JOIN chart_of_accounts child
                        ON child.account_id = ba.account_id
                    LEFT JOIN chart_of_accounts parent
                        ON parent.account_id = child.parent_id
                    WHERE  ba.bank_account_id = :bankAccountId and ba.company_id = :companyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bankAccountId", bankAccountId);
        params.addValue("companyId", companyId);
        return namedParameterJdbcTemplate.queryForObject(sql,
                params,
                new BankAccountsMapper());
    }

    // mengambil dan menampilkan kategori akun Kas dan bank bedasarkan companies id
    public List<ChartOfAccounts> findCashAndBankAccountsByCompanyId(UUID id) {
        String sql = """
                SELECT
                    parent.account_id,
                    parent.account_code,
                    parent.account_name,

                    child.account_id AS child_account_id,
                    child.account_code AS child_account_code,
                    child.account_name AS child_account_name

                FROM chart_of_accounts parent

                INNER JOIN chart_of_accounts child
                    ON child.parent_id = parent.account_id

                WHERE parent.account_name IN ('KAS', 'BANK')

                AND parent.company_id = :companyId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", id);
        return namedParameterJdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(ChartOfAccounts.class));
    }

    // mengambil COA bedasarkan parent_id saat combok di klik
    public List<ChartOfAccounts> findCoaByparentId(UUID companyId, UUID parentId) {
        String sql = """
                SELECT
                    c.*,
                    p.account_code AS parent_code,
                    p.account_name AS parent_name
                FROM chart_of_accounts c
                LEFT JOIN chart_of_accounts p
                    ON p.account_id = c.parent_id
                WHERE c.parent_id = :parentId
                and c.company_id = :companyId
                 """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);
        params.addValue("parentId", parentId);
        return namedParameterJdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(ChartOfAccounts.class));
    }

}
