package com.logikaintermedia.erp.modules.coamapping;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;

@Service
public class CoaMappingLineService {
	private final CoaMappingLineRepository repository;

	public CoaMappingLineService(CoaMappingLineRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<CoaMappingResponse> findCoaMappingByCompanyId(UUID companyId) {
		return repository.findCoaMappingByCompanyId(companyId).stream().map(CoaMappingResponse::comboFrom).toList();
	}

	@Transactional(readOnly = true)
	public List<ChartOfAccountsResponse> findCoaByCompanyId(UUID companyId) {
		return repository.findCoaByCompanyId(companyId).stream().map(ChartOfAccountsResponse::comboFrom).toList();
	}

	@Transactional(readOnly = true)
	public DataTableResponse<CoaMappingLineResponse> findCoaMappingLineByCompanyId(UUID companyId, int draw, int start,
			int length, String keyword) {
		// Validasi pagination
		if (start < 0) {
			start = 0;
		}
		if (length <= 0) {
			length = 10;
		}
		// Batasi maksimal data per request
		if (length > 100) {
			length = 100;
		}
		// Bersihkan keyword
		if (keyword != null) {
			keyword = keyword.trim();
			if (keyword.isEmpty()) {
				keyword = null;
			}
		}

		List<CoaMappingLineResponse> list = repository.findCoaMappingLineByCompanyId(companyId, start, length, keyword)
				.stream().map(CoaMappingLineResponse::from).toList();
		// total semua coamapping yang berelasi
		long recordsTotal = repository.countAll(companyId);
		// total semua coamapping yang berelasi bedasarkan pencarian
		long recordsFiltered = repository.countFiltered(companyId, keyword);
		return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
	}

	@Transactional
	public int[] save(List<CoaMappingLineRequest> lines, UUID companyId, UUID mappingId) {

		if (lines == null || lines.isEmpty()) {
			throw new IllegalArgumentException("Detail COA Mapping tidak boleh kosong.");
		}

		// Lock mapping terlebih dahulu
		System.out.print("MAPPING ID"+mappingId);
		repository.lockMapping(companyId, mappingId);

		Set<UUID> coaIds = new HashSet<>();
		for (CoaMappingLineRequest request : lines) {

			UUID coaId = request.getCoaId();
			
			if (coaId == null) {
			    throw new IllegalArgumentException("Akun COA wajib dipilih.");
			}
			
			// Cek duplikat dalam request
			if (!coaIds.add(coaId)) {
				throw new IllegalArgumentException("Akun COA tidak boleh dipilih lebih dari sekali.");
			}

			// Cek duplikat di database
			if (repository.existsMappingLine(companyId, mappingId, coaId)) {
				throw new IllegalArgumentException("Akun COA sudah terdaftar pada mapping ini.");
			}
		}
		
		// Baca urutan setelah lock diperoleh
		int lastOrder = repository.findMaxLineOrder(mappingId);
		AtomicInteger order = new AtomicInteger(lastOrder + 1);

		List<CoaMappingLine> list = lines.stream().map(request -> {
			CoaMappingLine line = new CoaMappingLine();
			line.setMappingLineId(Generators.timeBasedEpochGenerator().generate());
			line.setMappingId(mappingId);
			line.setCoaId(request.getCoaId());
			line.setPosition(request.getPosition());
			line.setLineOrder(order.getAndIncrement());
			line.setAmountType(request.getAmountType());
			line.setIsActive(request.getIsActive());
			line.setCreatedAt(request.getCreatedAt());
			line.setUpdatedAt(request.getUpdatedAt());
			return line;
		}).toList();

		return repository.save(list);
	}
}
