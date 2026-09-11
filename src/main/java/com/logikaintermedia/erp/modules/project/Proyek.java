package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Proyek {
    // header
    private UUID projectId;
    private String projectCode;
    private String projectPo;
    private String name;
    private String description;
    private UUID clientId;
    private String projectType;
    private BigDecimal dpp;

    // dpp
    private BigDecimal contractValue;
    private String location;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID companyId;
    private UUID userId;
    private String taxType;
    private BigDecimal vatRate;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;
    private LocalDate poDate;
    private String clientName;
}
