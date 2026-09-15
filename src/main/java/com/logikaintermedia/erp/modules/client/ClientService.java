package com.logikaintermedia.erp.modules.client;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.utility.CursorResponse;
import com.logikaintermedia.erp.utility.DataCountResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;
import lombok.extern.slf4j.Slf4j;
import com.logikaintermedia.erp.encryption.EncryptionUtil;

@Slf4j
@Service
public class ClientService {
    private final ClientRepository repository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client detailById(UUID clientId) {
        return repository.detailById(clientId);
    }

    public CursorResponse<ClientResponse> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt,
            UUID lastId,
            int limit) {

        List<Client> entities = repository.findById(companyId, keyword, lastCreatedAt, lastId, limit + 1);
        Boolean hasNext = entities.size() > limit;

        List<Client> currentData = hasNext ? entities.subList(0, limit) : entities;

        List<ClientResponse> list = currentData.stream().map(entity -> {
            ClientResponse dto = new ClientResponse();
            dto.setClientId(entity.getClientId());
            dto.setClientCode(entity.getClientCode());
            dto.setClientName(entity.getClientName());
            dto.setClientType(entity.getClientType());
            dto.setClientNpwp(entity.getClientNpwp());
            dto.setClientNik(entity.getClientNik());
            dto.setClientAddress(entity.getClientAddress());
            dto.setClientCity(entity.getClientCity());
            dto.setClientProvince(entity.getClientProvince());
            dto.setClientPostalCode(entity.getClientPostalCode());
            dto.setClientCountry(entity.getClientCountry());
            dto.setClientEmail(entity.getClientEmail());
            dto.setClientContactPerson(entity.getClientContactPerson());
            dto.setClientContactPhone(entity.getClientContactPhone());
            dto.setClientIsActive(entity.getClientIsActive());
            dto.setCompanyId(entity.getCompanyId());
            // Field Bank & Perpajakan
            dto.setClientBankName(entity.getClientBankName());
            dto.setClientAccountNumber(entity.getClientAccountNumber());
            dto.setClientAccountName(entity.getClientAccountName());
            dto.setClientIsPkp(entity.getClientIsPkp());
            dto.setClientNitku(entity.getClientNitku());
            // Audit info
            dto.setClientUpdatedAt(entity.getClientUpdatedAt());
            dto.setClientCreatedAt(entity.getClientCreatedAt());

            return dto;
        }).toList();

        // 5. Ambil "Kunci" dari data terakhir (Data ke-10) untuk ambil data 11-20 nanti
        OffsetDateTime nextCreatedAt = null;

        UUID nextId = null;

        if (!list.isEmpty()) {
            ClientResponse last = list.get(list.size() - 1);
            nextCreatedAt = last.getClientCreatedAt();
            nextId = last.getClientId();
        }
        return new CursorResponse<>(list, nextCreatedAt, nextId, hasNext);
    }

    @Transactional
    public Client createClient(ClientRequest dto, UUID companyId, UUID userId) {

        Client model = new Client();
        model.setClientId(Generators.timeBasedEpochGenerator().generate());
        // 🔥 DATA UTAMA
        // model.setClientCode(dto.getClientCode());
        model.setClientName(dto.getClientName());
        model.setClientType(dto.getClientType());
        model.setClientNpwp(dto.getClientNpwp());
        model.setClientNik(dto.getClientNik());
        System.out.println("ISI DTO NIK: '" + dto.getClientNik() + "'");
        model.setClientAddress(dto.getClientAddress());
        model.setClientCity(dto.getClientCity());
        model.setClientProvince(dto.getClientProvince());
        model.setClientPostalCode(dto.getClientPostalCode());
        model.setClientCountry(dto.getClientCountry());
        model.setClientEmail(dto.getClientEmail());
        model.setClientContactPerson(dto.getClientContactPerson());
        model.setClientContactPhone(dto.getClientContactPhone());
        model.setClientIsActive(true);
        model.setCompanyId(companyId);
        model.setClientBankName(dto.getClientBankName());
        model.setClientAccountNumber(dto.getClientAccountNumber());
        model.setClientAccountName(dto.getClientAccountName());

        // 🔥 AUDIT SYSTEM (WAJIB di backend, bukan dari DTO)
        model.setClientCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setUserId(userId);

        // optional
        model.setClientUpdatedAt(null);
        model.setClientDeletedAt(null);
        model.setClientNitku(dto.getClientNitku());
        model.setClientIsPkp(dto.getClientIsPkp());
        long count = repository.IntSequenceGenerator(companyId);
        String code = "CLN-" + count;
        model.setClientCode(code);
        int data = repository.save(model);

        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

    @Transactional
    public Client updateClient(ClientRequest dto, UUID clientId, UUID companyId) {
        Client model = new Client();
        model.setClientCode(dto.getClientCode());
        model.setClientName(dto.getClientName());
        model.setClientType(dto.getClientType());
        model.setClientNpwp(dto.getClientNpwp());
        model.setClientNik(dto.getClientNik());
        System.out.println("ISI DTO NIK: '" + dto.getClientNik() + "'");
        model.setClientAddress(dto.getClientAddress());
        model.setClientCity(dto.getClientCity());
        model.setClientProvince(dto.getClientProvince());
        model.setClientPostalCode(dto.getClientPostalCode());
        model.setClientCountry(dto.getClientCountry());
        model.setClientEmail(dto.getClientEmail());
        model.setClientContactPerson(dto.getClientContactPerson());
        model.setClientContactPhone(dto.getClientContactPhone());
        model.setClientIsActive(dto.getClientIsActive() != null ? dto.getClientIsActive() : true);
        model.setClientBankName(dto.getClientBankName());
        model.setClientAccountNumber(dto.getClientAccountNumber());
        model.setClientAccountName(dto.getClientAccountName());
        model.setClientUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setClientNitku(dto.getClientNitku());
        model.setClientIsPkp(dto.getClientIsPkp());

        model.setCompanyId(companyId);
        model.setClientId(clientId);
        int data = repository.update(model);
        // System.err.println("REALLY UPDATED ROWS: " + data);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

    @Transactional(readOnly = true)
    public DataTableResponse<ClientResponse> getClients(UUID companyId, int draw, int start, int length,
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
        List<Client> clients = repository.findClientByCompanyId(companyId, start, length, keyword);
        List<ClientResponse> list = clients.stream().map(entity -> {
            ClientResponse dto = new ClientResponse();
            dto.setClientId(entity.getClientId());
            dto.setClientName(entity.getClientName());
            dto.setClientCode(entity.getClientCode());
            dto.setClientContactPerson(entity.getClientContactPerson());
            dto.setClientEmail(EncryptionUtil.decryptSafely(entity.getClientEmail()));
            dto.setClientContactPhone(EncryptionUtil.decryptSafely(entity.getClientContactPhone()));
            dto.setClientIsActive(entity.getClientIsActive());
            return dto;
        }).toList();
        // Total semua client
        long recordsTotal = repository.countAll(companyId);
        // Total client setelah filter/search
        long recordsFiltered = repository.countFiltered(companyId, keyword);
        return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
    }

    @Transactional(readOnly = true)
    public DataCountResponse<Client> getTotal(UUID companyId) {
        int total = repository.countTotalByCompanyId(companyId);
        int totalActive = repository.countTotalActiveByCompanyId(companyId);
        int totalInactive = repository.countTotalInactiveByCompanyId(companyId);
        int totalNew = repository.countTotalNewByCompanyId(companyId);
        return new DataCountResponse<>(total, totalActive, totalInactive, totalNew);
    }
}
