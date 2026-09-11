package com.logikaintermedia.erp.modules.project;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

public class ProyekMapper implements RowMapper<Proyek> {

    @Override
    public Proyek mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Proyek mdl = new Proyek();
        mdl.setProjectId(rs.getObject("project_id", UUID.class));
        mdl.setProjectCode(rs.getString("project_code"));
        mdl.setProjectPo(rs.getString("project_po"));
        mdl.setName(rs.getString("name"));
        mdl.setDescription(rs.getString("description"));
        mdl.setClientId(rs.getObject("client_id", UUID.class));
        mdl.setClientName(rs.getString("client_name"));
        mdl.setProjectType(rs.getString("project_type"));
        mdl.setContractValue(rs.getBigDecimal("contract_value"));
        mdl.setLocation(rs.getString("location"));
        mdl.setCompanyId(rs.getObject("company_id", UUID.class));
        mdl.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        mdl.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
        mdl.setUserId(rs.getObject("user_id", UUID.class));
        mdl.setVatRate(rs.getBigDecimal("vat_rate"));
        Date poDate = rs.getDate("po_date");
        if (poDate != null) {
            mdl.setPoDate(poDate.toLocalDate());
        }

        mdl.setTaxType(rs.getString("tax_type"));
        mdl.setTotalTax(rs.getBigDecimal("total_tax"));
        mdl.setTotalAmount(rs.getBigDecimal("total_amount"));
        mdl.setDpp(
                rs.getBigDecimal("dpp"));
        return mdl;
    }

}
