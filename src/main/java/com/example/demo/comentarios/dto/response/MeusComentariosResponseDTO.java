package com.example.demo.comentarios.dto.response;

import com.example.demo.comentarios.dto.ComentarioDTO;
import java.util.List;

public class MeusComentariosResponseDTO {
    
    private String usuario;
    private Integer totalComentarios;
    private List<ComentarioDTO> comentarios;
    
    public MeusComentariosResponseDTO() {}
    
    public MeusComentariosResponseDTO(String usuario, Integer totalComentarios, List<ComentarioDTO> comentarios) {
        this.usuario = usuario;
        this.totalComentarios = totalComentarios;
        this.comentarios = comentarios;
    }
    
    // Getters e Setters
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public Integer getTotalComentarios() {
        return totalComentarios;
    }
    
    public void setTotalComentarios(Integer totalComentarios) {
        this.totalComentarios = totalComentarios;
    }
    
    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }
    
    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }
}