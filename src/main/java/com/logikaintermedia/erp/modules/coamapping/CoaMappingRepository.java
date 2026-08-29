package com.logikaintermedia.erp.modules.coamapping;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CoaMappingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CoaMappingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
