package com.example.demo.playlist.service;

import com.example.demo.playlist.model.Playlist;
import com.example.demo.playlist.repository.PlaylistRepository;
import com.example.demo.playlist.dto.request.*;
import com.example.demo.playlist.dto.response.*;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
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

    public PlaylistService(PlaylistRepository playlistRepository,
                          UsuarioRepository usuarioRepository,
                          InvestimentoRepository investimentoRepository) {
        this.playlistRepository = playlistRepository;
        this.usuarioRepository = usuarioRepository;
        this.investimentoRepository = investimentoRepository;
    }

    /**
     * Criar nova playlist
     */
    @Transactional
    public PlaylistOperacaoResponseDTO criarPlaylist(String emailUsuario, CriarPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Playlist playlist = new Playlist(request.getNome(), request.getDescricao(), usuario);
        playlist.setPublica(request.getPublica());
        playlist.setPermiteColaboracao(request.getPermiteColaboracao());

        playlist = playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Playlist '" + playlist.getNome() + "' criada com sucesso!",
            playlist.getId(),
            playlist.getNome()
        );
    }

    /**
     * Listar playlists do usuário (criadas por ele)
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
     * Listar playlists públicas
     */
    public List<PlaylistResumoResponseDTO> listarPlaylistsPublicas(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findByPublicaTrueAndAtivaTrue();

        return playlists.stream()
                       .map(playlist -> converterParaResumoDTO(playlist, usuario))
                       .collect(Collectors.toList());
    }

    /**
     * Buscar playlists por nome
     */
    public List<PlaylistResumoResponseDTO> buscarPlaylistsPorNome(String emailUsuario, String nome) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        List<Playlist> playlists = playlistRepository.findByNomeContainingIgnoreCaseAndPublicaTrueAndAtivaTrue(nome);

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

        if (request.getNome() != null) {
            playlist.setNome(request.getNome());
        }
        if (request.getDescricao() != null) {
            playlist.setDescricao(request.getDescricao());
        }
        if (request.getPublica() != null) {
            playlist.setPublica(request.getPublica());
        }
        if (request.getPermiteColaboracao() != null) {
            playlist.setPermiteColaboracao(request.getPermiteColaboracao());
        }

        playlist = playlistRepository.save(playlist);

        return new PlaylistOperacaoResponseDTO(
            "Playlist '" + playlist.getNome() + "' atualizada com sucesso!",
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
     * Compartilhar playlist com usuário (adicionar como seguidor)
     */
    @Transactional
    public PlaylistOperacaoResponseDTO compartilharPlaylist(String emailUsuario, Long playlistId, CompartilharPlaylistRequestDTO request) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);
        Playlist playlist = buscarPlaylistPorId(playlistId);
        Usuario usuarioDestino = buscarUsuarioPorEmail(request.getEmailUsuario());

        verificarSeEhCriador(playlist, usuario);

        if (playlist.isSeguidor(usuarioDestino)) {
            throw new IllegalArgumentException("Usuário já segue esta playlist");
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
        dto.setPublica(playlist.getPublica());
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
        dto.setPublica(playlist.getPublica());
        dto.setPermiteColaboracao(playlist.getPermiteColaboracao());
        dto.setTotalInvestimentos(playlist.getTotalInvestimentos());
        dto.setTotalSeguidores(playlist.getTotalSeguidores());
        dto.setDataCriacao(playlist.getDataCriacao());
        dto.setDataAtualizacao(playlist.getDataAtualizacao());
        dto.setIsCriador(playlist.isCriador(usuarioAtual));
        dto.setIsFollowing(playlist.isSeguidor(usuarioAtual));

        // Converter investimentos
        dto.setInvestimentos(playlist.getInvestimentos().stream()
            .map(this::converterInvestimentoParaDTO)
            .collect(Collectors.toList()));

        // Converter seguidores
        dto.setSeguidores(playlist.getSeguidores().stream()
            .map(this::converterUsuarioParaSeguidorDTO)
            .collect(Collectors.toList()));

        return dto;
    }

    private InvestimentoPlaylistResponseDTO converterInvestimentoParaDTO(Investimento investimento) {
        InvestimentoPlaylistResponseDTO dto = new InvestimentoPlaylistResponseDTO();
        dto.setId(investimento.getId());
        dto.setNome(investimento.getNome());
        dto.setSimbolo(investimento.getSimbolo());
        dto.setCategoria(investimento.getCategoria() != null ? investimento.getCategoria().getDescricao() : null);
        dto.setRisco(investimento.getRisco() != null ? investimento.getRisco().getDescricao() : null);
        dto.setPrecoAtual(investimento.getPrecoAtual());
        dto.setVariacaoPercentual(investimento.getVariacaoPercentual());
        dto.setDescricao(investimento.getDescricao());

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