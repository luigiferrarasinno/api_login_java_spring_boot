package com.example.demo.user.dto.Responses;

import com.example.demo.user.model.Usuario;

public class UsuarioListaResponseDTO {
    private Long id;
    private String nomeUsuario;
    private String email;
    private String role;
    private boolean ativo;

    public UsuarioListaResponseDTO() {}

    public UsuarioListaResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
        this.ativo = usuario.isUserIsActive();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}