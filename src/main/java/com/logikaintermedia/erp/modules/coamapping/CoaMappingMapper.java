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
        mapping.setMappingId(rs.getObject("mapping_id", UUID.class));
        mapping.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        mapping.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
        mapping.setDescription(rs.getString("description"));
        mapping.setCompanyId(rs.getObject("company_id", UUID.class));
        return mapping;
    }

}
