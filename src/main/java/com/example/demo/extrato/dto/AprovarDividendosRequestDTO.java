package com.example.demo.extrato.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para aprovação de dividendos pelo admin
 */
public class AprovarDividendosRequestDTO {

    @NotNull(message = "Lista de IDs de dividendos é obrigatória")
    private List<Long> dividendoIds;

    private String observacoes;

    // Constructors
    public AprovarDividendosRequestDTO() {}

    public AprovarDividendosRequestDTO(List<Long> dividendoIds, String observacoes) {
        this.dividendoIds = dividendoIds;
        this.observacoes = observacoes;
    }

    // Getters and Setters
    public List<Long> getDividendoIds() { return dividendoIds; }
    public void setDividendoIds(List<Long> dividendoIds) { this.dividendoIds = dividendoIds; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}