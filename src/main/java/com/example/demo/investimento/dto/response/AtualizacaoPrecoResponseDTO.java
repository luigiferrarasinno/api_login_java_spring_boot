package com.example.demo.investimento.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AtualizacaoPrecoResponseDTO {
    
    private String mensagem;
    private Long investimentoId;
    private BigDecimal novoPreco;
    private LocalDateTime timestamp;
    
    public AtualizacaoPrecoResponseDTO() {}
    
    public AtualizacaoPrecoResponseDTO(String mensagem, Long investimentoId, BigDecimal novoPreco, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.investimentoId = investimentoId;
        this.novoPreco = novoPreco;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    
    public BigDecimal getNovoPreco() {
        return novoPreco;
    }
    
    public void setNovoPreco(BigDecimal novoPreco) {
        this.novoPreco = novoPreco;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}