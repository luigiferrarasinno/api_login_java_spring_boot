package com.example.demo.investimento.repository;

import com.example.demo.investimento.model.InvestimentoRecomendado;
import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestimentoRecomendadoRepository extends JpaRepository<InvestimentoRecomendado, Long> {
    
    /**
     * Busca todos os investimentos recomendados de um usuário
     */
    List<InvestimentoRecomendado> findByUsuario(Usuario usuario);
    
    /**
     * Busca todos os investimentos recomendados de um usuário por ID
     */
    List<InvestimentoRecomendado> findByUsuarioId(Long usuarioId);
    
    /**
     * Verifica se já existe recomendação para este usuário e investimento
     */
    boolean existsByUsuarioIdAndInvestimentoId(Long usuarioId, Long investimentoId);
    
    /**
     * Verifica se o usuário tem ALGUMA recomendação
     */
    boolean existsByUsuarioId(Long usuarioId);
    
    /**
     * Remove todas as recomendações de um investimento específico para um usuário
     */
    void deleteByUsuarioIdAndInvestimentoId(Long usuarioId, Long investimentoId);
}
