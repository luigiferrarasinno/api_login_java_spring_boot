package com.example.demo.comentarios.controller;

import com.example.demo.comentarios.dto.ComentarioDTO;
import com.example.demo.comentarios.dto.CriarComentarioDTO;
import com.example.demo.comentarios.dto.EditarComentarioDTO;
import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller para sistema de comentários sobre investimentos/ações
 */
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

    /**
     * 📝 Criar novo comentário
     */
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
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensagem", "Comentário criado com sucesso!");
            resultado.put("comentario", response);
            resultado.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 📋 Buscar comentários de uma ação específica (público)
     */
    @GetMapping("/investimento/{investimentoId}")
    public ResponseEntity<?> buscarComentariosPorInvestimento(@PathVariable Long investimentoId) {
        try {
            List<Comentario> comentarios = comentarioService.buscarComentariosPorInvestimento(investimentoId);
            
            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("investimentoId", investimentoId);
            response.put("totalComentarios", comentariosDTO.size());
            response.put("comentarios", comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 👤 Buscar comentários do usuário logado
     */
    @GetMapping("/meus")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarMeusComentarios(Authentication auth) {
        try {
            List<Comentario> comentarios = comentarioService.buscarComentariosDoUsuario(auth.getName());
            
            List<ComentarioDTO> comentariosDTO = comentarios.stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", auth.getName());
            response.put("totalComentarios", comentariosDTO.size());
            response.put("comentarios", comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🔒 ADMIN: Buscar todos os comentários com filtros opcionais
     */
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
            
            Map<String, Object> response = new HashMap<>();
            response.put("filtros", Map.of(
                "investimentoId", investimentoId != null ? investimentoId : "todos",
                "usuarioId", usuarioId != null ? usuarioId : "todos",
                "conteudo", conteudo != null ? conteudo : "todos",
                "dataInicio", dataInicio != null ? dataInicio : "todos",
                "dataFim", dataFim != null ? dataFim : "todos"
            ));
            response.put("totalComentarios", comentariosDTO.size());
            response.put("comentarios", comentariosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * ✏️ Editar comentário
     * Usuário: apenas seus comentários
     * Admin: qualquer comentário
     */
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
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensagem", "Comentário editado com sucesso!");
            resultado.put("comentario", response);
            resultado.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🗑️ Excluir comentário (soft delete)
     * Usuário: apenas seus comentários
     * Admin: qualquer comentário
     */
    @DeleteMapping("/{comentarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> excluirComentario(@PathVariable Long comentarioId, Authentication auth) {
        try {
            boolean ehAdmin = isAdmin(auth);
            
            comentarioService.excluirComentario(comentarioId, auth.getName(), ehAdmin);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensagem", "Comentário excluído com sucesso!");
            resultado.put("comentarioId", comentarioId);
            resultado.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 📊 Estatísticas de comentários de um investimento
     */
    @GetMapping("/investimento/{investimentoId}/estatisticas")
    public ResponseEntity<?> estatisticasComentarios(@PathVariable Long investimentoId) {
        try {
            long totalComentarios = comentarioService.contarComentariosPorInvestimento(investimentoId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("investimentoId", investimentoId);
            stats.put("totalComentarios", totalComentarios);
            stats.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 📋 Buscar comentário específico por ID
     */
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