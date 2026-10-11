package com.logikaintermedia.erp.modules.coamapping;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import lombok.Data;

@Data
public class CoaMappingLine {
	private UUID mappingLineId;
	private UUID mappingId;
	private UUID coaId;
	private JournalPosition position;
	private Integer lineOrder;
	private AmountType amountType;
	private Boolean isActive;
	private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);
	private OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
	
	
//	masukan ke dalam coa mapping line -> coa mapping 
	private CoaMapping coaMapping;
	
//	masukan ke dalam coa mapping line -> chart of account 
	private ChartOfAccounts accounts;
}
