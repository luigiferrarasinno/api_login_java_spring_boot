package com.example.demo.investimento.dto;

import com.example.demo.investimento.model.Investimento;

public class InvestimentoDTO {
    private Long id;
    private String nome;
    private String categoria;
    private Double valor;
    private String descricao;
    private Long usuarioId; // apenas o ID do usuário

    public InvestimentoDTO(Investimento investimento) {
        this.id = investimento.getId();
        this.nome = investimento.getNome();
        this.categoria = investimento.getCategoria();
        this.valor = investimento.getValor() != null ? investimento.getValor().doubleValue() : null;
        this.descricao = investimento.getDescricao();
        this.usuarioId = investimento.getUsuario() != null ? investimento.getUsuario().getId() : null;
    }

    // getters e setters
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
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
