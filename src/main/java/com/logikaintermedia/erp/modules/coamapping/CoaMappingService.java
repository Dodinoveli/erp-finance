package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.logikaintermedia.erp.utility.DataTableResponse;

@Service
public class CoaMappingService {
    private final CoaMappingRepository repository;

    public CoaMappingService(CoaMappingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public DataTableResponse<CoaMappingResponse> findCoaMappingById(UUID companyId, int draw, int start, int length,
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
        List<CoaMappingResponse> list = repository.findCoaMappingById(companyId, start, length, keyword).stream()
                .map(CoaMappingResponse::from)
                .toList();
        // Total semua client
        long recordsTotal = repository.countAll(companyId);
        // Total client setelah filter/search
        long recordsFiltered = repository.countFiltered(companyId, keyword);
        return new DataTableResponse<>(draw, recordsTotal, recordsFiltered, list);
    }

    @Transactional(readOnly = true)
    public List<CoaMappingResponse> detail(String keyword, UUID companyId) {
        return repository.detail(keyword, companyId).stream().map(CoaMappingResponse::from).toList();
    }

    @Transactional
    public CoaMapping save(CoaMappingRequest request, UUID companyId) {

        if (repository.findByTransactionTypeAndPaymentType(
                request.getTransactionType(),
                request.getPaymentType(),
                companyId).isPresent()) {
            throw new IllegalArgumentException("Mapping tersebut sudah ada");
        }
        CoaMapping model = CoaMapping.from(request, companyId);
        int data = repository.save(model);
        if (data < 0) {
            throw new IllegalArgumentException("Gagal menyimpan data, silakan coba kembali");
        }
        return model;
    }

}
