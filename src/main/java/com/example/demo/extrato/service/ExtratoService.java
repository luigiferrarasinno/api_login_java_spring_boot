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
import java.time.LocalDateTime;
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
    private final DividendoService dividendoService;

    public ExtratoService(ExtratoRepository extratoRepository, 
                         UsuarioRepository usuarioRepository,
                         InvestimentoRepository investimentoRepository,
                         PosicaoCarteiraRepository posicaoCarteiraRepository,
                         CotacaoService cotacaoService,
                         DividendoService dividendoService) {
        this.extratoRepository = extratoRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
        this.posicaoCarteiraRepository = posicaoCarteiraRepository;
        this.cotacaoService = cotacaoService;
        this.dividendoService = dividendoService;
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
        String mensgemDividendo = dividendoService.pagarDividendoImediato(usuario, investimento, quantidade);
        
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
        String mensagemDividendo = dividendoService.pagarDividendoImediato(usuario, investimento, quantidadeBigDecimal);
        
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
        posicaoCarteiraRepository.save(posicao);
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

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                               .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }

    private Investimento buscarInvestimentoPorId(Long id) {
        return investimentoRepository.findById(id)
                                   .orElseThrow(() -> new RecursoNaoEncontradoException("Investimento não encontrado: " + id));
    }
}