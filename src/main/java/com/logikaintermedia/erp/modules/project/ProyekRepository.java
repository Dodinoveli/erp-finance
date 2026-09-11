package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
                .addValue("projectPo", mdl.getProjectPo())
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

    /**
     * menyimpan data proyek bedasarkan id perusahaan
     */
    public int save(Proyek proyek) {
        String sql = """
                INSERT INTO projects (
                	project_id, project_code, project_po, name, description, client_id,
                    project_type, contract_value, location, company_id,
                    created_at, updated_at, user_id, vat_rate, po_date,
                    tax_type, total_tax, total_amount, dpp
                    )
                	VALUES (
                        :projectId, :projectCode, :projectPo, :name, :description, :clientId,
                        :projectType, :contractValue, :location, :companyId,
                        :createdAt, :updatedAt, :userId, :vatRate, :poDate,
                        :taxType, :totalTax, :totalAmount, :dpp
                        )
                    """;
        try {
            return jdbcTemplate.update(sql, toParams(proyek));
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Project code atau ID sudah terdaftar!", e);

        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getMostSpecificCause();
            e.printStackTrace();
            throw new RuntimeException("Data tidak valid: " + rootCause.getMessage(), e);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menyimpan project!", e);
        }
    }

    /**
     * menyimpan dan menubah data proyek bedasarkan id perusahaan
     */
    public int update(Proyek proyek) {
        String sql = """
                    UPDATE public.projects
                         SET
                            project_po = :projectPo,
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

    /**
     * Generator Urutan Bilangan Bulat
     * 
     * @param companyId
     * @return
     */
    public long IntSequenceGenerator(UUID companyId) {
        String sql = """
                UPDATE company_sequences
                    SET current_value = current_value + 1
                WHERE company_id = :companyId
                  AND sequence_type = 'PROJECT'
                RETURNING current_value;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("companyId", companyId);

        Long rest = jdbcTemplate.queryForObject(sql, params, Long.class);
        if (rest == null) {
            throw new IllegalStateException("Gagal mendapatkan nomor proyek untuk company: " + companyId);
        }
        return rest;
    }

    /**
     * mengambil data proyek bedasarkan id perusahaan
     * 
     * @param companyId
     * @return
     */
    public List<Proyek> findProyekByCompanyId(UUID companyId,
            int start,
            int length,
            String keyword) {
        String sql = """
                select
                	projects.project_id,
                	projects.project_code,
                	projects.project_po,
                	projects.po_date,
                	projects.name,
                	projects.project_type,
                	clients.client_id,
                	clients.client_name,
                	projects.contract_value,
                    projects.tax_type,
                    projects.vat_rate,
                	projects.dpp,
                	projects.total_tax,
                	projects.total_amount,
                	projects.created_at
                FROM public.projects
                	INNER join clients on clients.client_id = projects.client_id
                WHERE projects.company_id = :companyId
                                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("companyId", companyId);
        params.addValue("start", start);
        params.addValue("length", length);

        if (keyword != null && !keyword.isBlank()) {
            sql += """
                    AND (
                        projects.name ILIKE :keyword
                    )
                    """;
            params.addValue("keyword", "%" + keyword.trim() + "%");
        }

        sql += """
                ORDER BY projects.project_id DESC
                LIMIT :length OFFSET :start
                """;
        try {
            List<Proyek> result = jdbcTemplate.query(sql, params,
                    new BeanPropertyRowMapper<>(Proyek.class));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("null")
    public long countAll(UUID companyId) {

        String sql = """
                SELECT COUNT(*)
                FROM projects
                WHERE company_id = :companyId
                """;

        Long result = jdbcTemplate.queryForObject(
                sql,
                Map.of("companyId", companyId),
                Long.class);

        return result != null ? result : 0;
    }

    @SuppressWarnings("null")
    public long countFiltered(
            UUID companyId,
            String keyword) {

        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM projects
                WHERE company_id = :companyId
                """);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyId", companyId);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
                    AND (
                        projects.name ILIKE :keyword
                    )
                    """);

            params.addValue(
                    "keyword",
                    "%" + keyword.trim() + "%");
        }

        Long result = jdbcTemplate.queryForObject(
                sql.toString(),
                params,
                Long.class);

        return result != null ? result : 0;
    }

    /**
     * mengambil data proyek bedasarkan projectId
     */
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

    /**
     * mengambil data clients bedasarkan companyId
     */
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
