package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logikaintermedia.erp.utility.DataTableResponse;

@Service
public class CoaMappingLineService {
	private final CoaMappingLineRepository repository;

	public CoaMappingLineService(CoaMappingLineRepository repository) {
		this.repository = repository;
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
		//total semua coamapping yang berelasi
		long recordsTotal = repository.countAll(companyId);
		//total semua coamapping yang berelasi bedasarkan pencarian
		long recordsFiltered = repository.countFiltered(companyId, keyword);
		return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
	}
}
