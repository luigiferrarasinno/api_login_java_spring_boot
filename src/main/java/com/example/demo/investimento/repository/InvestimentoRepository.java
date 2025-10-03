package com.example.demo.investimento.repository;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.model.Categoria;
import com.example.demo.investimento.model.Risco;
import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    List<Investimento> findByUsuarios(Usuario usuario);
    List<Investimento> findByAtivoTrue();
    
    // Para usuários comuns - apenas investimentos visíveis
    List<Investimento> findByVisivelParaUsuariosTrue();
    List<Investimento> findByVisivelParaUsuariosTrueAndAtivoTrue();
    
    // Filtros para admin (vê todos)
    List<Investimento> findByNomeContainingIgnoreCase(String nome);
    List<Investimento> findBySimboloContainingIgnoreCase(String simbolo);
    List<Investimento> findByCategoria(Categoria categoria);
    List<Investimento> findByRisco(Risco risco);
    List<Investimento> findByAtivo(boolean ativo);
    List<Investimento> findByVisivelParaUsuarios(boolean visivel);
    
    // Filtros por preço
    @Query("SELECT i FROM Investimento i WHERE i.precoAtual >= :precoMin")
    List<Investimento> findByPrecoAtualGreaterThanEqual(@Param("precoMin") BigDecimal precoMin);
    
    @Query("SELECT i FROM Investimento i WHERE i.precoAtual <= :precoMax")
    List<Investimento> findByPrecoAtualLessThanEqual(@Param("precoMax") BigDecimal precoMax);
    
    @Query("SELECT i FROM Investimento i WHERE i.precoAtual >= :precoMin AND i.precoAtual <= :precoMax")
    List<Investimento> findByPrecoAtualBetween(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax);
    
    // Filtros combinados para usuários comuns (apenas visíveis)
    @Query("SELECT i FROM Investimento i WHERE i.visivelParaUsuarios = true AND " +
           "(:nome IS NULL OR LOWER(i.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:simbolo IS NULL OR LOWER(i.simbolo) LIKE LOWER(CONCAT('%', :simbolo, '%'))) AND " +
           "(:categoria IS NULL OR i.categoria = :categoria) AND " +
           "(:risco IS NULL OR i.risco = :risco) AND " +
           "(:precoMin IS NULL OR i.precoAtual >= :precoMin) AND " +
           "(:precoMax IS NULL OR i.precoAtual <= :precoMax)")
    List<Investimento> findInvestimentosVisiveis(@Param("nome") String nome,
                                               @Param("simbolo") String simbolo,
                                               @Param("categoria") Categoria categoria,
                                               @Param("risco") Risco risco,
                                               @Param("precoMin") BigDecimal precoMin,
                                               @Param("precoMax") BigDecimal precoMax);
    
    // Filtros combinados para admin (vê todos)
    @Query("SELECT i FROM Investimento i WHERE " +
           "(:nome IS NULL OR LOWER(i.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:simbolo IS NULL OR LOWER(i.simbolo) LIKE LOWER(CONCAT('%', :simbolo, '%'))) AND " +
           "(:categoria IS NULL OR i.categoria = :categoria) AND " +
           "(:risco IS NULL OR i.risco = :risco) AND " +
           "(:ativo IS NULL OR i.ativo = :ativo) AND " +
           "(:visivel IS NULL OR i.visivelParaUsuarios = :visivel) AND " +
           "(:precoMin IS NULL OR i.precoAtual >= :precoMin) AND " +
           "(:precoMax IS NULL OR i.precoAtual <= :precoMax)")
    List<Investimento> findInvestimentosComFiltros(@Param("nome") String nome,
                                                  @Param("simbolo") String simbolo,
                                                  @Param("categoria") Categoria categoria,
                                                  @Param("risco") Risco risco,
                                                  @Param("ativo") Boolean ativo,
                                                  @Param("visivel") Boolean visivel,
                                                  @Param("precoMin") BigDecimal precoMin,
                                                  @Param("precoMax") BigDecimal precoMax);
}
