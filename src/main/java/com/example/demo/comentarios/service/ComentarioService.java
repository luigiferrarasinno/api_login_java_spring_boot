package com.example.demo.comentarios.service;

import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.repository.ComentarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final InvestimentoRepository investimentoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository,
                           UsuarioRepository usuarioRepository,
                           InvestimentoRepository investimentoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
    }

    /**
     * Criar novo comentário
     */
    @Transactional
    public Comentario criarComentario(String conteudo, Long investimentoId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
        
        Comentario comentario = new Comentario(conteudo, usuario, investimento);
        return comentarioRepository.save(comentario);
    }

    /**
     * Buscar comentários de um investimento específico
     */
    public List<Comentario> buscarComentariosPorInvestimento(Long investimentoId) {
        return comentarioRepository.findByInvestimentoIdAndAtivoTrue(investimentoId);
    }

    /**
     * Buscar comentário por ID
     */
    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado"));
    }

    /**
     * Editar comentário
     * Usuário só pode editar seus próprios comentários
     * Admin pode editar qualquer comentário
     */
    @Transactional
    public Comentario editarComentario(Long comentarioId, String novoConteudo, String emailUsuario, boolean ehAdmin) {
        Comentario comentario = buscarPorId(comentarioId);
        
        // Verifica se o usuário pode editar este comentário
        if (!ehAdmin && !comentario.getUsuario().getEmail().equals(emailUsuario)) {
            throw new SecurityException("Você só pode editar seus próprios comentários");
        }
        
        comentario.setConteudo(novoConteudo);
        return comentarioRepository.save(comentario);
    }

    /**
     * Excluir comentário (soft delete)
     * Usuário só pode excluir seus próprios comentários
     * Admin pode excluir qualquer comentário
     */
    @Transactional
    public void excluirComentario(Long comentarioId, String emailUsuario, boolean ehAdmin) {
        Comentario comentario = buscarPorId(comentarioId);
        
        // Verifica se o usuário pode excluir este comentário
        if (!ehAdmin && !comentario.getUsuario().getEmail().equals(emailUsuario)) {
            throw new SecurityException("Você só pode excluir seus próprios comentários");
        }
        
        comentario.setAtivo(false);
        comentarioRepository.save(comentario);
    }

    /**
     * Buscar comentários do usuário logado
     */
    public List<Comentario> buscarComentariosDoUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        return comentarioRepository.findByUsuarioAndAtivoTrueOrderByDataCriacaoDesc(usuario);
    }

    /**
     * Buscar todos os comentários (apenas admin)
     */
    public List<Comentario> buscarTodosComentarios() {
        return comentarioRepository.findByAtivoTrueOrderByDataCriacaoDesc();
    }

    /**
     * Buscar comentários com filtros (apenas admin)
     */
    public List<Comentario> buscarComFiltros(Long investimentoId, Long usuarioId, String conteudo,
                                           String dataInicio, String dataFim) {
        try {
            LocalDateTime dataInicioDateTime = null;
            if (dataInicio != null && !dataInicio.trim().isEmpty()) {
                dataInicioDateTime = java.time.LocalDate.parse(dataInicio).atStartOfDay();
            }
            
            LocalDateTime dataFimDateTime = null;
            if (dataFim != null && !dataFim.trim().isEmpty()) {
                dataFimDateTime = java.time.LocalDate.parse(dataFim).atTime(23, 59, 59);
            }
            
            return comentarioRepository.findComFiltros(
                investimentoId, usuarioId, conteudo, dataInicioDateTime, dataFimDateTime
            );
        } catch (Exception e) {
            return comentarioRepository.findByAtivoTrueOrderByDataCriacaoDesc();
        }
    }

    /**
     * Contar comentários de um investimento
     */
    public long contarComentariosPorInvestimento(Long investimentoId) {
        return comentarioRepository.countByInvestimentoIdAndAtivoTrue(investimentoId);
    }

    /**
     * Contar comentários de um usuário
     */
    public long contarComentariosPorUsuario(Long usuarioId) {
        return comentarioRepository.countByUsuarioIdAndAtivoTrue(usuarioId);
    }

    /**
     * Verificar se usuário é dono do comentário
     */
    public boolean ehDonoDoComentario(Long comentarioId, String emailUsuario) {
        try {
            Comentario comentario = buscarPorId(comentarioId);
            return comentario.getUsuario().getEmail().equals(emailUsuario);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}