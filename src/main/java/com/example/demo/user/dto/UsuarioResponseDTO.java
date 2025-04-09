// (saída de dados)
package com.example.demo.user.dto;

import com.example.demo.user.model.Usuario;

public class UsuarioResponseDTO {
    private Long id;
    private String nomeUsuario;
    private String role;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.role = usuario.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getRole() {
        return role;
    }
}
