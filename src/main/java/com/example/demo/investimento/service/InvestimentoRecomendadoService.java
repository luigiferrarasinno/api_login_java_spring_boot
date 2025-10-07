package com.example.demo.investimento.service;

import com.example.demo.investimento.model.InvestimentoRecomendado;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.dto.InvestimentoRecomendadoResponseDTO;
import com.example.demo.investimento.repository.InvestimentoRecomendadoRepository;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.exception.RecursoNaoEncontradoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestimentoRecomendadoService {
    
    private final InvestimentoRecomendadoRepository recomendadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InvestimentoRepository investimentoRepository;
    
    public InvestimentoRecomendadoService(
            InvestimentoRecomendadoRepository recomendadoRepository,
            UsuarioRepository usuarioRepository,
            InvestimentoRepository investimentoRepository) {
        this.recomendadoRepository = recomendadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
    }
    
    /**
     * Busca investimentos recomendados do usuário autenticado ou de outro usuário (se admin)
     */
    public List<InvestimentoRecomendadoResponseDTO> obterRecomendados(String emailUsuarioLogado, Long usuarioId, boolean isAdmin) {
        Usuario usuario;
        
        if (usuarioId != null) {
            // Admin quer ver recomendados de outro usuário
            if (!isAdmin) {
                throw new org.springframework.security.access.AccessDeniedException(
                    "Apenas administradores podem visualizar recomendações de outros usuários"
                );
            }
            usuario = buscarUsuarioPorId(usuarioId);
        } else {
            // Usuário quer ver seus próprios recomendados
            usuario = buscarUsuarioPorEmail(emailUsuarioLogado);
        }
        
        List<InvestimentoRecomendado> recomendados = recomendadoRepository.findByUsuario(usuario);
        
        // Filtrar investimentos invisíveis para usuários comuns
        return recomendados.stream()
            .filter(rec -> isAdmin || rec.getInvestimento().isVisivelParaUsuarios())
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Adiciona múltiplos investimentos recomendados para um usuário
     */
    @Transactional
    public List<InvestimentoRecomendadoResponseDTO> adicionarRecomendacoes(Long usuarioId, List<Long> investimentoIds, boolean isAdmin) {
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        
        List<InvestimentoRecomendado> novosRecomendados = investimentoIds.stream()
            .map(investimentoId -> {
                // Verifica se já existe
                if (recomendadoRepository.existsByUsuarioIdAndInvestimentoId(usuarioId, investimentoId)) {
                    throw new IllegalArgumentException(
                        "Investimento ID " + investimentoId + " já está recomendado para este usuário"
                    );
                }
                
                Investimento investimento = buscarInvestimentoPorId(investimentoId);
                
                // Validar que investimento é visível para usuários comuns
                if (!isAdmin && !investimento.isVisivelParaUsuarios()) {
                    throw new IllegalArgumentException(
                        "Investimento ID " + investimentoId + " não está disponível para recomendação"
                    );
                }
                
                return new InvestimentoRecomendado(usuario, investimento);
            })
            .collect(Collectors.toList());
        
        List<InvestimentoRecomendado> salvos = recomendadoRepository.saveAll(novosRecomendados);
        
        return salvos.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Remove uma recomendação específica
     */
    @Transactional
    public void removerRecomendacao(Long id, String emailUsuarioLogado, boolean isAdmin) {
        InvestimentoRecomendado recomendacao = recomendadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Recomendação não encontrada: " + id));
        
        // Verifica se o usuário tem permissão para deletar
        Usuario usuarioLogado = buscarUsuarioPorEmail(emailUsuarioLogado);
        
        if (!isAdmin && !recomendacao.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Você não tem permissão para remover esta recomendação"
            );
        }
        
        recomendadoRepository.deleteById(id);
    }
    
    /**
     * Converte entidade para DTO
     */
    private InvestimentoRecomendadoResponseDTO converterParaDTO(InvestimentoRecomendado recomendado) {
        InvestimentoRecomendadoResponseDTO dto = new InvestimentoRecomendadoResponseDTO();
        dto.setId(recomendado.getId());
        dto.setUsuarioId(recomendado.getUsuario().getId());
        dto.setInvestimentoId(recomendado.getInvestimento().getId());
        dto.setInvestimentoNome(recomendado.getInvestimento().getNome());
        dto.setInvestimentoSimbolo(recomendado.getInvestimento().getSimbolo());
        dto.setCategoria(recomendado.getInvestimento().getCategoria() != null 
            ? recomendado.getInvestimento().getCategoria().getDescricao() 
            : "N/A");
        dto.setRisco(recomendado.getInvestimento().getRisco() != null 
            ? recomendado.getInvestimento().getRisco().getDescricao() 
            : "N/A");
        dto.setDataRecomendacao(recomendado.getDataRecomendacao());
        return dto;
    }
    
    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
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
