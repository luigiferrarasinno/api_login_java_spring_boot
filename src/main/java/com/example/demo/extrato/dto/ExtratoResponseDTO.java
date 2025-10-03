package com.example.demo.extrato.dto;

import com.example.demo.extrato.model.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtratoResponseDTO {
    
    private Long id;
    private String nomeInvestimento;
    private String simboloInvestimento;
    private TipoTransacao tipoTransacao;
    private String descricaoTransacao;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoAtual;
    private String descricao;
    private LocalDateTime dataTransacao;

    // Constructors
    public ExtratoResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeInvestimento() { return nomeInvestimento; }
    public void setNomeInvestimento(String nomeInvestimento) { this.nomeInvestimento = nomeInvestimento; }

    public String getSimboloInvestimento() { return simboloInvestimento; }
    public void setSimboloInvestimento(String simboloInvestimento) { this.simboloInvestimento = simboloInvestimento; }

    public TipoTransacao getTipoTransacao() { return tipoTransacao; }
    public void setTipoTransacao(TipoTransacao tipoTransacao) { this.tipoTransacao = tipoTransacao; }

    public String getDescricaoTransacao() { return descricaoTransacao; }
    public void setDescricaoTransacao(String descricaoTransacao) { this.descricaoTransacao = descricaoTransacao; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(BigDecimal saldoAnterior) { this.saldoAnterior = saldoAnterior; }

    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDateTime dataTransacao) { this.dataTransacao = dataTransacao; }
}