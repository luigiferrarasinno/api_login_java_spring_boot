package com.example.demo.extrato.dto.response;

import java.time.LocalDateTime;

public class LiberacaoTodosDividendosResponseDTO {
    
    private String mensagem;
    private String criacao;
    private String pagamento;
    private Integer totalDividendosProcessados;
    private LocalDateTime timestamp;
    
    public LiberacaoTodosDividendosResponseDTO() {}
    
    public LiberacaoTodosDividendosResponseDTO(String mensagem, String criacao, String pagamento, Integer totalDividendosProcessados, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.criacao = criacao;
        this.pagamento = pagamento;
        this.totalDividendosProcessados = totalDividendosProcessados;
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
    
    public Integer getTotalDividendosProcessados() {
        return totalDividendosProcessados;
    }
    
    public void setTotalDividendosProcessados(Integer totalDividendosProcessados) {
        this.totalDividendosProcessados = totalDividendosProcessados;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}