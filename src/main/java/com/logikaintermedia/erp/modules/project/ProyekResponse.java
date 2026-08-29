package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ProyekResponse {
    private UUID projectId;
    private String projectCode;
    private String name;
    private String description;
    private UUID clientId;
    private String projectType;
    private BigDecimal dpp;

    // dpp
    private BigDecimal contractValue;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;
    private UUID userId;
    private String taxType;
    private BigDecimal vatRate;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;
    private LocalDate poDate;

    // client
    private String clientName;
}
