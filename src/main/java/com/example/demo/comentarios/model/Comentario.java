package com.example.demo.comentarios.model;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade para comentários sobre investimentos/ações
 * Usuários podem comentar sobre ações específicas
 * Suporta árvore de comentários (respostas a comentários)
 */
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Conteúdo do comentário não pode estar vazio")
    @Size(min = 1, max = 1000, message = "Comentário deve ter entre 1 e 1000 caracteres")
    @Column(name = "conteudo", nullable = false, length = 1000)
    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investimento_id", nullable = false)
    private Investimento investimento;

    // Relacionamento para árvore de comentários (respostas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_pai_id")
    @JsonIgnore
    private Comentario comentarioPai;

    @OneToMany(mappedBy = "comentarioPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> respostas = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "editado")
    private boolean editado = false;

    @Column(name = "ativo")
    private boolean ativo = true; // Para "soft delete"

    // Constructors
    public Comentario() {}

    public Comentario(String conteudo, Usuario usuario, Investimento investimento) {
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.investimento = investimento;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.editado = false;
        this.respostas = new ArrayList<>();
    }

    public Comentario(String conteudo, Usuario usuario, Investimento investimento, Comentario comentarioPai) {
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.investimento = investimento;
        this.comentarioPai = comentarioPai;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.editado = false;
        this.respostas = new ArrayList<>();
    }

    // Auditing automations
    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
        editado = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Investimento getInvestimento() { return investimento; }
    public void setInvestimento(Investimento investimento) { this.investimento = investimento; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public boolean isEditado() { return editado; }
    public void setEditado(boolean editado) { this.editado = editado; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Comentario getComentarioPai() { return comentarioPai; }
    public void setComentarioPai(Comentario comentarioPai) { this.comentarioPai = comentarioPai; }

    public List<Comentario> getRespostas() { return respostas; }
    public void setRespostas(List<Comentario> respostas) { this.respostas = respostas; }

    // Métodos auxiliares para gerenciar respostas
    public void adicionarResposta(Comentario resposta) {
        respostas.add(resposta);
        resposta.setComentarioPai(this);
    }

    public void removerResposta(Comentario resposta) {
        respostas.remove(resposta);
        resposta.setComentarioPai(null);
    }

    public int getNumeroRespostas() {
        return respostas != null ? respostas.size() : 0;
    }

    public boolean isResposta() {
        return comentarioPai != null;
    }
}