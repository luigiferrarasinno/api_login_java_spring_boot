package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class OperacaoErroResponseDTO {
    
    private String erro;
    private LocalDateTime timestamp;
    private String status;
    
    public OperacaoErroResponseDTO() {}
    
    public OperacaoErroResponseDTO(String erro, LocalDateTime timestamp, String status) {
        this.erro = erro;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    // Getters e Setters
    public String getErro() {
        return erro;
    }
    
    public void setErro(String erro) {
        this.erro = erro;
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