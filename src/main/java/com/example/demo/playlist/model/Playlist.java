package com.example.demo.playlist.model;

import com.example.demo.user.model.Usuario;
import com.example.demo.investimento.model.Investimento;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private PlaylistTipo tipo = PlaylistTipo.PRIVADA; // Tipo da playlist: PUBLICA, PRIVADA ou COMPARTILHADA

    @Column(name = "permite_colaboracao")
    private Boolean permiteColaboracao = true; // Outros usuários podem adicionar/remover investimentos

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "ativa")
    private Boolean ativa = true;

    // Relacionamentos Many-to-Many

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "playlist_investimentos",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "investimento_id")
    )
    private Set<Investimento> investimentos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "playlist_seguidores",
        joinColumns = @JoinColumn(name = "playlist_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> seguidores = new HashSet<>();

    // Construtores
    public Playlist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Playlist(String nome, String descricao, Usuario criador) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.criador = criador;
    }

    public Playlist(String nome, String descricao, Usuario criador, PlaylistTipo tipo) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.criador = criador;
        this.tipo = tipo;
    }

    // Métodos de conveniência
    public void adicionarInvestimento(Investimento investimento) {
        this.investimentos.add(investimento);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void removerInvestimento(Investimento investimento) {
        this.investimentos.remove(investimento);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void adicionarSeguidor(Usuario usuario) {
        this.seguidores.add(usuario);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void removerSeguidor(Usuario usuario) {
        this.seguidores.remove(usuario);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean isCriador(Usuario usuario) {
        return this.criador != null && this.criador.getId().equals(usuario.getId());
    }

    public boolean isSeguidor(Usuario usuario) {
        return this.seguidores.contains(usuario);
    }

    public int getTotalInvestimentos() {
        return this.investimentos.size();
    }

    public int getTotalSeguidores() {
        return this.seguidores.size();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public PlaylistTipo getTipo() {
        return tipo;
    }

    public void setTipo(PlaylistTipo tipo) {
        this.tipo = tipo;
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Métodos de conveniência para verificar tipo da playlist
     */
    public boolean isPublica() {
        return this.tipo == PlaylistTipo.PUBLICA;
    }

    public boolean isPrivada() {
        return this.tipo == PlaylistTipo.PRIVADA;
    }

    public boolean isCompartilhada() {
        return this.tipo == PlaylistTipo.COMPARTILHADA;
    }

    public Boolean getPermiteColaboracao() {
        return permiteColaboracao;
    }

    public void setPermiteColaboracao(Boolean permiteColaboracao) {
        this.permiteColaboracao = permiteColaboracao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Set<Investimento> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(Set<Investimento> investimentos) {
        this.investimentos = investimentos;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Set<Usuario> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Set<Usuario> seguidores) {
        this.seguidores = seguidores;
        this.dataAtualizacao = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return id != null && id.equals(playlist.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}