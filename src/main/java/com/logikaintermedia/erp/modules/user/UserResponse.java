package com.logikaintermedia.erp.modules.user;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponse {
    private UUID userId;
    private String userName;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private Boolean isActive;
    private UUID companyId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
