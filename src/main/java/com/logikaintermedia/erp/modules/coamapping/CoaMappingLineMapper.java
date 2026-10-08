package com.logikaintermedia.erp.modules.coamapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

public class CoaMappingLineMapper implements RowMapper<CoaMappingLine> {

	@Override
	@Nullable
	public CoaMappingLine mapRow(ResultSet rs, int rowNum) throws SQLException {
		CoaMappingLine line = new CoaMappingLine();
		line.setMappingLineId(rs.getObject("mapping_line_id", UUID.class));
		line.setMappingId(rs.getObject("mapping_id", UUID.class));
		line.setCoaId(rs.getObject("coa_id", UUID.class));
		line.setPosition(JournalPosition.valueOf(rs.getString("position")));
		line.setLineOrder(rs.getInt("line_order"));
		line.setAmountType(AmountType.valueOf(rs.getString("amount_type")));
		line.setIsActive(rs.getBoolean("is_active"));
		line.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
		line.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
		
//		masukan ke dalam coa mapping line 
		CoaMapping coaMapping = new CoaMapping();
		coaMapping.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
		coaMapping.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
		line.setCoaMapping(coaMapping);
		
//		masukan ke dalam coa mapping line 
		ChartOfAccounts accounts = new ChartOfAccounts();
		accounts.setAccountName(rs.getString("account_name"));
		line.setAccounts(accounts);
		
		return line;
	}

}
