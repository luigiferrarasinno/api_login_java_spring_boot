package com.example.demo.playlist.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PlaylistDetalhadaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String criadorNome;
    private String criadorEmail;
    private Boolean publica;
    private Boolean permiteColaboracao;
    private Integer totalInvestimentos;
    private Integer totalSeguidores;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean isCriador;
    private Boolean isFollowing;
    private List<InvestimentoPlaylistResponseDTO> investimentos;
    private List<UsuarioSeguidorResponseDTO> seguidores;

    public PlaylistDetalhadaResponseDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Boolean getPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public Boolean getPermiteColaboracao() {
        return permiteColaboracao;
    }

    public void setPermiteColaboracao(Boolean permiteColaboracao) {
        this.permiteColaboracao = permiteColaboracao;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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

    public List<InvestimentoPlaylistResponseDTO> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(List<InvestimentoPlaylistResponseDTO> investimentos) {
        this.investimentos = investimentos;
    }

    public List<UsuarioSeguidorResponseDTO> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<UsuarioSeguidorResponseDTO> seguidores) {
        this.seguidores = seguidores;
    }
}