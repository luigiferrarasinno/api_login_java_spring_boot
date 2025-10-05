package com.example.demo.historico.dto;

import java.math.BigDecimal;

public class ResumoHistoricoDTO {
    
    private Integer quantidadeInvestimentos;
    private BigDecimal totalInvestidoGeral;
    private BigDecimal totalRetornandoGeral;
    private BigDecimal retornoGeral;
    private BigDecimal percentualRetornoGeral;
    private String lucroPrejuizoGeral; // "LUCRO" ou "PREJUIZO"
    private Integer investimentosComLucro;
    private Integer investimentosComPrejuizo;
    
    // Construtores
    public ResumoHistoricoDTO() {}
    
    public ResumoHistoricoDTO(Integer quantidadeInvestimentos, BigDecimal totalInvestidoGeral, 
                             BigDecimal totalRetornandoGeral, BigDecimal retornoGeral, 
                             BigDecimal percentualRetornoGeral, String lucroPrejuizoGeral,
                             Integer investimentosComLucro, Integer investimentosComPrejuizo) {
        this.quantidadeInvestimentos = quantidadeInvestimentos;
        this.totalInvestidoGeral = totalInvestidoGeral;
        this.totalRetornandoGeral = totalRetornandoGeral;
        this.retornoGeral = retornoGeral;
        this.percentualRetornoGeral = percentualRetornoGeral;
        this.lucroPrejuizoGeral = lucroPrejuizoGeral;
        this.investimentosComLucro = investimentosComLucro;
        this.investimentosComPrejuizo = investimentosComPrejuizo;
    }
    
    // Getters e Setters
    public Integer getQuantidadeInvestimentos() {
        return quantidadeInvestimentos;
    }
    
    public void setQuantidadeInvestimentos(Integer quantidadeInvestimentos) {
        this.quantidadeInvestimentos = quantidadeInvestimentos;
    }
    
    public BigDecimal getTotalInvestidoGeral() {
        return totalInvestidoGeral;
    }
    
    public void setTotalInvestidoGeral(BigDecimal totalInvestidoGeral) {
        this.totalInvestidoGeral = totalInvestidoGeral;
    }
    
    public BigDecimal getTotalRetornandoGeral() {
        return totalRetornandoGeral;
    }
    
    public void setTotalRetornandoGeral(BigDecimal totalRetornandoGeral) {
        this.totalRetornandoGeral = totalRetornandoGeral;
    }
    
    public BigDecimal getRetornoGeral() {
        return retornoGeral;
    }
    
    public void setRetornoGeral(BigDecimal retornoGeral) {
        this.retornoGeral = retornoGeral;
    }
    
    public BigDecimal getPercentualRetornoGeral() {
        return percentualRetornoGeral;
    }
    
    public void setPercentualRetornoGeral(BigDecimal percentualRetornoGeral) {
        this.percentualRetornoGeral = percentualRetornoGeral;
    }
    
    public String getLucroPrejuizoGeral() {
        return lucroPrejuizoGeral;
    }
    
    public void setLucroPrejuizoGeral(String lucroPrejuizoGeral) {
        this.lucroPrejuizoGeral = lucroPrejuizoGeral;
    }
    
    public Integer getInvestimentosComLucro() {
        return investimentosComLucro;
    }
    
    public void setInvestimentosComLucro(Integer investimentosComLucro) {
        this.investimentosComLucro = investimentosComLucro;
    }
    
    public Integer getInvestimentosComPrejuizo() {
        return investimentosComPrejuizo;
    }
    
    public void setInvestimentosComPrejuizo(Integer investimentosComPrejuizo) {
        this.investimentosComPrejuizo = investimentosComPrejuizo;
    }
}