package com.logikaintermedia.erp.modules.supplier;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.utility.DataCountResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierService {
    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Supplier detailById(UUID detailId) {
        Supplier supplier = repository.detailById(detailId);
        if (supplier != null) {
            supplier.setSupplierNpwp(supplier.getSupplierNpwp());
            supplier.setSupplierEmail(supplier.getSupplierEmail());
            supplier.setSupplierContactPhone(supplier.getSupplierContactPhone());
            supplier.setSupplierBankAccountNumber(supplier.getSupplierBankAccountNumber());
        }
         
        return supplier;

    }

    @Transactional(readOnly = true)
    public DataTableResponse<SupplierResponse> getSupplierById(UUID companyId, int draw, int start, int length,
            String keyword) {
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

        List<Supplier> supplier = repository.findSupplierByCompanyId(companyId, start, length, keyword);
        List<SupplierResponse> list = supplier.stream().map(entity -> {
            SupplierResponse dto = new SupplierResponse();
            dto.setSupplierId(entity.getSupplierId());
            dto.setSupplierCode(entity.getSupplierCode());
            dto.setSupplierName(entity.getSupplierName());
            dto.setSupplierContactPerson(entity.getSupplierContactPerson());
            dto.setSupplierEmail(entity.getSupplierEmail());
            dto.setSupplierIsActive(entity.getSupplierIsActive());
            dto.setSupplierType(entity.getSupplierType());
            return dto;
        }).toList();
        // Total semua supplier
        long recordsTotal = repository.countAll(companyId);
        // Total supplier setelah filter/search
        long recordsFiltered = repository.countFiltered(companyId, keyword);
        return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
    }

    @Transactional(readOnly = true)
    public DataCountResponse<Supplier> getTotal(UUID companyId) {
        int total = repository.countTotalByCompanyId(companyId);
        int totalActive = repository.countTotalActiveByCompanyId(companyId);
        int totalInactive = repository.countTotalInactiveByCompanyId(companyId);
        int totalNew = repository.countTotalNewByCompanyId(companyId);
        return new DataCountResponse<>(total, totalActive, totalInactive, totalNew);
    }

    @Transactional
    public Supplier createSupplier(SupplierRequest dto, UUID companyId, UUID userId) {
        long count = repository.generateSupplierCode(companyId);
        String code = "SPL-" + count;
        Supplier model = new Supplier();
        model.setSupplierId(Generators.timeBasedEpochRandomGenerator().generate());
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
        model.setCompanyId(companyId);
        model.setSupplierCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
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
        model.setSupplierUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setSupplierId(supplierId);
        model.setCompanyId(companyId);
        int data = repository.update(model);
        if (data <= 0 && supplierId.toString().isBlank()) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

}
