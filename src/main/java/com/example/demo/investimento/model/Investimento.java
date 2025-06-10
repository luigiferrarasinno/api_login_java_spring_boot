package com.example.demo.investimento.model;

import com.example.demo.user.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Categoria categoria;

    @NotNull
    @PositiveOrZero
    private BigDecimal valor;

    private String descricao;

    @NotNull
    private LocalDate data;

    private LocalDate vencimento;

    private String liquidez;

    @PositiveOrZero
    private BigDecimal taxaRetorno;

    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Risco risco;

    @ManyToMany(mappedBy = "investimentos")
    private Set<Usuario> usuarios = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Auditing automations
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }

    public String getLiquidez() { return liquidez; }
    public void setLiquidez(String liquidez) { this.liquidez = liquidez; }

    public BigDecimal getTaxaRetorno() { return taxaRetorno; }
    public void setTaxaRetorno(BigDecimal taxaRetorno) { this.taxaRetorno = taxaRetorno; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Risco getRisco() { return risco; }
    public void setRisco(Risco risco) { this.risco = risco; }

    public Set<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
