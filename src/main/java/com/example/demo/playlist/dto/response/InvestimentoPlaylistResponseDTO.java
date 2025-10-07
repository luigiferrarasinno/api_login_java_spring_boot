package com.example.demo.playlist.dto.response;

import java.math.BigDecimal;

public class InvestimentoPlaylistResponseDTO {

    private Long id;
    private String nome;
    private String simbolo;
    private String categoria;
    private String risco;
    private BigDecimal precoAtual;
    private BigDecimal variacaoPercentual;
    private String descricao;
    private Boolean recomendadoParaVoce; // Null se usuário não tem recomendações, true/false caso contrário

    public InvestimentoPlaylistResponseDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRisco() {
        return risco;
    }

    public void setRisco(String risco) {
        this.risco = risco;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public BigDecimal getVariacaoPercentual() {
        return variacaoPercentual;
    }

    public void setVariacaoPercentual(BigDecimal variacaoPercentual) {
        this.variacaoPercentual = variacaoPercentual;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getRecomendadoParaVoce() {
        return recomendadoParaVoce;
    }

    public void setRecomendadoParaVoce(Boolean recomendadoParaVoce) {
        this.recomendadoParaVoce = recomendadoParaVoce;
    }
}