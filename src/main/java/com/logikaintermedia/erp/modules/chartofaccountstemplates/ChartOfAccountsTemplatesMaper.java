package com.logikaintermedia.erp.modules.chartofaccountstemplates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;

public class ChartOfAccountsTemplatesMaper implements RowMapper<ChartOfAccountsTemplates> {

    @Override
    @Nullable
    public ChartOfAccountsTemplates mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        UUID parentId = null;
        String parentIdStr = rs.getString("parent_template_id");
        if (parentIdStr != null) {
            parentId = UUID.fromString(parentIdStr);
        }

        return ChartOfAccountsTemplates.builder()
                .templateAccountId(UUID.fromString(rs.getString("template_account_id")))
                .accountCode(rs.getString("account_code"))
                .accountName(rs.getString("account_name"))
                .accountType(rs.getString("account_type"))
                .normalBalance(rs.getString("normal_balance"))
                .parentTemplateId(parentId)
                .accountLevel(rs.getShort("account_level"))
                .isHeader(rs.getBoolean("is_header"))
                .isPostable(rs.getBoolean("is_postable"))
                .description(rs.getString("description"))
                .sortOrder(rs.getInt("sort_order"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .build();

    }

}
