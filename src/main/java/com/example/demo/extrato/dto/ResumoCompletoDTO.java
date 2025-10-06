package com.example.demo.extrato.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 📊 DTO para resumo completo de investimentos com totais gerais
 */
public class ResumoCompletoDTO {
    
    private String periodo;
    private String nomeUsuario;
    private String emailUsuario;
    private List<ResumoInvestimentoDTO> investimentos;
    
    // Totais gerais
    private BigDecimal totalGeralInvestido;
    private BigDecimal totalGeralRecebido;
    private BigDecimal totalGeralDividendos;
    private BigDecimal resultadoGeralLiquido;
    private String situacaoGeral; // "LUCRO", "PREJUIZO", "NEUTRO"
    private BigDecimal percentualRetornoGeral;
    private int numeroTotalOperacoes;
    
    // Estatísticas
    private int quantidadeInvestimentosComLucro;
    private int quantidadeInvestimentosComPrejuizo;
    private int quantidadeInvestimentosNeutros;
    private BigDecimal maiorLucroIndividual;
    private BigDecimal maiorPrejuizoIndividual;
    private String investimentoMaisRentavel;
    private String investimentoMenosRentavel;
    
    // Constructors
    public ResumoCompletoDTO() {
        this.totalGeralInvestido = BigDecimal.ZERO;
        this.totalGeralRecebido = BigDecimal.ZERO;
        this.totalGeralDividendos = BigDecimal.ZERO;
        this.resultadoGeralLiquido = BigDecimal.ZERO;
        this.percentualRetornoGeral = BigDecimal.ZERO;
        this.numeroTotalOperacoes = 0;
        this.quantidadeInvestimentosComLucro = 0;
        this.quantidadeInvestimentosComPrejuizo = 0;
        this.quantidadeInvestimentosNeutros = 0;
        this.maiorLucroIndividual = BigDecimal.ZERO;
        this.maiorPrejuizoIndividual = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    
    public List<ResumoInvestimentoDTO> getInvestimentos() { return investimentos; }
    public void setInvestimentos(List<ResumoInvestimentoDTO> investimentos) { this.investimentos = investimentos; }
    
    public BigDecimal getTotalGeralInvestido() { return totalGeralInvestido; }
    public void setTotalGeralInvestido(BigDecimal totalGeralInvestido) { this.totalGeralInvestido = totalGeralInvestido; }
    
    public BigDecimal getTotalGeralRecebido() { return totalGeralRecebido; }
    public void setTotalGeralRecebido(BigDecimal totalGeralRecebido) { this.totalGeralRecebido = totalGeralRecebido; }
    
    public BigDecimal getTotalGeralDividendos() { return totalGeralDividendos; }
    public void setTotalGeralDividendos(BigDecimal totalGeralDividendos) { this.totalGeralDividendos = totalGeralDividendos; }
    
    public BigDecimal getResultadoGeralLiquido() { return resultadoGeralLiquido; }
    public void setResultadoGeralLiquido(BigDecimal resultadoGeralLiquido) { 
        this.resultadoGeralLiquido = resultadoGeralLiquido;
        // Atualizar situação automaticamente
        if (resultadoGeralLiquido.compareTo(BigDecimal.ZERO) > 0) {
            this.situacaoGeral = "LUCRO";
        } else if (resultadoGeralLiquido.compareTo(BigDecimal.ZERO) < 0) {
            this.situacaoGeral = "PREJUIZO";
        } else {
            this.situacaoGeral = "NEUTRO";
        }
    }
    
    public String getSituacaoGeral() { return situacaoGeral; }
    public void setSituacaoGeral(String situacaoGeral) { this.situacaoGeral = situacaoGeral; }
    
    public BigDecimal getPercentualRetornoGeral() { return percentualRetornoGeral; }
    public void setPercentualRetornoGeral(BigDecimal percentualRetornoGeral) { this.percentualRetornoGeral = percentualRetornoGeral; }
    
    public int getNumeroTotalOperacoes() { return numeroTotalOperacoes; }
    public void setNumeroTotalOperacoes(int numeroTotalOperacoes) { this.numeroTotalOperacoes = numeroTotalOperacoes; }
    
    public int getQuantidadeInvestimentosComLucro() { return quantidadeInvestimentosComLucro; }
    public void setQuantidadeInvestimentosComLucro(int quantidadeInvestimentosComLucro) { this.quantidadeInvestimentosComLucro = quantidadeInvestimentosComLucro; }
    
    public int getQuantidadeInvestimentosComPrejuizo() { return quantidadeInvestimentosComPrejuizo; }
    public void setQuantidadeInvestimentosComPrejuizo(int quantidadeInvestimentosComPrejuizo) { this.quantidadeInvestimentosComPrejuizo = quantidadeInvestimentosComPrejuizo; }
    
    public int getQuantidadeInvestimentosNeutros() { return quantidadeInvestimentosNeutros; }
    public void setQuantidadeInvestimentosNeutros(int quantidadeInvestimentosNeutros) { this.quantidadeInvestimentosNeutros = quantidadeInvestimentosNeutros; }
    
    public BigDecimal getMaiorLucroIndividual() { return maiorLucroIndividual; }
    public void setMaiorLucroIndividual(BigDecimal maiorLucroIndividual) { this.maiorLucroIndividual = maiorLucroIndividual; }
    
    public BigDecimal getMaiorPrejuizoIndividual() { return maiorPrejuizoIndividual; }
    public void setMaiorPrejuizoIndividual(BigDecimal maiorPrejuizoIndividual) { this.maiorPrejuizoIndividual = maiorPrejuizoIndividual; }
    
    public String getInvestimentoMaisRentavel() { return investimentoMaisRentavel; }
    public void setInvestimentoMaisRentavel(String investimentoMaisRentavel) { this.investimentoMaisRentavel = investimentoMaisRentavel; }
    
    public String getInvestimentoMenosRentavel() { return investimentoMenosRentavel; }
    public void setInvestimentoMenosRentavel(String investimentoMenosRentavel) { this.investimentoMenosRentavel = investimentoMenosRentavel; }
}