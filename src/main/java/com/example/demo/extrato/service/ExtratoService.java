package com.example.demo.extrato.service;

import com.example.demo.extrato.model.Extrato;
import com.example.demo.extrato.model.TipoTransacao;
import com.example.demo.extrato.dto.ExtratoResponseDTO;
import com.example.demo.extrato.repository.ExtratoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.carteira.model.PosicaoCarteira;
import com.example.demo.carteira.repository.PosicaoCarteiraRepository;
import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.investimento.service.CotacaoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtratoService {

    private final ExtratoRepository extratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final PosicaoCarteiraRepository posicaoCarteiraRepository;
    private final CotacaoService cotacaoService;
    private final PagamentoDividendoService pagamentoDividendoService;

    public ExtratoService(ExtratoRepository extratoRepository, 
                         UsuarioRepository usuarioRepository,
                         InvestimentoRepository investimentoRepository,
                         PosicaoCarteiraRepository posicaoCarteiraRepository,
                         CotacaoService cotacaoService,
                         PagamentoDividendoService pagamentoDividendoService) {
        this.extratoRepository = extratoRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
        this.posicaoCarteiraRepository = posicaoCarteiraRepository;
        this.cotacaoService = cotacaoService;
        this.pagamentoDividendoService = pagamentoDividendoService;
    }    @Transactional
    public String depositar(String emailUsuario, BigDecimal valor) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valor);
        
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.DEPOSITO, valor, saldoAnterior, novoSaldo,
                                     "Depósito realizado na carteira");
        extratoRepository.save(extrato);
        
        return "Depósito de R$ " + valor + " realizado com sucesso!";
    }

    @Transactional
    public String sacar(String emailUsuario, BigDecimal valor) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        
        if (saldoAnterior.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque");
        }
        
        BigDecimal novoSaldo = saldoAnterior.subtract(valor);
        
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.SAQUE, valor.negate(), saldoAnterior, novoSaldo,
                                     "Saque realizado da carteira");
        extratoRepository.save(extrato);
        
        return "Saque de R$ " + valor + " realizado com sucesso!";
    }

    @Transactional
    public String comprarAcao(String emailUsuario, Long investimentoId, BigDecimal quantidade, BigDecimal precoUnitario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);
        
        BigDecimal valorTotal = quantidade.multiply(precoUnitario);
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        
        if (saldoAnterior.compareTo(valorTotal) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a compra");
        }
        
        // Debitar do saldo
        BigDecimal novoSaldo = saldoAnterior.subtract(valorTotal);
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Atualizar posição na carteira
        atualizarPosicaoCompra(usuario, investimento, quantidade, precoUnitario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.COMPRA_ACAO, valorTotal.negate(), saldoAnterior, novoSaldo,
                                     "Compra de " + quantidade + " ações de " + investimento.getNome());
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidade);
        extrato.setPrecoUnitario(precoUnitario);
        extratoRepository.save(extrato);
        
        // 🎯 NOVO: Pagar dividendo imediato se o investimento paga dividendos
        String mensgemDividendo = pagamentoDividendoService.pagarDividendoImediato(usuario, investimento, quantidade);
        
        String mensagemCompra = "Compra de " + quantidade + " ações de " + investimento.getNome() + " realizada com sucesso!";
        if (mensgemDividendo != null) {
            return mensagemCompra + "\n" + mensgemDividendo;
        }
        return mensagemCompra;
    }

    /**
     * Compra ação pelo preço atual de mercado (RECOMENDADO)
     * SISTEMA BRASILEIRO: Apenas números inteiros
     */
    @Transactional
    public String comprarAcaoPrecoMercado(String emailUsuario, Long investimentoId, Integer quantidade) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);
        
        // Verificar estoque disponível
        Long quantidadeDisponivel = investimento.getQuantidadeDisponivel();
        Long quantidadeDesejada = quantidade.longValue();
        
        if (quantidadeDisponivel == null || quantidadeDisponivel < quantidadeDesejada) {
            throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + 
                (quantidadeDisponivel != null ? quantidadeDisponivel : 0) + " ações");
        }
        
        // Obter preço atual de mercado
        BigDecimal precoAtual = cotacaoService.obterPrecoAtual(investimentoId);
        BigDecimal valorTotal = BigDecimal.valueOf(quantidade).multiply(precoAtual);
        
        // Verificar saldo
        if (usuario.getSaldoCarteira().compareTo(valorTotal) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para compra");
        }
        
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.subtract(valorTotal);
        
        // Debitar do saldo
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Diminuir estoque disponível
        investimento.setQuantidadeDisponivel(quantidadeDisponivel - quantidadeDesejada);
        investimentoRepository.save(investimento);
        
        // Atualizar posição na carteira (converter para BigDecimal)
        BigDecimal quantidadeBigDecimal = BigDecimal.valueOf(quantidade);
        atualizarPosicaoCompra(usuario, investimento, quantidadeBigDecimal, precoAtual);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.COMPRA_ACAO, valorTotal, saldoAnterior, novoSaldo,
                                     "Compra de " + quantidade + " ações de " + investimento.getNome() + " a R$ " + precoAtual + " por ação");
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidadeBigDecimal);
        extrato.setPrecoUnitario(precoAtual);
        extratoRepository.save(extrato);
        
        // 🎯 NOVO: Pagar dividendo imediato se o investimento paga dividendos
        String mensagemDividendo = pagamentoDividendoService.pagarDividendoImediato(usuario, investimento, quantidadeBigDecimal);
        
        String mensagemCompra = "Compra realizada: " + quantidade + " ações de " + investimento.getNome() + " por R$ " + valorTotal + " (R$ " + precoAtual + "/ação)";
        if (mensagemDividendo != null) {
            return mensagemCompra + "\n" + mensagemDividendo;
        }
        return mensagemCompra;
    }

    @Transactional
    public String venderAcao(String emailUsuario, Long investimentoId, BigDecimal quantidade, BigDecimal precoUnitario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);
        
        // Verificar se possui a ação
        Optional<PosicaoCarteira> posicaoOpt = posicaoCarteiraRepository.findByUsuarioAndInvestimento(usuario, investimento);
        if (posicaoOpt.isEmpty() || posicaoOpt.get().getQuantidadeTotal().compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente de ações para venda");
        }
        
        BigDecimal valorTotal = quantidade.multiply(precoUnitario);
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorTotal);
        
        // Creditar no saldo
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Atualizar posição na carteira
        atualizarPosicaoVenda(usuario, investimento, quantidade);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.VENDA_ACAO, valorTotal, saldoAnterior, novoSaldo,
                                     "Venda de " + quantidade + " ações de " + investimento.getNome());
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidade);
        extrato.setPrecoUnitario(precoUnitario);
        extratoRepository.save(extrato);
        
        return "Venda de " + quantidade + " ações de " + investimento.getNome() + " realizada com sucesso!";
    }

    /**
     * Novo método de venda que usa preço de mercado automaticamente
     * SISTEMA BRASILEIRO: Apenas números inteiros
     */
    @Transactional
    public String venderAcaoPrecoMercado(String emailUsuario, Long investimentoId, Integer quantidade) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);
        
        // Converter para BigDecimal para comparações
        BigDecimal quantidadeBigDecimal = BigDecimal.valueOf(quantidade);
        
        // Verificar se possui a ação
        Optional<PosicaoCarteira> posicaoOpt = posicaoCarteiraRepository.findByUsuarioAndInvestimento(usuario, investimento);
        if (posicaoOpt.isEmpty() || posicaoOpt.get().getQuantidadeTotal().compareTo(quantidadeBigDecimal) < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente de ações para venda");
        }
        
        // Obter preço atual de mercado
        BigDecimal precoAtual = cotacaoService.obterPrecoAtual(investimentoId);
        BigDecimal valorTotal = quantidadeBigDecimal.multiply(precoAtual);
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorTotal);
        
        // Creditar no saldo
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Devolver ações ao estoque disponível
        Long quantidadeAtualDisponivel = investimento.getQuantidadeDisponivel();
        investimento.setQuantidadeDisponivel(quantidadeAtualDisponivel + quantidade.longValue());
        investimentoRepository.save(investimento);
        
        // Atualizar posição na carteira
        atualizarPosicaoVenda(usuario, investimento, quantidadeBigDecimal);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.VENDA_ACAO, valorTotal, saldoAnterior, novoSaldo,
                                     "Venda de " + quantidade + " ações de " + investimento.getNome() + " a R$ " + precoAtual + " por ação");
        extrato.setInvestimento(investimento);
        extrato.setQuantidade(quantidadeBigDecimal);
        extrato.setPrecoUnitario(precoAtual);
        extratoRepository.save(extrato);
        
        return "Venda realizada: " + quantidade + " ações de " + investimento.getNome() + " por R$ " + valorTotal + " (R$ " + precoAtual + "/ação)";
    }

    @Transactional
    public String receberDividendo(String emailUsuario, Long investimentoId, BigDecimal valorDividendo) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);
        
        BigDecimal saldoAnterior = usuario.getSaldoCarteira();
        BigDecimal novoSaldo = saldoAnterior.add(valorDividendo);
        
        // Creditar dividendo
        usuario.setSaldoCarteira(novoSaldo);
        usuarioRepository.save(usuario);
        
        // Registrar no extrato
        Extrato extrato = new Extrato(usuario, TipoTransacao.DIVIDENDO_RECEBIDO, valorDividendo, saldoAnterior, novoSaldo,
                                     "Dividendo recebido de " + investimento.getNome());
        extrato.setInvestimento(investimento);
        extratoRepository.save(extrato);
        
        return "Dividendo de R$ " + valorDividendo + " de " + investimento.getNome() + " creditado!";
    }

    public List<ExtratoResponseDTO> obterExtrato(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Extrato> extratos = extratoRepository.findByUsuarioOrderByDataTransacaoDesc(usuario);
        
        return extratos.stream()
                      .map(this::converterParaDTO)
                      .collect(Collectors.toList());
    }

    public List<ExtratoResponseDTO> obterExtratoPorInvestimento(String emailUsuario, Long investimentoId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Extrato> extratos = extratoRepository.findByUsuarioAndInvestimento(usuario, investimentoId);
        
        return extratos.stream()
                      .map(this::converterParaDTO)
                      .collect(Collectors.toList());
    }

    private void atualizarPosicaoCompra(Usuario usuario, Investimento investimento, BigDecimal quantidade, BigDecimal precoUnitario) {
        Optional<PosicaoCarteira> posicaoOpt = posicaoCarteiraRepository.findByUsuarioAndInvestimento(usuario, investimento);
        
        PosicaoCarteira posicao;
        if (posicaoOpt.isPresent()) {
            posicao = posicaoOpt.get();
        } else {
            posicao = new PosicaoCarteira(usuario, investimento);
        }
        
        posicao.adicionarCompra(quantidade, precoUnitario);
        posicaoCarteiraRepository.save(posicao);
    }

    private void atualizarPosicaoVenda(Usuario usuario, Investimento investimento, BigDecimal quantidade) {
        PosicaoCarteira posicao = posicaoCarteiraRepository.findByUsuarioAndInvestimento(usuario, investimento)
                                                          .orElseThrow(() -> new IllegalArgumentException("Posição não encontrada"));
        
        posicao.removerVenda(quantidade);
        
        // Se a posição zerou, deletar do banco (histórico está no extrato)
        if (posicao.getQuantidadeTotal().compareTo(BigDecimal.ZERO) == 0) {
            posicaoCarteiraRepository.delete(posicao);
        } else {
            posicaoCarteiraRepository.save(posicao);
        }
    }

    private ExtratoResponseDTO converterParaDTO(Extrato extrato) {
        ExtratoResponseDTO dto = new ExtratoResponseDTO();
        dto.setId(extrato.getId());
        dto.setTipoTransacao(extrato.getTipoTransacao());
        dto.setDescricaoTransacao(extrato.getTipoTransacao().getDescricao());
        dto.setQuantidade(extrato.getQuantidade());
        dto.setPrecoUnitario(extrato.getPrecoUnitario());
        dto.setValorTotal(extrato.getValorTotal());
        dto.setSaldoAnterior(extrato.getSaldoAnterior());
        dto.setSaldoAtual(extrato.getSaldoAtual());
        dto.setDescricao(extrato.getDescricao());
        dto.setDataTransacao(extrato.getDataTransacao());
        
        if (extrato.getInvestimento() != null) {
            dto.setNomeInvestimento(extrato.getInvestimento().getNome());
            dto.setSimboloInvestimento(extrato.getInvestimento().getSimbolo());
        }
        
        return dto;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                               .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }
    
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                               .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    private Investimento buscarInvestimentoPorId(Long id) {
        return investimentoRepository.findById(id)
                                   .orElseThrow(() -> new RecursoNaoEncontradoException("Investimento não encontrado: " + id));
    }
    
    // ===== NOVOS MÉTODOS PARA RESUMOS DE INVESTIMENTO =====
    
    /**
     * 📊 Gera resumo completo de investimentos com filtros
     */
    public com.example.demo.extrato.dto.ResumoCompletoDTO gerarResumo(String emailUsuario, Integer ano, Integer mes, Integer mesInicio, Integer mesFim, Long investimentoId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        
        List<Extrato> extratos;
        String periodo;
        
        if (ano != null && mes != null && investimentoId != null) {
            // Filtro específico: mês, ano e investimento
            extratos = extratoRepository.findByUsuarioAndInvestimentoAndMesAno(usuario, investimentoId, ano, mes);
            periodo = String.format("%04d-%02d - Investimento ID %d", ano, mes, investimentoId);
        } else if (ano != null && mesInicio != null && mesFim != null && investimentoId != null) {
            // Filtro: período de meses + investimento específico
            extratos = filtrarPorPeriodoEInvestimento(usuario, ano, mesInicio, mesFim, investimentoId);
            periodo = String.format("%04d-%02d a %02d - Investimento ID %d", ano, mesInicio, mesFim, investimentoId);
        } else if (ano != null && mesInicio != null && mesFim != null) {
            // Filtro: período de meses
            extratos = filtrarPorPeriodo(usuario, ano, mesInicio, mesFim);
            periodo = String.format("%04d-%02d a %02d", ano, mesInicio, mesFim);
        } else if (ano != null && mes != null) {
            // Filtro por mês e ano
            extratos = extratoRepository.findByUsuarioAndMesAno(usuario, ano, mes);
            periodo = String.format("%04d-%02d", ano, mes);
        } else if (investimentoId != null) {
            // Filtro por investimento específico
            extratos = extratoRepository.findByUsuarioAndInvestimento(usuario, investimentoId);
            periodo = String.format("Todo período - Investimento ID %d", investimentoId);
        } else {
            // Todos os extratos de investimentos
            extratos = extratoRepository.findTransacoesInvestimentoByUsuario(usuario);
            periodo = "Todo período";
        }
        
        return construirResumoCompleto(usuario, extratos, periodo);
    }
    
    /**
     * 📅 Filtra extratos por período de meses
     */
    private List<Extrato> filtrarPorPeriodo(Usuario usuario, Integer ano, Integer mesInicio, Integer mesFim) {
        List<Extrato> extratosCompletos = extratoRepository.findTransacoesInvestimentoByUsuario(usuario);
        
        return extratosCompletos.stream()
            .filter(extrato -> {
                int anoTransacao = extrato.getDataTransacao().getYear();
                int mesTransacao = extrato.getDataTransacao().getMonthValue();
                
                return anoTransacao == ano && mesTransacao >= mesInicio && mesTransacao <= mesFim;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 📈 Filtra extratos por período de meses e investimento específico
     */
    private List<Extrato> filtrarPorPeriodoEInvestimento(Usuario usuario, Integer ano, Integer mesInicio, Integer mesFim, Long investimentoId) {
        List<Extrato> extratosCompletos = extratoRepository.findTransacoesInvestimentoByUsuarioAndInvestimento(usuario, investimentoId);
        
        return extratosCompletos.stream()
            .filter(extrato -> {
                int anoTransacao = extrato.getDataTransacao().getYear();
                int mesTransacao = extrato.getDataTransacao().getMonthValue();
                
                return anoTransacao == ano && mesTransacao >= mesInicio && mesTransacao <= mesFim;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 🔧 Constrói o DTO de resumo completo baseado nos extratos
     */
    private com.example.demo.extrato.dto.ResumoCompletoDTO construirResumoCompleto(Usuario usuario, List<Extrato> extratos, String periodo) {
        com.example.demo.extrato.dto.ResumoCompletoDTO resumo = new com.example.demo.extrato.dto.ResumoCompletoDTO();
        resumo.setPeriodo(periodo);
        resumo.setNomeUsuario(usuario.getNomeUsuario());
        resumo.setEmailUsuario(usuario.getEmail());
        
        // Agrupar extratos por investimento
        var extratosPorInvestimento = extratos.stream()
            .collect(Collectors.groupingBy(e -> e.getInvestimento().getId()));
        
        List<com.example.demo.extrato.dto.ResumoInvestimentoDTO> resumosInvestimentos = extratosPorInvestimento.entrySet().stream()
            .map(entry -> {
                List<Extrato> extratosInvestimento = entry.getValue();
                Investimento investimento = extratosInvestimento.get(0).getInvestimento();
                
                return calcularResumoInvestimento(investimento, extratosInvestimento, periodo);
            })
            .collect(Collectors.toList());
        
        resumo.setInvestimentos(resumosInvestimentos);
        
        // Calcular totais gerais
        calcularTotaisGerais(resumo, resumosInvestimentos);
        
        // Calcular estatísticas
        calcularEstatisticas(resumo, resumosInvestimentos);
        
        return resumo;
    }
    
    /**
     * 📈 Calcula resumo para um investimento específico
     */
    private com.example.demo.extrato.dto.ResumoInvestimentoDTO calcularResumoInvestimento(Investimento investimento, List<Extrato> extratos, String periodo) {
        com.example.demo.extrato.dto.ResumoInvestimentoDTO resumo = new com.example.demo.extrato.dto.ResumoInvestimentoDTO(
            periodo, investimento.getId(), investimento.getSimbolo(), investimento.getNome()
        );
        
        BigDecimal totalInvestido = BigDecimal.ZERO;
        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal totalDividendos = BigDecimal.ZERO;
        int numeroOperacoes = 0;
        
        List<com.example.demo.extrato.dto.ExtratoResumoDTO> transacoes = extratos.stream()
            .map(extrato -> new com.example.demo.extrato.dto.ExtratoResumoDTO(
                extrato.getId(),
                extrato.getTipoTransacao(),
                extrato.getQuantidade(),
                extrato.getPrecoUnitario(),
                extrato.getValorTotal(),
                extrato.getDescricao(),
                extrato.getDataTransacao()
            ))
            .collect(Collectors.toList());
        
        for (Extrato extrato : extratos) {
            switch (extrato.getTipoTransacao()) {
                case COMPRA_ACAO:
                    totalInvestido = totalInvestido.add(extrato.getValorTotal().abs());
                    numeroOperacoes++;
                    break;
                case VENDA_ACAO:
                    totalRecebido = totalRecebido.add(extrato.getValorTotal());
                    numeroOperacoes++;
                    break;
                case DIVIDENDO_RECEBIDO:
                    totalDividendos = totalDividendos.add(extrato.getValorTotal());
                    break;
                case DEPOSITO:
                case SAQUE:
                    // Ignorar depósitos e saques para cálculo de investimentos
                    break;
            }
        }
        
        resumo.setTotalInvestido(totalInvestido);
        resumo.setTotalRecebido(totalRecebido);
        resumo.setTotalDividendos(totalDividendos);
        resumo.setNumeroOperacoes(numeroOperacoes);
        resumo.setTransacoes(transacoes);
        
        // Calcular resultado líquido e percentual de retorno
        BigDecimal resultadoLiquido = totalRecebido.add(totalDividendos).subtract(totalInvestido);
        resumo.setResultadoLiquido(resultadoLiquido);
        
        if (totalInvestido.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percentualRetorno = resultadoLiquido
                .divide(totalInvestido, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            resumo.setPercentualRetorno(percentualRetorno);
        }
        
        return resumo;
    }
    
    /**
     * 🧮 Calcula totais gerais do resumo
     */
    private void calcularTotaisGerais(com.example.demo.extrato.dto.ResumoCompletoDTO resumo, 
                                     List<com.example.demo.extrato.dto.ResumoInvestimentoDTO> investimentos) {
        BigDecimal totalInvestido = BigDecimal.ZERO;
        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal totalDividendos = BigDecimal.ZERO;
        int totalOperacoes = 0;
        
        for (com.example.demo.extrato.dto.ResumoInvestimentoDTO inv : investimentos) {
            totalInvestido = totalInvestido.add(inv.getTotalInvestido());
            totalRecebido = totalRecebido.add(inv.getTotalRecebido());
            totalDividendos = totalDividendos.add(inv.getTotalDividendos());
            totalOperacoes += inv.getNumeroOperacoes();
        }
        
        resumo.setTotalGeralInvestido(totalInvestido);
        resumo.setTotalGeralRecebido(totalRecebido);
        resumo.setTotalGeralDividendos(totalDividendos);
        resumo.setNumeroTotalOperacoes(totalOperacoes);
        
        BigDecimal resultadoGeral = totalRecebido.add(totalDividendos).subtract(totalInvestido);
        resumo.setResultadoGeralLiquido(resultadoGeral);
        
        if (totalInvestido.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percentualGeral = resultadoGeral
                .divide(totalInvestido, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            resumo.setPercentualRetornoGeral(percentualGeral);
        }
    }
    
    /**
     * 📊 Calcula estatísticas avançadas
     */
    private void calcularEstatisticas(com.example.demo.extrato.dto.ResumoCompletoDTO resumo, 
                                     List<com.example.demo.extrato.dto.ResumoInvestimentoDTO> investimentos) {
        int lucros = 0;
        int prejuizos = 0;
        int neutros = 0;
        BigDecimal maiorLucro = BigDecimal.ZERO;
        BigDecimal maiorPrejuizo = BigDecimal.ZERO;
        String maisRentavel = "";
        String menosRentavel = "";
        BigDecimal melhorPercentual = new BigDecimal("-999999");
        BigDecimal piorPercentual = new BigDecimal("999999");
        
        for (com.example.demo.extrato.dto.ResumoInvestimentoDTO inv : investimentos) {
            switch (inv.getSituacao()) {
                case "LUCRO" -> lucros++;
                case "PREJUIZO" -> prejuizos++;
                case "NEUTRO" -> neutros++;
            }
            
            if (inv.getResultadoLiquido().compareTo(maiorLucro) > 0) {
                maiorLucro = inv.getResultadoLiquido();
            }
            
            if (inv.getResultadoLiquido().compareTo(maiorPrejuizo) < 0) {
                maiorPrejuizo = inv.getResultadoLiquido();
            }
            
            if (inv.getPercentualRetorno().compareTo(melhorPercentual) > 0) {
                melhorPercentual = inv.getPercentualRetorno();
                maisRentavel = inv.getSimboloInvestimento();
            }
            
            if (inv.getPercentualRetorno().compareTo(piorPercentual) < 0) {
                piorPercentual = inv.getPercentualRetorno();
                menosRentavel = inv.getSimboloInvestimento();
            }
        }
        
        resumo.setQuantidadeInvestimentosComLucro(lucros);
        resumo.setQuantidadeInvestimentosComPrejuizo(prejuizos);
        resumo.setQuantidadeInvestimentosNeutros(neutros);
        resumo.setMaiorLucroIndividual(maiorLucro);
        resumo.setMaiorPrejuizoIndividual(maiorPrejuizo);
        resumo.setInvestimentoMaisRentavel(maisRentavel);
        resumo.setInvestimentoMenosRentavel(menosRentavel);
    }
}