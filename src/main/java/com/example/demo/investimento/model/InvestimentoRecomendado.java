package com.example.demo.investimento.model;

import com.example.demo.user.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "investimento_recomendado")
public class InvestimentoRecomendado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "investimento_id", nullable = false)
    private Investimento investimento;
    
    @Column(name = "data_recomendacao")
    private LocalDateTime dataRecomendacao;
    
    // Constructors
    public InvestimentoRecomendado() {
        this.dataRecomendacao = LocalDateTime.now();
    }
    
    public InvestimentoRecomendado(Usuario usuario, Investimento investimento) {
        this.usuario = usuario;
        this.investimento = investimento;
        this.dataRecomendacao = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Investimento getInvestimento() {
        return investimento;
    }
    
    public void setInvestimento(Investimento investimento) {
        this.investimento = investimento;
    }
    
    public LocalDateTime getDataRecomendacao() {
        return dataRecomendacao;
    }
    
    public void setDataRecomendacao(LocalDateTime dataRecomendacao) {
        this.dataRecomendacao = dataRecomendacao;
    }
}
