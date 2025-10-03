package com.example.demo.comentarios.dto;

import com.example.demo.comentarios.model.Comentario;
import java.time.format.DateTimeFormatter;

/**
 * DTO para exibir comentários
 */
public class ComentarioDTO {
    
    private Long id;
    private String conteudo;
    private Long usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
    private Long investimentoId;
    private String nomeInvestimento;
    private String simboloInvestimento;
    private String dataCriacao;
    private String dataAtualizacao;
    private boolean editado;
    private boolean ativo;

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ComentarioDTO() {}

    public ComentarioDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.conteudo = comentario.getConteudo();
        this.usuarioId = comentario.getUsuario().getId();
        this.nomeUsuario = comentario.getUsuario().getNomeUsuario();
        this.emailUsuario = comentario.getUsuario().getEmail();
        this.investimentoId = comentario.getInvestimento().getId();
        this.nomeInvestimento = comentario.getInvestimento().getNome();
        this.simboloInvestimento = comentario.getInvestimento().getSimbolo();
        this.dataCriacao = comentario.getDataCriacao().format(DATETIME_FORMAT);
        this.dataAtualizacao = comentario.getDataAtualizacao() != null ? 
            comentario.getDataAtualizacao().format(DATETIME_FORMAT) : null;
        this.editado = comentario.isEditado();
        this.ativo = comentario.isAtivo();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public Long getInvestimentoId() { return investimentoId; }
    public void setInvestimentoId(Long investimentoId) { this.investimentoId = investimentoId; }

    public String getNomeInvestimento() { return nomeInvestimento; }
    public void setNomeInvestimento(String nomeInvestimento) { this.nomeInvestimento = nomeInvestimento; }

    public String getSimboloInvestimento() { return simboloInvestimento; }
    public void setSimboloInvestimento(String simboloInvestimento) { this.simboloInvestimento = simboloInvestimento; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(String dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public boolean isEditado() { return editado; }
    public void setEditado(boolean editado) { this.editado = editado; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}