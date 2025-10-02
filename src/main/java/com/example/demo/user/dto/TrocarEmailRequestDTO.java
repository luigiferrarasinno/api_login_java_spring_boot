package com.example.demo.user.dto;

public class TrocarEmailRequestDTO {
    private String novoEmail;

    public TrocarEmailRequestDTO() {}

    public TrocarEmailRequestDTO(String novoEmail) {
        this.novoEmail = novoEmail;
    }

    public String getNovoEmail() {
        return novoEmail;
    }

    public void setNovoEmail(String novoEmail) {
        this.novoEmail = novoEmail;
    }
}