package com.example.demo.extrato.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para compra de ações pelo preço de mercado atual (RECOMENDADO)
 * SISTEMA BRASILEIRO: Apenas números inteiros são permitidos
 */
public class ComprarAcaoMercadoRequestDTO {
    
    @NotNull(message = "ID do investimento é obrigatório")
    private Long investimentoId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1 ação (sistema brasileiro não permite frações)")
    private Integer quantidade;

    // Constructors
    public ComprarAcaoMercadoRequestDTO() {}

    public ComprarAcaoMercadoRequestDTO(Long investimentoId, Integer quantidade) {
        this.investimentoId = investimentoId;
        this.quantidade = quantidade;
    }

    // Getters and Setters
    public Long getInvestimentoId() { return investimentoId; }
    public void setInvestimentoId(Long investimentoId) { this.investimentoId = investimentoId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}