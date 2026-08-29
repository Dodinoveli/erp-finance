package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.modules.client.Client;

@Repository
public class ProyekRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProyekRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public @NonNull MapSqlParameterSource toParams(Proyek mdl) {
        return new MapSqlParameterSource()
                .addValue("projectId", mdl.getProjectId())
                .addValue("projectCode", mdl.getProjectCode())
                .addValue("name", mdl.getName())
                .addValue("description", mdl.getDescription())
                .addValue("clientId", mdl.getClientId())
                .addValue("projectType", mdl.getProjectType())
                .addValue("contractValue", mdl.getContractValue())
                .addValue("location", mdl.getLocation())
                .addValue("companyId", mdl.getCompanyId())
                .addValue("createdAt", mdl.getCreatedAt())
                .addValue("updatedAt", mdl.getUpdatedAt())
                .addValue("userId", mdl.getUserId())
                .addValue("taxType", mdl.getTaxType())
                .addValue("vatRate", mdl.getVatRate())
                .addValue("poDate", mdl.getPoDate())
                .addValue("totalTax", mdl.getTotalTax())
                .addValue("totalAmount", mdl.getTotalAmount())
                .addValue("dpp", mdl.getDpp());
    }

    public int save(Proyek proyek) {
        String sql = """
                INSERT INTO projects (
                	project_id, project_code, name, description, client_id,
                    project_type, contract_value, location, company_id,
                    created_at, updated_at, user_id, vat_rate, po_date,
                    tax_type, total_tax, total_amount, dpp
                    )
                	VALUES (
                        :projectId, :projectCode, :name, :description, :clientId,
                        :projectType, :contractValue, :location, :companyId,
                        :createdAt, :updatedAt, :userId, :vatRate, :poDate,
                        :taxType, :totalTax, :totalAmount, :dpp
                        )
                    """;
        try {
            return jdbcTemplate.update(sql, toParams(proyek));
        } catch (DuplicateKeyException e) {
            // ❌ Error: Duplicate project_code atau project_id
            // System.err.println("DUPLICATE KEY ERROR: " + e.getMessage());
            // System.err.println("Cek: project_code atau project_id sudah ada");
            e.printStackTrace();
            throw new RuntimeException("Project code atau ID sudah terdaftar!", e);

        } catch (DataIntegrityViolationException e) {
            // ❌ Error: Foreign key atau NOT NULL violation
            // System.err.println("DATA INTEGRITY ERROR: " + e.getMessage());
            // System.err.println("Cek: foreign key (company_id, client_id, user_id) atau
            // kolom NOT NULL");

            // Ambil pesan error dari database
            Throwable rootCause = e.getMostSpecificCause();
            // System.err.println("Database Error: " + rootCause.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Data tidak valid: " + rootCause.getMessage(), e);

        } catch (Exception e) {
            // ❌ Error lainnya
            // System.err.println("UNKNOWN ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal menyimpan project!", e);
        }
    }

    public int update(Proyek proyek) {
        String sql = """
                    UPDATE public.projects
                         SET
                            project_code = :projectCode,
                            name = :name,
                            description = :description,
                            client_id = :clientId,
                            project_type = :projectType,
                            contract_value = :contractValue,
                            location = :location,
                            updated_at = :updatedAt,
                            vat_rate = :vatRate,
                            po_date = :poDate,
                            tax_type = :taxType,
                            total_tax = :totalTax,
                            total_amount = :totalAmount,
                            dpp = :dpp
                 WHERE project_id = :projectId
                 and company_id = :companyId
                 and user_id = :userId ;
                """;
        try {

            int data = jdbcTemplate.update(sql, toParams(proyek));

            System.out.println("========== UPDATE PROJECT ==========");
            System.out.println("projectId = " + proyek.getProjectId());
            System.out.println("companyId = " + proyek.getCompanyId());
            System.out.println("userId    = " + proyek.getUserId());
            System.out.println("ROWS      = " + data);
            System.out.println("====================================");

            if (data == 0) {
                throw new IllegalArgumentException(
                        "Project tidak ditemukan atau akses tidak sesuai");
            }
            return data;
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Gagal menyimpan data: " + e.getMostSpecificCause().getMessage(),
                    e);
        }
    }

    public List<Proyek> findById(UUID companyId, String keyword, LocalDateTime lastCreatedAt, UUID lastId,
            int limit) {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        params.put("limit", limit);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            params.put("keyword", keyword + "%");
        }
        String sql = """
                SELECT
                	projects.project_id,
                	projects.project_code,
                    projects.po_date,
                	projects.name,
                    projects.project_type,
                    clients.client_id,
                	clients.client_name,
                    projects.contract_value,
                	projects.company_id,
                    projects.created_at,
                    projects.tax_type,
                    projects.dpp,
                    projects.vat_rate,
                    projects.total_tax,
                    projects.total_amount,
                    projects.created_at
                FROM public.projects
                    INNER join  clients on clients.client_id = projects.client_id
                WHERE projects.company_id = :companyId
                """;
        if (hasKeyword) {
            sql += " AND (name LIKE :keyword)";
        }

        // 🔥 keyset filter (optional)
        if (lastCreatedAt != null && lastId != null) {
            sql += """
                        AND (
                                projects.created_at < :lastCreatedAt
                                OR (projects.created_at = :lastCreatedAt AND projects.project_id < :lastId)
                            )
                    """;
            params.put("lastCreatedAt", lastCreatedAt);
            params.put("lastId", lastId);
        }

        sql += " ORDER BY projects.created_at DESC, projects.project_id DESC LIMIT :limit ";
        List<Proyek> result = new ArrayList<>();
        try {
            result = jdbcTemplate.query(sql, params, new DataClassRowMapper<>(Proyek.class));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Proyek detailById(UUID id) {
        String sql = """
                select
                    *
                from projects
                    INNER join  clients on clients.client_id = projects.client_id
                where projects.project_id = :projectId
                    """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("projectId", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, new ProyekMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Client> findClientByCompanyId(UUID companyId) {

        String sql = """
                select client_id, client_name, company_id from clients
                where company_id = :companyId
                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);
        try {
            return jdbcTemplate.query(sql, params, new DataClassRowMapper<>(Client.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public BigDecimal totalRevenueByProject(UUID projectId, UUID companyId) {
        String sql = """
                SELECT COALESCE(SUM(contract_value), 0)
                FROM projects
                WHERE company_id = :companyId
                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    params,
                    BigDecimal.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public BigDecimal totalRevenueByCompany(UUID companyId) {

        String sql = """
                SELECT COALESCE(SUM(contract_value), 0)
                FROM projects
                WHERE company_id = :companyId
                """;
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("companyId", companyId);

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    params,
                    BigDecimal.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
