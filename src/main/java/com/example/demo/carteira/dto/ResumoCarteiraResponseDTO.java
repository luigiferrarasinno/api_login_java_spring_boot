package com.example.demo.carteira.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumoCarteiraResponseDTO {
    
    private BigDecimal saldoDisponivel;
    private BigDecimal valorTotalInvestido;
    private BigDecimal valorAtualCarteira;
    private BigDecimal ganhoTotalCarteira;
    private BigDecimal percentualGanhoCarteira;
    private Integer quantidadePosicoes;
    private List<PosicaoCarteiraResponseDTO> posicoes;

    // Constructors
    public ResumoCarteiraResponseDTO() {}

    // Getters and Setters
    public BigDecimal getSaldoDisponivel() { return saldoDisponivel; }
    public void setSaldoDisponivel(BigDecimal saldoDisponivel) { this.saldoDisponivel = saldoDisponivel; }

    public BigDecimal getValorTotalInvestido() { return valorTotalInvestido; }
    public void setValorTotalInvestido(BigDecimal valorTotalInvestido) { this.valorTotalInvestido = valorTotalInvestido; }

    public BigDecimal getValorAtualCarteira() { return valorAtualCarteira; }
    public void setValorAtualCarteira(BigDecimal valorAtualCarteira) { this.valorAtualCarteira = valorAtualCarteira; }

    public BigDecimal getGanhoTotalCarteira() { return ganhoTotalCarteira; }
    public void setGanhoTotalCarteira(BigDecimal ganhoTotalCarteira) { this.ganhoTotalCarteira = ganhoTotalCarteira; }

    public BigDecimal getPercentualGanhoCarteira() { return percentualGanhoCarteira; }
    public void setPercentualGanhoCarteira(BigDecimal percentualGanhoCarteira) { this.percentualGanhoCarteira = percentualGanhoCarteira; }

    public Integer getQuantidadePosicoes() { return quantidadePosicoes; }
    public void setQuantidadePosicoes(Integer quantidadePosicoes) { this.quantidadePosicoes = quantidadePosicoes; }

    public List<PosicaoCarteiraResponseDTO> getPosicoes() { return posicoes; }
    public void setPosicoes(List<PosicaoCarteiraResponseDTO> posicoes) { this.posicoes = posicoes; }
}