package com.example.demo.investimento.service;

import com.example.demo.investimento.dto.InvestimentoDTO;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.investimento.repository.InvestimentoRecomendadoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import com.example.demo.exception.RecursoNaoEncontradoException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InvestimentoRecomendadoRepository investimentoRecomendadoRepository;

    public InvestimentoService(InvestimentoRepository investimentoRepository, 
                              UsuarioRepository usuarioRepository,
                              InvestimentoRecomendadoRepository investimentoRecomendadoRepository) {
        this.investimentoRepository = investimentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRecomendadoRepository = investimentoRecomendadoRepository;
    }

    public List<Investimento> listarTodos() {
        return investimentoRepository.findAll();
    }

    public Investimento salvar(Investimento investimento) {
        return investimentoRepository.save(investimento);
    }

    public Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
    }

    @Transactional
    public Investimento toggleAtivo(Long investimentoId) {
        Investimento investimento = buscarPorId(investimentoId);
        investimento.setAtivo(!investimento.isAtivo());
        return investimentoRepository.save(investimento);
    }

    /**
     * Verifica se há estoque disponível para compra
     * @param investimentoId ID do investimento
     * @param quantidadeDesejada Quantidade desejada para compra
     * @return true se há estoque suficiente, false caso contrário
     */
    public boolean temEstoqueDisponivel(Long investimentoId, Long quantidadeDesejada) {
        Investimento investimento = buscarPorId(investimentoId);
        Long disponivel = investimento.getQuantidadeDisponivel();
        return disponivel != null && disponivel >= quantidadeDesejada;
    }

    /**
     * Retorna informações do estoque de um investimento
     * @param investimentoId ID do investimento
     * @return String com informações do estoque
     */
    public String getInfoEstoque(Long investimentoId) {
        Investimento investimento = buscarPorId(investimentoId);
        Long total = investimento.getQuantidadeTotal();
        Long disponivel = investimento.getQuantidadeDisponivel();
        Long vendidas = (total != null && disponivel != null) ? total - disponivel : 0;
        
        return String.format("📊 Estoque %s: %d/%d disponíveis (%d vendidas)", 
                           investimento.getSimbolo(), 
                           disponivel != null ? disponivel : 0,
                           total != null ? total : 0,
                           vendidas);
    }

    public void deletar(Long id) {
        Optional<Investimento> investimento = investimentoRepository.findById(id);

        if (investimento.isEmpty()) {
            throw new RecursoNaoEncontradoException("Investimento com ID " + id + " não encontrado");
        }

        investimentoRepository.deleteById(id);
    }



    // Lista investimentos de um usuário específico
    public List<Investimento> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Set<Investimento> investimentos = usuario.getInvestimentos();
        return investimentos.stream().collect(Collectors.toList());
    }

    // Vincula ou desvincula um investimento a um usuário
    @Transactional
    public Investimento vincularInvestimentoAUsuario(Long investimentoId, Long usuarioId) {
        Investimento investimento = buscarPorId(investimentoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (investimento.getUsuarios().contains(usuario)) {
            // Se já vinculado, remove a associação dos dois lados
            investimento.getUsuarios().remove(usuario);
            usuario.getInvestimentos().remove(investimento);
        } else {
            // Se não vinculado, adiciona a associação dos dois lados
            investimento.getUsuarios().add(usuario);
            usuario.getInvestimentos().add(investimento);
        }

        // Salva as duas entidades para garantir sincronização
        usuarioRepository.save(usuario);
        return investimentoRepository.save(investimento);
    }

    /**
     * Lista investimentos com filtros opcionais
     * Se for admin: vê todos os investimentos
     * Se for usuário comum: vê apenas investimentos visíveis
     */
    public List<Investimento> listarComFiltros(String nome, String simbolo, String categoria, 
                                             String risco, Boolean ativo, Boolean visivel,
                                             String precoMin, String precoMax, boolean ehAdmin) {
        try {
            // Converte strings para enums e BigDecimal
            com.example.demo.investimento.model.Categoria categoriaEnum = null;
            if (categoria != null && !categoria.trim().isEmpty()) {
                categoriaEnum = com.example.demo.investimento.model.Categoria.valueOf(categoria.toUpperCase());
            }
            
            com.example.demo.investimento.model.Risco riscoEnum = null;
            if (risco != null && !risco.trim().isEmpty()) {
                riscoEnum = com.example.demo.investimento.model.Risco.valueOf(risco.toUpperCase());
            }
            
            java.math.BigDecimal precoMinDecimal = null;
            if (precoMin != null && !precoMin.trim().isEmpty()) {
                precoMinDecimal = new java.math.BigDecimal(precoMin);
            }
            
            java.math.BigDecimal precoMaxDecimal = null;
            if (precoMax != null && !precoMax.trim().isEmpty()) {
                precoMaxDecimal = new java.math.BigDecimal(precoMax);
            }
            
            // Se é admin, usa o filtro completo
            if (ehAdmin) {
                return investimentoRepository.findInvestimentosComFiltros(
                    nome, simbolo, categoriaEnum, riscoEnum, ativo, visivel,
                    precoMinDecimal, precoMaxDecimal
                );
            } else {
                // Se é usuário comum, usa apenas filtro para investimentos visíveis
                return investimentoRepository.findInvestimentosVisiveis(
                    nome, simbolo, categoriaEnum, riscoEnum,
                    precoMinDecimal, precoMaxDecimal
                );
            }
        } catch (Exception e) {
            // Em caso de erro nos filtros, retorna lista baseada na permissão
            if (ehAdmin) {
                return investimentoRepository.findAll();
            } else {
                return investimentoRepository.findByVisivelParaUsuariosTrueAndAtivoTrue();
            }
        }
    }

    /**
     * Toggle da visibilidade do investimento (apenas admin)
     */
    @Transactional
    public Investimento toggleVisibilidade(Long investimentoId) {
        Investimento investimento = buscarPorId(investimentoId);
        investimento.setVisivelParaUsuarios(!investimento.isVisivelParaUsuarios());
        return investimentoRepository.save(investimento);
    }

    /**
     * Converte uma lista de investimentos para DTOs, populando o campo recomendadoParaVoce
     * Se o usuário NÃO tem nenhuma recomendação, o campo será null
     * Se o usuário TEM recomendações, será true/false para cada investimento
     */
    public List<InvestimentoDTO> converterParaDTOComRecomendacao(List<Investimento> investimentos, 
                                                                  Long usuarioId, 
                                                                  boolean incluirUsuarioIds) {
        // Verificar se o usuário tem ALGUMA recomendação
        boolean usuarioTemRecomendacoes = investimentoRecomendadoRepository.existsByUsuarioId(usuarioId);
        
        // Se não tem nenhuma recomendação, retorna com campo null
        if (!usuarioTemRecomendacoes) {
            return investimentos.stream()
                .map(inv -> {
                    InvestimentoDTO dto = new InvestimentoDTO(inv, incluirUsuarioIds);
                    dto.setRecomendadoParaVoce(null); // null indica que usuário não tem recomendações
                    return dto;
                })
                .collect(Collectors.toList());
        }
        
        // Se tem recomendações, buscar os IDs recomendados e marcar true/false
        Set<Long> investimentosRecomendadosIds = investimentoRecomendadoRepository
            .findByUsuarioId(usuarioId)
            .stream()
            .map(rec -> rec.getInvestimento().getId())
            .collect(Collectors.toSet());
        
        return investimentos.stream()
            .map(inv -> {
                InvestimentoDTO dto = new InvestimentoDTO(inv, incluirUsuarioIds);
                dto.setRecomendadoParaVoce(investimentosRecomendadosIds.contains(inv.getId()));
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Converte um investimento para DTO, populando o campo recomendadoParaVoce
     * Se o usuário NÃO tem nenhuma recomendação, o campo será null
     * Se o usuário TEM recomendações, será true/false
     */
    public InvestimentoDTO converterParaDTOComRecomendacao(Investimento investimento, 
                                                           Long usuarioId, 
                                                           boolean incluirUsuarioIds) {
        InvestimentoDTO dto = new InvestimentoDTO(investimento, incluirUsuarioIds);
        
        // Verificar se o usuário tem ALGUMA recomendação
        boolean usuarioTemRecomendacoes = investimentoRecomendadoRepository.existsByUsuarioId(usuarioId);
        
        if (!usuarioTemRecomendacoes) {
            dto.setRecomendadoParaVoce(null); // null indica que usuário não tem recomendações
        } else {
            boolean recomendado = investimentoRecomendadoRepository
                .existsByUsuarioIdAndInvestimentoId(usuarioId, investimento.getId());
            dto.setRecomendadoParaVoce(recomendado);
        }
        
        return dto;
    }
}
