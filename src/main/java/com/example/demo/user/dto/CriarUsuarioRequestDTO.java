package com.example.demo.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class CriarUsuarioRequestDTO {
    private String nomeUsuario;
    private String senha;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dt_nascimento;
    private Long cpf;

    public CriarUsuarioRequestDTO() {}

    public CriarUsuarioRequestDTO(String nomeUsuario, String senha, String email, LocalDate dt_nascimento, Long cpf) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
        this.dt_nascimento = dt_nascimento;
        this.cpf = cpf;
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

    public LocalDate getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(LocalDate dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }
}