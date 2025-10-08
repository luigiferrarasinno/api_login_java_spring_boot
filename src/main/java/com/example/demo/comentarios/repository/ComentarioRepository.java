package com.example.demo.comentarios.repository;

import com.example.demo.comentarios.model.Comentario;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    // Buscar comentários ativos de um investimento específico
    List<Comentario> findByInvestimentoAndAtivoTrueOrderByDataCriacaoDesc(Investimento investimento);
    
    // Buscar comentários de um usuário específico
    List<Comentario> findByUsuarioAndAtivoTrueOrderByDataCriacaoDesc(Usuario usuario);
    
    // Buscar comentários ativos de um investimento por ID
    @Query("SELECT c FROM Comentario c WHERE c.investimento.id = :investimentoId AND c.ativo = true ORDER BY c.dataCriacao DESC")
    List<Comentario> findByInvestimentoIdAndAtivoTrue(@Param("investimentoId") Long investimentoId);
    
    // Buscar todos os comentários ativos (para admin)
    List<Comentario> findByAtivoTrueOrderByDataCriacaoDesc();
    
    // Buscar comentários por período
    @Query("SELECT c FROM Comentario c WHERE c.ativo = true AND c.dataCriacao >= :dataInicio AND c.dataCriacao <= :dataFim ORDER BY c.dataCriacao DESC")
    List<Comentario> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
    
    // Buscar comentários com filtro de texto
    @Query("SELECT c FROM Comentario c WHERE c.ativo = true AND LOWER(c.conteudo) LIKE LOWER(CONCAT('%', :texto, '%')) ORDER BY c.dataCriacao DESC")
    List<Comentario> findByConteudoContaining(@Param("texto") String texto);
    
    // Contar comentários de um investimento
    long countByInvestimentoIdAndAtivoTrue(Long investimentoId);
    
    // Contar comentários de um usuário
    long countByUsuarioIdAndAtivoTrue(Long usuarioId);
    
    // Buscar todos os comentários ativos
    List<Comentario> findByAtivoTrue();
    
    // Buscar comentários com filtros combinados
    @Query("SELECT c FROM Comentario c WHERE c.ativo = true " +
           "AND (:investimentoId IS NULL OR c.investimento.id = :investimentoId) " +
           "AND (:usuarioId IS NULL OR c.usuario.id = :usuarioId) " +
           "AND (:conteudo IS NULL OR LOWER(c.conteudo) LIKE LOWER(CONCAT('%', :conteudo, '%'))) " +
           "AND (:dataInicio IS NULL OR c.dataCriacao >= :dataInicio) " +
           "AND (:dataFim IS NULL OR c.dataCriacao <= :dataFim) " +
           "ORDER BY c.dataCriacao DESC")
    List<Comentario> findComFiltros(@Param("investimentoId") Long investimentoId,
                                   @Param("usuarioId") Long usuarioId,
                                   @Param("conteudo") String conteudo,
                                   @Param("dataInicio") LocalDateTime dataInicio,
                                   @Param("dataFim") LocalDateTime dataFim);
}