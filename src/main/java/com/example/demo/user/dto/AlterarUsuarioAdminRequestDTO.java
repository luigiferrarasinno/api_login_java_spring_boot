package com.example.demo.user.dto;

import com.example.demo.user.model.TipoPerfil;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public class AlterarUsuarioAdminRequestDTO {
    
    private String nomeUsuario;
    
    @Email
    private String email;
    
    private Long cpf;
    
    private LocalDate dt_nascimento;
    
    private String senha;
    
    private Boolean userIsActive;
    
    private String role;
    
    private TipoPerfil tipo;

    // Constructors
    public AlterarUsuarioAdminRequestDTO() {}

    public AlterarUsuarioAdminRequestDTO(String nomeUsuario, String email, Long cpf, 
                                       LocalDate dt_nascimento, String senha, 
                                       Boolean userIsActive, String role, TipoPerfil tipo) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.cpf = cpf;
        this.dt_nascimento = dt_nascimento;
        this.senha = senha;
        this.userIsActive = userIsActive;
        this.role = role;
        this.tipo = tipo;
    }

    // Getters and Setters
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getCpf() { return cpf; }
    public void setCpf(Long cpf) { this.cpf = cpf; }

    public LocalDate getDt_nascimento() { return dt_nascimento; }
    public void setDt_nascimento(LocalDate dt_nascimento) { this.dt_nascimento = dt_nascimento; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Boolean getUserIsActive() { return userIsActive; }
    public void setUserIsActive(Boolean userIsActive) { this.userIsActive = userIsActive; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public TipoPerfil getTipo() { return tipo; }
    public void setTipo(TipoPerfil tipo) { this.tipo = tipo; }
}