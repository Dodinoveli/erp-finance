package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CoaMappingLineRepository {
	public final NamedParameterJdbcTemplate jdbcTemplate;

	public CoaMappingLineRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

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

		StringBuilder sql = new StringBuilder("""
				select
					COUNT(*)  as total
				from coa_mapping coam
					inner join coa_mapping_lines coal
				on coam.mapping_id = coal.mapping_id
					inner join chart_of_accounts cofac
				on coal.coa_id = cofac.account_id
				where coam.company_id= :companyId
				""");

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("companyId", companyId);
		if (keyword != null && !keyword.isBlank()) {
			sql.append("""
					AND (
					    CAST(coam.transaction_type AS TEXT) ILIKE :keyword
					)
					""");
			params.addValue("keyword", "%" + keyword.trim() + "%");
		}
		Long result = jdbcTemplate.queryForObject(sql.toString(), params, Long.class);
		return result != null ? result : 0;
	}

}
