package com.example.demo.comentarios.dto.response;

import com.example.demo.comentarios.dto.ComentarioDTO;
import java.util.List;
import java.util.Map;

public class AdminComentariosResponseDTO {
    
    private Map<String, Object> filtros;
    private Integer totalComentarios;
    private List<ComentarioDTO> comentarios;
    
    public AdminComentariosResponseDTO() {}
    
    public AdminComentariosResponseDTO(Map<String, Object> filtros, Integer totalComentarios, List<ComentarioDTO> comentarios) {
        this.filtros = filtros;
        this.totalComentarios = totalComentarios;
        this.comentarios = comentarios;
    }
    
    // Getters e Setters
    public Map<String, Object> getFiltros() {
        return filtros;
    }
    
    public void setFiltros(Map<String, Object> filtros) {
        this.filtros = filtros;
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