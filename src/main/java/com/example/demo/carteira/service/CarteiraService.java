package com.example.demo.carteira.service;

import com.example.demo.carteira.model.PosicaoCarteira;
import com.example.demo.carteira.dto.PosicaoCarteiraResponseDTO;
import com.example.demo.carteira.dto.ResumoCarteiraResponseDTO;
import com.example.demo.carteira.repository.PosicaoCarteiraRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.exception.RecursoNaoEncontradoException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarteiraService {

    private final PosicaoCarteiraRepository posicaoCarteiraRepository;
    private final UsuarioRepository usuarioRepository;

    public CarteiraService(PosicaoCarteiraRepository posicaoCarteiraRepository,
                          UsuarioRepository usuarioRepository) {
        this.posicaoCarteiraRepository = posicaoCarteiraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResumoCarteiraResponseDTO obterResumoCarteira(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<PosicaoCarteira> posicoes = posicaoCarteiraRepository.findPosicoesAtivasByUsuario(usuario);
        
        ResumoCarteiraResponseDTO resumo = new ResumoCarteiraResponseDTO();
        resumo.setSaldoDisponivel(usuario.getSaldoCarteira());
        resumo.setQuantidadePosicoes(posicoes.size());
        
        if (posicoes.isEmpty()) {
            resumo.setValorTotalInvestido(BigDecimal.ZERO);
            resumo.setValorAtualCarteira(BigDecimal.ZERO);
            resumo.setGanhoTotalCarteira(BigDecimal.ZERO);
            resumo.setPercentualGanhoCarteira(BigDecimal.ZERO);
            resumo.setPosicoes(List.of());
            return resumo;
        }
        
        List<PosicaoCarteiraResponseDTO> posicoesDTO = posicoes.stream()
                                                              .map(this::converterParaDTO)
                                                              .collect(Collectors.toList());
        
        BigDecimal valorTotalInvestido = posicoes.stream()
                                                .map(PosicaoCarteira::getValorInvestido)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                                                
        BigDecimal valorAtualCarteira = posicoesDTO.stream()
                                                  .map(PosicaoCarteiraResponseDTO::getValorAtual)
                                                  .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal ganhoTotalCarteira = valorAtualCarteira.subtract(valorTotalInvestido);
        
        BigDecimal percentualGanhoCarteira = BigDecimal.ZERO;
        if (valorTotalInvestido.compareTo(BigDecimal.ZERO) > 0) {
            percentualGanhoCarteira = ganhoTotalCarteira
                                    .divide(valorTotalInvestido, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100));
        }
        
        resumo.setValorTotalInvestido(valorTotalInvestido);
        resumo.setValorAtualCarteira(valorAtualCarteira);
        resumo.setGanhoTotalCarteira(ganhoTotalCarteira);
        resumo.setPercentualGanhoCarteira(percentualGanhoCarteira);
        resumo.setPosicoes(posicoesDTO);
        
        return resumo;
    }

    public List<PosicaoCarteiraResponseDTO> obterPosicoesCarteira(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<PosicaoCarteira> posicoes = posicaoCarteiraRepository.findPosicoesAtivasByUsuario(usuario);
        
        return posicoes.stream()
                      .map(this::converterParaDTO)
                      .collect(Collectors.toList());
    }

    private PosicaoCarteiraResponseDTO converterParaDTO(PosicaoCarteira posicao) {
        Investimento investimento = posicao.getInvestimento();
        
        PosicaoCarteiraResponseDTO dto = new PosicaoCarteiraResponseDTO();
        dto.setId(posicao.getId());
        dto.setNomeInvestimento(investimento.getNome());
        dto.setSimboloInvestimento(investimento.getSimbolo());
        dto.setCategoria(investimento.getCategoria() != null ? investimento.getCategoria().getDescricao() : "N/A");
        dto.setRisco(investimento.getRisco() != null ? investimento.getRisco().getDescricao() : "N/A");
        dto.setQuantidadeTotal(posicao.getQuantidadeTotal());
        dto.setPrecoMedio(posicao.getPrecoMedio());
        dto.setValorInvestido(posicao.getValorInvestido());
        dto.setDataPrimeiraCompra(posicao.getDataPrimeiraCompra());
        dto.setDataUltimaMovimentacao(posicao.getDataUltimaMovimentacao());
        
        // Simular preço atual (em um sistema real, viria de uma API de mercado)
        BigDecimal precoAtual = simularPrecoAtual(posicao.getPrecoMedio());
        dto.setPrecoAtual(precoAtual);
        
        BigDecimal valorAtual = posicao.getQuantidadeTotal().multiply(precoAtual);
        dto.setValorAtual(valorAtual);
        
        BigDecimal ganhoPerda = valorAtual.subtract(posicao.getValorInvestido());
        dto.setGanhoPerda(ganhoPerda);
        
        BigDecimal percentualGanhoPerda = BigDecimal.ZERO;
        if (posicao.getValorInvestido().compareTo(BigDecimal.ZERO) > 0) {
            percentualGanhoPerda = ganhoPerda
                                 .divide(posicao.getValorInvestido(), 4, RoundingMode.HALF_UP)
                                 .multiply(BigDecimal.valueOf(100));
        }
        dto.setPercentualGanhoPerda(percentualGanhoPerda);
        
        return dto;
    }

    private BigDecimal simularPrecoAtual(BigDecimal precoMedio) {
        // Simula variação entre -20% e +30% do preço médio
        double variacao = -0.20 + (Math.random() * 0.50); // -20% a +30%
        BigDecimal multiplicador = BigDecimal.valueOf(1 + variacao);
        return precoMedio.multiply(multiplicador).setScale(2, RoundingMode.HALF_UP);
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                               .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }
}