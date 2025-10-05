package com.example.demo.historico.model;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "historico")
public class Historico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id", nullable = false)
    @NotNull
    private Investimento investimento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    @Column(name = "total_investido", precision = 15, scale = 2)
    @PositiveOrZero
    @NotNull
    private BigDecimal totalInvestido;
    
    @Column(name = "total_retornando", precision = 15, scale = 2)
    @NotNull
    private BigDecimal totalRetornando;
    
    @Column(name = "mes_ano_registro")
    @NotNull
    private YearMonth mesAnoRegistro;
    
    @Column(name = "data_registro")
    @NotNull
    private LocalDate dataRegistro;
    
    // Construtores
    public Historico() {
        this.dataRegistro = LocalDate.now();
    }
    
    public Historico(Investimento investimento, Usuario usuario, BigDecimal totalInvestido, 
                    BigDecimal totalRetornando, YearMonth mesAnoRegistro) {
        this.investimento = investimento;
        this.usuario = usuario;
        this.totalInvestido = totalInvestido;
        this.totalRetornando = totalRetornando;
        this.mesAnoRegistro = mesAnoRegistro;
        this.dataRegistro = LocalDate.now();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Investimento getInvestimento() {
        return investimento;
    }
    
    public void setInvestimento(Investimento investimento) {
        this.investimento = investimento;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public BigDecimal getTotalInvestido() {
        return totalInvestido;
    }
    
    public void setTotalInvestido(BigDecimal totalInvestido) {
        this.totalInvestido = totalInvestido;
    }
    
    public BigDecimal getTotalRetornando() {
        return totalRetornando;
    }
    
    public void setTotalRetornando(BigDecimal totalRetornando) {
        this.totalRetornando = totalRetornando;
    }
    
    public YearMonth getMesAnoRegistro() {
        return mesAnoRegistro;
    }
    
    public void setMesAnoRegistro(YearMonth mesAnoRegistro) {
        this.mesAnoRegistro = mesAnoRegistro;
    }
    
    public LocalDate getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    // Método utilitário para calcular o retorno
    public BigDecimal calcularRetorno() {
        if (totalInvestido != null && totalRetornando != null && totalInvestido.compareTo(BigDecimal.ZERO) > 0) {
            return totalRetornando.subtract(totalInvestido);
        }
        return BigDecimal.ZERO;
    }
    
    // Método utilitário para calcular o percentual de retorno
    public BigDecimal calcularPercentualRetorno() {
        if (totalInvestido != null && totalRetornando != null && totalInvestido.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal retorno = totalRetornando.subtract(totalInvestido);
            return retorno.divide(totalInvestido, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
        return BigDecimal.ZERO;
    }
}
