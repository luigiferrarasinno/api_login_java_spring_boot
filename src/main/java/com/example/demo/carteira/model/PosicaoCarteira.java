package com.example.demo.carteira.model;

import com.example.demo.user.model.Usuario;
import com.example.demo.investimento.model.Investimento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "posicao_carteira", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "investimento_id"}))
public class PosicaoCarteira {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id", nullable = false)
    private Investimento investimento;

    @Column(name = "quantidade_total", precision = 15, scale = 6, nullable = false)
    private BigDecimal quantidadeTotal = BigDecimal.ZERO;

    @Column(name = "preco_medio", precision = 15, scale = 2)
    private BigDecimal precoMedio = BigDecimal.ZERO;

    @Column(name = "valor_investido", precision = 15, scale = 2)
    private BigDecimal valorInvestido = BigDecimal.ZERO;

    @Column(name = "data_primeira_compra")
    private LocalDateTime dataPrimeiraCompra;

    @Column(name = "data_ultima_movimentacao")
    private LocalDateTime dataUltimaMovimentacao;

    // Constructors
    public PosicaoCarteira() {}

    public PosicaoCarteira(Usuario usuario, Investimento investimento) {
        this.usuario = usuario;
        this.investimento = investimento;
        this.dataPrimeiraCompra = LocalDateTime.now();
        this.dataUltimaMovimentacao = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Investimento getInvestimento() { return investimento; }
    public void setInvestimento(Investimento investimento) { this.investimento = investimento; }

    public BigDecimal getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(BigDecimal quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public BigDecimal getPrecoMedio() { return precoMedio; }
    public void setPrecoMedio(BigDecimal precoMedio) { this.precoMedio = precoMedio; }

    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }

    public LocalDateTime getDataPrimeiraCompra() { return dataPrimeiraCompra; }
    public void setDataPrimeiraCompra(LocalDateTime dataPrimeiraCompra) { this.dataPrimeiraCompra = dataPrimeiraCompra; }

    public LocalDateTime getDataUltimaMovimentacao() { return dataUltimaMovimentacao; }
    public void setDataUltimaMovimentacao(LocalDateTime dataUltimaMovimentacao) { this.dataUltimaMovimentacao = dataUltimaMovimentacao; }

    // Métodos utilitários
    public void adicionarCompra(BigDecimal quantidade, BigDecimal precoUnitario) {
        BigDecimal valorCompra = quantidade.multiply(precoUnitario);
        
        if (this.quantidadeTotal.equals(BigDecimal.ZERO)) {
            // Primeira compra
            this.dataPrimeiraCompra = LocalDateTime.now();
            this.precoMedio = precoUnitario;
        } else {
            // Recalcular preço médio
            BigDecimal valorTotalAnterior = this.quantidadeTotal.multiply(this.precoMedio);
            BigDecimal novoValorTotal = valorTotalAnterior.add(valorCompra);
            BigDecimal novaQuantidadeTotal = this.quantidadeTotal.add(quantidade);
            this.precoMedio = novoValorTotal.divide(novaQuantidadeTotal, 2, java.math.RoundingMode.HALF_UP);
        }
        
        this.quantidadeTotal = this.quantidadeTotal.add(quantidade);
        this.valorInvestido = this.valorInvestido.add(valorCompra);
        this.dataUltimaMovimentacao = LocalDateTime.now();
    }

    public void removerVenda(BigDecimal quantidade) {
        if (quantidade.compareTo(this.quantidadeTotal) > 0) {
            throw new IllegalArgumentException("Quantidade de venda não pode ser maior que a posição atual");
        }
        
        this.quantidadeTotal = this.quantidadeTotal.subtract(quantidade);
        this.dataUltimaMovimentacao = LocalDateTime.now();
        
        // Se zerou a posição, reseta os valores
        if (this.quantidadeTotal.equals(BigDecimal.ZERO)) {
            this.precoMedio = BigDecimal.ZERO;
            this.valorInvestido = BigDecimal.ZERO;
        }
    }
}