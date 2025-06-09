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
    private String user_permissions = "nenhuma por enquanto";
    private boolean firstLogin = true; // Novo campo adicionado



    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
        this.cpf = usuario.getCpf();
        this.dt_nascimento = usuario.getDt_nascimento();
        this.tipo_de_investidor = usuario.getTipo_de_investidor();
        this.userIsActive = usuario.isUserIsActive();
        this.user_permissions = usuario.getUser_permissions();
        this.firstLogin = usuario.isFirstLogin(); // Acessando o novo campo
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
  
    public boolean isUserIsActive() {
        return userIsActive;
    }
   
    public String getUser_permissions() {
        return user_permissions;
    }
    public boolean isFirstLogin() {
        return firstLogin;
    }
   
}
