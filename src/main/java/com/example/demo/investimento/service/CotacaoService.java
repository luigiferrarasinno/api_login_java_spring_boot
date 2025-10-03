package com.example.demo.investimento.service;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CotacaoService {
    
    private final InvestimentoRepository investimentoRepository;
    private final Random random = new Random();
    
    public CotacaoService(InvestimentoRepository investimentoRepository) {
        this.investimentoRepository = investimentoRepository;
    }
    
    /**
     * Atualiza o preço atual de um investimento baseado em simulação de mercado
     */
    @Transactional
    public void atualizarPrecoMercado(Long investimentoId) {
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
            
        BigDecimal novoPreco = calcularNovoPreco(investimento);
        BigDecimal variacao = calcularVariacaoPercentual(investimento.getPrecoBase(), novoPreco);
        
        investimento.setPrecoAtual(novoPreco);
        investimento.setVariacaoPercentual(variacao);
        investimento.setUltimaAtualizacaoPreco(LocalDateTime.now());
        
        investimentoRepository.save(investimento);
    }
    
    /**
     * Atualiza todos os preços de mercado
     */
    @Transactional
    public void atualizarTodosPrecos() {
        List<Investimento> investimentos = investimentoRepository.findByAtivoTrue();
        
        for (Investimento investimento : investimentos) {
            atualizarPrecoMercado(investimento.getId());
        }
    }
    
    /**
     * Obtém o preço atual de mercado de um investimento
     */
    public BigDecimal obterPrecoAtual(Long investimentoId) {
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
            
        // Se não tem preço atual definido ou está muito desatualizado, atualiza
        if (investimento.getPrecoAtual() == null || 
            investimento.getUltimaAtualizacaoPreco() == null ||
            investimento.getUltimaAtualizacaoPreco().isBefore(LocalDateTime.now().minusHours(1))) {
            atualizarPrecoMercado(investimentoId);
            investimento = investimentoRepository.findById(investimentoId).get();
        }
        
        return investimento.getPrecoAtual();
    }
    
    /**
     * Calcula novo preço baseado no tipo de investimento e volatilidade
     */
    private BigDecimal calcularNovoPreco(Investimento investimento) {
        BigDecimal precoBase = investimento.getPrecoBase();
        if (precoBase == null) {
            return BigDecimal.valueOf(100); // Preço padrão se não definido
        }
        
        // Define volatilidade baseada no risco e categoria
        double volatilidade = obterVolatilidadePorTipo(investimento);
        
        // Gera variação aleatória dentro da volatilidade
        double variacaoAleatoria = (random.nextGaussian() * volatilidade);
        
        // Aplica a variação ao preço atual (ou base se não existe preço atual)
        BigDecimal precoAnterior = investimento.getPrecoAtual() != null ? 
            investimento.getPrecoAtual() : precoBase;
            
        BigDecimal variacao = precoAnterior.multiply(BigDecimal.valueOf(variacaoAleatoria));
        BigDecimal novoPreco = precoAnterior.add(variacao);
        
        // Garante que o preço não fique negativo ou muito distante do preço base
        BigDecimal precoMinimo = precoBase.multiply(BigDecimal.valueOf(0.5)); // Mínimo 50% do preço base
        BigDecimal precoMaximo = precoBase.multiply(BigDecimal.valueOf(2.0)); // Máximo 200% do preço base
        
        if (novoPreco.compareTo(precoMinimo) < 0) {
            novoPreco = precoMinimo;
        } else if (novoPreco.compareTo(precoMaximo) > 0) {
            novoPreco = precoMaximo;
        }
        
        return novoPreco.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Define volatilidade baseada no tipo de investimento
     */
    private double obterVolatilidadePorTipo(Investimento investimento) {
        switch (investimento.getRisco()) {
            case BAIXO:
                return 0.005; // 0.5% de volatilidade para risco baixo (ex: Tesouro)
            case MEDIO:
                return 0.015; // 1.5% para risco médio (ex: Fundos)
            case ALTO:
                return 0.035; // 3.5% para risco alto (ex: Ações)
            default:
                return 0.02; // 2% padrão
        }
    }
    
    /**
     * Calcula variação percentual entre preço base e atual
     */
    private BigDecimal calcularVariacaoPercentual(BigDecimal precoBase, BigDecimal precoAtual) {
        if (precoBase == null || precoBase.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal diferenca = precoAtual.subtract(precoBase);
        BigDecimal percentual = diferenca.divide(precoBase, 4, RoundingMode.HALF_UP)
                                       .multiply(BigDecimal.valueOf(100));
        
        return percentual.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Inicializa preços para investimentos que não têm preço atual
     */
    @Transactional
    public void inicializarPrecos() {
        List<Investimento> investimentos = investimentoRepository.findAll();
        
        for (Investimento investimento : investimentos) {
            if (investimento.getPrecoAtual() == null) {
                investimento.setPrecoAtual(investimento.getPrecoBase());
                investimento.setVariacaoPercentual(BigDecimal.ZERO);
                investimento.setUltimaAtualizacaoPreco(LocalDateTime.now());
                investimentoRepository.save(investimento);
            }
        }
    }
}