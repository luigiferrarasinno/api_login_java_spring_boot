package com.example.demo.playlist.dto.request;

import jakarta.validation.constraints.NotNull;

public class AdicionarInvestimentoRequestDTO {

    @NotNull(message = "ID do investimento é obrigatório")
    private Long investimentoId;

    public AdicionarInvestimentoRequestDTO() {}

    public AdicionarInvestimentoRequestDTO(Long investimentoId) {
        this.investimentoId = investimentoId;
    }

    public Long getInvestimentoId() {
        return investimentoId;
    }

    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
}