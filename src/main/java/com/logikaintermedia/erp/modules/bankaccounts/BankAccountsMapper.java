package com.logikaintermedia.erp.modules.bankaccounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class BankAccountsMapper implements RowMapper<BankAccounts> {

    @Override
    @Nullable
    public BankAccounts mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {

        BankAccounts account = new BankAccounts();
        account.setBankAccountId((UUID) rs.getObject("bank_account_id"));
        account.setAccountCode(rs.getString("account_code"));
        account.setAccountName(rs.getString("account_name"));

        // Konversi String ke Enum
        // String accountTypeStr = rs.getString("account_type");
        // if (accountTypeStr != null) {
        // account.setAccountType(AccountType.valueOf(accountTypeStr));
        // }
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
        account.setCoaId((UUID) rs.getObject("coa_id"));
        account.setUserId((UUID) rs.getObject("user_id"));
        account.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        account.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        account.setDeletedAt(rs.getObject("deleted_at", LocalDateTime.class));

        return account;
    }

}
