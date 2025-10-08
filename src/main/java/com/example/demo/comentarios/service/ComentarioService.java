package com.example.demo.comentarios.service;

import com.example.demo.comentarios.dto.request.FiltroComentarioRequestDTO;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return criarComentario(conteudo, investimentoId, emailUsuario, null);
    }

    /**
     * Criar novo comentário ou resposta a um comentário
     */
    @Transactional
    public Comentario criarComentario(String conteudo, Long investimentoId, String emailUsuario, Long comentarioPaiId) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        Investimento investimento = investimentoRepository.findById(investimentoId)
            .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
        
        Comentario comentario;
        
        if (comentarioPaiId != null) {
            // É uma resposta a outro comentário
            Comentario comentarioPai = buscarPorId(comentarioPaiId);
            
            // Validar que o comentário pai está ativo
            if (!comentarioPai.isAtivo()) {
                throw new IllegalArgumentException("Não é possível responder a um comentário inativo");
            }
            
            // Validar que o comentário pai é do mesmo investimento
            if (!comentarioPai.getInvestimento().getId().equals(investimentoId)) {
                throw new IllegalArgumentException("O comentário pai não pertence ao mesmo investimento");
            }
            
            comentario = new Comentario(conteudo, usuario, investimento, comentarioPai);
        } else {
            // É um comentário raiz
            comentario = new Comentario(conteudo, usuario, investimento);
        }
        
        return comentarioRepository.save(comentario);
    }

    /**
     * Buscar comentários de um investimento específico
     */
    public List<Comentario> buscarComentariosPorInvestimento(Long investimentoId) {
        return comentarioRepository.findByInvestimentoIdAndAtivoTrue(investimentoId);
    }

    /**
     * Buscar apenas comentários raiz (sem pai) de um investimento
     * Útil para exibir a árvore de comentários de forma hierárquica
     */
    public List<Comentario> buscarComentariosRaizPorInvestimento(Long investimentoId) {
        List<Comentario> todosComentarios = comentarioRepository.findByInvestimentoIdAndAtivoTrue(investimentoId);
        return todosComentarios.stream()
            .filter(c -> c.getComentarioPai() == null)
            .toList();
    }

    /**
     * Buscar respostas de um comentário específico
     */
    public List<Comentario> buscarRespostasDoComentario(Long comentarioPaiId) {
        Comentario comentarioPai = buscarPorId(comentarioPaiId);
        return comentarioPai.getRespostas().stream()
            .filter(Comentario::isAtivo)
            .toList();
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

    /**
     * 🎯 MÉTODO UNIFICADO: Buscar comentários com filtros avançados
     * 
     * Este método centraliza todas as buscas de comentários, aplicando filtros
     * e ordenação de forma combinada.
     */
    public List<Comentario> buscarComentariosComFiltros(
            String emailUsuario, 
            FiltroComentarioRequestDTO filtros,
            boolean ehAdmin) {
        
        List<Comentario> comentarios;
        
        // Aplicar filtro rápido
        if (filtros.getFiltro() != null) {
            comentarios = aplicarFiltroRapido(filtros.getFiltro(), emailUsuario, filtros, ehAdmin);
        } else {
            // Sem filtro rápido, usar filtros específicos
            comentarios = aplicarFiltrosEspecificos(emailUsuario, filtros, ehAdmin);
        }
        
        // Aplicar filtros adicionais
        comentarios = aplicarFiltrosAdicionais(comentarios, filtros, ehAdmin);
        
        // Aplicar ordenação
        return ordenarComentarios(comentarios, filtros.getOrdenacao());
    }

    /**
     * Aplicar filtro rápido
     */
    private List<Comentario> aplicarFiltroRapido(
            FiltroComentarioRequestDTO.FiltroRapido filtro, 
            String emailUsuario,
            FiltroComentarioRequestDTO filtros,
            boolean ehAdmin) {
        
        return switch (filtro) {
            case MEUS -> buscarComentariosDoUsuario(emailUsuario);
            
            case INVESTIMENTO -> {
                if (filtros.getInvestimentoId() == null) {
                    throw new IllegalArgumentException("investimentoId é obrigatório para filtro INVESTIMENTO");
                }
                yield filtros.getApenasRaiz() != null && filtros.getApenasRaiz()
                    ? buscarComentariosRaizPorInvestimento(filtros.getInvestimentoId())
                    : buscarComentariosPorInvestimento(filtros.getInvestimentoId());
            }
            
            case RESPOSTAS -> {
                if (filtros.getComentarioPaiId() == null) {
                    throw new IllegalArgumentException("comentarioPaiId é obrigatório para filtro RESPOSTAS");
                }
                yield buscarRespostasDoComentario(filtros.getComentarioPaiId());
            }
            
            case TODOS -> {
                if (!ehAdmin) {
                    throw new SecurityException("Acesso negado: apenas administradores podem usar filtro TODOS");
                }
                yield comentarioRepository.findAll();
            }
            
            case TODOS_PUBLICOS -> comentarioRepository.findByAtivoTrue();
        };
    }

    /**
     * Aplicar filtros específicos quando não há filtro rápido
     */
    private List<Comentario> aplicarFiltrosEspecificos(
            String emailUsuario,
            FiltroComentarioRequestDTO filtros,
            boolean ehAdmin) {
        
        // Se tem investimentoId, buscar por investimento
        if (filtros.getInvestimentoId() != null) {
            return filtros.getApenasRaiz() != null && filtros.getApenasRaiz()
                ? buscarComentariosRaizPorInvestimento(filtros.getInvestimentoId())
                : buscarComentariosPorInvestimento(filtros.getInvestimentoId());
        }
        
        // Se tem comentarioPaiId, buscar respostas
        if (filtros.getComentarioPaiId() != null) {
            return buscarRespostasDoComentario(filtros.getComentarioPaiId());
        }
        
        // Se tem usuarioEmail e é admin, buscar por usuário
        if (filtros.getUsuarioEmail() != null && ehAdmin) {
            return buscarComentariosDoUsuario(filtros.getUsuarioEmail());
        }
        
        // Padrão: comentários do próprio usuário
        return buscarComentariosDoUsuario(emailUsuario);
    }

    /**
     * Aplicar filtros adicionais (admin apenas para alguns)
     */
    private List<Comentario> aplicarFiltrosAdicionais(
            List<Comentario> comentarios,
            FiltroComentarioRequestDTO filtros,
            boolean ehAdmin) {
        
        // Filtro por conteúdo (admin apenas)
        if (filtros.getConteudo() != null && !filtros.getConteudo().isBlank()) {
            if (!ehAdmin) {
                throw new SecurityException("Acesso negado: apenas administradores podem filtrar por conteúdo");
            }
            String conteudoBusca = filtros.getConteudo().toLowerCase();
            comentarios = comentarios.stream()
                .filter(c -> c.getConteudo().toLowerCase().contains(conteudoBusca))
                .collect(Collectors.toList());
        }
        
        // Filtro por data início (admin apenas)
        if (filtros.getDataInicio() != null && !filtros.getDataInicio().isBlank()) {
            if (!ehAdmin) {
                throw new SecurityException("Acesso negado: apenas administradores podem filtrar por data");
            }
            LocalDateTime dataInicio = LocalDateTime.parse(filtros.getDataInicio());
            comentarios = comentarios.stream()
                .filter(c -> !c.getDataCriacao().isBefore(dataInicio))
                .collect(Collectors.toList());
        }
        
        // Filtro por data fim (admin apenas)
        if (filtros.getDataFim() != null && !filtros.getDataFim().isBlank()) {
            if (!ehAdmin) {
                throw new SecurityException("Acesso negado: apenas administradores podem filtrar por data");
            }
            LocalDateTime dataFim = LocalDateTime.parse(filtros.getDataFim());
            comentarios = comentarios.stream()
                .filter(c -> !c.getDataCriacao().isAfter(dataFim))
                .collect(Collectors.toList());
        }
        
        // Filtro apenasRaiz (se ainda não foi aplicado)
        if (filtros.getApenasRaiz() != null && filtros.getApenasRaiz() 
            && filtros.getFiltro() != FiltroComentarioRequestDTO.FiltroRapido.INVESTIMENTO) {
            comentarios = comentarios.stream()
                .filter(c -> c.getComentarioPai() == null)
                .collect(Collectors.toList());
        }
        
        return comentarios;
    }

    /**
     * Ordenar comentários
     */
    private List<Comentario> ordenarComentarios(
            List<Comentario> comentarios,
            FiltroComentarioRequestDTO.OrdenacaoComentario ordenacao) {
        
        if (ordenacao == null) {
            ordenacao = FiltroComentarioRequestDTO.OrdenacaoComentario.MAIS_RECENTES;
        }
        
        return switch (ordenacao) {
            case DATA_CRIACAO_ASC, MAIS_ANTIGOS -> comentarios.stream()
                .sorted(Comparator.comparing(Comentario::getDataCriacao))
                .collect(Collectors.toList());
                
            case DATA_CRIACAO_DESC, MAIS_RECENTES -> comentarios.stream()
                .sorted(Comparator.comparing(Comentario::getDataCriacao).reversed())
                .collect(Collectors.toList());
        };
    }
}