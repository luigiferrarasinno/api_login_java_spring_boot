package com.example.demo.comentarios.dto.response;

import com.example.demo.comentarios.dto.ComentarioDTO;
import java.util.List;

public class ComentariosPorInvestimentoResponseDTO {
    
    private Long investimentoId;
    private Integer totalComentarios;
    private List<ComentarioDTO> comentarios;
    
    public ComentariosPorInvestimentoResponseDTO() {}
    
    public ComentariosPorInvestimentoResponseDTO(Long investimentoId, Integer totalComentarios, List<ComentarioDTO> comentarios) {
        this.investimentoId = investimentoId;
        this.totalComentarios = totalComentarios;
        this.comentarios = comentarios;
    }
    
    // Getters e Setters
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
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