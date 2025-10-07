package com.example.demo.comentarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para criar novos comentários
 */
public class CriarComentarioDTO {
    
    @NotBlank(message = "Conteúdo do comentário não pode estar vazio")
    @Size(min = 1, max = 1000, message = "Comentário deve ter entre 1 e 1000 caracteres")
    private String conteudo;
    
    private Long investimentoId;
    
    // ID do comentário pai, se for uma resposta (null se for comentário raiz)
    private Long comentarioPaiId;

    public CriarComentarioDTO() {}

    public CriarComentarioDTO(String conteudo, Long investimentoId) {
        this.conteudo = conteudo;
        this.investimentoId = investimentoId;
    }

    public CriarComentarioDTO(String conteudo, Long investimentoId, Long comentarioPaiId) {
        this.conteudo = conteudo;
        this.investimentoId = investimentoId;
        this.comentarioPaiId = comentarioPaiId;
    }

    // Getters and Setters
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Long getInvestimentoId() { return investimentoId; }
    public void setInvestimentoId(Long investimentoId) { this.investimentoId = investimentoId; }

    public Long getComentarioPaiId() { return comentarioPaiId; }
    public void setComentarioPaiId(Long comentarioPaiId) { this.comentarioPaiId = comentarioPaiId; }
}