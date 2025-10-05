package com.example.demo.historico.dto;

import java.util.List;

public class HistoricoComResumoDTO {
    
    private List<HistoricoResponseDTO> historicos;
    private ResumoHistoricoDTO resumo;
    
    // Construtores
    public HistoricoComResumoDTO() {}
    
    public HistoricoComResumoDTO(List<HistoricoResponseDTO> historicos, ResumoHistoricoDTO resumo) {
        this.historicos = historicos;
        this.resumo = resumo;
    }
    
    // Getters e Setters
    public List<HistoricoResponseDTO> getHistoricos() {
        return historicos;
    }
    
    public void setHistoricos(List<HistoricoResponseDTO> historicos) {
        this.historicos = historicos;
    }
    
    public ResumoHistoricoDTO getResumo() {
        return resumo;
    }
    
    public void setResumo(ResumoHistoricoDTO resumo) {
        this.resumo = resumo;
    }
}