package com.example.demo.investimento.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class AdicionarRecomendacoesRequestDTO {
    
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotEmpty(message = "A lista de investimentos não pode estar vazia")
    private List<Long> investimentoIds;
    
    // Constructors
    public AdicionarRecomendacoesRequestDTO() {}
    
    public AdicionarRecomendacoesRequestDTO(Long usuarioId, List<Long> investimentoIds) {
        this.usuarioId = usuarioId;
        this.investimentoIds = investimentoIds;
    }
    
    // Getters and Setters
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public List<Long> getInvestimentoIds() {
        return investimentoIds;
    }
    
    public void setInvestimentoIds(List<Long> investimentoIds) {
        this.investimentoIds = investimentoIds;
    }
}
