package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class DistribuicaoDividendosResponseDTO {
    
    private String mensagem;
    private LocalDateTime timestamp;
    private String status;
    private String tipo;
    
    public DistribuicaoDividendosResponseDTO() {}
    
    public DistribuicaoDividendosResponseDTO(String mensagem, LocalDateTime timestamp, String status, String tipo) {
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.status = status;
        this.tipo = tipo;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}