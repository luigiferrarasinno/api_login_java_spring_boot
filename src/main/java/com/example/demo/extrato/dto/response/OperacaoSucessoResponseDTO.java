package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class OperacaoSucessoResponseDTO {
    
    private String mensagem;
    private LocalDateTime timestamp;
    private String status;
    
    public OperacaoSucessoResponseDTO() {}
    
    public OperacaoSucessoResponseDTO(String mensagem, LocalDateTime timestamp, String status) {
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.status = status;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}