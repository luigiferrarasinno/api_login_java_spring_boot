package com.example.demo.feller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para enviar prompt para a IA Feller
 */
public class FellerPromptDTO {

    @NotBlank(message = "O prompt não pode estar vazio")
    private String prompt;

    public FellerPromptDTO() {
    }

    public FellerPromptDTO(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
