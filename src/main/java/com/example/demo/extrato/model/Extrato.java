package com.example.demo.extrato.model;

import com.example.demo.user.model.Usuario;
import com.example.demo.investimento.model.Investimento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "extrato")
public class Extrato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id")
    private Investimento investimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacao tipoTransacao;

    @Column(name = "quantidade", precision = 15, scale = 6)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", precision = 15, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "valor_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "saldo_anterior", precision = 15, scale = 2)
    private BigDecimal saldoAnterior;

    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime dataTransacao;

    // Constructors
    public Extrato() {
        this.dataTransacao = LocalDateTime.now();
    }

    public Extrato(Usuario usuario, TipoTransacao tipoTransacao, BigDecimal valorTotal, 
                   BigDecimal saldoAnterior, BigDecimal saldoAtual, String descricao) {
        this();
        this.usuario = usuario;
        this.tipoTransacao = tipoTransacao;
        this.valorTotal = valorTotal;
        this.saldoAnterior = saldoAnterior;
        this.saldoAtual = saldoAtual;
        this.descricao = descricao;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Investimento getInvestimento() { return investimento; }
    public void setInvestimento(Investimento investimento) { this.investimento = investimento; }

    public TipoTransacao getTipoTransacao() { return tipoTransacao; }
    public void setTipoTransacao(TipoTransacao tipoTransacao) { this.tipoTransacao = tipoTransacao; }

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