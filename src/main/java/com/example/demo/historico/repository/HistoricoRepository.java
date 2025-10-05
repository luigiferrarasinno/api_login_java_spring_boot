package com.example.demo.historico.repository;

import com.example.demo.historico.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    
    // Buscar histórico por usuário
    List<Historico> findByUsuarioId(Long usuarioId);
    
    // Buscar histórico por investimento
    List<Historico> findByInvestimentoId(Long investimentoId);
    
    // Buscar histórico por usuário e investimento
    List<Historico> findByUsuarioIdAndInvestimentoId(Long usuarioId, Long investimentoId);
    
    // Buscar histórico por mês/ano
    List<Historico> findByMesAnoRegistro(YearMonth mesAnoRegistro);
    
    // Buscar histórico por usuário e mês/ano
    List<Historico> findByUsuarioIdAndMesAnoRegistro(Long usuarioId, YearMonth mesAnoRegistro);
    
    // Buscar histórico por investimento e mês/ano
    List<Historico> findByInvestimentoIdAndMesAnoRegistro(Long investimentoId, YearMonth mesAnoRegistro);
    
    // Buscar histórico completo (usuário, investimento e mês/ano) - evita duplicatas
    Optional<Historico> findByUsuarioIdAndInvestimentoIdAndMesAnoRegistro(
            Long usuarioId, Long investimentoId, YearMonth mesAnoRegistro);
    
    // Buscar histórico ordenado por data de registro (mais recente primeiro)
    List<Historico> findByUsuarioIdOrderByDataRegistroDesc(Long usuarioId);
    
    // Buscar histórico de um usuário em um período
    @Query("SELECT h FROM Historico h WHERE h.usuario.id = :usuarioId " +
           "AND h.mesAnoRegistro BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY h.mesAnoRegistro DESC")
    List<Historico> findByUsuarioIdAndPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("dataInicio") YearMonth dataInicio,
            @Param("dataFim") YearMonth dataFim);
    
    // Buscar últimos registros de um usuário
    @Query("SELECT h FROM Historico h WHERE h.usuario.id = :usuarioId " +
           "ORDER BY h.dataRegistro DESC")
    List<Historico> findTop10ByUsuarioIdOrderByDataRegistroDesc(@Param("usuarioId") Long usuarioId);
    
    // Buscar histórico de investimentos com performance positiva
    @Query("SELECT h FROM Historico h WHERE h.usuario.id = :usuarioId " +
           "AND h.totalRetornando > h.totalInvestido " +
           "ORDER BY h.dataRegistro DESC")
    List<Historico> findInvestimentosComLucro(@Param("usuarioId") Long usuarioId);
    
    // Buscar histórico de investimentos com performance negativa
    @Query("SELECT h FROM Historico h WHERE h.usuario.id = :usuarioId " +
           "AND h.totalRetornando < h.totalInvestido " +
           "ORDER BY h.dataRegistro DESC")
    List<Historico> findInvestimentosComPrejuizo(@Param("usuarioId") Long usuarioId);
}