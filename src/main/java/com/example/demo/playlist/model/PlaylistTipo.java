package com.example.demo.playlist.model;

public enum PlaylistTipo {
    PUBLICA("Pública", "Visível para todos os usuários da plataforma"),
    PRIVADA("Privada", "Visível apenas para o criador da playlist"),
    COMPARTILHADA("Compartilhada", "Visível para usuários específicos convidados pelo criador");

    private final String descricao;
    private final String detalhes;

    PlaylistTipo(String descricao, String detalhes) {
        this.descricao = descricao;
        this.detalhes = detalhes;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    /**
     * Verifica se o tipo permite visualização pública
     */
    public boolean isPublica() {
        return this == PUBLICA;
    }

    /**
     * Verifica se o tipo é privado (apenas criador)
     */
    public boolean isPrivada() {
        return this == PRIVADA;
    }

    /**
     * Verifica se o tipo permite compartilhamento específico
     */
    public boolean isCompartilhada() {
        return this == COMPARTILHADA;
    }

    /**
     * Verifica se outros usuários podem descobrir a playlist
     */
    public boolean isDiscoveravel() {
        return this == PUBLICA;
    }
}