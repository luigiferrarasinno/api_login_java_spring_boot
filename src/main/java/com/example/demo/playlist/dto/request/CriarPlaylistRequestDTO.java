package com.example.demo.playlist.dto.request;

import com.example.demo.playlist.model.PlaylistTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarPlaylistRequestDTO {

    @NotBlank(message = "Nome da playlist é obrigatório")
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private PlaylistTipo tipo = PlaylistTipo.PRIVADA;

    private Boolean permiteColaboracao = true;

    public CriarPlaylistRequestDTO() {}

    public CriarPlaylistRequestDTO(String nome, String descricao, PlaylistTipo tipo, Boolean permiteColaboracao) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.permiteColaboracao = permiteColaboracao;
    }

    // Getters e Setters
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

    public PlaylistTipo getTipo() {
        return tipo;
    }

    public void setTipo(PlaylistTipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getPermiteColaboracao() {
        return permiteColaboracao;
    }

    public void setPermiteColaboracao(Boolean permiteColaboracao) {
        this.permiteColaboracao = permiteColaboracao;
    }
}