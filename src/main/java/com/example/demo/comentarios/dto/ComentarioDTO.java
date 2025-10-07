package com.example.demo.comentarios.dto;

import com.example.demo.comentarios.model.Comentario;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para exibir comentários com suporte para árvore de respostas
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
    private Long comentarioPaiId;
    private int numeroRespostas;
    private List<ComentarioDTO> respostas;

    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ComentarioDTO() {}

    public ComentarioDTO(Comentario comentario) {
        this(comentario, true); // Por padrão, incluir respostas
    }

    public ComentarioDTO(Comentario comentario, boolean incluirRespostas) {
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
        this.comentarioPaiId = comentario.getComentarioPai() != null ? 
            comentario.getComentarioPai().getId() : null;
        this.numeroRespostas = comentario.getNumeroRespostas();
        
        if (incluirRespostas && comentario.getRespostas() != null && !comentario.getRespostas().isEmpty()) {
            this.respostas = comentario.getRespostas().stream()
                .filter(Comentario::isAtivo)
                .map(resposta -> new ComentarioDTO(resposta, true))
                .collect(Collectors.toList());
        } else {
            this.respostas = new ArrayList<>();
        }
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

    public Long getComentarioPaiId() { return comentarioPaiId; }
    public void setComentarioPaiId(Long comentarioPaiId) { this.comentarioPaiId = comentarioPaiId; }

    public int getNumeroRespostas() { return numeroRespostas; }
    public void setNumeroRespostas(int numeroRespostas) { this.numeroRespostas = numeroRespostas; }

    public List<ComentarioDTO> getRespostas() { return respostas; }
    public void setRespostas(List<ComentarioDTO> respostas) { this.respostas = respostas; }
}