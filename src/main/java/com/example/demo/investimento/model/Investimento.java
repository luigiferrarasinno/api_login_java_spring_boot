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

    @NotBlank
    private String simbolo;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Categoria categoria;

    @NotNull
    @PositiveOrZero
    @Column(name = "preco_base")
    private BigDecimal precoBase; // Preço de referência inicial

    @PositiveOrZero
    @Column(name = "preco_atual")
    private BigDecimal precoAtual; // Preço atual de mercado

    @Column(name = "variacao_percentual")
    private BigDecimal variacaoPercentual; // Variação % em relação ao preço base

    @Column(name = "ultima_atualizacao_preco")
    private LocalDateTime ultimaAtualizacaoPreco;

    private String descricao;

    @NotNull
    private LocalDate data;

    private String liquidez;

    @PositiveOrZero
    @Column(name = "quantidade_total")
    private Long quantidadeTotal; // Quantidade total de ações emitidas

    @PositiveOrZero
    @Column(name = "quantidade_disponivel")
    private Long quantidadeDisponivel; // Quantidade disponível para compra

    @PositiveOrZero
    @Column(name = "dividend_yield")
    private BigDecimal dividendYield; // Yield de dividendos anual (%)

    @PositiveOrZero
    @Column(name = "frequencia_dividendo")
    private Integer frequenciaDividendo = 4; // Quantas vezes por ano paga dividendo (padrão: trimestral)

    private boolean ativo = true;

    @Column(name = "visivel_para_usuarios")
    private boolean visivelParaUsuarios = true; // Admin pode controlar se usuários veem este investimento

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

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

    public BigDecimal getPrecoAtual() { return precoAtual; }
    public void setPrecoAtual(BigDecimal precoAtual) { this.precoAtual = precoAtual; }

    public BigDecimal getVariacaoPercentual() { return variacaoPercentual; }
    public void setVariacaoPercentual(BigDecimal variacaoPercentual) { this.variacaoPercentual = variacaoPercentual; }

    public LocalDateTime getUltimaAtualizacaoPreco() { return ultimaAtualizacaoPreco; }
    public void setUltimaAtualizacaoPreco(LocalDateTime ultimaAtualizacaoPreco) { this.ultimaAtualizacaoPreco = ultimaAtualizacaoPreco; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getLiquidez() { return liquidez; }
    public void setLiquidez(String liquidez) { this.liquidez = liquidez; }

    public Long getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(Long quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public Long getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(Long quantidadeDisponivel) { this.quantidadeDisponivel = quantidadeDisponivel; }

    public BigDecimal getDividendYield() { return dividendYield; }
    public void setDividendYield(BigDecimal dividendYield) { this.dividendYield = dividendYield; }

    public Integer getFrequenciaDividendo() { return frequenciaDividendo; }
    public void setFrequenciaDividendo(Integer frequenciaDividendo) { this.frequenciaDividendo = frequenciaDividendo; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isVisivelParaUsuarios() { return visivelParaUsuarios; }
    public void setVisivelParaUsuarios(boolean visivelParaUsuarios) { this.visivelParaUsuarios = visivelParaUsuarios; }

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
