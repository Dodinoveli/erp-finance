package com.logikaintermedia.erp.modules.user;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    private UUID userId;

    @NotBlank(message = "username wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String userName;

    @NotBlank(message = "password wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    private String password;

    private String deviceId;
    private String ipAddress;
    private String userAgent;

    private String fullName;
    private String email;
    private String phone;
    private Boolean isActive;
    private UUID companyId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
