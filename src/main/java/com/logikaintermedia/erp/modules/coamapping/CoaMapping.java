package com.logikaintermedia.erp.modules.coamapping;

import java.util.UUID;
import com.fasterxml.uuid.Generators;
import lombok.Data;

@Data
public class CoaMapping {
	private UUID mappingId;
	private TransactionType transactionType;
	private PaymentType paymentType;
	private String description;
	private UUID companyId;
	private int total;

	/**
	 * Save Mengonversi CoaMappingRequest menjadi CoaMapping.
	 */
	public static CoaMapping from(CoaMappingRequest request, UUID companyId) {
		CoaMapping model = new CoaMapping();
		model.setMappingId(Generators.timeBasedEpochGenerator().generate());
		model.setTransactionType(TransactionType.valueOf(request.getTransactionType()));
		model.setPaymentType(PaymentType.valueOf(request.getPaymentType()));
		model.setDescription(request.getDescription());
		model.setCompanyId(companyId);
		return model;
	}

	/**
	 * Update Mengonversi CoaMappingRequest menjadi CoaMapping.
	 */
	public static CoaMapping updateFrom(CoaMappingRequest request, UUID id, UUID companyId) {
		CoaMapping model = new CoaMapping();
		model.setMappingId(id);
		// model.setTransactionType(TransactionType.valueOf(request.getTransactionType()));
		// model.setPaymentType(PaymentType.valueOf(request.getPaymentType()));
		model.setDescription(request.getDescription());
		model.setCompanyId(companyId);
		return model;
	}
}
