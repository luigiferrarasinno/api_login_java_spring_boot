package com.example.demo.playlist.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CompartilharPlaylistRequestDTO {

    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String emailUsuario;

    public CompartilharPlaylistRequestDTO() {}

    public CompartilharPlaylistRequestDTO(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}