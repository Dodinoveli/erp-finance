package com.logikaintermedia.erp.modules.coamapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class CoaMappingMapper implements RowMapper<CoaMapping> {

    @Override
    @Nullable
    public CoaMapping mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        CoaMapping mapping = new CoaMapping();
        mapping.setMappingId((UUID) rs.getObject("mapping_id"));
        mapping.setTransactionType(rs.getString("transaction_type"));
        mapping.setPaymentType(rs.getString("paymentType"));
        mapping.setDebitCoaId((UUID) rs.getObject("debit_coa_id"));
        mapping.setCreditCoaId((UUID) rs.getObject("credit_coa_id"));
        mapping.setDescription(rs.getString("description"));
        mapping.setCompanyId((UUID) rs.getObject("company_id"));
        return mapping;
    }

}
