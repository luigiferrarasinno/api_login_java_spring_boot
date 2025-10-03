package com.example.demo.extrato.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DepositarRequestDTO {
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    // Constructors
    public DepositarRequestDTO() {}

    public DepositarRequestDTO(BigDecimal valor) {
        this.valor = valor;
    }

    // Getters and Setters
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}