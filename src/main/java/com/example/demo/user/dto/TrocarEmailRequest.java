package com.example.demo.user.dto;

public class TrocarEmailRequest {
    private Long cpf;
    private String novoEmail;

    // Getters e setters

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getNovoEmail() {
        return novoEmail;
    }

    public void setNovoEmail(String novoEmail) {
        this.novoEmail = novoEmail;
    }
}
