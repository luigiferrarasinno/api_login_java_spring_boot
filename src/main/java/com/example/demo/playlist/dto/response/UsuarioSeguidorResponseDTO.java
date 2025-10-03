package com.example.demo.playlist.dto.response;

public class UsuarioSeguidorResponseDTO {

    private Long id;
    private String nomeUsuario;
    private String email;

    public UsuarioSeguidorResponseDTO() {}

    public UsuarioSeguidorResponseDTO(Long id, String nomeUsuario, String email) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}