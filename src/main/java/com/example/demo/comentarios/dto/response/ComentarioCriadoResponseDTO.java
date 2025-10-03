package com.example.demo.comentarios.dto.response;

import com.example.demo.comentarios.dto.ComentarioDTO;
import java.time.LocalDateTime;

public class ComentarioCriadoResponseDTO {
    
    private String mensagem;
    private ComentarioDTO comentario;
    private LocalDateTime timestamp;
    
    public ComentarioCriadoResponseDTO() {}
    
    public ComentarioCriadoResponseDTO(String mensagem, ComentarioDTO comentario, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.comentario = comentario;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public ComentarioDTO getComentario() {
        return comentario;
    }
    
    public void setComentario(ComentarioDTO comentario) {
        this.comentario = comentario;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}