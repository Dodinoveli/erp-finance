package com.logikaintermedia.erp.modules.client;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import com.fasterxml.uuid.Generators;
import lombok.Data;

@Data
public class Client {
    // Identity
    private UUID clientId;
    private String clientCode;

    // Client information
    private String clientName;
    private String clientType;

    // Tax information
    private String clientNpwp;
    private String clientNik;
    private String clientNitku;
    private Boolean clientIsPkp;

    // Address
    private String clientAddress;
    private String clientCity;
    private String clientProvince;
    private String clientPostalCode;
    private String clientCountry;

    // Contact
    private String clientEmail;
    private String clientContactPerson;
    private String clientContactPhone;

    // Status
    private Boolean clientIsActive;

    // Company
    private UUID companyId;

    // Audit
    private OffsetDateTime clientCreatedAt;
    private UUID userId;
    private OffsetDateTime clientUpdatedAt;
    private OffsetDateTime clientDeletedAt;

    // Bank
    private String clientBankName;
    private String clientAccountNumber;
    private String clientAccountName;

    /**
     * Membuat Client baru dari ClientRequest.
     * 
     * @param request   data client
     * @param code      kode client
     * @param companyId ID perusahaan
     * @param userId    ID user pembuat data
     * @return Client baru
     */
    public static Client from(ClientRequest request, String code, UUID companyId, UUID userId) {
        Client model = new Client();
        model.setClientId(Generators.timeBasedEpochGenerator().generate());
        model.setClientCode(code);
        model.setClientName(request.getClientName());
        model.setClientType(request.getClientType());
        model.setClientNpwp(request.getClientNpwp());
        model.setClientNik(request.getClientNik());
        model.setClientAddress(request.getClientAddress());
        model.setClientCity(request.getClientCity());
        model.setClientProvince(request.getClientProvince());
        model.setClientPostalCode(request.getClientPostalCode());
        model.setClientCountry(request.getClientCountry());
        model.setClientEmail(request.getClientEmail());
        model.setClientContactPerson(request.getClientContactPerson());
        model.setClientContactPhone(request.getClientContactPhone());
        model.setClientIsActive(true);
        model.setCompanyId(companyId);
        model.setClientBankName(request.getClientBankName());
        model.setClientAccountNumber(request.getClientAccountNumber());
        model.setClientAccountName(request.getClientAccountName());

        // 🔥 AUDIT SYSTEM (WAJIB di backend, bukan dari DTO)
        model.setClientCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        model.setUserId(userId);

        // optional
        model.setClientUpdatedAt(null);
        model.setClientDeletedAt(null);
        model.setClientNitku(request.getClientNitku());
        model.setClientIsPkp(request.getClientIsPkp());
        return model;
    }

    /**
     * Membuat perubahan data Client berdasarkan ClientRequest.
     *
     * @param dto       data client yang diperbarui
     * @param clientId  ID client yang diperbarui
     * @param companyId ID perusahaan pemilik data
     * @return Client dengan data yang telah diperbarui
     */
    public static Client updateFrom(ClientRequest dto, UUID clientId, UUID companyId) {
        Client model = new Client();
        model.setClientCode(dto.getClientCode());
        model.setClientName(dto.getClientName());
        model.setClientType(dto.getClientType());
        model.setClientNpwp(dto.getClientNpwp());
        model.setClientNik(dto.getClientNik());
        // System.out.println("ISI DTO NIK: '" + dto.getClientNik() + "'");
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
        return model;
    }

}
