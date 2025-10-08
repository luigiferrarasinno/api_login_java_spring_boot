package com.example.demo.playlist.dto.request;

import com.example.demo.playlist.model.PlaylistTipo;

/**
 * DTO para filtrar playlists com múltiplos critérios
 * 
 * Exemplos de uso:
 * - ?filtro=MINHAS → Minhas playlists
 * - ?filtro=SEGUINDO → Playlists que sigo
 * - ?filtro=PUBLICAS → Todas públicas
 * - ?filtro=COMPARTILHADAS → Compartilhadas comigo
 * - ?nome=dividendos → Busca por nome
 * - ?tipo=PUBLICA&nome=tech → Públicas com "tech" no nome
 */
public class FiltroPlaylistRequestDTO {
    
    /**
     * Filtro rápido predefinido
     * Valores possíveis: MINHAS, SEGUINDO, PUBLICAS, COMPARTILHADAS, TODAS
     */
    private FiltroRapido filtro;
    
    /**
     * Tipo da playlist (PUBLICA, PRIVADA, COMPARTILHADA)
     */
    private PlaylistTipo tipo;
    
    /**
     * Busca por nome (case-insensitive, busca parcial)
     */
    private String nome;
    
    /**
     * Filtrar apenas playlists que permitem colaboração
     */
    private Boolean permiteColaboracao;
    
    /**
     * Email do criador (filtrar por criador específico)
     */
    private String criadorEmail;
    
    /**
     * Ordenação (padrao: DATA_CRIACAO_DESC)
     * Valores: DATA_CRIACAO_ASC, DATA_CRIACAO_DESC, NOME_ASC, NOME_DESC, 
     *          TOTAL_INVESTIMENTOS_ASC, TOTAL_INVESTIMENTOS_DESC,
     *          TOTAL_SEGUIDORES_ASC, TOTAL_SEGUIDORES_DESC
     */
    private OrdenacaoPlaylist ordenacao;
    
    public enum FiltroRapido {
        MINHAS,          // Playlists criadas por mim
        SEGUINDO,        // Playlists que estou seguindo
        PUBLICAS,        // Todas as playlists públicas
        COMPARTILHADAS,  // Playlists compartilhadas comigo (tipo COMPARTILHADA)
        TODAS,           // Todas as playlists que tenho acesso (minhas + seguindo + públicas)
        TODAS_ADMIN      // ADMIN ONLY: Literalmente TODAS as playlists do sistema (incluindo privadas de outros)
    }
    
    public enum OrdenacaoPlaylist {
        DATA_CRIACAO_ASC,
        DATA_CRIACAO_DESC,
        NOME_ASC,
        NOME_DESC,
        TOTAL_INVESTIMENTOS_ASC,
        TOTAL_INVESTIMENTOS_DESC,
        TOTAL_SEGUIDORES_ASC,
        TOTAL_SEGUIDORES_DESC
    }

    // Constructors
    public FiltroPlaylistRequestDTO() {
        this.ordenacao = OrdenacaoPlaylist.DATA_CRIACAO_DESC; // Padrão
    }

    // Getters and Setters
    public FiltroRapido getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRapido filtro) {
        this.filtro = filtro;
    }

    public PlaylistTipo getTipo() {
        return tipo;
    }

    public void setTipo(PlaylistTipo tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPermiteColaboracao() {
        return permiteColaboracao;
    }

    public void setPermiteColaboracao(Boolean permiteColaboracao) {
        this.permiteColaboracao = permiteColaboracao;
    }

    public String getCriadorEmail() {
        return criadorEmail;
    }

    public void setCriadorEmail(String criadorEmail) {
        this.criadorEmail = criadorEmail;
    }

    public OrdenacaoPlaylist getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(OrdenacaoPlaylist ordenacao) {
        this.ordenacao = ordenacao;
    }
}
