package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class LiberacaoInvestimentoResponseDTO {
    
    private String mensagem;
    private String criacao;
    private String pagamento;
    private Integer dividendosProcessados;
    private LocalDateTime timestamp;
    
    public LiberacaoInvestimentoResponseDTO() {}
    
    public LiberacaoInvestimentoResponseDTO(String mensagem, String criacao, String pagamento, Integer dividendosProcessados, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.criacao = criacao;
        this.pagamento = pagamento;
        this.dividendosProcessados = dividendosProcessados;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getCriacao() {
        return criacao;
    }
    
    public void setCriacao(String criacao) {
        this.criacao = criacao;
    }
    
    public String getPagamento() {
        return pagamento;
    }
    
    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }
    
    public Integer getDividendosProcessados() {
        return dividendosProcessados;
    }
    
    public void setDividendosProcessados(Integer dividendosProcessados) {
        this.dividendosProcessados = dividendosProcessados;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}