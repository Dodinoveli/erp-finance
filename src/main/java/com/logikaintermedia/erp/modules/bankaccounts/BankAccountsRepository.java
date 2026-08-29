package com.logikaintermedia.erp.modules.bankaccounts;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

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
                .addValue("coaId", acc.getCoaId())
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
                    opening_balance, is_default, is_active, company_id, coa_id,
                    created_at, updated_at, user_id, deleted_at
                    )
                    VALUES(
                        :bankAccountId, :accountCode, :accountName, :accountType,
                        :bankName, :bankBranch, :accountNumber, :accountHolder, :currency,
                        :openingBalance, :isDefault, :isActive, :companyId, :coaId,
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
                    coa_id = :coaId,
                    is_active = :isActive,
                    updated_at = :updatedAt
                where bank_account_id = :bankAccountId
                and company_id = :companyId
                and user_id = :userId
                        """;
        return jdbcTemplate.update(sql, toParams(accounts));
    }

    public List<BankAccounts> findBankAccountsById(UUID companyId) {
        String sql = """
                select
                    bank_accounts.bank_account_id,
                    bank_accounts.account_code,
                    bank_accounts.account_name,
                    bank_accounts.account_type,
                    bank_accounts.bank_name,
                    bank_accounts.bank_branch,
                    bank_accounts.account_number,
                    bank_accounts.account_holder,
                    bank_accounts.is_active,
                    chart_of_accounts.coa_id,
                    chart_of_accounts.name
                from bank_accounts
                    inner join chart_of_accounts
                    on chart_of_accounts.coa_id= bank_accounts.coa_id
                where bank_accounts.company_id = :companyId
                                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        return jdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(BankAccounts.class));
    }

    public BankAccounts findDetailById(UUID bankAccountId) {
        String sql = """
                select
                    bank_accounts.bank_account_id,
                    bank_accounts.account_code,
                    bank_accounts.account_name,
                    bank_accounts.account_type,
                    bank_accounts.bank_name,
                    bank_accounts.bank_branch,
                    bank_accounts.account_number,
                    bank_accounts.account_holder,
                    bank_accounts.is_active,
                    chart_of_accounts.coa_id,
                    chart_of_accounts.name
                from bank_accounts
                    inner join chart_of_accounts
                    on chart_of_accounts.coa_id= bank_accounts.coa_id
                where bank_accounts.bank_account_id = :bankAccountId
                                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("bankAccountId", bankAccountId);
        return jdbcTemplate.queryForObject(sql,
                params,
                new BeanPropertyRowMapper<>(BankAccounts.class));
    }

    public List<CoaAccounts> findCoaById(UUID companyId) {
        String sql = """
                select * from chart_of_accounts
                where coa_code in (:cd1, :cd2)
                and company_id = :companyId
                """;

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("cd1", "1.1.01");
        params.put("cd2", "1.1.02");
        params.put("companyId", companyId);
        return jdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(CoaAccounts.class));
    }

    public List<CoaAccounts> findCoaByName(UUID companyId, String keyword) {
        String sql = """
                select coa_id, name from chart_of_accounts
                where lower(name) like lower(:keyword)
                and company_id = :companyId
                 """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("keyword", keyword + "%");
        params.put("companyId", companyId);
        return jdbcTemplate.query(sql,
                params,
                new BeanPropertyRowMapper<>(CoaAccounts.class));
    }

}
