package com.example.demo.playlist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarPlaylistRequestDTO {

    @NotBlank(message = "Nome da playlist é obrigatório")
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private Boolean publica = false;

    private Boolean permiteColaboracao = true;

    public CriarPlaylistRequestDTO() {}

    public CriarPlaylistRequestDTO(String nome, String descricao, Boolean publica, Boolean permiteColaboracao) {
        this.nome = nome;
        this.descricao = descricao;
        this.publica = publica;
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
}