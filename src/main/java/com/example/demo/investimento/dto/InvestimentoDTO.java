package com.example.demo.investimento.dto;

import com.example.demo.investimento.model.Investimento;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class InvestimentoDTO {

    private Long id;
    private String nome;
    private String simbolo;
    private String categoria;   // Usamos String para facilitar JSON, mas vem do Enum Categoria
    private Double precoBase;
    private Double precoAtual;
    private Double variacaoPercentual;
    private String descricao;
    private Set<Long> usuarioIds;  // todos os usuários relacionados
    private String data;          // ISO_LOCAL_DATE
    private String liquidez;      // novo campo
    private Double dividendYield;   // novo campo
    private Integer frequenciaDividendo; // novo campo
    private boolean ativo;        // novo campo
    private boolean visivelParaUsuarios; // flag de visibilidade controlada pelo admin
    private Long quantidadeTotal;     // quantidade total emitida
    private Long quantidadeDisponivel; // quantidade disponível para compra
    private String risco;         // do Enum Risco
    private String createdAt;     // ISO_LOCAL_DATE_TIME
    private String updatedAt;     // ISO_LOCAL_DATE_TIME
    private Boolean recomendadoParaVoce; // Null se usuário não tem recomendações, true/false caso contrário

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public InvestimentoDTO(Investimento investimento) {
        this(investimento, true); // Por padrão, inclui usuarioIds (para compatibilidade)
    }

    public InvestimentoDTO(Investimento investimento, boolean incluirUsuarioIds) {
        this.id = investimento.getId();
        this.nome = investimento.getNome();
        this.simbolo = investimento.getSimbolo();
        this.categoria = investimento.getCategoria() != null ? investimento.getCategoria().name() : null;
        this.precoBase = investimento.getPrecoBase() != null ? investimento.getPrecoBase().doubleValue() : null;
        this.precoAtual = investimento.getPrecoAtual() != null ? investimento.getPrecoAtual().doubleValue() : null;
        this.variacaoPercentual = investimento.getVariacaoPercentual() != null ? investimento.getVariacaoPercentual().doubleValue() : null;
        this.descricao = investimento.getDescricao();
        
        // Só inclui usuarioIds se for permitido
        if (incluirUsuarioIds) {
            this.usuarioIds = investimento.getUsuarios() != null ?
                investimento.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toSet())
                : null;
        } else {
            this.usuarioIds = null; // Oculta para usuários não-admin
        }
        
        this.data = investimento.getData() != null ? investimento.getData().format(DATE_FORMAT) : null;
        this.liquidez = investimento.getLiquidez();
        this.dividendYield = investimento.getDividendYield() != null ? investimento.getDividendYield().doubleValue() : null;
        this.frequenciaDividendo = investimento.getFrequenciaDividendo();
        this.ativo = investimento.isAtivo();
        this.visivelParaUsuarios = investimento.isVisivelParaUsuarios();
        this.risco = investimento.getRisco() != null ? investimento.getRisco().name() : null;
        this.quantidadeTotal = investimento.getQuantidadeTotal();
        this.quantidadeDisponivel = investimento.getQuantidadeDisponivel();
        this.createdAt = investimento.getCreatedAt() != null ? investimento.getCreatedAt().format(DATETIME_FORMAT) : null;
        this.updatedAt = investimento.getUpdatedAt() != null ? investimento.getUpdatedAt().format(DATETIME_FORMAT) : null;
        this.recomendadoParaVoce = null; // Será null se usuário não tem recomendações, ou true/false se tiver
    }

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public Double getPrecoBase() { return precoBase; }
    public void setPrecoBase(Double precoBase) { this.precoBase = precoBase; }

    public Double getPrecoAtual() { return precoAtual; }
    public void setPrecoAtual(Double precoAtual) { this.precoAtual = precoAtual; }

    public Double getVariacaoPercentual() { return variacaoPercentual; }
    public void setVariacaoPercentual(Double variacaoPercentual) { this.variacaoPercentual = variacaoPercentual; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Set<Long> getUsuarioIds() { return usuarioIds; }
    public void setUsuarioIds(Set<Long> usuarioIds) { this.usuarioIds = usuarioIds; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getLiquidez() { return liquidez; }
    public void setLiquidez(String liquidez) { this.liquidez = liquidez; }

    public Double getDividendYield() { return dividendYield; }
    public void setDividendYield(Double dividendYield) { this.dividendYield = dividendYield; }

    public Integer getFrequenciaDividendo() { return frequenciaDividendo; }
    public void setFrequenciaDividendo(Integer frequenciaDividendo) { this.frequenciaDividendo = frequenciaDividendo; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isVisivelParaUsuarios() { return visivelParaUsuarios; }
    public void setVisivelParaUsuarios(boolean visivelParaUsuarios) { this.visivelParaUsuarios = visivelParaUsuarios; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public Long getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(Long quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public Long getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(Long quantidadeDisponivel) { this.quantidadeDisponivel = quantidadeDisponivel; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getRecomendadoParaVoce() { return recomendadoParaVoce; }
    public void setRecomendadoParaVoce(Boolean recomendadoParaVoce) { this.recomendadoParaVoce = recomendadoParaVoce; }
}
