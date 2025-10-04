package com.example.demo.playlist.dto.response;

import java.time.LocalDateTime;

public class PlaylistOperacaoResponseDTO {

    private String mensagem;
    private Long playlistId;
    private String nomePlaylist;
    private LocalDateTime timestamp;
    private String status;
    private String acao;
    private Integer totalUsuarios;

    public PlaylistOperacaoResponseDTO() {
        this.timestamp = LocalDateTime.now();
        this.status = "sucesso";
    }

    public PlaylistOperacaoResponseDTO(String mensagem, Long playlistId, String nomePlaylist) {
        this();
        this.mensagem = mensagem;
        this.playlistId = playlistId;
        this.nomePlaylist = nomePlaylist;
    }

    public PlaylistOperacaoResponseDTO(String mensagem, Long playlistId, String nomePlaylist, String acao, Integer totalUsuarios) {
        this();
        this.mensagem = mensagem;
        this.playlistId = playlistId;
        this.nomePlaylist = nomePlaylist;
        this.acao = acao;
        this.totalUsuarios = totalUsuarios;
    }

    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Integer getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Integer totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }
}