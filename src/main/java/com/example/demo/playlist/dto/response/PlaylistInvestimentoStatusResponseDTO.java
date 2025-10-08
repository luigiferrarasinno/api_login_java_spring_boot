package com.example.demo.playlist.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de resposta mostrando playlists e se um investimento pertence a elas
 */
public class PlaylistInvestimentoStatusResponseDTO {
    
    private Long playlistId;
    private String nomePlaylist;
    private String descricao;
    private String criadorNome;
    private String criadorEmail;
    private String tipo;
    private Boolean permiteColaboracao;
    private Boolean pertenceAPlaylist;  // Indica se o investimento está nesta playlist
    private Integer totalInvestimentos;
    private Integer totalSeguidores;
    private Boolean isCriador;
    private Boolean isFollowing;
    private LocalDateTime dataCriacao;

    public PlaylistInvestimentoStatusResponseDTO() {
    }

    public PlaylistInvestimentoStatusResponseDTO(Long playlistId, String nomePlaylist, String descricao,
                                                  String criadorNome, String criadorEmail, String tipo,
                                                  Boolean permiteColaboracao, Boolean pertenceAPlaylist,
                                                  Integer totalInvestimentos, Integer totalSeguidores,
                                                  Boolean isCriador, Boolean isFollowing, LocalDateTime dataCriacao) {
        this.playlistId = playlistId;
        this.nomePlaylist = nomePlaylist;
        this.descricao = descricao;
        this.criadorNome = criadorNome;
        this.criadorEmail = criadorEmail;
        this.tipo = tipo;
        this.permiteColaboracao = permiteColaboracao;
        this.pertenceAPlaylist = pertenceAPlaylist;
        this.totalInvestimentos = totalInvestimentos;
        this.totalSeguidores = totalSeguidores;
        this.isCriador = isCriador;
        this.isFollowing = isFollowing;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public String getNomePlaylist() {
        return nomePlaylist;
    }

    public void setNomePlaylist(String nomePlaylist) {
        this.nomePlaylist = nomePlaylist;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCriadorNome() {
        return criadorNome;
    }

    public void setCriadorNome(String criadorNome) {
        this.criadorNome = criadorNome;
    }

    public String getCriadorEmail() {
        return criadorEmail;
    }

    public void setCriadorEmail(String criadorEmail) {
        this.criadorEmail = criadorEmail;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getPermiteColaboracao() {
        return permiteColaboracao;
    }

    public void setPermiteColaboracao(Boolean permiteColaboracao) {
        this.permiteColaboracao = permiteColaboracao;
    }

    public Boolean getPertenceAPlaylist() {
        return pertenceAPlaylist;
    }

    public void setPertenceAPlaylist(Boolean pertenceAPlaylist) {
        this.pertenceAPlaylist = pertenceAPlaylist;
    }

    public Integer getTotalInvestimentos() {
        return totalInvestimentos;
    }

    public void setTotalInvestimentos(Integer totalInvestimentos) {
        this.totalInvestimentos = totalInvestimentos;
    }

    public Integer getTotalSeguidores() {
        return totalSeguidores;
    }

    public void setTotalSeguidores(Integer totalSeguidores) {
        this.totalSeguidores = totalSeguidores;
    }

    public Boolean getIsCriador() {
        return isCriador;
    }

    public void setIsCriador(Boolean isCriador) {
        this.isCriador = isCriador;
    }

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
