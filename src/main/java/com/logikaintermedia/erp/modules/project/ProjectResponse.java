package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ProjectResponse {
	private UUID projectId;
	private String projectCode;
	private String projectPo;
	private String name;
	private String description;
	private UUID clientId;
	private ProjectType projectType;
	private BigDecimal dpp;

	// dpp
	private BigDecimal contractValue;
	private String location;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private UUID companyId;
	private UUID userId;
	private TaxType taxType;
	private BigDecimal vatRate;
	private BigDecimal totalTax;
	private BigDecimal totalAmount;
	private LocalDate poDate;

	// client
	private String clientName;

	public static ProjectResponse from(Project entity) {
		ProjectResponse dto = new ProjectResponse();
		dto.setProjectId(entity.getProjectId());
		dto.setProjectCode(entity.getProjectCode());
		dto.setProjectPo(entity.getProjectPo());
		dto.setPoDate(entity.getPoDate());
		dto.setName(entity.getName());
		dto.setProjectType(entity.getProjectType());
		dto.setContractValue(entity.getContractValue());
		dto.setClientName(entity.getClientName());
		dto.setTaxType(entity.getTaxType());
		dto.setTotalTax(entity.getTotalTax());
		dto.setDpp(entity.getDpp());
		dto.setVatRate(entity.getVatRate());
		dto.setTotalAmount(entity.getTotalAmount());
		dto.setCreatedAt(entity.getCreatedAt());
		return dto;
	}
}
