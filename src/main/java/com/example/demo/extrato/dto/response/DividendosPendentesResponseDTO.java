package com.example.demo.extrato.dto.response;

import com.example.demo.extrato.dto.DividendoPendenteDTO;
import java.time.LocalDateTime;
import java.util.List;

public class DividendosPendentesResponseDTO {
    
    private Integer totalPendentes;
    private List<DividendoPendenteDTO> dividendos;
    private LocalDateTime timestamp;
    
    public DividendosPendentesResponseDTO() {}
    
    public DividendosPendentesResponseDTO(Integer totalPendentes, List<DividendoPendenteDTO> dividendos, LocalDateTime timestamp) {
        this.totalPendentes = totalPendentes;
        this.dividendos = dividendos;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public Integer getTotalPendentes() {
        return totalPendentes;
    }
    
    public void setTotalPendentes(Integer totalPendentes) {
        this.totalPendentes = totalPendentes;
    }
    
    public List<DividendoPendenteDTO> getDividendos() {
        return dividendos;
    }
    
    public void setDividendos(List<DividendoPendenteDTO> dividendos) {
        this.dividendos = dividendos;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}