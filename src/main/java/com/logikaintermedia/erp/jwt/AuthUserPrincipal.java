package com.logikaintermedia.erp.jwt;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserPrincipal {
    private UUID userId;
    private String username;
    private UUID companyId;
    private List<String> roles;
    private String deviceId;
}
