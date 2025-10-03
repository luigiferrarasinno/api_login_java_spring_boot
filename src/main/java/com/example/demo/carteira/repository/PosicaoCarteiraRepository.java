package com.example.demo.carteira.repository;

import com.example.demo.carteira.model.PosicaoCarteira;
import com.example.demo.user.model.Usuario;
import com.example.demo.investimento.model.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PosicaoCarteiraRepository extends JpaRepository<PosicaoCarteira, Long> {
    
    List<PosicaoCarteira> findByUsuarioOrderByDataUltimaMovimentacaoDesc(Usuario usuario);
    
    Optional<PosicaoCarteira> findByUsuarioAndInvestimento(Usuario usuario, Investimento investimento);
    
    @Query("SELECT pc FROM PosicaoCarteira pc WHERE pc.usuario = :usuario AND pc.quantidadeTotal > 0 ORDER BY pc.valorInvestido DESC")
    List<PosicaoCarteira> findPosicoesAtivasByUsuario(@Param("usuario") Usuario usuario);
    
    @Query("SELECT SUM(pc.valorInvestido) FROM PosicaoCarteira pc WHERE pc.usuario = :usuario AND pc.quantidadeTotal > 0")
    BigDecimal calcularValorTotalInvestido(@Param("usuario") Usuario usuario);
    
    boolean existsByUsuarioAndInvestimento(Usuario usuario, Investimento investimento);
    
    List<PosicaoCarteira> findByInvestimento(Investimento investimento);
}