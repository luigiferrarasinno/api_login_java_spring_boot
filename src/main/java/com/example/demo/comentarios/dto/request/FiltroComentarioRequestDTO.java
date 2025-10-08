package com.example.demo.comentarios.dto.request;

/**
 * DTO para filtros avançados de busca de comentários
 */
public class FiltroComentarioRequestDTO {
    
    /**
     * Filtro rápido de acesso
     */
    public enum FiltroRapido {
        MEUS,                    // Comentários do usuário logado
        INVESTIMENTO,            // Comentários de um investimento específico
        RESPOSTAS,               // Respostas de um comentário específico
        TODOS,                   // Todos os comentários (admin apenas)
        TODOS_PUBLICOS          // Todos comentários não excluídos (público)
    }
    
    /**
     * Ordenação dos resultados
     */
    public enum OrdenacaoComentario {
        DATA_CRIACAO_ASC,
        DATA_CRIACAO_DESC,
        MAIS_RECENTES,          // Alias para DATA_CRIACAO_DESC
        MAIS_ANTIGOS            // Alias para DATA_CRIACAO_ASC
    }
    
    // Filtro rápido
    private FiltroRapido filtro;
    
    // Filtros específicos
    private Long investimentoId;
    private Long comentarioPaiId;      // Para buscar respostas
    private String usuarioEmail;       // Admin pode buscar por usuário específico
    private String conteudo;           // Busca parcial no conteúdo (admin apenas)
    private String dataInicio;         // Filtro por data (admin apenas)
    private String dataFim;            // Filtro por data (admin apenas)
    private Boolean apenasRaiz;        // true = apenas comentários raiz (não respostas)
    
    // Ordenação
    private OrdenacaoComentario ordenacao = OrdenacaoComentario.MAIS_RECENTES;

    // Getters e Setters
    public FiltroRapido getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRapido filtro) {
        this.filtro = filtro;
    }

    public Long getInvestimentoId() {
        return investimentoId;
    }

    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }

    public Long getComentarioPaiId() {
        return comentarioPaiId;
    }

    public void setComentarioPaiId(Long comentarioPaiId) {
        this.comentarioPaiId = comentarioPaiId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getApenasRaiz() {
        return apenasRaiz;
    }

    public void setApenasRaiz(Boolean apenasRaiz) {
        this.apenasRaiz = apenasRaiz;
    }

    public OrdenacaoComentario getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(OrdenacaoComentario ordenacao) {
        this.ordenacao = ordenacao;
    }
}
