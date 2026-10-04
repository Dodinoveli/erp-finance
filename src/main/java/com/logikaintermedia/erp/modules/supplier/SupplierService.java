package com.logikaintermedia.erp.modules.supplier;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        List<SupplierResponse> list = repository.findSupplierByCompanyId(companyId, start, length, keyword).stream()
                .map(SupplierResponse::from).toList();
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
        Supplier model = Supplier.from(dto, companyId, userId, code);
        int data = repository.save(model);
        if (data <= 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

    @Transactional
    public Supplier updateSupplier(SupplierRequest dto, UUID supplierId, UUID companyId) {
        Supplier model = Supplier.updateFrom(dto, supplierId, companyId);
        int data = repository.update(model);
        if (data <= 0 && supplierId.toString().isBlank()) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

}
