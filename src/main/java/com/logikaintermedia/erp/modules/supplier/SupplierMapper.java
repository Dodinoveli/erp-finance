package com.logikaintermedia.erp.modules.supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.logikaintermedia.erp.encryption.EncryptionUtil;

public class SupplierMapper implements RowMapper<Supplier> {

    public Supplier mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Supplier mdl = new Supplier();

        mdl.setSupplierId(rs.getObject("supplier_id", java.util.UUID.class));
        mdl.setSupplierCode(rs.getString("supplier_code"));
        mdl.setSupplierName(rs.getString("supplier_name"));
        mdl.setSupplierType(rs.getString("supplier_type"));
        mdl.setSupplierNpwp(EncryptionUtil.decrypt(rs.getString("supplier_npwp")));
        mdl.setSupplierAddress(rs.getString("supplier_address"));
        mdl.setSupplierCity(rs.getString("supplier_city"));
        mdl.setSupplierProvince(rs.getString("supplier_province"));
        mdl.setSupplierPostalCode(rs.getString("supplier_postal_code"));
        mdl.setSupplierCountry(rs.getString("supplier_country"));
        mdl.setSupplierEmail(EncryptionUtil.decrypt(rs.getString("supplier_email")));
        mdl.setSupplierContactPerson(rs.getString("supplier_contact_person"));
        mdl.setSupplierContactPhone(EncryptionUtil.decrypt(rs.getString("supplier_contact_phone")));
        mdl.setSupplierPaymentTermDays(rs.getInt("supplier_payment_term_days"));
        mdl.setSupplierCreditLimit(rs.getBigDecimal("supplier_credit_limit"));
        mdl.setSupplierBankName(rs.getString("supplier_bank_name"));
        mdl.setSupplierBankAccountNumber(EncryptionUtil.decrypt(rs.getString("supplier_bank_account_number")));
        mdl.setSupplierBankAccountName(rs.getString("supplier_bank_account_name"));
        mdl.setSupplierPkp(rs.getObject("supplier_pkp", Boolean.class));
        mdl.setSupplierIsActive(rs.getObject("supplier_is_active", Boolean.class));
        mdl.setCompanyId(rs.getObject("company_id", java.util.UUID.class));
        mdl.setSupplierCreatedAt(rs.getObject("supplier_created_at", LocalDateTime.class));
        mdl.setSupplierUpdatedAt(rs.getObject("supplier_updated_at", LocalDateTime.class));
        mdl.setUserId(rs.getObject("user_id", java.util.UUID.class));
        mdl.setSupplierDeletedAt(rs.getObject("supplier_deleted_at", LocalDateTime.class));
        return mdl;
    }

}
