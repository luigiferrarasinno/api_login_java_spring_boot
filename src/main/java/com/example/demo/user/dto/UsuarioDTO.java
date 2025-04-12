//  (entrada de dados)
package com.example.demo.user.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;



public class UsuarioDTO {
    private String nomeUsuario;
    private String senha;
    private String email;
    private String  cpf;

    private String dt_nascimento;



    public UsuarioDTO() {}

    public UsuarioDTO(String nomeUsuario, String senha, String email) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
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
}
