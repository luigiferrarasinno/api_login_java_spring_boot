package com.example.demo.extrato.repository;

import com.example.demo.extrato.model.Extrato;
import com.example.demo.extrato.model.TipoTransacao;
import com.example.demo.user.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long> {
    
    List<Extrato> findByUsuarioOrderByDataTransacaoDesc(Usuario usuario);
    
    Page<Extrato> findByUsuarioOrderByDataTransacaoDesc(Usuario usuario, Pageable pageable);
    
    List<Extrato> findByUsuarioAndTipoTransacaoOrderByDataTransacaoDesc(Usuario usuario, TipoTransacao tipoTransacao);
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND e.dataTransacao BETWEEN :dataInicio AND :dataFim ORDER BY e.dataTransacao DESC")
    List<Extrato> findByUsuarioAndPeriodo(@Param("usuario") Usuario usuario, 
                                         @Param("dataInicio") LocalDateTime dataInicio, 
                                         @Param("dataFim") LocalDateTime dataFim);
                                         
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND e.investimento.id = :investimentoId ORDER BY e.dataTransacao DESC")
    List<Extrato> findByUsuarioAndInvestimento(@Param("usuario") Usuario usuario, @Param("investimentoId") Long investimentoId);
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND FUNCTION('YEAR', e.dataTransacao) = :ano AND FUNCTION('MONTH', e.dataTransacao) = :mes ORDER BY e.dataTransacao DESC")
    List<Extrato> findByUsuarioAndMesAno(@Param("usuario") Usuario usuario, @Param("ano") int ano, @Param("mes") int mes);
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND e.investimento.id = :investimentoId AND FUNCTION('YEAR', e.dataTransacao) = :ano AND FUNCTION('MONTH', e.dataTransacao) = :mes ORDER BY e.dataTransacao DESC")
    List<Extrato> findByUsuarioAndInvestimentoAndMesAno(@Param("usuario") Usuario usuario, @Param("investimentoId") Long investimentoId, @Param("ano") int ano, @Param("mes") int mes);
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND e.tipoTransacao IN ('COMPRA_ACAO', 'VENDA_ACAO') ORDER BY e.dataTransacao DESC")
    List<Extrato> findTransacoesInvestimentoByUsuario(@Param("usuario") Usuario usuario);
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario = :usuario AND e.investimento.id = :investimentoId AND e.tipoTransacao IN ('COMPRA_ACAO', 'VENDA_ACAO') ORDER BY e.dataTransacao DESC")
    List<Extrato> findTransacoesInvestimentoByUsuarioAndInvestimento(@Param("usuario") Usuario usuario, @Param("investimentoId") Long investimentoId);
    
    @Query("SELECT COALESCE(SUM(e.valorTotal), 0) FROM Extrato e WHERE e.usuario = :usuario AND e.investimento.id = :investimentoId AND e.tipoTransacao = 'DIVIDENDO_RECEBIDO'")
    java.math.BigDecimal calcularTotalDividendosPorInvestimento(@Param("usuario") Usuario usuario, @Param("investimentoId") Long investimentoId);
}