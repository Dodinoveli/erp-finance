package com.logikaintermedia.erp.modules.refreshtoken;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiredAt;
    private String ipAddress;
    private String userAgent;
    private String deviceId;
    private LocalDateTime createdAt;
}
