package com.example.demo.extrato.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 📊 DTO para retorno de resumo de investimentos baseado no extrato
 */
public class ResumoInvestimentoDTO {
    
    private String periodo;
    private Long investimentoId;
    private String simboloInvestimento;
    private String nomeInvestimento;
    private BigDecimal totalInvestido;
    private BigDecimal totalRecebido;
    private BigDecimal totalDividendos;
    private BigDecimal resultadoLiquido;
    private String situacao; // "LUCRO" ou "PREJUIZO"
    private BigDecimal percentualRetorno;
    private int numeroOperacoes;
    private List<ExtratoResumoDTO> transacoes;
    
    // Constructors
    public ResumoInvestimentoDTO() {}
    
    public ResumoInvestimentoDTO(String periodo, Long investimentoId, String simboloInvestimento, String nomeInvestimento) {
        this.periodo = periodo;
        this.investimentoId = investimentoId;
        this.simboloInvestimento = simboloInvestimento;
        this.nomeInvestimento = nomeInvestimento;
        this.totalInvestido = BigDecimal.ZERO;
        this.totalRecebido = BigDecimal.ZERO;
        this.totalDividendos = BigDecimal.ZERO;
        this.resultadoLiquido = BigDecimal.ZERO;
        this.percentualRetorno = BigDecimal.ZERO;
        this.numeroOperacoes = 0;
    }
    
    // Getters and Setters
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    
    public Long getInvestimentoId() { return investimentoId; }
    public void setInvestimentoId(Long investimentoId) { this.investimentoId = investimentoId; }
    
    public String getSimboloInvestimento() { return simboloInvestimento; }
    public void setSimboloInvestimento(String simboloInvestimento) { this.simboloInvestimento = simboloInvestimento; }
    
    public String getNomeInvestimento() { return nomeInvestimento; }
    public void setNomeInvestimento(String nomeInvestimento) { this.nomeInvestimento = nomeInvestimento; }
    
    public BigDecimal getTotalInvestido() { return totalInvestido; }
    public void setTotalInvestido(BigDecimal totalInvestido) { this.totalInvestido = totalInvestido; }
    
    public BigDecimal getTotalRecebido() { return totalRecebido; }
    public void setTotalRecebido(BigDecimal totalRecebido) { this.totalRecebido = totalRecebido; }
    
    public BigDecimal getTotalDividendos() { return totalDividendos; }
    public void setTotalDividendos(BigDecimal totalDividendos) { this.totalDividendos = totalDividendos; }
    
    public BigDecimal getResultadoLiquido() { return resultadoLiquido; }
    public void setResultadoLiquido(BigDecimal resultadoLiquido) { 
        this.resultadoLiquido = resultadoLiquido;
        // Atualizar situação automaticamente
        if (resultadoLiquido.compareTo(BigDecimal.ZERO) > 0) {
            this.situacao = "LUCRO";
        } else if (resultadoLiquido.compareTo(BigDecimal.ZERO) < 0) {
            this.situacao = "PREJUIZO";
        } else {
            this.situacao = "NEUTRO";
        }
    }
    
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    
    public BigDecimal getPercentualRetorno() { return percentualRetorno; }
    public void setPercentualRetorno(BigDecimal percentualRetorno) { this.percentualRetorno = percentualRetorno; }
    
    public int getNumeroOperacoes() { return numeroOperacoes; }
    public void setNumeroOperacoes(int numeroOperacoes) { this.numeroOperacoes = numeroOperacoes; }
    
    public List<ExtratoResumoDTO> getTransacoes() { return transacoes; }
    public void setTransacoes(List<ExtratoResumoDTO> transacoes) { this.transacoes = transacoes; }
}