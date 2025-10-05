package com.example.demo.historico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class HistoricoResponseDTO {
    
    private Long id;
    private Long investimentoId;
    private String nomeInvestimento;
    private String simboloInvestimento;
    private Long usuarioId;
    private String nomeUsuario;
    private BigDecimal totalInvestido;
    private BigDecimal totalRetornando;
    private BigDecimal retorno;
    private BigDecimal percentualRetorno;
    private String lucroPrejuizo; // "LUCRO" ou "PREJUIZO"
    private YearMonth mesAnoRegistro;
    private LocalDate dataRegistro;
    
    // Construtores
    public HistoricoResponseDTO() {}
    
    public HistoricoResponseDTO(Long id, Long investimentoId, String nomeInvestimento, 
                               String simboloInvestimento, Long usuarioId, String nomeUsuario,
                               BigDecimal totalInvestido, BigDecimal totalRetornando, 
                               BigDecimal retorno, BigDecimal percentualRetorno, String lucroPrejuizo,
                               YearMonth mesAnoRegistro, LocalDate dataRegistro) {
        this.id = id;
        this.investimentoId = investimentoId;
        this.nomeInvestimento = nomeInvestimento;
        this.simboloInvestimento = simboloInvestimento;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.totalInvestido = totalInvestido;
        this.totalRetornando = totalRetornando;
        this.retorno = retorno;
        this.percentualRetorno = percentualRetorno;
        this.lucroPrejuizo = lucroPrejuizo;
        this.mesAnoRegistro = mesAnoRegistro;
        this.dataRegistro = dataRegistro;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getInvestimentoId() {
        return investimentoId;
    }
    
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    
    public String getNomeInvestimento() {
        return nomeInvestimento;
    }
    
    public void setNomeInvestimento(String nomeInvestimento) {
        this.nomeInvestimento = nomeInvestimento;
    }
    
    public String getSimboloInvestimento() {
        return simboloInvestimento;
    }
    
    public void setSimboloInvestimento(String simboloInvestimento) {
        this.simboloInvestimento = simboloInvestimento;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public BigDecimal getTotalInvestido() {
        return totalInvestido;
    }
    
    public void setTotalInvestido(BigDecimal totalInvestido) {
        this.totalInvestido = totalInvestido;
    }
    
    public BigDecimal getTotalRetornando() {
        return totalRetornando;
    }
    
    public void setTotalRetornando(BigDecimal totalRetornando) {
        this.totalRetornando = totalRetornando;
    }
    
    public BigDecimal getRetorno() {
        return retorno;
    }
    
    public void setRetorno(BigDecimal retorno) {
        this.retorno = retorno;
    }
    
    public BigDecimal getPercentualRetorno() {
        return percentualRetorno;
    }
    
    public void setPercentualRetorno(BigDecimal percentualRetorno) {
        this.percentualRetorno = percentualRetorno;
    }
    
    public String getLucroPrejuizo() {
        return lucroPrejuizo;
    }
    
    public void setLucroPrejuizo(String lucroPrejuizo) {
        this.lucroPrejuizo = lucroPrejuizo;
    }
    
    public YearMonth getMesAnoRegistro() {
        return mesAnoRegistro;
    }
    
    public void setMesAnoRegistro(YearMonth mesAnoRegistro) {
        this.mesAnoRegistro = mesAnoRegistro;
    }
    
    public LocalDate getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}