package com.example.demo.comentarios.dto.response;

import java.time.LocalDateTime;

public class ComentarioExcluidoResponseDTO {
    
    private String mensagem;
    private Long comentarioId;
    private LocalDateTime timestamp;
    
    public ComentarioExcluidoResponseDTO() {}
    
    public ComentarioExcluidoResponseDTO(String mensagem, Long comentarioId, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.comentarioId = comentarioId;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public Long getComentarioId() {
        return comentarioId;
    }
    
    public void setComentarioId(Long comentarioId) {
        this.comentarioId = comentarioId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}