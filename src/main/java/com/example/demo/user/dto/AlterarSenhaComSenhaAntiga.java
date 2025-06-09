package com.example.demo.user.dto;

public class AlterarSenhaComSenhaAntiga {
     private String email;
    private String senhaAntiga;
    private String senhaNova;
    //geters e setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenhaAntiga() {
        return senhaAntiga;
    }
    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }
    public String getSenhaNova() {
        return senhaNova;
    }
    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
    
}
