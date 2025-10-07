package com.example.demo.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PosicaoCarteiraResponseDTO {
    
    private Long id;
    private String nomeInvestimento;
    private String simboloInvestimento;
    private String categoria;
    private String risco;
    private BigDecimal quantidadeTotal;
    private BigDecimal precoMedio;
    private BigDecimal valorInvestido;
    private BigDecimal precoAtual;
    private BigDecimal valorAtual;
    private BigDecimal ganhoPerda;
    private BigDecimal percentualGanhoPerda;
    private BigDecimal totalDividendosRecebidos;  // Total de dividendos recebidos desta posição
    private LocalDateTime dataPrimeiraCompra;
    private LocalDateTime dataUltimaMovimentacao;
    private Boolean recomendadoParaVoce;  // Null se usuário não tem recomendações, true/false caso contrário

    // Constructors
    public PosicaoCarteiraResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeInvestimento() { return nomeInvestimento; }
    public void setNomeInvestimento(String nomeInvestimento) { this.nomeInvestimento = nomeInvestimento; }

    public String getSimboloInvestimento() { return simboloInvestimento; }
    public void setSimboloInvestimento(String simboloInvestimento) { this.simboloInvestimento = simboloInvestimento; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public BigDecimal getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(BigDecimal quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public BigDecimal getPrecoMedio() { return precoMedio; }
    public void setPrecoMedio(BigDecimal precoMedio) { this.precoMedio = precoMedio; }

    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }

    public BigDecimal getPrecoAtual() { return precoAtual; }
    public void setPrecoAtual(BigDecimal precoAtual) { this.precoAtual = precoAtual; }

    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }

    public BigDecimal getGanhoPerda() { return ganhoPerda; }
    public void setGanhoPerda(BigDecimal ganhoPerda) { this.ganhoPerda = ganhoPerda; }

    public BigDecimal getPercentualGanhoPerda() { return percentualGanhoPerda; }
    public void setPercentualGanhoPerda(BigDecimal percentualGanhoPerda) { this.percentualGanhoPerda = percentualGanhoPerda; }

    public BigDecimal getTotalDividendosRecebidos() { return totalDividendosRecebidos; }
    public void setTotalDividendosRecebidos(BigDecimal totalDividendosRecebidos) { this.totalDividendosRecebidos = totalDividendosRecebidos; }

    public LocalDateTime getDataPrimeiraCompra() { return dataPrimeiraCompra; }
    public void setDataPrimeiraCompra(LocalDateTime dataPrimeiraCompra) { this.dataPrimeiraCompra = dataPrimeiraCompra; }

    public LocalDateTime getDataUltimaMovimentacao() { return dataUltimaMovimentacao; }
    public void setDataUltimaMovimentacao(LocalDateTime dataUltimaMovimentacao) { this.dataUltimaMovimentacao = dataUltimaMovimentacao; }

    public Boolean getRecomendadoParaVoce() { return recomendadoParaVoce; }
    public void setRecomendadoParaVoce(Boolean recomendadoParaVoce) { this.recomendadoParaVoce = recomendadoParaVoce; }
}