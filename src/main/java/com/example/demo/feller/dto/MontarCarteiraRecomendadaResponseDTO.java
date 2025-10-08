package com.example.demo.feller.dto;

import java.util.List;

/**
 * DTO para resposta da montagem automática de carteira recomendada
 */
public class MontarCarteiraRecomendadaResponseDTO {

    private List<Long> investimentosAdicionados;      // IDs que foram ADICIONADOS agora
    private List<Long> investimentosJaExistentes;     // IDs que JÁ EXISTIAM (duplicatas ignoradas)
    private String mensagem;
    private Integer quantidadeAdicionados;
    private Integer quantidadeJaExistentes;

    public MontarCarteiraRecomendadaResponseDTO() {
    }

    public MontarCarteiraRecomendadaResponseDTO(
            List<Long> investimentosAdicionados, 
            List<Long> investimentosJaExistentes, 
            String mensagem) {
        this.investimentosAdicionados = investimentosAdicionados;
        this.investimentosJaExistentes = investimentosJaExistentes;
        this.mensagem = mensagem;
        this.quantidadeAdicionados = investimentosAdicionados != null ? investimentosAdicionados.size() : 0;
        this.quantidadeJaExistentes = investimentosJaExistentes != null ? investimentosJaExistentes.size() : 0;
    }

    public List<Long> getInvestimentosAdicionados() {
        return investimentosAdicionados;
    }

    public void setInvestimentosAdicionados(List<Long> investimentosAdicionados) {
        this.investimentosAdicionados = investimentosAdicionados;
        this.quantidadeAdicionados = investimentosAdicionados != null ? investimentosAdicionados.size() : 0;
    }

    public List<Long> getInvestimentosJaExistentes() {
        return investimentosJaExistentes;
    }

    public void setInvestimentosJaExistentes(List<Long> investimentosJaExistentes) {
        this.investimentosJaExistentes = investimentosJaExistentes;
        this.quantidadeJaExistentes = investimentosJaExistentes != null ? investimentosJaExistentes.size() : 0;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getQuantidadeAdicionados() {
        return quantidadeAdicionados;
    }

    public void setQuantidadeAdicionados(Integer quantidadeAdicionados) {
        this.quantidadeAdicionados = quantidadeAdicionados;
    }

    public Integer getQuantidadeJaExistentes() {
        return quantidadeJaExistentes;
    }

    public void setQuantidadeJaExistentes(Integer quantidadeJaExistentes) {
        this.quantidadeJaExistentes = quantidadeJaExistentes;
    }
}
