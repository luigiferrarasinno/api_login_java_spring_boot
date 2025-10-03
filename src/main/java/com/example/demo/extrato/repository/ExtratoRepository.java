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
}