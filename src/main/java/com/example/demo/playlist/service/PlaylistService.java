package com.example.demo.playlist.service;

import com.example.demo.playlist.model.Playlist;
import com.example.demo.playlist.model.PlaylistTipo;
import com.example.demo.playlist.repository.PlaylistRepository;
import com.example.demo.playlist.dto.request.*;
import com.example.demo.playlist.dto.response.*;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.investimento.repository.InvestimentoRecomendadoRepository;
import com.example.demo.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final InvestimentoRecomendadoRepository investimentoRecomendadoRepository;

    public PlaylistService(PlaylistRepository playlistRepository,
                          UsuarioRepository usuarioRepository,
                          InvestimentoRepository investimentoRepository,
                          InvestimentoRecomendadoRepository investimentoRecomendadoRepository) {
        this.playlistRepository = playlistRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
        this.investimentoRecomendadoRepository = investimentoRecomendadoRepository;
    }

    /**
     * Criar nova playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO criarPlaylist(String emailUsuario, CriarPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Playlist playlist = new Playlist(request.getNome(), request.getDescricao(), usuario, request.getTipo());
        playlist.setPermiteColaboracao(request.getPermiteColaboracao());

        playlist = playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Playlist '" + playlist.getNome() + "' criada com sucesso!",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * 🎯 MÉTODO UNIFICADO: Listar playlists com filtros avançados
     * 
     * Este método substitui todos os endpoints GET específicos por um único endpoint com filtros.
     * Suporta filtros rápidos, busca por nome, tipo, ordenação customizada, etc.
     */
    public List<PlaylistResumoResponseDTO> listarPlaylistsComFiltros(String emailUsuario, FiltroPlaylistRequestDTO filtros) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        
        List<Playlist> playlists;
        
        // 1. Aplicar filtro rápido (se especificado)
        if (filtros.getFiltro() != null) {
            playlists = aplicarFiltroRapido(filtros.getFiltro(), usuario);
        } else {
            // Se não especificou filtro rápido, busca todas que o usuário tem acesso
            playlists = playlistRepository.findAllAcessiveisPorUsuario(usuario);
        }
        
        // 2. Aplicar filtros adicionais
        playlists = aplicarFiltrosAdicionais(playlists, filtros, usuario);
        
        // 3. Ordenar resultados
        playlists = ordenarPlaylists(playlists, filtros.getOrdenacao());
        
        // 4. Converter para DTO
        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }
    
    /**
     * Aplica filtro rápido predefinido
     */
    private List<Playlist> aplicarFiltroRapido(FiltroPlaylistRequestDTO.FiltroRapido filtro, Usuario usuario) {
        return switch (filtro) {
            case MINHAS -> playlistRepository.findByCriadorAndAtivaTrue(usuario);
            case SEGUINDO -> playlistRepository.findPlaylistsSeguidasPorUsuario(usuario);
            case PUBLICAS -> playlistRepository.findByTipoPublicaAndAtivaTrue();
            case COMPARTILHADAS -> playlistRepository.findPlaylistsCompartilhadasPorUsuario(usuario);
            case TODAS -> playlistRepository.findAllAcessiveisPorUsuario(usuario);
        };
    }
    
    /**
     * Aplica filtros adicionais (tipo, nome, colaboração, criador)
     */
    private List<Playlist> aplicarFiltrosAdicionais(List<Playlist> playlists, FiltroPlaylistRequestDTO filtros, Usuario usuario) {
        return playlists.stream()
            // Filtrar por tipo (PUBLICA, PRIVADA, COMPARTILHADA)
            .filter(p -> filtros.getTipo() == null || p.getTipo() == filtros.getTipo())
            
            // Filtrar por nome (busca parcial, case-insensitive)
            .filter(p -> filtros.getNome() == null || 
                        p.getNome().toLowerCase().contains(filtros.getNome().toLowerCase()))
            
            // Filtrar por permite colaboração
            .filter(p -> filtros.getPermiteColaboracao() == null || 
                        p.getPermiteColaboracao() == filtros.getPermiteColaboracao())
            
            // Filtrar por email do criador
            .filter(p -> filtros.getCriadorEmail() == null || 
                        p.getCriador().getEmail().equalsIgnoreCase(filtros.getCriadorEmail()))
            
            .collect(Collectors.toList());
    }
    
    /**
     * Ordena playlists conforme critério especificado
     */
    private List<Playlist> ordenarPlaylists(List<Playlist> playlists, FiltroPlaylistRequestDTO.OrdenacaoPlaylist ordenacao) {
        if (ordenacao == null) {
            ordenacao = FiltroPlaylistRequestDTO.OrdenacaoPlaylist.DATA_CRIACAO_DESC;
        }
        
        return switch (ordenacao) {
            case DATA_CRIACAO_ASC -> playlists.stream()
                .sorted((p1, p2) -> p1.getDataCriacao().compareTo(p2.getDataCriacao()))
                .collect(Collectors.toList());
                
            case DATA_CRIACAO_DESC -> playlists.stream()
                .sorted((p1, p2) -> p2.getDataCriacao().compareTo(p1.getDataCriacao()))
                .collect(Collectors.toList());
                
            case NOME_ASC -> playlists.stream()
                .sorted((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()))
                .collect(Collectors.toList());
                
            case NOME_DESC -> playlists.stream()
                .sorted((p1, p2) -> p2.getNome().compareToIgnoreCase(p1.getNome()))
                .collect(Collectors.toList());
                
            case TOTAL_INVESTIMENTOS_ASC -> playlists.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getInvestimentos().size(), p2.getInvestimentos().size()))
                .collect(Collectors.toList());
                
            case TOTAL_INVESTIMENTOS_DESC -> playlists.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getInvestimentos().size(), p1.getInvestimentos().size()))
                .collect(Collectors.toList());
                
            case TOTAL_SEGUIDORES_ASC -> playlists.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getSeguidores().size(), p2.getSeguidores().size()))
                .collect(Collectors.toList());
                
            case TOTAL_SEGUIDORES_DESC -> playlists.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getSeguidores().size(), p1.getSeguidores().size()))
                .collect(Collectors.toList());
        };
    }

    /**
     * Listar playlists do usuário (criadas por ele)
     * @deprecated Use listarPlaylistsComFiltros com filtro=MINHAS
     */
    public List<PlaylistResumoResponseDTO> listarMinhasPlaylists(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findByCriadorAndAtivaTrue(usuario);

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Listar playlists que o usuário segue
     */
    public List<PlaylistResumoResponseDTO> listarPlaylistsSeguidas(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findPlaylistsSeguidasPorUsuario(usuario);

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Listar playlists compartilhadas com o usuário
     */
    public List<PlaylistResumoResponseDTO> listarPlaylistsCompartilhadas(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findPlaylistsCompartilhadasPorUsuario(usuario);

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Listar playlists públicas
     */
    public List<PlaylistResumoResponseDTO> listarPlaylistsPublicas(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findByTipoPublicaAndAtivaTrue();

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Buscar playlists por nome
     */
    public List<PlaylistResumoResponseDTO> buscarPlaylistsPorNome(String emailUsuario, String nome) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findByNomeContainingIgnoreCaseAndTipoPublicaAndAtivaTrue(nome);

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Obter detalhes da playlist
     */
    public PlaylistDetalhadaResponseDTO obterDetalhesPlaylist(String emailUsuario, Long playlistId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = playlistRepository.findPlaylistComAcesso(playlistId, usuario)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist não encontrada ou acesso negado"));

        return converterParaDetalhadaDTO(playlist, usuario);
    }

    /**
     * Atualizar playlist (apenas criador)
     */
    @Transactional
    public PlaylistOperacaoResponseDTO atualizarPlaylist(String emailUsuario, Long playlistId, AtualizarPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);

        verificarSeEhCriador(playlist, usuario);

        // Rastreia se mudou para PRIVADA para remover seguidores
        PlaylistTipo tipoAnterior = playlist.getTipo();
        int totalSeguidoresRemovidos = 0;

        if (request.getNome() != null) {
            playlist.setNome(request.getNome());
        }
        if (request.getDescricao() != null) {
            playlist.setDescricao(request.getDescricao());
        }
        if (request.getTipo() != null) {
            // Se mudar para PRIVADA, remove todos os seguidores
            if (request.getTipo() == PlaylistTipo.PRIVADA && tipoAnterior != PlaylistTipo.PRIVADA) {
                if (!playlist.getSeguidores().isEmpty()) {
                    totalSeguidoresRemovidos = playlist.getSeguidores().size();
                    playlist.getSeguidores().clear();
                }
            }
            playlist.setTipo(request.getTipo());
        }
        if (request.getPermiteColaboracao() != null) {
            playlist.setPermiteColaboracao(request.getPermiteColaboracao());
        }

        playlist = playlistRepository.save(playlist);

        // Mensagem personalizada se removeu seguidores
        String mensagem;
        if (totalSeguidoresRemovidos > 0) {
            mensagem = String.format(
                "Playlist '%s' atualizada com sucesso! %d seguidor(es) foram removidos ao alterar para Privada.",
                playlist.getNome(),
                totalSeguidoresRemovidos
            );
        } else {
            mensagem = "Playlist '" + playlist.getNome() + "' atualizada com sucesso!";
        }

        return new PlaylistOperacaoResponseDTO(
            mensagem,
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Excluir playlist (apenas criador)
     */
    @Transactional
    public PlaylistOperacaoResponseDTO excluirPlaylist(String emailUsuario, Long playlistId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);

        verificarSeEhCriador(playlist, usuario);

        playlist.setAtiva(false); // Soft delete
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Playlist '" + playlist.getNome() + "' excluída com sucesso!",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Adicionar investimento à playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO adicionarInvestimento(String emailUsuario, Long playlistId, AdicionarInvestimentoRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);
        Investimento investimento = buscarInvestimentoPorId(request.getInvestimentoId());

        verificarPermissaoColaboracao(playlist, usuario);
        
        // Validar se investimento está visível para usuários comuns
        boolean isAdmin = "ROLE_ADMIN".equals(usuario.getRole());
        if (!isAdmin && !investimento.isVisivelParaUsuarios()) {
            throw new IllegalArgumentException(
                "Este investimento não está disponível para ser adicionado à playlist"
            );
        }

        if (playlist.getInvestimentos().contains(investimento)) {
            throw new IllegalArgumentException("Investimento já está na playlist");
        }

        playlist.adicionarInvestimento(investimento);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Investimento '" + investimento.getNome() + "' adicionado à playlist '" + playlist.getNome() + "'",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Remover investimento da playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO removerInvestimento(String emailUsuario, Long playlistId, Long investimentoId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);
        Investimento investimento = buscarInvestimentoPorId(investimentoId);

        verificarPermissaoColaboracao(playlist, usuario);

        if (!playlist.getInvestimentos().contains(investimento)) {
            throw new IllegalArgumentException("Investimento não está na playlist");
        }

        playlist.removerInvestimento(investimento);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Investimento '" + investimento.getNome() + "' removido da playlist '" + playlist.getNome() + "'",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Seguir playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO seguirPlaylist(String emailUsuario, Long playlistId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);

        if (playlist.isCriador(usuario)) {
            throw new IllegalArgumentException("Você não pode seguir sua própria playlist");
        }

        if (playlist.isSeguidor(usuario)) {
            throw new IllegalArgumentException("Você já segue esta playlist");
        }

        playlist.adicionarSeguidor(usuario);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Você começou a seguir a playlist '" + playlist.getNome() + "'",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Parar de seguir playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO pararDeSeguir(String emailUsuario, Long playlistId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);

        if (!playlist.isSeguidor(usuario)) {
            throw new IllegalArgumentException("Você não segue esta playlist");
        }

        playlist.removerSeguidor(usuario);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Você parou de seguir a playlist '" + playlist.getNome() + "'",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Compartilhar playlist com usuário específico
     * Valida se a playlist não é PRIVADA antes de compartilhar
     */
    @Transactional
    public PlaylistOperacaoResponseDTO compartilharPlaylist(String emailUsuario, Long playlistId, CompartilharPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);
        Usuario usuarioDestino = buscarUsuarioPorEmail(request.getEmailUsuario());

        verificarSeEhCriador(playlist, usuario);

        // Validação: não permite compartilhar playlist PRIVADA
        if (playlist.getTipo() == PlaylistTipo.PRIVADA) {
            throw new IllegalArgumentException(
                "Não é possível compartilhar uma playlist PRIVADA. " +
                "Altere o tipo da playlist para PUBLICA ou COMPARTILHADA primeiro usando o endpoint PUT /playlists/" + playlistId + "/alterar-tipo"
            );
        }

        if (playlist.isSeguidor(usuarioDestino)) {
            throw new IllegalArgumentException("Usuário já tem acesso a esta playlist");
        }

        if (playlist.isCriador(usuarioDestino)) {
            throw new IllegalArgumentException("Não é possível compartilhar playlist com o próprio criador");
        }

        playlist.adicionarSeguidor(usuarioDestino);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Playlist '" + playlist.getNome() + "' compartilhada com " + usuarioDestino.getNomeUsuario(),
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Remover seguidor da playlist (apenas criador)
     */
    @Transactional
    public PlaylistOperacaoResponseDTO removerSeguidor(String emailUsuario, Long playlistId, String emailSeguidor) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);
        Usuario seguidor = buscarUsuarioPorEmail(emailSeguidor);

        verificarSeEhCriador(playlist, usuario);

        if (!playlist.isSeguidor(seguidor)) {
            throw new IllegalArgumentException("Usuário não segue esta playlist");
        }

        playlist.removerSeguidor(seguidor);
        playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            seguidor.getNomeUsuario() + " foi removido da playlist '" + playlist.getNome() + "'",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Alterar tipo da playlist com remoção automática de seguidores se virar PRIVADA
     */
    @Transactional
    public PlaylistOperacaoResponseDTO alterarTipoPlaylist(String emailUsuario, Long playlistId, AlterarTipoPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);

        verificarSeEhCriador(playlist, usuario);

        PlaylistTipo tipoAnterior = playlist.getTipo();
        PlaylistTipo novoTipo = request.getNovoTipo();

        // Se o tipo não mudou, retorna sem fazer nada
        if (tipoAnterior == novoTipo) {
            return new PlaylistOperacaoResponseDTO(
                "A playlist já é do tipo " + novoTipo.getDescricao(),
                playlist.getId(),
                playlist.getNome()
            );
        }

        // Contar seguidores antes da alteração
        int totalSeguidoresRemovidos = 0;

        // Se mudar para PRIVADA, remove todos os seguidores
        if (novoTipo == PlaylistTipo.PRIVADA && !playlist.getSeguidores().isEmpty()) {
            totalSeguidoresRemovidos = playlist.getSeguidores().size();
            playlist.getSeguidores().clear();
        }

        // Atualiza o tipo da playlist
        playlist.setTipo(novoTipo);
        playlistRepository.save(playlist);

        // Mensagem personalizada baseada na ação
        String mensagem;
        if (totalSeguidoresRemovidos > 0) {
            mensagem = String.format(
                "Playlist '%s' alterada para %s. %d seguidor(es) foram removidos automaticamente.",
                playlist.getNome(),
                novoTipo.getDescricao(),
                totalSeguidoresRemovidos
            );
        } else {
            mensagem = String.format(
                "Playlist '%s' alterada para %s com sucesso!",
                playlist.getNome(),
                novoTipo.getDescricao()
            );
        }

        return new PlaylistOperacaoResponseDTO(
            mensagem,
            playlist.getId(),
            playlist.getNome()
        );
    }

    // Métodos auxiliares privados

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }

    private Playlist buscarPlaylistPorId(Long id) {
        return playlistRepository.findByIdAndAtivaTrue(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist não encontrada"));
    }

    private Investimento buscarInvestimentoPorId(Long id) {
        return investimentoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Investimento não encontrado"));
    }

    private void verificarSeEhCriador(Playlist playlist, Usuario usuario) {
        if (!playlist.isCriador(usuario)) {
            throw new IllegalArgumentException("Apenas o criador da playlist pode realizar esta operação");
        }
    }

    private void verificarPermissaoColaboracao(Playlist playlist, Usuario usuario) {
        if (!playlist.isCriador(usuario) && 
            (!playlist.getPermiteColaboracao() || !playlist.isSeguidor(usuario))) {
            throw new IllegalArgumentException("Sem permissão para modificar investimentos desta playlist");
        }
    }

    private PlaylistResumoResponseDTO converterParaResumoDTO(Playlist playlist, Usuario usuarioAtual) {
        PlaylistResumoResponseDTO dto = new PlaylistResumoResponseDTO();
        dto.setId(playlist.getId());
        dto.setNome(playlist.getNome());
        dto.setDescricao(playlist.getDescricao());
        dto.setCriadorNome(playlist.getCriador().getNomeUsuario());
        dto.setCriadorEmail(playlist.getCriador().getEmail());
        dto.setTipo(playlist.getTipo());
        dto.setPermiteColaboracao(playlist.getPermiteColaboracao());
        dto.setTotalInvestimentos(playlist.getTotalInvestimentos());
        dto.setTotalSeguidores(playlist.getTotalSeguidores());
        dto.setDataCriacao(playlist.getDataCriacao());
        dto.setDataAtualizacao(playlist.getDataAtualizacao());
        dto.setIsCriador(playlist.isCriador(usuarioAtual));
        dto.setIsFollowing(playlist.isSeguidor(usuarioAtual));

        return dto;
    }

    private PlaylistDetalhadaResponseDTO converterParaDetalhadaDTO(Playlist playlist, Usuario usuarioAtual) {
        PlaylistDetalhadaResponseDTO dto = new PlaylistDetalhadaResponseDTO();
        dto.setId(playlist.getId());
        dto.setNome(playlist.getNome());
        dto.setDescricao(playlist.getDescricao());
        dto.setCriadorNome(playlist.getCriador().getNomeUsuario());
        dto.setCriadorEmail(playlist.getCriador().getEmail());
        dto.setTipo(playlist.getTipo());
        dto.setPermiteColaboracao(playlist.getPermiteColaboracao());
        dto.setDataCriacao(playlist.getDataCriacao());
        dto.setDataAtualizacao(playlist.getDataAtualizacao());
        dto.setIsCriador(playlist.isCriador(usuarioAtual));
        dto.setIsFollowing(playlist.isSeguidor(usuarioAtual));

        // Verificar se usuário é admin
        boolean isAdmin = "ROLE_ADMIN".equals(usuarioAtual.getRole());

        // Converter investimentos - filtrar invisíveis para usuários comuns
        dto.setInvestimentos(playlist.getInvestimentos().stream()
            .filter(inv -> isAdmin || inv.isVisivelParaUsuarios())
            .map(inv -> converterInvestimentoParaDTO(inv, usuarioAtual))
            .collect(Collectors.toList()));
        
        // Atualizar total de investimentos com base nos visíveis
        dto.setTotalInvestimentos(dto.getInvestimentos().size());
        dto.setTotalSeguidores(playlist.getTotalSeguidores());

        // Converter seguidores
        dto.setSeguidores(playlist.getSeguidores().stream()
            .map(this::converterUsuarioParaSeguidorDTO)
            .collect(Collectors.toList()));

        return dto;
    }

    private InvestimentoPlaylistResponseDTO converterInvestimentoParaDTO(Investimento investimento, Usuario usuario) {
        InvestimentoPlaylistResponseDTO dto = new InvestimentoPlaylistResponseDTO();
        dto.setId(investimento.getId());
        dto.setNome(investimento.getNome());
        dto.setSimbolo(investimento.getSimbolo());
        dto.setCategoria(investimento.getCategoria() != null ? investimento.getCategoria().getDescricao() : null);
        dto.setRisco(investimento.getRisco() != null ? investimento.getRisco().getDescricao() : null);
        dto.setPrecoAtual(investimento.getPrecoAtual());
        dto.setVariacaoPercentual(investimento.getVariacaoPercentual());
        dto.setDescricao(investimento.getDescricao());
        
        // Verificar se o usuário tem ALGUMA recomendação
        boolean usuarioTemRecomendacoes = investimentoRecomendadoRepository.existsByUsuarioId(usuario.getId());
        
        if (!usuarioTemRecomendacoes) {
            dto.setRecomendadoParaVoce(null); // null indica que usuário não tem recomendações
        } else {
            // Verificar se este investimento foi recomendado para o usuário
            boolean recomendado = investimentoRecomendadoRepository
                .existsByUsuarioIdAndInvestimentoId(usuario.getId(), investimento.getId());
            dto.setRecomendadoParaVoce(recomendado);
        }

        return dto;
    }

    private UsuarioSeguidorResponseDTO converterUsuarioParaSeguidorDTO(Usuario usuario) {
        return new UsuarioSeguidorResponseDTO(
            usuario.getId(),
            usuario.getNomeUsuario(),
            usuario.getEmail()
        );
    }

    /**
     * Toggle de usuários comuns na playlist (Admin)
     * Verifica se todos os usuários comuns estão na playlist:
     * - Se todos estão: remove todos
     * - Se nem todos estão: adiciona todos
     */
    @Transactional
    public PlaylistOperacaoResponseDTO toggleUsuariosComunsPlaylist(String emailAdmin, Long playlistId) {
        // Verifica se o usuário é admin
        Usuario admin = buscarUsuarioPorEmail(emailAdmin);
        if (!"ROLE_ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Acesso negado! Apenas administradores podem usar esta funcionalidade.");
        }

        // Busca a playlist
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist não encontrada com ID: " + playlistId));

        // Busca todos os usuários comuns (ROLE_USER) ativos
        List<Usuario> usuariosComuns = usuarioRepository.findByRoleAndUserIsActiveTrue("ROLE_USER");

        if (usuariosComuns.isEmpty()) {
            return new PlaylistOperacaoResponseDTO(
                "Nenhum usuário comum encontrado no sistema",
                playlist.getId(),
                playlist.getNome(),
                "nenhum_usuario",
                0
            );
        }

        // Verifica quantos usuários comuns já seguem a playlist
        List<Usuario> seguidoresComuns = playlist.getSeguidores().stream()
            .filter(seguidor -> "ROLE_USER".equals(seguidor.getRole()))
            .collect(Collectors.toList());

        String acao;
        int totalAfetados;

        // Se todos os usuários comuns já seguem, remove todos
        if (seguidoresComuns.size() == usuariosComuns.size()) {
            // Remove todos os usuários comuns da playlist
            for (Usuario usuario : seguidoresComuns) {
                playlist.getSeguidores().remove(usuario);
            }
            acao = "desvinculados";
            totalAfetados = seguidoresComuns.size();
        } else {
            // Adiciona todos os usuários comuns que ainda não seguem
            int adicionados = 0;
            for (Usuario usuario : usuariosComuns) {
                if (!playlist.getSeguidores().contains(usuario)) {
                    playlist.getSeguidores().add(usuario);
                    adicionados++;
                }
            }
            acao = "vinculados";
            totalAfetados = adicionados;
        }

        playlistRepository.save(playlist);

        String mensagem = String.format("%d usuários comuns foram %s à playlist '%s'", 
                                      totalAfetados, acao, playlist.getNome());

        return new PlaylistOperacaoResponseDTO(
            mensagem,
            playlist.getId(),
            playlist.getNome(),
            acao,
            totalAfetados
        );
    }
}