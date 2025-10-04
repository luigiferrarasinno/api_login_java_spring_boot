package com.example.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para login de usuários usando CPF e senha")
public class LoginRequestDTO {
    
    @Schema(description = "CPF do usuário (apenas números)", 
            example = "12345678901", 
            required = true)
    private String cpf;
    
    @Schema(description = "Senha do usuário", 
            example = "minhasenha123", 
            required = true)
    private String senha;
    
    @Schema(description = "Indica se é o primeiro login", 
            example = "false")
    private boolean firstLogin = false; // Campo opcional

    public LoginRequestDTO() {}

    public LoginRequestDTO(String cpf, String senha, boolean firstLogin) {
        this.cpf = cpf;
        this.senha = senha;
        this.firstLogin = firstLogin;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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