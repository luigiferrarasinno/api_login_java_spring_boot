package com.example.demo.user.dto.Responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta do login exclusivo para administradores")
public class LoginAdminResponseDTO {
    
    @Schema(description = "Token JWT para autenticação admin", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "ID do usuário administrador", 
            example = "1")
    private String adminId;
    
    @Schema(description = "Nome do administrador", 
            example = "Admin Sistema")
    private String nomeAdmin;
    
    @Schema(description = "Nível de acesso", 
            example = "ROLE_ADMIN")
    private String role;
    
    public LoginAdminResponseDTO() {}
    
    public LoginAdminResponseDTO(String token, String adminId, String nomeAdmin, String role) {
        this.token = token;
        this.adminId = adminId;
        this.nomeAdmin = nomeAdmin;
        this.role = role;
    }
    
    // Getters e Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getAdminId() {
        return adminId;
    }
    
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    
    public String getNomeAdmin() {
        return nomeAdmin;
    }
    
    public void setNomeAdmin(String nomeAdmin) {
        this.nomeAdmin = nomeAdmin;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}