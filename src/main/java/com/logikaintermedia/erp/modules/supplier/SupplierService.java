package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.encryption.EncryptionUtil;
import com.logikaintermedia.erp.utility.CursorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class SupplierService {
    private SupplierRepository repository;

    public String generateCode(UUID companyId) {
        return repository.generateCode(companyId);
    }

    public Supplier detailById(UUID detailId) {
        Supplier supplier = repository.detailById(detailId);
        if (supplier != null) {
            supplier.setSupplierNpwp(EncryptionUtil.decryptSafely(supplier.getSupplierNpwp()));
            supplier.setSupplierEmail(EncryptionUtil.decryptSafely(supplier.getSupplierEmail()));
            supplier.setSupplierContactPhone(EncryptionUtil.decryptSafely(supplier.getSupplierContactPhone()));
            supplier.setSupplierBankAccountNumber(
                    EncryptionUtil.decryptSafely(supplier.getSupplierBankAccountNumber()));
        }
        return supplier;

    }

    @Transactional(readOnly = true)
    public CursorResponse<SupplierResponse> getSupplierById(UUID companyId, String keyword, LocalDateTime lastCreatedAt,
            UUID lastId,
            int limit) {

        List<Supplier> entities = repository.findById(companyId, keyword, lastCreatedAt, lastId, limit + 1);
        Boolean hasNext = entities.size() > limit;

        List<Supplier> currentData = hasNext ? entities.subList(0, limit) : entities;

        List<SupplierResponse> list = currentData.stream().map(entity -> {
            SupplierResponse dto = new SupplierResponse();
            dto.setSupplierId(entity.getSupplierId());
            dto.setSupplierCode(entity.getSupplierCode());
            dto.setSupplierName(entity.getSupplierName());
            dto.setSupplierEmail(EncryptionUtil.decryptSafely(entity.getSupplierEmail()));
            dto.setSupplierContactPhone(EncryptionUtil.decryptSafely(entity.getSupplierContactPhone()));
            dto.setSupplierIsActive(entity.getSupplierIsActive());
            dto.setSupplierCreatedAt(entity.getSupplierCreatedAt());
            return dto;
        }).toList();

        // 5. Ambil "Kunci" dari data terakhir (Data ke-10) untuk ambil data 11-20 nanti
        LocalDateTime nextCreatedAt = null;

        UUID nextId = null;

        if (!list.isEmpty()) {
            SupplierResponse last = list.get(list.size() - 1);
            nextCreatedAt = last.getSupplierCreatedAt();
            nextId = last.getSupplierId();
        }
        return new CursorResponse<>(list, nextCreatedAt, nextId, hasNext);
    }

    @Transactional
    public Supplier createSupplier(SupplierRequest dto, UUID companyId, UUID userId) {
        Supplier model = new Supplier();
        model.setSupplierId(Generators.timeBasedEpochRandomGenerator().generate());
        String code = repository.generateSupplierCode(model.getCompanyId());
        model.setSupplierCode(code);
        model.setSupplierName(dto.getSupplierName());
        model.setSupplierType(dto.getSupplierType());
        model.setSupplierNpwp(dto.getSupplierNpwp());
        model.setSupplierAddress(dto.getSupplierAddress());
        model.setSupplierCity(dto.getSupplierCity());
        model.setSupplierProvince(dto.getSupplierProvince());
        model.setSupplierPostalCode(dto.getSupplierPostalCode());
        model.setSupplierCountry(dto.getSupplierCountry());
        model.setSupplierEmail(dto.getSupplierEmail());
        model.setSupplierContactPerson(dto.getSupplierContactPerson());
        model.setSupplierContactPhone(dto.getSupplierContactPhone());
        model.setSupplierPaymentTermDays(0);
        model.setSupplierCreditLimit(BigDecimal.ZERO);
        model.setSupplierBankName(dto.getSupplierBankName());
        model.setSupplierBankAccountNumber(dto.getSupplierBankAccountNumber());
        model.setSupplierBankAccountName(dto.getSupplierBankAccountName());
        model.setSupplierPkp(dto.getSupplierPkp());
        model.setSupplierIsActive(true);
        ;
        model.setCompanyId(companyId);
        model.setSupplierCreatedAt(LocalDateTime.now());
        model.setSupplierUpdatedAt(null);
        model.setSupplierDeletedAt(null);
        model.setUserId(userId);

        int data = repository.save(model);

        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }

        return model;
    }

    @Transactional
    public Supplier updateSupplier(SupplierRequest dto, UUID supplierId, UUID companyId) {
        Supplier model = new Supplier();
        model.setSupplierCode(dto.getSupplierCode());
        model.setSupplierName(dto.getSupplierName());
        model.setSupplierType(dto.getSupplierType());
        model.setSupplierNpwp(dto.getSupplierNpwp());
        model.setSupplierAddress(dto.getSupplierAddress());
        model.setSupplierCity(dto.getSupplierCity());
        model.setSupplierProvince(dto.getSupplierProvince());
        model.setSupplierPostalCode(dto.getSupplierPostalCode());
        model.setSupplierCountry(dto.getSupplierCountry());
        model.setSupplierEmail(dto.getSupplierEmail());
        model.setSupplierContactPerson(dto.getSupplierContactPerson());
        model.setSupplierContactPhone(dto.getSupplierContactPhone());
        model.setSupplierPaymentTermDays(0);
        model.setSupplierCreditLimit(BigDecimal.ZERO);
        model.setSupplierBankName(dto.getSupplierBankName());
        model.setSupplierBankAccountNumber(dto.getSupplierBankAccountNumber());
        model.setSupplierBankAccountName(dto.getSupplierBankAccountName());
        model.setSupplierPkp(dto.getSupplierPkp());
        model.setSupplierIsActive(dto.getSupplierIsActive() != null ? dto.getSupplierIsActive() : true);
        model.setSupplierUpdatedAt(LocalDateTime.now());
        model.setSupplierId(supplierId);
        model.setCompanyId(companyId);
        int data = repository.update(model);
        if (data <= 0 && supplierId.toString().isBlank()) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

}
