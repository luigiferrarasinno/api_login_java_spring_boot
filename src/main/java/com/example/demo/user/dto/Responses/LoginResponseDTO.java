package com.example.demo.user.dto.Responses;

public class LoginResponseDTO {
    private String token;
    private Long userId;
    private boolean firstLogin;

    public LoginResponseDTO(String token, Long userId, boolean firstLogin) {
        this.token = token;
        this.userId = userId;
        this.firstLogin = firstLogin;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }
}

