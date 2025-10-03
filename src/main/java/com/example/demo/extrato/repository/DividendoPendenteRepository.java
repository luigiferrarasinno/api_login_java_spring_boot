package com.example.demo.extrato.repository;

import com.example.demo.extrato.model.DividendoPendente;
import com.example.demo.extrato.model.DividendoPendente.StatusDividendo;
import com.example.demo.investimento.model.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividendoPendenteRepository extends JpaRepository<DividendoPendente, Long> {

    /**
     * Busca dividendos por status
     */
    List<DividendoPendente> findByStatusOrderByDataCalculoDesc(StatusDividendo status);

    /**
     * Busca dividendos pendentes por investimento
     */
    List<DividendoPendente> findByInvestimentoAndStatusOrderByDataCalculoDesc(
            Investimento investimento, StatusDividendo status);

    /**
     * Conta quantos dividendos estão pendentes
     */
    long countByStatus(StatusDividendo status);

    /**
     * Busca dividendos por investimento (todos os status)
     */
    List<DividendoPendente> findByInvestimentoOrderByDataCalculoDesc(Investimento investimento);

    /**
     * Query personalizada para resumo administrativo
     */
    @Query("SELECT dp.investimento.id, dp.investimento.nome, COUNT(dp), SUM(dp.valorTotal) " +
           "FROM DividendoPendente dp WHERE dp.status = :status " +
           "GROUP BY dp.investimento.id, dp.investimento.nome")
    List<Object[]> findResumoByStatus(@Param("status") StatusDividendo status);

    /**
     * Busca dividendos aprovados mas ainda não pagos
     */
    List<DividendoPendente> findByStatusOrderByDataAprovacaoDesc(StatusDividendo status);
}