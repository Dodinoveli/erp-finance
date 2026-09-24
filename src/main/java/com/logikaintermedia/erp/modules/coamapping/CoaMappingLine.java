package com.logikaintermedia.erp.modules.coamapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CoaMappingLine {
    private UUID mappingLineId;
    private UUID mappingId;
    private UUID coaId;
    private String position;
    private Integer lineOrder;
    private String amountType;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
