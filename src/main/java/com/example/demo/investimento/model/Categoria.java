package com.example.demo.investimento.model;

public enum Categoria {
    RENDA_FIXA("Renda Fixa"),
    RENDA_VARIAVEL("Renda Variável"),
    FUNDO_IMOBILIARIO("Fundo Imobiliário"),
    CRIPTO("Criptomoedas"),
    OUTROS("Outros"),
    FUNDO("Fundos");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
