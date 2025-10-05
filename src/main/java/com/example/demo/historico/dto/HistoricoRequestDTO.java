package com.example.demo.historico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.YearMonth;

public class HistoricoRequestDTO {
    
    @NotNull(message = "ID do investimento é obrigatório")
    private Long investimentoId;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotNull(message = "Total investido é obrigatório")
    @PositiveOrZero(message = "Total investido deve ser maior ou igual a zero")
    private BigDecimal totalInvestido;
    
    @NotNull(message = "Total retornando é obrigatório")
    private BigDecimal totalRetornando;
    
    @NotNull(message = "Mês/Ano de registro é obrigatório")
    private YearMonth mesAnoRegistro;
    
    // Construtores
    public HistoricoRequestDTO() {}
    
    public HistoricoRequestDTO(Long investimentoId, Long usuarioId, BigDecimal totalInvestido, 
                              BigDecimal totalRetornando, YearMonth mesAnoRegistro) {
        this.investimentoId = investimentoId;
        this.usuarioId = usuarioId;
        this.totalInvestido = totalInvestido;
        this.totalRetornando = totalRetornando;
        this.mesAnoRegistro = mesAnoRegistro;
    }
    
    // Getters e Setters
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
}