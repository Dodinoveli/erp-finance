package com.logikaintermedia.erp.modules.coamapping;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import lombok.Data;

@Data
public class CoaMappingLineResponse {
	private UUID mappingLineId;
	private UUID mappingId;
	private UUID coaId;
	private JournalPosition position;
	private Integer lineOrder;
	private AmountType amountType;
	private Boolean isActive;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private CoaMapping coaMapping;
    private ChartOfAccounts accounts;
    
	public static CoaMappingLineResponse from(CoaMappingLine mapping) {
		CoaMappingLineResponse response = new CoaMappingLineResponse();
		response.setCoaMapping(mapping.getCoaMapping());
		response.setMappingId(mapping.getMappingId());
		response.setCoaId(mapping.getCoaId());
		response.setPosition(mapping.getPosition());
		response.setLineOrder(mapping.getLineOrder());
		response.setAccounts(mapping.getAccounts());
		return response;
	}
}
