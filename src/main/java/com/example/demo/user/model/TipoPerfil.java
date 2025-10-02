package com.example.demo.user.model;

public enum TipoPerfil {
    PERFIL_CONSERVADOR("Perfil Conservador"),
    PERFIL_MODERADO("Perfil Moderado"), 
    PERFIL_ARROJADO("Perfil Arrojado");

    private final String descricao;

    TipoPerfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}