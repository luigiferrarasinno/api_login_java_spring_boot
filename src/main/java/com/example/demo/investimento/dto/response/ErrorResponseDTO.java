package com.example.demo.investimento.dto.response;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    
    private String erro;
    private LocalDateTime timestamp;
    
    public ErrorResponseDTO() {}
    
    public ErrorResponseDTO(String erro, LocalDateTime timestamp) {
        this.erro = erro;
        this.timestamp = timestamp;
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
}