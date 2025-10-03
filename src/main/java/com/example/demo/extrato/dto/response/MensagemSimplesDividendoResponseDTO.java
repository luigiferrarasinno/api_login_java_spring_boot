package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class MensagemSimplesDividendoResponseDTO {
    
    private String mensagem;
    private Long investimentoId;
    private LocalDateTime timestamp;
    
    public MensagemSimplesDividendoResponseDTO() {}
    
    public MensagemSimplesDividendoResponseDTO(String mensagem, Long investimentoId, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.investimentoId = investimentoId;
        this.timestamp = timestamp;
    }
    
    // Constructor para quando não tem investimentoId específico
    public MensagemSimplesDividendoResponseDTO(String mensagem, LocalDateTime timestamp) {
        this.mensagem = mensagem;
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
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}