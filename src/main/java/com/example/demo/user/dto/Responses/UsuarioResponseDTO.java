package com.example.demo.user.dto.Responses;

import com.example.demo.user.model.Usuario;
import java.time.LocalDate;

public class UsuarioResponseDTO {
    private Long id;
    private String nomeUsuario;
    private String email;
    private String role;
    private Long cpf;
    private LocalDate dt_nascimento;
    private String tipo_de_investidor = "nenhum por enquanto";
    private boolean userIsActive = true;


    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
        this.cpf = usuario.getCpf();
        this.dt_nascimento = usuario.getDt_nascimento();
    }

    public Long getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getCpf() {
        return cpf;
    }

    public LocalDate getDt_nascimento() {
        return dt_nascimento;
    }
    public String getTipo_de_investidor() {
        return tipo_de_investidor;
    }
    public void setTipo_de_investidor(String tipo_de_investidor) {
        this.tipo_de_investidor = tipo_de_investidor;
    }
    public boolean isUserIsActive() {
        return userIsActive;
    }
    public void setUserIsActive(boolean userIsActive) {
        this.userIsActive = userIsActive;
    }
}
