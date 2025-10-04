package com.example.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para login exclusivo de administradores usando CPF")
public class LoginAdminRequestDTO {
    
    @Schema(description = "CPF do administrador (apenas números)", 
            example = "12345678901", 
            required = true)
    private String cpf;
    
    @Schema(description = "Senha do administrador", 
            example = "admin123", 
            required = true)
    private String senha;
    
    public LoginAdminRequestDTO() {}
    
    public LoginAdminRequestDTO(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }
    
    // Getters e Setters
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
}