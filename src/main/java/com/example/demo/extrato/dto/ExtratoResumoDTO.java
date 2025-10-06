package com.example.demo.extrato.dto;

import com.example.demo.extrato.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 📋 DTO simplificado para transações do extrato em resumos
 */
public class ExtratoResumoDTO {
    
    private Long id;
    private TipoTransacao tipoTransacao;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private String descricao;
    private LocalDateTime dataTransacao;
    
    // Constructors
    public ExtratoResumoDTO() {}
    
    public ExtratoResumoDTO(Long id, TipoTransacao tipoTransacao, BigDecimal quantidade, 
                           BigDecimal precoUnitario, BigDecimal valorTotal, 
                           String descricao, LocalDateTime dataTransacao) {
        this.id = id;
        this.tipoTransacao = tipoTransacao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
        this.descricao = descricao;
        this.dataTransacao = dataTransacao;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TipoTransacao getTipoTransacao() { return tipoTransacao; }
    public void setTipoTransacao(TipoTransacao tipoTransacao) { this.tipoTransacao = tipoTransacao; }
    
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDateTime getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDateTime dataTransacao) { this.dataTransacao = dataTransacao; }
}