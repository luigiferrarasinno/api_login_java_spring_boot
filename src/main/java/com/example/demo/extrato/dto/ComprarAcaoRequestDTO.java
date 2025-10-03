package com.example.demo.extrato.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ComprarAcaoRequestDTO {
    
    @NotNull(message = "ID do investimento é obrigatório")
    private Long investimentoId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.000001", message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;
    
    @NotNull(message = "Preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço unitário deve ser maior que zero")
    private BigDecimal precoUnitario;

    // Constructors
    public ComprarAcaoRequestDTO() {}

    public ComprarAcaoRequestDTO(Long investimentoId, BigDecimal quantidade, BigDecimal precoUnitario) {
        this.investimentoId = investimentoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters and Setters
    public Long getInvestimentoId() { return investimentoId; }
    public void setInvestimentoId(Long investimentoId) { this.investimentoId = investimentoId; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}