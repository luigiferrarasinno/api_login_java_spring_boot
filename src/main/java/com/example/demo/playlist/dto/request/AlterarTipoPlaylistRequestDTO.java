package com.example.demo.playlist.dto.request;

import com.example.demo.playlist.model.PlaylistTipo;
import jakarta.validation.constraints.NotNull;

public class AlterarTipoPlaylistRequestDTO {

    @NotNull(message = "O tipo da playlist é obrigatório")
    private PlaylistTipo novoTipo;

    public AlterarTipoPlaylistRequestDTO() {}

    public AlterarTipoPlaylistRequestDTO(PlaylistTipo novoTipo) {
        this.novoTipo = novoTipo;
    }

    // Getters e Setters
    public PlaylistTipo getNovoTipo() {
        return novoTipo;
    }

    public void setNovoTipo(PlaylistTipo novoTipo) {
        this.novoTipo = novoTipo;
    }
}
