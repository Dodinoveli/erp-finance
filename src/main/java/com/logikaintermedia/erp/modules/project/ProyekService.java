package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.modules.client.Client;
import com.logikaintermedia.erp.modules.client.ClientResponse;
import com.logikaintermedia.erp.utility.CursorResponse;

@Service
public class ProyekService {
    private ProyekRepository repository;

    public ProyekService(ProyekRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Proyek detailById(UUID id) {
        if (id.toString().isBlank()) {
            throw new IllegalArgumentException("id proyek kosong");
        }
        return repository.detailById(id);
    }

    @Transactional
    public Proyek saveProyek(ProyekRequest request, UUID companyId, UUID userId) {
        Proyek mdl = new Proyek();
        mdl.setProjectId(Generators.timeBasedEpochGenerator().generate());
        mdl.setProjectCode(request.getProjectCode());
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

        String isTaxt = request.getTaxType();
        BigDecimal nilaiKontrak = request.getContractValue();

        BigDecimal dpp = BigDecimal.ZERO;
        BigDecimal vatRate = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Nilai di atas BELUM termasuk PPN (Exclusive)
        if (isTaxt.equalsIgnoreCase("EXCLUSIVE")) {
            vatRate = BigDecimal.valueOf(11);
            totalTax = nilaiKontrak.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalAmount = nilaiKontrak.add(totalTax);
            // mdl.setContractValue(dpp);
            mdl.setDpp(request.getContractValue());
            mdl.setVatRate(vatRate); // 12
            mdl.setTotalTax(totalTax); //
            mdl.setTotalAmount(totalAmount);
        }

        // Nilai di atas SUDAH termasuk PPN (Inclusive)
        if (isTaxt.equalsIgnoreCase("INCLUSIVE")) {
            vatRate = BigDecimal.valueOf(11);
            // Angka 1 itu berasal dari nilai DPP itu sendiri, yaitu 100%.

            // 1. Cari Dpp
            dpp = nilaiKontrak.divide(BigDecimal.valueOf(1.11), 2, RoundingMode.HALF_UP);

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
        if (isTaxt.equalsIgnoreCase("NON_PPN")) {
            mdl.setContractValue(nilaiKontrak);
            mdl.setDpp(BigDecimal.ZERO);
            mdl.setVatRate(BigDecimal.ZERO); // 12
            mdl.setTotalTax(BigDecimal.ZERO); //
            mdl.setTotalAmount(nilaiKontrak);
        }

        mdl.setTaxType(isTaxt);
        int data = repository.save(mdl);

        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return mdl;
    }

    @Transactional
    public Proyek updateProyek(ProyekRequest request, UUID projectId, UUID companyId, UUID userId) {
        Proyek mdl = new Proyek();
        mdl.setProjectId(projectId);
        mdl.setProjectCode(request.getProjectCode());
        mdl.setName(request.getName());
        mdl.setDescription(request.getDescription());
        mdl.setClientId(request.getClientId());
        mdl.setProjectType(request.getProjectType());
        mdl.setLocation(request.getLocation());
        mdl.setContractValue(request.getContractValue());
        mdl.setUpdatedAt(request.getUpdatedAt());
        mdl.setPoDate(request.getPoDate());
        String isTaxt = request.getTaxType();
        BigDecimal nilaiKontrak = request.getContractValue();

        BigDecimal dpp = BigDecimal.ZERO;
        BigDecimal vatRate = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Nilai di atas BELUM termasuk PPN (Exclusive)
        if (isTaxt.equalsIgnoreCase("EXCLUSIVE")) {
            vatRate = BigDecimal.valueOf(11);
            totalTax = nilaiKontrak.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalAmount = nilaiKontrak.add(totalTax);

            mdl.setDpp(nilaiKontrak);
            mdl.setVatRate(vatRate); // 12
            mdl.setTotalTax(totalTax); //
            mdl.setTotalAmount(totalAmount);
        }

        // Nilai di atas SUDAH termasuk PPN (Inclusive)
        if (isTaxt.equalsIgnoreCase("INCLUSIVE")) {
            vatRate = BigDecimal.valueOf(11);
            // Angka 1 itu berasal dari nilai DPP itu sendiri, yaitu 100%.

            // 1. Cari Dpp
            dpp = nilaiKontrak.divide(BigDecimal.valueOf(1.11), 2, RoundingMode.HALF_UP);

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
        if (isTaxt.equalsIgnoreCase("NON_PPN")) {
            mdl.setContractValue(nilaiKontrak);
            mdl.setDpp(BigDecimal.ZERO);
            mdl.setVatRate(BigDecimal.ZERO); // 12
            mdl.setTotalTax(BigDecimal.ZERO); //
            mdl.setTotalAmount(nilaiKontrak);
        }

        mdl.setTaxType(isTaxt);
        mdl.setUserId(userId);
        mdl.setCompanyId(companyId);

        repository.update(mdl);

        return mdl;
    }

    @Transactional(readOnly = true)
    public CursorResponse<ProyekResponse> getProjectById(UUID companyId, String keyword,
            LocalDateTime lastCreatedAt,
            UUID lastId,
            int limit) {

        List<Proyek> entities = repository.findById(companyId, keyword, lastCreatedAt, lastId, limit + 1);
        Boolean hasNext = entities.size() > limit;

        List<Proyek> currentData = hasNext ? entities.subList(0, limit) : entities;

        List<ProyekResponse> list = currentData.stream().map(entity -> {
            ProyekResponse mdl = new ProyekResponse();
            mdl.setProjectId(entity.getProjectId());
            mdl.setProjectCode(entity.getProjectCode());
            mdl.setPoDate(entity.getPoDate());
            mdl.setName(entity.getName());
            mdl.setProjectType(entity.getProjectType());
            mdl.setContractValue(entity.getContractValue());
            mdl.setClientName(entity.getClientName());
            mdl.setTaxType(entity.getTaxType());
            mdl.setTotalTax(entity.getTotalTax());
            mdl.setDpp(entity.getDpp());
            mdl.setVatRate(entity.getVatRate());
            mdl.setTotalAmount(entity.getTotalAmount());
            mdl.setCreatedAt(entity.getCreatedAt());
            return mdl;
        }).toList();

        // Ambil "Kunci" dari data terakhir (Data ke-10) untuk ambil data 11-20 nanti
        LocalDateTime nextCreatedAt = null;

        UUID nextId = null;

        if (!list.isEmpty()) {
            ProyekResponse last = list.get(list.size() - 1);
            nextCreatedAt = last.getCreatedAt();
            nextId = last.getProjectId();
        }
        return new CursorResponse<>(list, nextCreatedAt, nextId, hasNext);
    }

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
