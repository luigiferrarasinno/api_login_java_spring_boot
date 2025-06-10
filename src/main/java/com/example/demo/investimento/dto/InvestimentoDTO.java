package com.example.demo.investimento.dto;

import com.example.demo.investimento.model.Investimento;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class InvestimentoDTO {

    private Long id;
    private String nome;
    private String categoria;   // Usamos String para facilitar JSON, mas vem do Enum Categoria
    private Double valor;
    private String descricao;
    private Set<Long> usuarioIds;  // todos os usuários relacionados
    private String data;          // ISO_LOCAL_DATE
    private String vencimento;    // ISO_LOCAL_DATE, novo campo
    private String liquidez;      // novo campo
    private Double taxaRetorno;   // novo campo
    private boolean ativo;        // novo campo
    private String risco;         // do Enum Risco
    private String createdAt;     // ISO_LOCAL_DATE_TIME
    private String updatedAt;     // ISO_LOCAL_DATE_TIME

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public InvestimentoDTO(Investimento investimento) {
        this.id = investimento.getId();
        this.nome = investimento.getNome();
        this.categoria = investimento.getCategoria() != null ? investimento.getCategoria().name() : null;
        this.valor = investimento.getValor() != null ? investimento.getValor().doubleValue() : null;
        this.descricao = investimento.getDescricao();
        this.usuarioIds = investimento.getUsuarios() != null ?
            investimento.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toSet())
            : null;
        this.data = investimento.getData() != null ? investimento.getData().format(DATE_FORMAT) : null;
        this.vencimento = investimento.getVencimento() != null ? investimento.getVencimento().format(DATE_FORMAT) : null;
        this.liquidez = investimento.getLiquidez();
        this.taxaRetorno = investimento.getTaxaRetorno() != null ? investimento.getTaxaRetorno().doubleValue() : null;
        this.ativo = investimento.isAtivo();
        this.risco = investimento.getRisco() != null ? investimento.getRisco().name() : null;
        this.createdAt = investimento.getCreatedAt() != null ? investimento.getCreatedAt().format(DATETIME_FORMAT) : null;
        this.updatedAt = investimento.getUpdatedAt() != null ? investimento.getUpdatedAt().format(DATETIME_FORMAT) : null;
    }

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Set<Long> getUsuarioIds() { return usuarioIds; }
    public void setUsuarioIds(Set<Long> usuarioIds) { this.usuarioIds = usuarioIds; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getVencimento() { return vencimento; }
    public void setVencimento(String vencimento) { this.vencimento = vencimento; }

    public String getLiquidez() { return liquidez; }
    public void setLiquidez(String liquidez) { this.liquidez = liquidez; }

    public Double getTaxaRetorno() { return taxaRetorno; }
    public void setTaxaRetorno(Double taxaRetorno) { this.taxaRetorno = taxaRetorno; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
