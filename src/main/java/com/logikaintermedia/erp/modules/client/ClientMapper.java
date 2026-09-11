package com.logikaintermedia.erp.modules.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import com.logikaintermedia.erp.encryption.EncryptionUtil;

public class ClientMapper implements RowMapper<Client> {

        @Override
        public Client mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                Client c = new Client();
                c.setClientId(rs.getObject("client_id", java.util.UUID.class));
                c.setClientCode(rs.getString("client_code"));
                c.setClientName(rs.getString("client_name"));
                c.setClientType(rs.getString("client_type"));
                c.setClientNpwp(EncryptionUtil.decryptSafely(rs.getString("client_npwp")));
                c.setClientNik(EncryptionUtil.decryptSafely(rs.getString("client_nik")));
                c.setClientAddress(rs.getString("client_address"));
                c.setClientCity(rs.getString("client_city"));
                c.setClientProvince(rs.getString("client_province"));
                c.setClientPostalCode(rs.getString("client_postal_code"));
                c.setClientCountry(rs.getString("client_country"));
                c.setClientEmail(EncryptionUtil.decryptSafely(rs.getString("client_email")));
                c.setClientContactPerson(rs.getString("client_contact_person"));
                c.setClientContactPhone(EncryptionUtil.decryptSafely(rs.getString("client_contact_phone")));
                c.setClientIsActive(rs.getBoolean("client_is_active"));
                c.setCompanyId(rs.getObject("company_id", java.util.UUID.class));
                c.setClientCreatedAt(rs.getObject("client_created_at", OffsetDateTime.class));
                c.setClientUpdatedAt(rs.getObject("client_updated_at", OffsetDateTime.class));
                c.setClientDeletedAt(rs.getObject("client_deleted_at", OffsetDateTime.class));
                c.setClientBankName(rs.getString("client_bank_name"));
                c.setClientAccountNumber(EncryptionUtil.decryptSafely(rs.getString("client_account_number")));
                c.setClientAccountName(rs.getString("client_account_name"));
                c.setClientNitku(EncryptionUtil.decryptSafely(rs.getString("client_nitku")));
                c.setClientIsPkp(rs.getBoolean("client_is_pkp"));
                return c;
        }

}
