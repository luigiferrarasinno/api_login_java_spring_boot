package com.example.demo.investimento.dto;

public class InvestimentoRecomendadoResponseDTO {
    
    private Long id;
    private Long usuarioId;
    private Long investimentoId;
    private String investimentoNome;
    private String investimentoSimbolo;
    private String categoria;
    private String risco;
    private java.time.LocalDateTime dataRecomendacao;
    
    // Constructors
    public InvestimentoRecomendadoResponseDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    
    public String getInvestimentoNome() {
        return investimentoNome;
    }
    
    public void setInvestimentoNome(String investimentoNome) {
        this.investimentoNome = investimentoNome;
    }
    
    public String getInvestimentoSimbolo() {
        return investimentoSimbolo;
    }
    
    public void setInvestimentoSimbolo(String investimentoSimbolo) {
        this.investimentoSimbolo = investimentoSimbolo;
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
    
    public java.time.LocalDateTime getDataRecomendacao() {
        return dataRecomendacao;
    }
    
    public void setDataRecomendacao(java.time.LocalDateTime dataRecomendacao) {
        this.dataRecomendacao = dataRecomendacao;
    }
}
