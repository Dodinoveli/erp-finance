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
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private @NonNull MapSqlParameterSource toParams(BankAccounts acc) {
        return new MapSqlParameterSource()
                .addValue("bankAccountId", acc.getBankAccountId())
                .addValue("accountCode", acc.getAccountCode())
                .addValue("accountName", acc.getAccountName())
                .addValue("accountType", acc.getAccountType())
                .addValue("bankName", acc.getBankName())
                .addValue("bankBranch", acc.getBankBranch())
                .addValue("accountNumber", acc.getAccountNumber())
                .addValue("accountHolder", acc.getAccountHolder())
                .addValue("currency", acc.getCurrency())
                .addValue("openingBalance", acc.getOpeningBalance())
                .addValue("isDefault", acc.getIsDefault())
                .addValue("isActive", acc.getIsActive())
                .addValue("companyId", acc.getCompanyId())
                .addValue("account_id", acc.getAccountId())
                .addValue("createdAt", acc.getCreatedAt())
                .addValue("updatedAt", acc.getUpdatedAt())
                .addValue("userId", acc.getUserId())
                .addValue("deletedAt", acc.getDeletedAt());
    }

    public int save(BankAccounts accounts) {
        String sql = """
                INSERT INTO bank_accounts(
                	bank_account_id, account_code, account_name, account_type,
                    bank_name, bank_branch, account_number, account_holder, currency,
                    opening_balance, is_default, is_active, company_id, account_id,
                    created_at, updated_at, user_id, deleted_at
                    )
                    VALUES(
                        :bankAccountId, :accountCode, :accountName, :accountType,
                        :bankName, :bankBranch, :accountNumber, :accountHolder, :currency,
                        :openingBalance, :isDefault, :isActive, :companyId, :accountId,
                        :createdAt, :updatedAt, :userId, :deletedAt
                        );
                                """;
        return jdbcTemplate.update(sql, toParams(accounts));
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
                    updated_at = :updatedAt
                where bank_account_id = :bankAccountId
                and company_id = :companyId
                and user_id = :userId
                        """;
        return jdbcTemplate.update(sql, toParams(accounts));
    }

    public List<BankAccounts> findBankAccountsByCompanyId(UUID companyId) {
        String sql = """
                select
                    *
                from bank_accounts
                    inner join chart_of_accounts
                    on chart_of_accounts.account_id= bank_accounts.account_id
                where bank_accounts.company_id = :companyId
                                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);

        return jdbcTemplate.query(sql,
                params,
                new BankAccountsMapper());
    }

    public BankAccounts findDetailById(UUID bankAccountId) {
        String sql = """
                select
                    *
                from bank_accounts
                    inner join chart_of_accounts
                    on chart_of_accounts.account_id= bank_accounts.account_id
                where bank_accounts.bank_account_id = :bankAccountId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bankAccountId", bankAccountId);
        return jdbcTemplate.queryForObject(sql,
                params,
                new BankAccountsMapper());
    }

    // mengambil dan menampilkan  kategori akun Kas dan bank bedasarkan companies id 
    public List<ChartOfAccounts>  findCashAndBankAccountsByCompanyId(UUID id){
        String sql = """
            select * from chart_of_accounts 
                where account_name 
            IN ('KAS','BANK') and company_id = :companyId
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", id);
        return jdbcTemplate.query(sql,
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
        params.addValue("parentId",parentId);
        return jdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(ChartOfAccounts.class));
    }

}
