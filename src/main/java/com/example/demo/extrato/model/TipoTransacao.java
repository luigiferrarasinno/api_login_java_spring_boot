package com.example.demo.extrato.model;

public enum TipoTransacao {
    COMPRA_ACAO("Compra de Ação"),
    VENDA_ACAO("Venda de Ação"),
    DIVIDENDO_RECEBIDO("Dividendo Recebido"),
    DEPOSITO("Depósito na Carteira"),
    SAQUE("Saque da Carteira");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}