package com.logikaintermedia.erp.modules.bankaccounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

public class BankAccountsMapper implements RowMapper<BankAccounts> {

    @Override
    @Nullable
    public BankAccounts mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {

        BankAccounts account = new BankAccounts();
        account.setBankAccountId((UUID) rs.getObject("bank_account_id"));
        account.setAccountCode(rs.getString("account_code"));
        account.setAccountName(rs.getString("account_name"));
        account.setAccountType(rs.getString("account_type"));
        account.setBankName(rs.getString("bank_name"));
        account.setBankBranch(rs.getString("bank_branch"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountHolder(rs.getString("account_holder"));
        account.setCurrency(rs.getString("currency"));
        account.setOpeningBalance(rs.getBigDecimal("opening_balance"));
        account.setIsDefault(rs.getBoolean("is_default"));
        account.setIsActive(rs.getBoolean("is_active"));
        account.setCompanyId((UUID) rs.getObject("company_id"));
        account.setAccountId((UUID) rs.getObject("account_id"));
        ChartOfAccounts coa = new ChartOfAccounts();
        coa.setAccountName(rs.getString("account_name"));
        account.setUserId((UUID) rs.getObject("user_id"));
        account.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        account.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
        account.setDeletedAt(rs.getObject("deleted_at", OffsetDateTime.class));

        return account;
    }

}
