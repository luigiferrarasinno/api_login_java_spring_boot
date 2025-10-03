package com.example.demo.comentarios.controller;

import com.example.demo.comentarios.dto.ComentarioDTO;
import com.example.demo.comentarios.dto.CriarComentarioDTO;
import com.example.demo.comentarios.dto.EditarComentarioDTO;
import com.example.demo.comentarios.dto.response.*;
import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller para sistema de comentários sobre investimentos/ações
 */
@Tag(name = "💬 Comentários", description = "Sistema de comentários sobre investimentos com permissões de usuário e admin")
@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    @Operation(summary = "Criar novo comentário", 
               description = "Cria um novo comentário em um investimento (usuário autenticado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Comentário criado com sucesso!\", \"comentario\": {\"id\": 1, \"conteudo\": \"Excelente investimento!\", \"autor\": \"usuario@email.com\", \"dataComentario\": \"2024-01-01T10:00:00\"}, \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "400", description = "Erro na criação do comentário")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> criarComentario(@Valid @RequestBody CriarComentarioDTO criarDTO, 
                                           Authentication auth) {
        try {
            Comentario comentario = comentarioService.criarComentario(
                criarDTO.getConteudo(), 
                criarDTO.getInvestimentoId(), 
                auth.getName()
            );
            
            ComentarioDTO response = new ComentarioDTO(comentario);
            ComentarioCriadoResponseDTO resultado = new ComentarioCriadoResponseDTO(
                "Comentário criado com sucesso!", response, LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar comentários por investimento", 
               description = "Lista todos os comentários de um investimento específico (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de comentários do investimento",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"investimentoId\": 1, \"totalComentarios\": 2, \"comentarios\": [{\"id\": 1, \"conteudo\": \"Bom investimento\", \"autor\": \"user1@email.com\"}]}"))),
        @ApiResponse(responseCode = "400", description = "Erro ao buscar comentários")
    })
    @GetMapping("/investimento/{investimentoId}")
    public ResponseEntity<?> buscarComentariosPorInvestimento(@PathVariable Long investimentoId) {
        try {
            List<Comentario> comentarios = comentarioService.buscarComentariosPorInvestimento(investimentoId);
            
            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
            
            ComentariosPorInvestimentoResponseDTO response = new ComentariosPorInvestimentoResponseDTO(
                investimentoId, comentariosDTO.size(), comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar meus comentários", 
               description = "Lista todos os comentários do usuário logado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dos comentários do usuário",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"usuario\": \"user@email.com\", \"totalComentarios\": 3, \"comentarios\": [{\"id\": 1, \"conteudo\": \"Meu comentário\", \"investimentoNome\": \"Tesouro Direto\"}]}"))),
        @ApiResponse(responseCode = "400", description = "Erro ao buscar comentários")
    })
    @GetMapping("/meus")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarMeusComentarios(Authentication auth) {
        try {
            List<Comentario> comentarios = comentarioService.buscarComentariosDoUsuario(auth.getName());
            
            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
            
            MeusComentariosResponseDTO response = new MeusComentariosResponseDTO(
                auth.getName(), comentariosDTO.size(), comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "[ADMIN] Buscar todos os comentários", 
               description = "Lista todos os comentários com filtros avançados (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista filtrada de comentários",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"filtros\": {\"investimentoId\": \"todos\", \"usuarioId\": \"todos\"}, \"totalComentarios\": 10, \"comentarios\": [{\"id\": 1, \"conteudo\": \"Comentário exemplo\"}]}"))),
        @ApiResponse(responseCode = "400", description = "Erro ao buscar comentários")
    })
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> buscarTodosComentarios(
            @RequestParam(required = false) Long investimentoId,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String conteudo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim) {
        try {
            List<Comentario> comentarios = comentarioService.buscarComFiltros(
                investimentoId, usuarioId, conteudo, dataInicio, dataFim
            );
            
            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
            
            Map<String, Object> filtros = Map.of(
                "investimentoId", investimentoId != null ? investimentoId : "todos",
                "usuarioId", usuarioId != null ? usuarioId : "todos",
                "conteudo", conteudo != null ? conteudo : "todos",
                "dataInicio", dataInicio != null ? dataInicio : "todos",
                "dataFim", dataFim != null ? dataFim : "todos"
            );
            
            AdminComentariosResponseDTO response = new AdminComentariosResponseDTO(
                filtros, comentariosDTO.size(), comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Editar comentário", 
               description = "Edita um comentário existente (próprio comentário ou admin pode editar qualquer um)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário editado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Comentário editado com sucesso!\", \"comentario\": {\"id\": 1, \"conteudo\": \"Conteúdo editado\", \"editado\": true}, \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "403", description = "Sem permissão para editar este comentário"),
        @ApiResponse(responseCode = "400", description = "Erro na edição")
    })
    @PutMapping("/{comentarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editarComentario(@PathVariable Long comentarioId,
                                            @Valid @RequestBody EditarComentarioDTO editarDTO,
                                            Authentication auth) {
        try {
            boolean ehAdmin = isAdmin(auth);
            
            Comentario comentario = comentarioService.editarComentario(
                comentarioId, 
                editarDTO.getConteudo(), 
                auth.getName(), 
                ehAdmin
            );
            
            ComentarioDTO response = new ComentarioDTO(comentario);
            ComentarioCriadoResponseDTO resultado = new ComentarioCriadoResponseDTO(
                "Comentário editado com sucesso!", response, LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Excluir comentário", 
               description = "Remove um comentário (soft delete) - próprio comentário ou admin pode excluir qualquer um")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário excluído com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Comentário excluído com sucesso!\", \"comentarioId\": 1, \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "403", description = "Sem permissão para excluir este comentário"),
        @ApiResponse(responseCode = "400", description = "Erro na exclusão")
    })
    @DeleteMapping("/{comentarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> excluirComentario(@PathVariable Long comentarioId, Authentication auth) {
        try {
            boolean ehAdmin = isAdmin(auth);
            
            comentarioService.excluirComentario(comentarioId, auth.getName(), ehAdmin);
            
            ComentarioExcluidoResponseDTO resultado = new ComentarioExcluidoResponseDTO(
                "Comentário excluído com sucesso!", comentarioId, LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }



    @Operation(summary = "Buscar comentário por ID", 
               description = "Busca um comentário específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comentário encontrado",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"conteudo\": \"Comentário exemplo\", \"autor\": \"user@email.com\", \"dataComentario\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "400", description = "Comentário não encontrado")
    })
    @GetMapping("/{comentarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarComentario(@PathVariable Long comentarioId, Authentication auth) {
        try {
            Comentario comentario = comentarioService.buscarPorId(comentarioId);
            ComentarioDTO response = new ComentarioDTO(comentario);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}