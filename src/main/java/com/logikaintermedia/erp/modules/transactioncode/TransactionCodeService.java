package com.logikaintermedia.erp.modules.transactioncode;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logikaintermedia.erp.modules.company.Company;
import com.logikaintermedia.erp.modules.company.CompanyRequest;

@Service
public class TransactionCodeService {
    private final TransactionCodeRepository repository;

    public TransactionCodeService(TransactionCodeRepository repository) {
        this.repository = repository;
    }

    public  Optional<Map<String, Object>>  companyCodeById(UUID id) {
        return repository.companyById(id);
    }

    @Transactional 
    public  void updateCode(CompanyRequest dto, UUID companyId){
        System.out.println("SERVICE CODE: [" + dto.getCompanyCode() + "]");
        if(dto.getCompanyCode() == null || dto.getCompanyCode().isBlank()){
            throw new IllegalArgumentException("Kode tidak boleh kosong");
        }
        Company model = new Company();
        model.setCompanyCode(dto.getCompanyCode().trim());
        model.setCompanyId(companyId);
        int data = repository.updateCode(model);
        System.out.println("ROWS UPDATED: " + data);
        if (data ==0 ) {
            throw new IllegalArgumentException("Gagal mengubah data, silakan coba kembali");
        }
    }
}
