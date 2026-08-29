package com.logikaintermedia.erp.modules.coa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class CoaMapper implements RowMapper<Coa> {

    @Override
    @Nullable
    public Coa mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Coa accounts = new Coa();
        accounts.setCoaId((UUID) rs.getObject("coa_id"));
        accounts.setCoaCode(rs.getString("coa_code"));
        accounts.setName(rs.getString("name"));
        accounts.setAccountCategory(rs.getString("account_category"));
        accounts.setParentId((UUID) rs.getObject("parent_id"));
        accounts.setHeader(rs.getBoolean("is_header"));
        accounts.setNormalBalance(rs.getString("normal_balance"));
        return accounts;
    }

}
