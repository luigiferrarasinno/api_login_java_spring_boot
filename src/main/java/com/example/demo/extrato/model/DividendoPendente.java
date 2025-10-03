package com.example.demo.extrato.model;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa um dividendo PENDENTE de aprovação administrativa
 */
@Entity
@Table(name = "dividendo_pendente")
public class DividendoPendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id", nullable = false)
    private Investimento investimento;

    @Column(name = "quantidade_acoes", nullable = false)
    private BigDecimal quantidadeAcoes;

    @Column(name = "dividendo_por_acao", nullable = false, precision = 10, scale = 4)
    private BigDecimal dividendoPorAcao;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_calculo", nullable = false)
    private LocalDateTime dataCalculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusDividendo status;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "aprovado_por_admin_id")
    private Long aprovadoPorAdminId;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    // Constructors
    public DividendoPendente() {}

    public DividendoPendente(Usuario usuario, Investimento investimento, 
                           BigDecimal quantidadeAcoes, BigDecimal dividendoPorAcao, 
                           BigDecimal valorTotal) {
        this.usuario = usuario;
        this.investimento = investimento;
        this.quantidadeAcoes = quantidadeAcoes;
        this.dividendoPorAcao = dividendoPorAcao;
        this.valorTotal = valorTotal;
        this.dataCalculo = LocalDateTime.now();
        this.status = StatusDividendo.PENDENTE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Investimento getInvestimento() { return investimento; }
    public void setInvestimento(Investimento investimento) { this.investimento = investimento; }

    public BigDecimal getQuantidadeAcoes() { return quantidadeAcoes; }
    public void setQuantidadeAcoes(BigDecimal quantidadeAcoes) { this.quantidadeAcoes = quantidadeAcoes; }

    public BigDecimal getDividendoPorAcao() { return dividendoPorAcao; }
    public void setDividendoPorAcao(BigDecimal dividendoPorAcao) { this.dividendoPorAcao = dividendoPorAcao; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }

    public StatusDividendo getStatus() { return status; }
    public void setStatus(StatusDividendo status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Long getAprovadoPorAdminId() { return aprovadoPorAdminId; }
    public void setAprovadoPorAdminId(Long aprovadoPorAdminId) { this.aprovadoPorAdminId = aprovadoPorAdminId; }

    public LocalDateTime getDataAprovacao() { return dataAprovacao; }
    public void setDataAprovacao(LocalDateTime dataAprovacao) { this.dataAprovacao = dataAprovacao; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    // Enum for status
    public enum StatusDividendo {
        PENDENTE,      // Aguardando aprovação do admin
        APROVADO,      // Aprovado pelo admin, ainda não pago
        PAGO,          // Já foi creditado para o usuário
        REJEITADO,     // Rejeitado pelo admin
        CANCELADO      // Cancelado por algum motivo
    }
}