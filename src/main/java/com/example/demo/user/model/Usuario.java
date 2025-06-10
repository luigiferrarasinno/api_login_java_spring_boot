package com.example.demo.user.model;

import com.example.demo.investimento.model.Investimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NotBlank
    private String nomeUsuario;

    @NotBlank
    private String tipo_de_investidor = "nenhum por enquanto";

    @NotBlank
    private LocalDate dt_nascimento;

    @NotBlank
    private String senha;

    @Column(unique = true)
    private Long cpf;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private boolean userIsActive = true;

    private String user_permissions = "nenhuma por enquanto";

    private String role = "ROLE_USER";

    private boolean firstLogin = true;

    @ManyToMany
    @JoinTable(
        name = "usuario_investimento",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "investimento_id")
    )
    private Set<Investimento> investimentos = new HashSet<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getTipo_de_investidor() { return tipo_de_investidor; }
    public void setTipo_de_investidor(String tipo_de_investidor) { this.tipo_de_investidor = tipo_de_investidor; }

    public LocalDate getDt_nascimento() { return dt_nascimento; }
    public void setDt_nascimento(LocalDate dt_nascimento) { this.dt_nascimento = dt_nascimento; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Long getCpf() { return cpf; }
    public void setCpf(Long cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isUserIsActive() { return userIsActive; }
    public void setUserIsActive(boolean userIsActive) { this.userIsActive = userIsActive; }

    public String getUser_permissions() { return user_permissions; }
    public void setUser_permissions(String user_permissions) { this.user_permissions = user_permissions; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isFirstLogin() { return firstLogin; }
    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }

    public Set<Investimento> getInvestimentos() { return investimentos; }
    public void setInvestimentos(Set<Investimento> investimentos) { this.investimentos = investimentos; }
}
