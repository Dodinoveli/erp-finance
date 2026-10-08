package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.uuid.Generators;
import lombok.Data;

@Data
public class Project {
	// header
	private UUID projectId;
	private String projectCode;
	private String projectPo;
	private String name;
	private String description;
	private UUID clientId;
	// private String projectType;
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
	private String clientName;

	public static Project from(ProjectRequest request, UUID companyId, UUID userId, String code) {
		Project mdl = new Project();
		mdl.setProjectId(Generators.timeBasedEpochGenerator().generate());
		mdl.setProjectCode(code);
		mdl.setProjectPo(request.getProjectPo());
		mdl.setName(request.getName());
		mdl.setDescription(request.getDescription());
		mdl.setClientId(request.getClientId());
		mdl.setProjectType(request.getProjectType());
		mdl.setLocation(request.getLocation());
		mdl.setContractValue(request.getContractValue());
		mdl.setCompanyId(companyId);
		mdl.setCreatedAt(request.getCreatedAt());
		mdl.setUpdatedAt(request.getUpdatedAt());
		mdl.setUserId(userId);
		mdl.setPoDate(request.getPoDate());
		BigDecimal nilaiKontrak = request.getContractValue();

		BigDecimal dpp = BigDecimal.ZERO;
		BigDecimal vatRate = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;

		// Nilai di atas BELUM termasuk PPN (Exclusive)
		if (request.getTaxType() == TaxType.EXCLUSIVE) {
			vatRate = BigDecimal.valueOf(12);
			totalTax = nilaiKontrak.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			totalAmount = nilaiKontrak.add(totalTax);
			// mdl.setContractValue(dpp);
			mdl.setDpp(request.getContractValue());
			mdl.setVatRate(vatRate); // 12
			mdl.setTotalTax(totalTax); //
			mdl.setTotalAmount(totalAmount);
		}

		// Nilai di atas SUDAH termasuk PPN (Inclusive)
		if (request.getTaxType() == TaxType.INCLUSIVE) {
			vatRate = BigDecimal.valueOf(12);
			// Angka 1 itu berasal dari nilai DPP itu sendiri, yaitu 100%.

			// 1. Cari Dpp
			dpp = nilaiKontrak.divide(BigDecimal.valueOf(1.12), 2, RoundingMode.HALF_UP);

			// 2. Cari PPN
			totalTax = dpp.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

			// 3. Total
			totalAmount = dpp.add(totalTax);
			mdl.setContractValue(nilaiKontrak);
			mdl.setDpp(dpp);
			mdl.setVatRate(vatRate);
			mdl.setTotalTax(totalTax); //
			mdl.setTotalAmount(totalAmount);
		}

		// Nilai di atas TIDAK kena PPN
		if (request.getTaxType() == TaxType.NON_PPN) {
			mdl.setContractValue(nilaiKontrak);
			mdl.setDpp(BigDecimal.ZERO);
			mdl.setVatRate(BigDecimal.ZERO); // 12
			mdl.setTotalTax(BigDecimal.ZERO); //
			mdl.setTotalAmount(nilaiKontrak);
		}

		mdl.setTaxType(request.getTaxType());
		return mdl;
	}

	public static Project updateFrom(ProjectRequest request, UUID projectId, UUID companyId, UUID userId) {
		Project mdl = new Project();
		mdl.setProjectId(projectId);
		mdl.setProjectPo(request.getProjectPo());
		mdl.setName(request.getName());
		mdl.setDescription(request.getDescription());
		mdl.setClientId(request.getClientId());
		mdl.setProjectType(request.getProjectType());
		mdl.setLocation(request.getLocation());
		mdl.setContractValue(request.getContractValue());
		mdl.setUpdatedAt(request.getUpdatedAt());
		mdl.setPoDate(request.getPoDate());
		BigDecimal nilaiKontrak = request.getContractValue();

		BigDecimal dpp = BigDecimal.ZERO;
		BigDecimal vatRate = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;

		// Nilai di atas BELUM termasuk PPN (Exclusive)
		if (request.getTaxType() == TaxType.EXCLUSIVE) {
			vatRate = BigDecimal.valueOf(12);
			totalTax = nilaiKontrak.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			totalAmount = nilaiKontrak.add(totalTax);

			mdl.setDpp(nilaiKontrak);
			mdl.setVatRate(vatRate); // 12
			mdl.setTotalTax(totalTax); //
			mdl.setTotalAmount(totalAmount);
		}

		// Nilai di atas SUDAH termasuk PPN (Inclusive)
		if (request.getTaxType() == TaxType.INCLUSIVE) {
			vatRate = BigDecimal.valueOf(12);
			// Angka 1 itu berasal dari nilai DPP itu sendiri, yaitu 100%.

			// 1. Cari Dpp
			dpp = nilaiKontrak.divide(BigDecimal.valueOf(1.12), 2, RoundingMode.HALF_UP);

			// 2. Cari PPN
			totalTax = dpp.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

			// 3. Total
			totalAmount = dpp.add(totalTax);
			mdl.setContractValue(nilaiKontrak);
			mdl.setDpp(dpp);
			mdl.setVatRate(vatRate);
			mdl.setTotalTax(totalTax); //
			mdl.setTotalAmount(totalAmount);
		}

		// Nilai di atas TIDAK kena PPN
		if (request.getTaxType() == TaxType.NON_PPN) {
			mdl.setContractValue(nilaiKontrak);
			mdl.setDpp(BigDecimal.ZERO);
			mdl.setVatRate(BigDecimal.ZERO); // 12
			mdl.setTotalTax(BigDecimal.ZERO); //
			mdl.setTotalAmount(nilaiKontrak);
		}
		mdl.setTaxType(request.getTaxType());
		mdl.setUserId(userId);
		mdl.setCompanyId(companyId);
		return mdl;
	}
}
