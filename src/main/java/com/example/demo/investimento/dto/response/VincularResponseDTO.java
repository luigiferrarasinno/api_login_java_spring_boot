package com.example.demo.investimento.dto.response;

import com.example.demo.investimento.dto.InvestimentoDTO;



public class VincularResponseDTO {
    private InvestimentoDTO investimento;
    private String mensagem;

    public VincularResponseDTO(InvestimentoDTO investimento, String mensagem) {
        this.investimento = investimento;
        this.mensagem = mensagem;
    }

    public InvestimentoDTO getInvestimento() {
        return investimento;
    }

    public void setInvestimento(InvestimentoDTO investimento) {
        this.investimento = investimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
