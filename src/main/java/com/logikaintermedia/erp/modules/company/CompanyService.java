package com.logikaintermedia.erp.modules.company;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.modules.user.User;
import com.logikaintermedia.erp.modules.user.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository repository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public int insert(CompanyRequest dto) {

        Company model = new Company();
        UUID companyId = Generators.timeBasedEpochRandomGenerator().generate();
        model.setCompanyId(companyId);
        model.setLegalName(dto.getLegalName());
        model.setCompanyCode(null);
        model.setAddress(dto.getAddress());
        model.setCity(dto.getCity());
        model.setProvince(dto.getProvince());
        model.setPostalCode(dto.getPostalCode());
        model.setCountry("Indonesia");
        model.setIsActive(true);
        model.setPhone(dto.getPhone());
        model.setEmail(dto.getEmail());
        model.setCompanyType(dto.getCompanyType());
        model.setBaseCurrency(dto.getBaseCurrency());
        model.setCreatedAt(OffsetDateTime.now());
        model.setUpdatedAt(OffsetDateTime.now());

        int result = repository.insert(model);

        if (result <= 0) {
            throw new RuntimeException("Gagal menyimpan company");
        }

        User user = new User();
        user.setUserId(Generators.timeBasedEpochRandomGenerator().generate());
        user.setUserName(dto.getUserName());
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);
        user.setFullName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setIsActive(true);
        user.setCompanyId(companyId);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        user.setRoles("Owner");
        int userResult = userRepository.insert(user);

        if (userResult <= 0) {
            throw new RuntimeException("Gagal menyimpan user");
        }

        UUID id = Generators.timeBasedEpochGenerator().generate();
        repository.insertCompanySequences(id, companyId);
        return result;
    }

}
