package com.example.demo.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeUsuario;

    @NotBlank
    private String tipo_de_investidor="nenhum por enquanto";

    @NotBlank
    private String dt_nascimento;

    @NotBlank
    private String senha;

    @NotBlank
    private String  cpf;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private boolean userIsActive = true;

    private String role = "ROLE_USER"; // Pode ser "USER" ou "ADMIN"

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(boolean userIsActive) {
        this.userIsActive = userIsActive;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(String dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public String  getCpf() {
        return cpf;
    }

    public void setCpf(String  cpf) {
        this.cpf = cpf;
    }

    public String  tipo_de_investidor() {
        return tipo_de_investidor;
    }

    public void tipo_de_investidor(String  tipo_de_investidor) {
        this.tipo_de_investidor = tipo_de_investidor;
    }
}
