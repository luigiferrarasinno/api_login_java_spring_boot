package com.example.demo.user.dto.Responses;


public class StatusAtivoResponseDTO {
    private String mensagem;
    private boolean ativo;

    public StatusAtivoResponseDTO(String mensagem, boolean ativo) {
        this.mensagem = mensagem;
        this.ativo = ativo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isAtivo() {
        return ativo;
    }
}