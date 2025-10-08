package com.example.demo.feller.dto;

import java.util.List;

/**
 * DTO para resposta da montagem automática de carteira recomendada
 */
public class MontarCarteiraRecomendadaResponseDTO {

    private List<Long> investimentosRecomendados;
    private String mensagem;
    private Integer quantidadeRecomendacoes;

    public MontarCarteiraRecomendadaResponseDTO() {
    }

    public MontarCarteiraRecomendadaResponseDTO(List<Long> investimentosRecomendados, String mensagem) {
        this.investimentosRecomendados = investimentosRecomendados;
        this.mensagem = mensagem;
        this.quantidadeRecomendacoes = investimentosRecomendados != null ? investimentosRecomendados.size() : 0;
    }

    public List<Long> getInvestimentosRecomendados() {
        return investimentosRecomendados;
    }

    public void setInvestimentosRecomendados(List<Long> investimentosRecomendados) {
        this.investimentosRecomendados = investimentosRecomendados;
        this.quantidadeRecomendacoes = investimentosRecomendados != null ? investimentosRecomendados.size() : 0;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getQuantidadeRecomendacoes() {
        return quantidadeRecomendacoes;
    }

    public void setQuantidadeRecomendacoes(Integer quantidadeRecomendacoes) {
        this.quantidadeRecomendacoes = quantidadeRecomendacoes;
    }
}
