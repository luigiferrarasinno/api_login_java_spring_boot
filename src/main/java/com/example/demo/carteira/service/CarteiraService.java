package com.example.demo.carteira.service;

import com.example.demo.carteira.model.PosicaoCarteira;
import com.example.demo.carteira.dto.PosicaoCarteiraResponseDTO;
import com.example.demo.carteira.dto.ResumoCarteiraResponseDTO;
import com.example.demo.carteira.repository.PosicaoCarteiraRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.service.CotacaoService;
import com.example.demo.extrato.repository.ExtratoRepository;
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
    private final CotacaoService cotacaoService;
    private final ExtratoRepository extratoRepository;

    public CarteiraService(PosicaoCarteiraRepository posicaoCarteiraRepository,
                          UsuarioRepository usuarioRepository,
                          CotacaoService cotacaoService,
                          ExtratoRepository extratoRepository) {
        this.posicaoCarteiraRepository = posicaoCarteiraRepository;
        this.usuarioRepository = usuarioRepository;
        this.cotacaoService = cotacaoService;
        this.extratoRepository = extratoRepository;
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
        
        // Calcular total de dividendos da carteira
        BigDecimal totalDividendosCarteira = posicoesDTO.stream()
                                                       .map(PosicaoCarteiraResponseDTO::getTotalDividendosRecebidos)
                                                       .reduce(BigDecimal.ZERO, BigDecimal::add);
        resumo.setTotalDividendosCarteira(totalDividendosCarteira);
        
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
        
        // Obter preço atual do investimento via CotacaoService
        BigDecimal precoAtual = cotacaoService.obterPrecoAtual(investimento.getId());
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
        
        // Buscar total de dividendos recebidos deste investimento
        BigDecimal totalDividendos = extratoRepository.calcularTotalDividendosPorInvestimento(
            posicao.getUsuario(), 
            investimento.getId()
        );
        dto.setTotalDividendosRecebidos(totalDividendos != null ? totalDividendos : BigDecimal.ZERO);
        
        return dto;
    }

    /**
     * Método unificado para obter carteira com filtros opcionais
     * @param emailUsuarioLogado Email do usuário autenticado
     * @param usuarioId ID do usuário para filtrar (apenas para admins)
     * @param investimentoId ID do investimento para filtrar
     * @param posicaoId ID da posição específica para filtrar
     * @param incluirResumo Se deve incluir o resumo da carteira
     * @param isAdmin Se o usuário logado é admin
     * @return ResumoCarteiraResponseDTO com dados filtrados
     */
    public ResumoCarteiraResponseDTO obterCarteiraComFiltros(
            String emailUsuarioLogado,
            Long usuarioId,
            Long investimentoId,
            Long posicaoId,
            boolean incluirResumo,
            boolean isAdmin) {
        
        // Determinar qual usuário buscar
        Usuario usuario;
        if (usuarioId != null) {
            // Apenas admins podem ver carteira de outros usuários
            if (!isAdmin) {
                throw new org.springframework.security.access.AccessDeniedException(
                    "Apenas administradores podem visualizar carteira de outros usuários"
                );
            }
            usuario = buscarUsuarioPorId(usuarioId);
        } else {
            usuario = buscarUsuarioPorEmail(emailUsuarioLogado);
        }
        
        // Buscar posições com filtros
        List<PosicaoCarteira> posicoes;
        
        if (posicaoId != null) {
            // Filtro por posição específica
            PosicaoCarteira posicao = posicaoCarteiraRepository.findById(posicaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Posição não encontrada: " + posicaoId));
            
            // Verificar se a posição pertence ao usuário
            if (!posicao.getUsuario().getId().equals(usuario.getId())) {
                throw new org.springframework.security.access.AccessDeniedException(
                    "Você não tem permissão para acessar esta posição"
                );
            }
            
            posicoes = List.of(posicao);
        } else if (investimentoId != null) {
            // Filtro por investimento
            posicoes = posicaoCarteiraRepository.findPosicoesAtivasByUsuario(usuario).stream()
                .filter(p -> p.getInvestimento().getId().equals(investimentoId))
                .collect(Collectors.toList());
        } else {
            // Todas as posições do usuário
            posicoes = posicaoCarteiraRepository.findPosicoesAtivasByUsuario(usuario);
        }
        
        // Converter para DTOs
        List<PosicaoCarteiraResponseDTO> posicoesDTO = posicoes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        
        // Criar resposta
        ResumoCarteiraResponseDTO resumo = new ResumoCarteiraResponseDTO();
        
        if (incluirResumo) {
            // Incluir informações completas do resumo
            resumo.setSaldoDisponivel(usuario.getSaldoCarteira());
            resumo.setQuantidadePosicoes(posicoes.size());
            
            if (posicoes.isEmpty()) {
                resumo.setValorTotalInvestido(BigDecimal.ZERO);
                resumo.setValorAtualCarteira(BigDecimal.ZERO);
                resumo.setGanhoTotalCarteira(BigDecimal.ZERO);
                resumo.setPercentualGanhoCarteira(BigDecimal.ZERO);
                resumo.setTotalDividendosCarteira(BigDecimal.ZERO);
            } else {
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
                
                BigDecimal totalDividendosCarteira = posicoesDTO.stream()
                    .map(PosicaoCarteiraResponseDTO::getTotalDividendosRecebidos)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                resumo.setValorTotalInvestido(valorTotalInvestido);
                resumo.setValorAtualCarteira(valorAtualCarteira);
                resumo.setGanhoTotalCarteira(ganhoTotalCarteira);
                resumo.setPercentualGanhoCarteira(percentualGanhoCarteira);
                resumo.setTotalDividendosCarteira(totalDividendosCarteira);
            }
        }
        
        resumo.setPosicoes(posicoesDTO);
        
        return resumo;
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                               .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }
}