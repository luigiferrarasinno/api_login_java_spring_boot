package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class StatusSistemaResponseDTO {
    
    private Boolean sistemaAtivo;
    private String fluxo;
    private Long dividendosPendentes;
    private String modoOperacao;
    private Map<String, String> endpoints;
    private LocalDateTime timestamp;
    
    public StatusSistemaResponseDTO() {}
    
    public StatusSistemaResponseDTO(Boolean sistemaAtivo, String fluxo, Long dividendosPendentes, String modoOperacao, Map<String, String> endpoints, LocalDateTime timestamp) {
        this.sistemaAtivo = sistemaAtivo;
        this.fluxo = fluxo;
        this.dividendosPendentes = dividendosPendentes;
        this.modoOperacao = modoOperacao;
        this.endpoints = endpoints;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public Boolean getSistemaAtivo() {
        return sistemaAtivo;
    }
    
    public void setSistemaAtivo(Boolean sistemaAtivo) {
        this.sistemaAtivo = sistemaAtivo;
    }
    
    public String getFluxo() {
        return fluxo;
    }
    
    public void setFluxo(String fluxo) {
        this.fluxo = fluxo;
    }
    
    public Long getDividendosPendentes() {
        return dividendosPendentes;
    }
    
    public void setDividendosPendentes(Long dividendosPendentes) {
        this.dividendosPendentes = dividendosPendentes;
    }
    
    public String getModoOperacao() {
        return modoOperacao;
    }
    
    public void setModoOperacao(String modoOperacao) {
        this.modoOperacao = modoOperacao;
    }
    
    public Map<String, String> getEndpoints() {
        return endpoints;
    }
    
    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}