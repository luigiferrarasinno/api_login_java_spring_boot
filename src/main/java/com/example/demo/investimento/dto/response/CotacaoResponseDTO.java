package com.example.demo.investimento.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CotacaoResponseDTO {
    
    private Long investimentoId;
    private BigDecimal precoAtual;
    private LocalDateTime timestamp;
    
    public CotacaoResponseDTO() {}
    
    public CotacaoResponseDTO(Long investimentoId, BigDecimal precoAtual, LocalDateTime timestamp) {
        this.investimentoId = investimentoId;
        this.precoAtual = precoAtual;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    
    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }
    
    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}