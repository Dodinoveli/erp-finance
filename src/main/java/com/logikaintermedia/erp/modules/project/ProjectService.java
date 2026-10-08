package com.logikaintermedia.erp.modules.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.logikaintermedia.erp.modules.client.Client;
import com.logikaintermedia.erp.modules.client.ClientResponse;
import com.logikaintermedia.erp.modules.company.CompanyRepository;
import com.logikaintermedia.erp.utility.DataTableResponse;

@Service
public class ProjectService {
	private final ProjectRepository repository;
	private final CompanyRepository cRepository;

	public ProjectService(ProjectRepository repository, CompanyRepository cRepository) {
		this.repository = repository;
		this.cRepository = cRepository;
	}

	@Transactional(readOnly = true)
	public DataTableResponse<ProjectResponse> getProyek(UUID companyId, int draw, int start, int length,
			String keyword) {

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

		List<ProjectResponse> list = repository.findProyekByCompanyId(companyId, start, length, keyword).stream()
				.map(ProjectResponse::from).toList();

		// Total semua client
		long recordsTotal = repository.countAll(companyId);
		// Total client setelah filter/search
		long recordsFiltered = repository.countFiltered(companyId, keyword);
		return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
	}

	@Transactional(readOnly = true)
	public Project detailById(UUID id) {
		if (id.toString().isBlank()) {
			throw new IllegalArgumentException("id proyek kosong");
		}
		return repository.detailById(id);
	}

	@Transactional
	public Project saveProyek(ProjectRequest request, UUID companyId, UUID userId) {
		long count = repository.nextProjectNumber(companyId);
		String pjCode = cRepository.companyCodeById(companyId);
		if (pjCode == null || pjCode.isBlank()) {
			throw new IllegalArgumentException(
					"Kode transaksi kosong, pergi ke menu setiing, edit dan isi kode transaksi");
		}
		String code = pjCode + "/PRJ/" + count;
		Project mdl = Project.from(request, companyId, userId, code);
		int data = repository.save(mdl);

		if (data <= 0) {
			throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
		}
		return mdl;
	}

	@Transactional
	public Project updateProyek(ProjectRequest request, UUID projectId, UUID companyId, UUID userId) {
		Project mdl = Project.updateFrom(request, projectId, companyId, userId);
		repository.update(mdl);
		return mdl;
	}

	// @Transactional(readOnly = true)
	// public CursorResponse<ProyekResponse> getProjectById(UUID companyId, String
	// keyword,
	// LocalDateTime lastCreatedAt,
	// UUID lastId,
	// int limit) {

	// List<Proyek> entities = repository.findById(companyId, keyword,
	// lastCreatedAt, lastId, limit + 1);
	// Boolean hasNext = entities.size() > limit;

	// List<Proyek> currentData = hasNext ? entities.subList(0, limit) : entities;

	// List<ProyekResponse> list = currentData.stream().map(entity -> {
	// ProyekResponse mdl = new ProyekResponse();
	// mdl.setProjectId(entity.getProjectId());
	// mdl.setProjectCode(entity.getProjectCode());
	// mdl.setPoDate(entity.getPoDate());
	// mdl.setName(entity.getName());
	// mdl.setProjectType(entity.getProjectType());
	// mdl.setContractValue(entity.getContractValue());
	// mdl.setClientName(entity.getClientName());
	// mdl.setTaxType(entity.getTaxType());
	// mdl.setTotalTax(entity.getTotalTax());
	// mdl.setDpp(entity.getDpp());
	// mdl.setVatRate(entity.getVatRate());
	// mdl.setTotalAmount(entity.getTotalAmount());
	// mdl.setCreatedAt(entity.getCreatedAt());
	// return mdl;
	// }).toList();

	// // Ambil "Kunci" dari data terakhir (Data ke-10) untuk ambil data 11-20 nanti
	// OffsetDateTime nextCreatedAt = null;

	// UUID nextId = null;

	// if (!list.isEmpty()) {
	// ProyekResponse last = list.get(list.size() - 1);
	// nextCreatedAt = last.getCreatedAt();
	// nextId = last.getProjectId();
	// }
	// return new CursorResponse<>(list, nextCreatedAt, nextId, hasNext);
	// }

	@Transactional(readOnly = true)
	public List<ClientResponse> findClientByCompanyId(UUID companyId) {
		List<Client> clientList = repository.findClientByCompanyId(companyId);
		if (clientList.isEmpty()) {
			return null;
		}
		List<ClientResponse> responseList = new ArrayList<>();
		for (Client list : clientList) {
			ClientResponse response = new ClientResponse();
			response.setClientId(list.getClientId());
			response.setClientName(list.getClientName());
			responseList.add(response);
		}
		return responseList;
	}
}
