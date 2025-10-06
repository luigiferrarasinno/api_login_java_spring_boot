package com.example.demo.extrato.service;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.extrato.model.Extrato;
import com.example.demo.extrato.model.TipoTransacao;
import com.example.demo.extrato.repository.ExtratoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Serviço para pagamento automático de dividendos nas compras
 */
@Service
public class PagamentoDividendoService {
    
    private final UsuarioRepository usuarioRepository;
    private final ExtratoRepository extratoRepository;
    
    public PagamentoDividendoService(UsuarioRepository usuarioRepository,
                                   ExtratoRepository extratoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.extratoRepository = extratoRepository;
    }
    
    /**
     * 🎯 Paga dividendo IMEDIATO ao comprar ação
     * Chamado automaticamente na compra - SEM aprovação administrativa
     */
    @Transactional
    public String pagarDividendoImediato(Usuario usuario, Investimento investimento, BigDecimal quantidadeComprada) {
        // Verificar se o investimento paga dividendos
        if (investimento.getDividendYield() == null || investimento.getDividendYield().compareTo(BigDecimal.ZERO) <= 0) {
            return null; // Não paga dividendo, retorna null silenciosamente
        }
        
        // Calcular dividendo por ação
        BigDecimal dividendoPorAcao = calcularDividendoPorAcao(investimento);
        BigDecimal valorDividendoTotal = quantidadeComprada.multiply(dividendoPorAcao).setScale(2, RoundingMode.HALF_UP);
        
        // Creditar dividendo IMEDIATAMENTE na carteira
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorDividendoTotal);
        
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato como dividendo de boas-vindas
        Extrato extrato = new Extrato(usuario, TipoTransacao.DIVIDENDO_RECEBIDO, valorDividendoTotal, 
                                     saldoAnterior, novoSaldo,
                                     "🎁 Dividendo de Boas-Vindas: " + investimento.getNome() + 
                                     " - " + quantidadeComprada + " ações × R$ " + dividendoPorAcao);
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidadeComprada);
        extrato.setPrecoUnitario(dividendoPorAcao);
        
        extratoRepository.save(extrato);
        
        return String.format("🎁 Dividendo de boas-vindas creditado: R$ %.2f (%s ações × R$ %.2f)", 
                            valorDividendoTotal, quantidadeComprada, dividendoPorAcao);
    }
    
    /**
     * Calcula dividendo por ação baseado no yield e frequência
     */
    private BigDecimal calcularDividendoPorAcao(Investimento investimento) {
        BigDecimal precoAtual = investimento.getPrecoAtual() != null ? 
            investimento.getPrecoAtual() : investimento.getPrecoBase();
            
        if (precoAtual == null) {
            precoAtual = BigDecimal.valueOf(100); // Valor padrão
        }
        
        // Dividendo = (Preço da ação × Yield anual) / Frequência de pagamento
        BigDecimal yieldDecimal = investimento.getDividendYield().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal dividendoAnual = precoAtual.multiply(yieldDecimal);
        BigDecimal dividendoPeriodo = dividendoAnual.divide(BigDecimal.valueOf(investimento.getFrequenciaDividendo()), 2, RoundingMode.HALF_UP);
        
        return dividendoPeriodo;
    }
}