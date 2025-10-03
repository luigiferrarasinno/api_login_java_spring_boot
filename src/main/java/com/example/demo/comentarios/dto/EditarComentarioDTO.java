package com.example.demo.comentarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para editar comentários existentes
 */
public class EditarComentarioDTO {
    
    @NotBlank(message = "Conteúdo do comentário não pode estar vazio")
    @Size(min = 1, max = 1000, message = "Comentário deve ter entre 1 e 1000 caracteres")
    private String conteudo;

    public EditarComentarioDTO() {}

    public EditarComentarioDTO(String conteudo) {
        this.conteudo = conteudo;
    }

    // Getters and Setters
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}