package com.example.demo.playlist.controller;

import com.example.demo.playlist.service.PlaylistService;
import com.example.demo.playlist.dto.request.*;
import com.example.demo.playlist.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Sistema de playlists de investimentos - crie, compartilhe e gerencie suas seleções de investimentos como no Spotify!")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * 🎵 Criar nova playlist
     */
    @Operation(
        summary = "Criar nova playlist de investimentos",
        description = "Permite ao usuário criar uma nova playlist de investimentos, definindo se será pública e se permitirá colaboração de outros usuários.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Playlist criada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Ações Promissoras 2024' criada com sucesso!\", \"playlistId\": 1, \"nomePlaylist\": \"Ações Promissoras 2024\", \"timestamp\": \"2024-10-03T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da playlist"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> criarPlaylist(@Valid @RequestBody CriarPlaylistRequestDTO request, 
                                                                    Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.criarPlaylist(authentication.getName(), request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * 📋 Listar minhas playlists
     */
    @Operation(
        summary = "Listar minhas playlists",
        description = "Retorna todas as playlists criadas pelo usuário autenticado.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de playlists retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 1, \"nome\": \"Minha Carteira Conservadora\", \"descricao\": \"Investimentos de baixo risco\", \"criadorNome\": \"João Silva\", \"publica\": false, \"totalInvestimentos\": 5, \"totalSeguidores\": 0, \"isCriador\": true, \"isFollowing\": false}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/minhas")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PlaylistResumoResponseDTO>> listarMinhasPlaylists(Authentication authentication) {
        List<PlaylistResumoResponseDTO> playlists = playlistService.listarMinhasPlaylists(authentication.getName());
        return ResponseEntity.ok(playlists);
    }

    /**
     * 👥 Listar playlists que sigo
     */
    @Operation(
        summary = "Listar playlists que sigo",
        description = "Retorna todas as playlists que o usuário autenticado está seguindo.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de playlists seguidas retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 2, \"nome\": \"Top Dividendos 2024\", \"criadorNome\": \"Maria Santos\", \"criadorEmail\": \"maria@email.com\", \"publica\": true, \"totalInvestimentos\": 8, \"totalSeguidores\": 15, \"isCriador\": false, \"isFollowing\": true}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/seguindo")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PlaylistResumoResponseDTO>> listarPlaylistsSeguidas(Authentication authentication) {
        List<PlaylistResumoResponseDTO> playlists = playlistService.listarPlaylistsSeguidas(authentication.getName());
        return ResponseEntity.ok(playlists);
    }

    /**
     * 🌍 Listar playlists públicas
     */
    @Operation(
        summary = "Listar playlists públicas",
        description = "Retorna todas as playlists públicas disponíveis na plataforma.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de playlists públicas retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 3, \"nome\": \"Ações ESG Sustentáveis\", \"descricao\": \"Empresas com foco em sustentabilidade\", \"criadorNome\": \"Carlos Admin\", \"publica\": true, \"totalInvestimentos\": 12, \"totalSeguidores\": 45, \"isCriador\": false, \"isFollowing\": false}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/publicas")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PlaylistResumoResponseDTO>> listarPlaylistsPublicas(Authentication authentication) {
        List<PlaylistResumoResponseDTO> playlists = playlistService.listarPlaylistsPublicas(authentication.getName());
        return ResponseEntity.ok(playlists);
    }

    /**
     * 🔍 Buscar playlists por nome
     */
    @Operation(
        summary = "Buscar playlists por nome",
        description = "Busca playlists públicas que contenham o termo especificado no nome (busca parcial, case-insensitive).",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Resultados da busca retornados com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 4, \"nome\": \"Dividendos Mensais\", \"descricao\": \"FIIs e ações que pagam mensalmente\", \"criadorNome\": \"Ana Investidora\", \"totalInvestimentos\": 6, \"totalSeguidores\": 23}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PlaylistResumoResponseDTO>> buscarPlaylistsPorNome(@RequestParam String nome, 
                                                                                 Authentication authentication) {
        List<PlaylistResumoResponseDTO> playlists = playlistService.buscarPlaylistsPorNome(authentication.getName(), nome);
        return ResponseEntity.ok(playlists);
    }

    /**
     * 📖 Obter detalhes da playlist
     */
    @Operation(
        summary = "Obter detalhes completos de uma playlist",
        description = "Retorna informações detalhadas de uma playlist, incluindo lista de investimentos e seguidores (se o usuário tiver acesso).",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Detalhes da playlist retornados com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"id\": 1, \"nome\": \"Top Tech 2024\", \"investimentos\": [{\"nome\": \"PETR4\", \"simbolo\": \"PETR4\", \"precoAtual\": 28.50, \"variacaoPercentual\": 2.5}], \"seguidores\": [{\"nomeUsuario\": \"João Silva\", \"email\": \"joao@email.com\"}]}"
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acesso negado à playlist privada"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistDetalhadaResponseDTO> obterDetalhesPlaylist(@PathVariable Long id, 
                                                                             Authentication authentication) {
        PlaylistDetalhadaResponseDTO playlist = playlistService.obterDetalhesPlaylist(authentication.getName(), id);
        return ResponseEntity.ok(playlist);
    }

    /**
     * ✏️ Atualizar playlist
     */
    @Operation(
        summary = "Atualizar playlist",
        description = "Permite ao criador da playlist atualizar suas informações (nome, descrição, visibilidade, etc.).",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist atualizada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Minha Carteira Atualizada' atualizada com sucesso!\", \"playlistId\": 1, \"nomePlaylist\": \"Minha Carteira Atualizada\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode atualizar a playlist"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> atualizarPlaylist(@PathVariable Long id, 
                                                                        @Valid @RequestBody AtualizarPlaylistRequestDTO request,
                                                                        Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.atualizarPlaylist(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 🗑️ Excluir playlist
     */
    @Operation(
        summary = "Excluir playlist",
        description = "Permite ao criador excluir permanentemente sua playlist. Esta ação não pode ser desfeita.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist excluída com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Antiga Carteira' excluída com sucesso!\", \"playlistId\": 1, \"nomePlaylist\": \"Antiga Carteira\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode excluir a playlist"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> excluirPlaylist(@PathVariable Long id, 
                                                                      Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.excluirPlaylist(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }

    /**
     * ➕ Adicionar investimento à playlist
     */
    @Operation(
        summary = "Adicionar investimento à playlist",
        description = "Adiciona um investimento à playlist. Criadores sempre podem adicionar. Outros usuários só podem adicionar se a playlist permitir colaboração e eles a estiverem seguindo.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Investimento adicionado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Investimento 'PETR4' adicionado à playlist 'Petróleo e Gás'\", \"playlistId\": 1, \"nomePlaylist\": \"Petróleo e Gás\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Investimento já está na playlist ou dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Sem permissão para modificar a playlist")
    })
    @PostMapping("/{id}/investimentos")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> adicionarInvestimento(@PathVariable Long id, 
                                                                           @Valid @RequestBody AdicionarInvestimentoRequestDTO request,
                                                                           Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.adicionarInvestimento(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * ➖ Remover investimento da playlist
     */
    @Operation(
        summary = "Remover investimento da playlist",
        description = "Remove um investimento da playlist. Criadores sempre podem remover. Outros usuários só podem remover se a playlist permitir colaboração e eles a estiverem seguindo.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Investimento removido com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Investimento 'VALE3' removido da playlist 'Mineração'\", \"playlistId\": 1, \"nomePlaylist\": \"Mineração\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Investimento não está na playlist"),
        @ApiResponse(responseCode = "403", description = "Sem permissão para modificar a playlist")
    })
    @DeleteMapping("/{id}/investimentos/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> removerInvestimento(@PathVariable Long id, 
                                                                          @PathVariable Long investimentoId,
                                                                          Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.removerInvestimento(authentication.getName(), id, investimentoId);
        return ResponseEntity.ok(response);
    }

    /**
     * ❤️ Seguir playlist
     */
    @Operation(
        summary = "Seguir playlist",
        description = "Começar a seguir uma playlist pública ou compartilhada. Seguidores podem colaborar (se permitido) e receber atualizações.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist seguida com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Você começou a seguir a playlist 'Top Dividendos'\", \"playlistId\": 2, \"nomePlaylist\": \"Top Dividendos\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Você já segue esta playlist ou não pode seguir sua própria playlist"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @PostMapping("/{id}/seguir")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> seguirPlaylist(@PathVariable Long id, 
                                                                     Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.seguirPlaylist(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }

    /**
     * 💔 Parar de seguir playlist
     */
    @Operation(
        summary = "Parar de seguir playlist",
        description = "Parar de seguir uma playlist. Você perderá acesso a playlists privadas e não poderá mais colaborar.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Parou de seguir playlist com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Você parou de seguir a playlist 'Ações Arriscadas'\", \"playlistId\": 3, \"nomePlaylist\": \"Ações Arriscadas\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Você não segue esta playlist"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @DeleteMapping("/{id}/seguir")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> pararDeSeguir(@PathVariable Long id, 
                                                                    Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.pararDeSeguir(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }

    /**
     * 📤 Compartilhar playlist
     */
    @Operation(
        summary = "Compartilhar playlist com usuário",
        description = "Permite ao criador da playlist compartilhá-la diretamente com outro usuário (adiciona como seguidor).",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist compartilhada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Meus Favoritos' compartilhada com João Silva\", \"playlistId\": 1, \"nomePlaylist\": \"Meus Favoritos\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Usuário já segue a playlist ou dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode compartilhar a playlist")
    })
    @PostMapping("/{id}/compartilhar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> compartilharPlaylist(@PathVariable Long id, 
                                                                           @Valid @RequestBody CompartilharPlaylistRequestDTO request,
                                                                           Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.compartilharPlaylist(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 🚫 Remover seguidor
     */
    @Operation(
        summary = "Remover seguidor da playlist",
        description = "Permite ao criador remover um seguidor específico da playlist.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Seguidor removido com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Maria Santos foi removida da playlist 'Carteira Privada'\", \"playlistId\": 1, \"nomePlaylist\": \"Carteira Privada\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Usuário não segue esta playlist"),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode remover seguidores")
    })
    @DeleteMapping("/{id}/seguidores")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> removerSeguidor(@PathVariable Long id, 
                                                                      @RequestParam String emailSeguidor,
                                                                      Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.removerSeguidor(authentication.getName(), id, emailSeguidor);
        return ResponseEntity.ok(response);
    }

    /**
     * � Listar playlists compartilhadas comigo
     */
    @Operation(
        summary = "Listar playlists compartilhadas",
        description = "Retorna todas as playlists do tipo COMPARTILHADA que foram compartilhadas especificamente com o usuário autenticado.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de playlists compartilhadas retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 5, \"nome\": \"Carteira VIP Exclusiva\", \"criadorNome\": \"Admin Sistema\", \"tipo\": \"COMPARTILHADA\", \"totalInvestimentos\": 10, \"totalSeguidores\": 3, \"isCriador\": false, \"isFollowing\": true}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/compartilhadas")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PlaylistResumoResponseDTO>> listarPlaylistsCompartilhadas(Authentication authentication) {
        String email = authentication.getName();
        List<PlaylistResumoResponseDTO> playlists = playlistService.listarPlaylistsCompartilhadas(email);
        return ResponseEntity.ok(playlists);
    }

    /**
     * 🔄 Tornar playlist compartilhada
     */
    @Operation(
        summary = "Tornar playlist compartilhada",
        description = "Converte uma playlist privada ou pública para o tipo COMPARTILHADA, permitindo compartilhamento seletivo com usuários específicos.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist convertida para compartilhada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Minha Carteira Especial' agora é do tipo compartilhada\", \"playlistId\": 1, \"nomePlaylist\": \"Minha Carteira Especial\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode alterar o tipo da playlist"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @PutMapping("/{id}/tornar-compartilhada")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> tornarPlaylistCompartilhada(@PathVariable Long id, 
                                                                                  Authentication authentication) {
        String email = authentication.getName();
        PlaylistOperacaoResponseDTO response = playlistService.tornarPlaylistCompartilhada(email, id);
        return ResponseEntity.ok(response);
    }

    /**
     * 📤 Compartilhar com usuário específico
     */
    @Operation(
        summary = "Compartilhar playlist com usuário específico",
        description = "Compartilha a playlist com um usuário específico, convertendo-a automaticamente para o tipo COMPARTILHADA se necessário.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Playlist compartilhada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Playlist 'Estratégias Premium' compartilhada com João Silva\", \"playlistId\": 1, \"nomePlaylist\": \"Estratégias Premium\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Usuário já tem acesso à playlist ou dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Apenas o criador pode compartilhar a playlist")
    })
    @PostMapping("/{id}/compartilhar-com")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> compartilharComUsuario(@PathVariable Long id, 
                                                                             @Valid @RequestBody CompartilharPlaylistRequestDTO request,
                                                                             Authentication authentication) {
        String email = authentication.getName();
        PlaylistOperacaoResponseDTO response = playlistService.compartilharComUsuario(email, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * �👥 Toggle usuários comuns na playlist (Admin)
     */
    @Operation(
        summary = "Vincular/Desvincular todos os usuários comuns de uma playlist (Admin)",
        description = "Permite ao admin alternar entre vincular todos os usuários comuns (ROLE_USER) a uma playlist ou desvinculá-los. Funciona como um toggle: se todos estão vinculados, desvincula; se nem todos estão vinculados, vincula todos.",
        tags = { "Playlists" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Toggle realizado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"15 usuários comuns foram vinculados à playlist 'Carteira Modelo Iniciante'\", \"playlistId\": 1, \"nomePlaylist\": \"Carteira Modelo Iniciante\", \"acao\": \"vinculados\", \"totalUsuarios\": 15, \"timestamp\": \"2024-10-04T14:30:15\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acesso restrito apenas para administradores"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada")
    })
    @PostMapping("/{id}/admin/toggle-usuarios")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlaylistOperacaoResponseDTO> toggleUsuariosComunsPlaylist(@PathVariable Long id, 
                                                                                   Authentication authentication) {
        PlaylistOperacaoResponseDTO response = playlistService.toggleUsuariosComunsPlaylist(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }
}