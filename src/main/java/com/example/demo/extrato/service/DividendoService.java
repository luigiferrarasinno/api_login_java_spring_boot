package com.example.demo.extrato.service;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.carteira.model.PosicaoCarteira;
import com.example.demo.carteira.repository.PosicaoCarteiraRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.extrato.model.Extrato;
import com.example.demo.extrato.model.TipoTransacao;
import com.example.demo.extrato.repository.ExtratoRepository;
import com.example.demo.extrato.model.DividendoPendente;
import com.example.demo.extrato.model.DividendoPendente.StatusDividendo;
import com.example.demo.extrato.repository.DividendoPendenteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DividendoService {
    
    private final InvestimentoRepository investimentoRepository;
    private final PosicaoCarteiraRepository posicaoCarteiraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExtratoRepository extratoRepository;
    private final DividendoPendenteRepository dividendoPendenteRepository;
    
    public DividendoService(InvestimentoRepository investimentoRepository,
                           PosicaoCarteiraRepository posicaoCarteiraRepository,
                           UsuarioRepository usuarioRepository,
                           ExtratoRepository extratoRepository,
                           DividendoPendenteRepository dividendoPendenteRepository) {
        this.investimentoRepository = investimentoRepository;
        this.posicaoCarteiraRepository = posicaoCarteiraRepository;
        this.usuarioRepository = usuarioRepository;
        this.extratoRepository = extratoRepository;
        this.dividendoPendenteRepository = dividendoPendenteRepository;
    }
    
    /**
     * Calcula dividendos e cria PENDÊNCIAS para aprovação administrativa
     * NÃO PAGA DIRETAMENTE - só cria para admin revisar
     */
    @Transactional
    public String criarPendenciasDividendos(Long investimentoId) {
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
            
        if (investimento.getDividendYield() == null || investimento.getDividendYield().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Investimento '" + investimento.getNome() + "' não paga dividendos");
        }
        
        // Buscar todas as posições deste investimento
        List<PosicaoCarteira> posicoes = posicaoCarteiraRepository.findByInvestimento(investimento);
        
        if (posicoes.isEmpty()) {
            return "Nenhum investidor possui ações de " + investimento.getNome();
        }
        
        // Calcular dividendo por ação
        BigDecimal dividendoPorAcao = calcularDividendoPorAcao(investimento);
        int pendenciasCriadas = 0;
        
        // Criar PENDÊNCIAS (não pagar ainda)
        for (PosicaoCarteira posicao : posicoes) {
            BigDecimal valorTotal = posicao.getQuantidadeTotal().multiply(dividendoPorAcao);
            
            DividendoPendente pendencia = new DividendoPendente(
                posicao.getUsuario(),
                investimento, 
                posicao.getQuantidadeTotal(),
                dividendoPorAcao,
                valorTotal
            );
            
            dividendoPendenteRepository.save(pendencia);
            pendenciasCriadas++;
        }
        
        return String.format("📋 Criadas %d pendências de dividendos para %s (R$ %.2f por ação). Aguardando aprovação administrativa.",
                            pendenciasCriadas, investimento.getNome(), dividendoPorAcao);
    }
    
    /**
     * Paga dividendo para uma posição específica
     */
    @Transactional
    public void pagarDividendo(PosicaoCarteira posicao, BigDecimal dividendoPorAcao, Investimento investimento) {
        Usuario usuario = posicao.getUsuario();
        BigDecimal quantidadeAcoes = posicao.getQuantidadeTotal();
        BigDecimal valorDividendo = quantidadeAcoes.multiply(dividendoPorAcao).setScale(2, RoundingMode.HALF_UP);
        
        // Creditar dividendo na carteira do usuário
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorDividendo);
        
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.DIVIDENDO_RECEBIDO, valorDividendo, 
                                     saldoAnterior, novoSaldo,
                                     "Dividendo de " + investimento.getNome() + " - " + quantidadeAcoes + " ações × R$ " + dividendoPorAcao);
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidadeAcoes);
        extrato.setPrecoUnitario(dividendoPorAcao);
        
        extratoRepository.save(extrato);
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
    
    /**
     * Cria pendências de dividendos para todos os investimentos que pagam dividendos
     */
    @Transactional
    public String criarTodasPendenciasDividendos() {
        List<Investimento> investimentos = investimentoRepository.findByAtivoTrue();
        int investimentosProcessados = 0;
        StringBuilder resultado = new StringBuilder();
        
        for (Investimento investimento : investimentos) {
            if (investimento.getDividendYield() != null && 
                investimento.getDividendYield().compareTo(BigDecimal.ZERO) > 0) {
                
                String msg = criarPendenciasDividendos(investimento.getId());
                resultado.append(msg).append("\n");
                investimentosProcessados++;
            }
        }
        
        return String.format("📋 Criadas pendências para %d investimentos. Admin deve revisar e aprovar.", investimentosProcessados);
    }
    
    /**
     * Calcula próxima data de pagamento baseada na frequência
     */
    public LocalDate calcularProximaDataDividendo(Investimento investimento) {
        LocalDate hoje = LocalDate.now();
        int mesesEntrePagamentos = 12 / investimento.getFrequenciaDividendo();
        
        // Próximo pagamento será no próximo período
        return hoje.plusMonths(mesesEntrePagamentos);
    }
    
    /**
     * Verifica se é hora de pagar dividendos para um investimento
     */
    public boolean ehDataPagamentoDividendo(Investimento investimento) {
        // Esta lógica pode ser expandida para verificar datas específicas
        // Por enquanto, simula que é sempre hora de pagar (para testes)
        return true;
    }

    /**
     * Distribui dividendos e retorna detalhes da operação
     */
    @Transactional
    public String distribuirDividendosComDetalhes(Long investimentoId) {
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
            
        if (investimento.getDividendYield() == null || investimento.getDividendYield().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Investimento '" + investimento.getNome() + "' não paga dividendos");
        }
        
        // Buscar todas as posições deste investimento
        List<PosicaoCarteira> posicoes = posicaoCarteiraRepository.findByInvestimento(investimento);
        
        if (posicoes.isEmpty()) {
            return "Nenhum investidor possui ações de " + investimento.getNome() + " - nenhum dividendo distribuído";
        }
        
        // Calcular dividendo por ação
        BigDecimal dividendoPorAcao = calcularDividendoPorAcao(investimento);
        BigDecimal totalDistribuido = BigDecimal.ZERO;
        int investidoresBeneficiados = 0;
        
        // Distribuir para cada posição
        for (PosicaoCarteira posicao : posicoes) {
            BigDecimal valorDividendo = posicao.getQuantidadeTotal().multiply(dividendoPorAcao);
            pagarDividendo(posicao, dividendoPorAcao, investimento);
            totalDistribuido = totalDistribuido.add(valorDividendo);
            investidoresBeneficiados++;
        }
        
        // Retornar resumo detalhado
        return String.format("💎 Dividendos distribuídos com sucesso!\n" +
                           "📊 Investimento: %s\n" +
                           "💰 Dividendo por ação: R$ %.2f\n" +
                           "👥 Investidores beneficiados: %d\n" +
                           "💸 Total distribuído: R$ %.2f\n" +
                           "📅 Frequência: %s",
                           investimento.getNome(),
                           dividendoPorAcao,
                           investidoresBeneficiados, 
                           totalDistribuido,
                           obterDescricaoFrequencia(investimento.getFrequenciaDividendo()));
    }

    /**
     * Converte frequência numérica em descrição
     */
    private String obterDescricaoFrequencia(Integer frequencia) {
        if (frequencia == null || frequencia == 0) return "Não paga";
        switch (frequencia) {
            case 1: return "Anual";
            case 2: return "Semestral";
            case 4: return "Trimestral";
            case 12: return "Mensal";
            default: return frequencia + "x por ano";
        }
    }

    /**
     * Lista dividendos pendentes de aprovação
     */
    public List<DividendoPendente> listarDividendosPendentes() {
        return dividendoPendenteRepository.findByStatusOrderByDataCalculoDesc(StatusDividendo.PENDENTE);
    }

    /**
     * Aprova e paga dividendos selecionados
     */
    @Transactional
    public String aprovarEPagarDividendos(List<Long> dividendoIds, Long adminId, String observacoes) {
        List<DividendoPendente> dividendos = dividendoPendenteRepository.findAllById(dividendoIds);
        int aprovados = 0;
        BigDecimal totalPago = BigDecimal.ZERO;
        
        for (DividendoPendente dividendo : dividendos) {
            if (dividendo.getStatus() == StatusDividendo.PENDENTE) {
                // Pagar o dividendo
                pagarDividendoAprovado(dividendo);
                
                // Marcar como aprovado e pago
                dividendo.setStatus(StatusDividendo.PAGO);
                dividendo.setAprovadoPorAdminId(adminId);
                dividendo.setDataAprovacao(java.time.LocalDateTime.now());
                dividendo.setDataPagamento(java.time.LocalDateTime.now());
                dividendo.setObservacoes(observacoes);
                
                dividendoPendenteRepository.save(dividendo);
                
                aprovados++;
                totalPago = totalPago.add(dividendo.getValorTotal());
            }
        }
        
        return String.format("💰 %d dividendos aprovados e pagos. Total: R$ %.2f", aprovados, totalPago);
    }

    /**
     * Efetiva o pagamento de um dividendo aprovado
     */
    private void pagarDividendoAprovado(DividendoPendente dividendo) {
        Usuario usuario = dividendo.getUsuario();
        BigDecimal valorDividendo = dividendo.getValorTotal();
        
        // Creditar dividendo na carteira do usuário
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorDividendo);
        
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.DIVIDENDO_RECEBIDO, valorDividendo, 
                                     saldoAnterior, novoSaldo,
                                     "Dividendo aprovado: " + dividendo.getInvestimento().getNome() + 
                                     " - " + dividendo.getQuantidadeAcoes() + " ações × R$ " + dividendo.getDividendoPorAcao());
        extrato.setInvestimento(dividendo.getInvestimento());
        extrato.setQuantidade(dividendo.getQuantidadeAcoes());
        extrato.setPrecoUnitario(dividendo.getDividendoPorAcao());
        
        extratoRepository.save(extrato);
    }

    /**
     * Rejeita dividendos selecionados
     */
    @Transactional
    public String rejeitarDividendos(List<Long> dividendoIds, Long adminId, String motivo) {
        List<DividendoPendente> dividendos = dividendoPendenteRepository.findAllById(dividendoIds);
        int rejeitados = 0;
        
        for (DividendoPendente dividendo : dividendos) {
            if (dividendo.getStatus() == StatusDividendo.PENDENTE) {
                dividendo.setStatus(StatusDividendo.REJEITADO);
                dividendo.setAprovadoPorAdminId(adminId);
                dividendo.setDataAprovacao(java.time.LocalDateTime.now());
                dividendo.setObservacoes(motivo);
                
                dividendoPendenteRepository.save(dividendo);
                rejeitados++;
            }
        }
        
        return String.format("⛔ %d dividendos rejeitados. Motivo: %s", rejeitados, motivo);
    }

    /**
     * Conta dividendos pendentes
     */
    public long contarDividendosPendentes() {
        return dividendoPendenteRepository.countByStatus(StatusDividendo.PENDENTE);
    }

    /**
     * 🎯 NOVO: Paga dividendo IMEDIATO ao comprar ação
     * Chamado automaticamente na compra - SEM aprovação administrativa
     */
    @Transactional
    public String pagarDividendoImediato(Usuario usuario, Investimento investimento, BigDecimal quantidadeComprada) {
        // Verificar se o investimento paga dividendos
        if (investimento.getDividendYield() == null || investimento.getDividendYield().compareTo(BigDecimal.ZERO) <= 0) {
            return null; // Não paga dividendo, retorna null silenciosamente
        }
        
        // Calcular dividendo por ação (mesma lógica existente)
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
}