package com.logikaintermedia.erp.jwt;

import lombok.Data;

@Data
public class AuthResult {
    private String accessToken;
    private String refreshToken;

    public AuthResult(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
