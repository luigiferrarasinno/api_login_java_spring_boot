package com.example.demo.user.dto;

public class LoginRequestDTO {
    private String email;
    private String senha;
    private boolean firstLogin = false; // Campo opcional

    public LoginRequestDTO() {}

    public LoginRequestDTO(String email, String senha, boolean firstLogin) {
        this.email = email;
        this.senha = senha;
        this.firstLogin = firstLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}