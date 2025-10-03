package com.example.demo.extrato.dto;

import com.example.demo.extrato.model.DividendoPendente;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * DTO para exibir dividendos pendentes para o admin
 */
public class DividendoPendenteDTO {
    
    private Long id;
    private String nomeUsuario;
    private String emailUsuario;
    private String nomeInvestimento;
    private String simboloInvestimento;
    private BigDecimal quantidadeAcoes;
    private BigDecimal dividendoPorAcao;
    private BigDecimal valorTotal;
    private String status;
    private String dataCalculo;
    private String observacoes;

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DividendoPendenteDTO(DividendoPendente dividendo) {
        this.id = dividendo.getId();
        this.nomeUsuario = dividendo.getUsuario().getNomeUsuario();
        this.emailUsuario = dividendo.getUsuario().getEmail();
        this.nomeInvestimento = dividendo.getInvestimento().getNome();
        this.simboloInvestimento = dividendo.getInvestimento().getSimbolo();
        this.quantidadeAcoes = dividendo.getQuantidadeAcoes();
        this.dividendoPorAcao = dividendo.getDividendoPorAcao();
        this.valorTotal = dividendo.getValorTotal();
        this.status = dividendo.getStatus().name();
        this.dataCalculo = dividendo.getDataCalculo().format(DATETIME_FORMAT);
        this.observacoes = dividendo.getObservacoes();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public String getNomeInvestimento() { return nomeInvestimento; }
    public void setNomeInvestimento(String nomeInvestimento) { this.nomeInvestimento = nomeInvestimento; }

    public String getSimboloInvestimento() { return simboloInvestimento; }
    public void setSimboloInvestimento(String simboloInvestimento) { this.simboloInvestimento = simboloInvestimento; }

    public BigDecimal getQuantidadeAcoes() { return quantidadeAcoes; }
    public void setQuantidadeAcoes(BigDecimal quantidadeAcoes) { this.quantidadeAcoes = quantidadeAcoes; }

    public BigDecimal getDividendoPorAcao() { return dividendoPorAcao; }
    public void setDividendoPorAcao(BigDecimal dividendoPorAcao) { this.dividendoPorAcao = dividendoPorAcao; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(String dataCalculo) { this.dataCalculo = dataCalculo; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}