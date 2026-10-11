package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;

@Repository
public class CoaMappingLineRepository {
	public final NamedParameterJdbcTemplate jdbcTemplate;

	public CoaMappingLineRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public @NonNull MapSqlParameterSource toParams(CoaMappingLine mapping) {
		return new MapSqlParameterSource().addValue("mappingLineId", mapping.getMappingLineId())
				.addValue("mappingId", mapping.getMappingId()).addValue("coaId", mapping.getCoaId())
				.addValue("position", mapping.getPosition().name()).addValue("lineOrder", mapping.getLineOrder())
				.addValue("amountType", mapping.getAmountType().name()).addValue("isActive", mapping.getIsActive())
				.addValue("createdAt", mapping.getCreatedAt()).addValue("updatedAt", mapping.getUpdatedAt());
	}

	/**
	 * Menampilkan combobox coa_mapping ke dalam form coa mapping line
	 */
	public List<CoaMapping> findCoaMappingByCompanyId(UUID companyId) {
		String sql = """
				select
					co.mapping_id,
					co.transaction_type,
					co.payment_type,
					co.description
				from coa_mapping co
					where co.company_id = :companyId
				""";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("companyId", companyId);
		return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(CoaMapping.class));
	}

	/**
	 * Menampilkan combobox chart_of_accounts ke dalam form coa mapping line
	 */
	public List<ChartOfAccounts> findCoaByCompanyId(UUID companyId) {
		String sql = """
					SELECT
				    coa.account_id,
				    coa.account_code,
				    coa.account_name,
				    coa.account_type,
				    coa.parent_id
				FROM chart_of_accounts coa
				WHERE coa.company_id = :companyId
				  AND coa.account_level = 4
				ORDER BY coa.account_code ASC;
					""";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("companyId", companyId);
		return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ChartOfAccounts.class));
	}

	/**
	 * Menampilkan coa_mapping line
	 */
	public List<CoaMappingLine> findCoaMappingLineByCompanyId(UUID companyId, int start, int length, String keyword) {
		String sql = """
				select
					coam.transaction_type,
					coam.payment_type,
					coal.mapping_id,
					coal.coa_id,
					coal.position,
					coal.line_order,
					cofac.account_name as account_name
				from coa_mapping coam
					inner join coa_mapping_lines coal
				on coam.mapping_id = coal.mapping_id
					inner join chart_of_accounts cofac
				on coal.coa_id = cofac.account_id
				where coam.company_id= :companyId
								""";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("companyId", companyId);

		if (keyword != null && !keyword.isBlank()) {
			sql += """
					and (account_name ILIKE :keyword)
					""";
		}
		sql += """
				order by coal.mapping_line_id asc
				""";
		try {
			List<CoaMappingLine> rst = jdbcTemplate.query(sql, params, new CoaMappingLineMapper());
			return rst;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("null")
	public long countAll(UUID companyId) {
		String sql = """
				Select
					COUNT(*)  as total
				from coa_mapping coam
					inner join coa_mapping_lines coal
				on coam.mapping_id = coal.mapping_id
					inner join chart_of_accounts cofac
				on coal.coa_id = cofac.account_id
				where coam.company_id= :companyId
				""";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("companyId", companyId);
		Long result = jdbcTemplate.queryForObject(sql, parameterSource, Long.class);
		return result != null ? result : 0;
	}

	@SuppressWarnings("null")
	public long countFiltered(UUID companyId, String keyword) {
		String sql = """
				select
						COUNT(*)  as total
					from coa_mapping coam
					inner join coa_mapping_lines coal
					on coam.mapping_id = coal.mapping_id
					inner join chart_of_accounts cofac
					on coal.coa_id = cofac.account_id
					where coam.company_id= :companyId
								""";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("companyId", companyId);
		if (keyword != null && !keyword.isBlank()) {
			sql += """
					AND (
					    CAST(coam.transaction_type AS TEXT) ILIKE :keyword
					)
					""";
			params.addValue("keyword", "%" + keyword.trim() + "%");
		}
		Long result = jdbcTemplate.queryForObject(sql.toString(), params, Long.class);
		return result != null ? result : 0;
	}

	public void lockMapping(UUID companyId, UUID mappingId) {
		String sql = """
				SELECT mapping_id
				FROM coa_mapping
				WHERE company_id = :companyId
				  AND mapping_id = :mappingId
				FOR UPDATE
				""";
		
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("companyId", companyId)
				.addValue("mappingId", mappingId);
		List<UUID> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getObject("mapping_id", UUID.class));

		if (result.isEmpty()) {
			throw new IllegalArgumentException("COA Mapping tidak ditemukan atau bukan milik perusahaan ini.");
		}
	}

	public int findMaxLineOrder(UUID mappingId) {
		String sql = """
				SELECT COALESCE(MAX(line_order), 0)
				FROM coa_mapping_lines
				WHERE mapping_id = :mappingId
				""";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("mappingId", mappingId);
		return jdbcTemplate.queryForObject(sql, params, Integer.class);
	}

	public boolean existsMappingLine(UUID companyId, UUID mappingId, UUID coaId) {
		String sql = """
				select EXISTS(
				select 1
					from coa_mapping_lines coal
					inner join coa_mapping coa on coal.mapping_id = coa.mapping_id
				where coa.company_id = :companyId
				and coal.mapping_id = :mappingId
				and coal.coa_id = :coaId
				)
				""";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("companyId", companyId);
		source.addValue("mappingId", mappingId);
		source.addValue("coaId", coaId);
		Boolean exists = jdbcTemplate.queryForObject(sql, source, Boolean.class);
		return Boolean.TRUE.equals(exists);
	}

	/**
	 * Save data ke data base
	 */
	public int[] save(List<CoaMappingLine> lines) {
		String sql = """
				INSERT INTO public.coa_mapping_lines (
				    mapping_line_id, mapping_id, coa_id, position, line_order, amount_type, is_active, created_at, updated_at
				)
				VALUES (
				    :mappingLineId, :mappingId, :coaId, :position, :lineOrder, :amountType, :isActive, :createdAt, :updatedAt
				)
				""";
		SqlParameterSource[] params = lines.stream().map(this::toParams).toArray(SqlParameterSource[]::new);
		return jdbcTemplate.batchUpdate(sql, params);
	}

}
