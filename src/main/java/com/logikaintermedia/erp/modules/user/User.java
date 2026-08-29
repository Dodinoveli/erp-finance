package com.logikaintermedia.erp.modules.user;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class User {
    private UUID userId;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;
    private UUID companyId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private String deviceId;
}
