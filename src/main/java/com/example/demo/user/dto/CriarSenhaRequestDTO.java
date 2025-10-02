package com.example.demo.user.dto;

public class CriarSenhaRequestDTO {
    private Long cpf;
    private String senhaNova;

    public CriarSenhaRequestDTO() {}

    public CriarSenhaRequestDTO(Long cpf, String senhaNova) {
        this.cpf = cpf;
        this.senhaNova = senhaNova;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
}