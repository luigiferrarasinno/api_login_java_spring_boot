package com.example.demo.investimento.dto.response;

import java.time.LocalDateTime;

public class AtualizacaoTodosPrecoResponseDTO {
    
    private String mensagem;
    private LocalDateTime timestamp;
    
    public AtualizacaoTodosPrecoResponseDTO() {}
    
    public AtualizacaoTodosPrecoResponseDTO(String mensagem, LocalDateTime timestamp) {
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
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}