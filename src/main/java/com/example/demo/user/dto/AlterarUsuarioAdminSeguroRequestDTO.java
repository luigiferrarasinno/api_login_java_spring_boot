package com.example.demo.user.dto;

import com.example.demo.user.model.TipoPerfil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Schema(description = "DTO para alteração de dados de usuário pelo admin (sem senha e CPF por segurança)")
public class AlterarUsuarioAdminSeguroRequestDTO {
    
    @Schema(description = "Nome do usuário", example = "João Silva Santos")
    private String nomeUsuario;
    
    @Schema(description = "Email do usuário", example = "joao.silva@email.com")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @Schema(description = "Data de nascimento", example = "1990-05-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dt_nascimento;
    
    @Schema(description = "Status de ativação da conta", example = "true")
    private Boolean userIsActive;
    
    @Schema(description = "Papel do usuário no sistema", example = "ROLE_USER", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
    private String role;
    
    @Schema(description = "Tipo de perfil de investimento", example = "PERFIL_MODERADO")
    private TipoPerfil tipo;

    // Constructors
    public AlterarUsuarioAdminSeguroRequestDTO() {}

    public AlterarUsuarioAdminSeguroRequestDTO(String nomeUsuario, String email, 
                                             LocalDate dt_nascimento, Boolean userIsActive, 
                                             String role, TipoPerfil tipo) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.dt_nascimento = dt_nascimento;
        this.userIsActive = userIsActive;
        this.role = role;
        this.tipo = tipo;
    }

    // Getters and Setters
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDt_nascimento() { return dt_nascimento; }
    public void setDt_nascimento(LocalDate dt_nascimento) { this.dt_nascimento = dt_nascimento; }

    public Boolean getUserIsActive() { return userIsActive; }
    public void setUserIsActive(Boolean userIsActive) { this.userIsActive = userIsActive; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public TipoPerfil getTipo() { return tipo; }
    public void setTipo(TipoPerfil tipo) { this.tipo = tipo; }

    // Método para converter para o DTO completo (usado internamente)
    public AlterarUsuarioAdminRequestDTO toFullDTO() {
        AlterarUsuarioAdminRequestDTO fullDTO = new AlterarUsuarioAdminRequestDTO();
        fullDTO.setNomeUsuario(this.nomeUsuario);
        fullDTO.setEmail(this.email);
        fullDTO.setDt_nascimento(this.dt_nascimento);
        fullDTO.setUserIsActive(this.userIsActive);
        fullDTO.setRole(this.role);
        fullDTO.setTipo(this.tipo);
        // senha e cpf ficam null por segurança
        return fullDTO;
    }
}