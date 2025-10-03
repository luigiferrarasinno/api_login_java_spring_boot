package com.example.demo.user.controller;

import com.example.demo.user.dto.UsuarioDTO;
import com.example.demo.user.dto.LoginRequestDTO;
import com.example.demo.user.dto.CriarUsuarioRequestDTO;
import com.example.demo.user.dto.CriarSenhaRequestDTO;
import com.example.demo.user.dto.TrocarEmailRequestDTO;
import com.example.demo.user.dto.AlterarSenhaRequestDTO;
import com.example.demo.user.dto.AlterarUsuarioAdminRequestDTO;
import com.example.demo.user.dto.Responses.LoginResponseDTO;
import com.example.demo.user.dto.Responses.StatusAtivoResponseDTO;
import com.example.demo.user.dto.Responses.UsuarioResponseDTO;
import com.example.demo.user.dto.Responses.UsuarioListaResponseDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.service.UsuarioService;
import com.example.demo.user.dto.AlterarSenhaPorCpfEmailDTO;
import com.example.demo.user.dto.AlterarSenhaComSenhaAntiga;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import java.util.Map;
import com.example.demo.user.dto.TrocarEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

@Tag(name = "👤 Usuários", description = "Sistema de autenticação e gerenciamento de usuários com filtros avançados")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Login de usuário", 
               description = "Autentica usuário no sistema e retorna token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"userId\": \"1\"}")))
    })
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        return usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());
    }

    @Operation(summary = "Criar novo usuário", 
               description = "Cria uma nova conta de usuário (apenas admins)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "\"Usuário criado com sucesso!\"")))
    })
    @PostMapping("/criar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String criarConta(@RequestBody CriarUsuarioRequestDTO criarRequest) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(criarRequest.getNomeUsuario());
        usuario.setSenha(criarRequest.getSenha());
        usuario.setEmail(criarRequest.getEmail());
        usuario.setDt_nascimento(criarRequest.getDt_nascimento());
        usuario.setCpf(criarRequest.getCpf());
        return usuarioService.criarConta(usuario);
    }

    @Operation(summary = "Alterar senha com senha atual", 
               description = "Permite ao usuário alterar sua senha fornecendo a senha atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Senha alterada com sucesso!\"}")))
    })
    @PutMapping("/alterar-senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> alterarSenha(@RequestBody AlterarSenhaRequestDTO alterarSenhaRequest, Authentication auth) {
        String emailUsuario = auth.getName(); // Email do usuário logado
        
        String resultado = usuarioService.alterarSenhaPorToken(emailUsuario, alterarSenhaRequest.getSenhaAntiga(), alterarSenhaRequest.getSenhaNova());
        
        Map<String, String> response = new HashMap<>();
        
        if (resultado.equals("Senha alterada com sucesso!")) {
            response.put("mensagem", resultado);
            return ResponseEntity.ok(response);
        } else {
            response.put("erro", resultado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @Operation(summary = "Redefinir senha por CPF", 
               description = "Permite redefinir senha usando CPF, email e data de nascimento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Senha redefinida com sucesso!\"}")))
    })
    @PostMapping("/criar-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@RequestBody CriarSenhaRequestDTO criarSenhaRequest) {
        String resultado = usuarioService.redefinirSenhaPorCpf(criarSenhaRequest.getCpf(), criarSenhaRequest.getSenhaNova());
        
        Map<String, String> response = new HashMap<>();
        
        if (resultado.equals("Senha redefinida com sucesso!")) {
            response.put("mensagem", resultado);
            return ResponseEntity.ok(response);
        } else {
            response.put("erro", resultado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Trocar email do usuário", 
               description = "Permite ao usuário alterar seu endereço de email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email alterado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"timestamp\": \"2024-10-02T14:30:15\", \"mensagem\": \"Email alterado com sucesso de user@old.com para user@new.com\"}")))
    })
    @PutMapping("/trocar-email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> trocarEmail(@RequestBody TrocarEmailRequestDTO trocarEmailRequest, Authentication auth) {
        String emailAtual = auth.getName(); // Email do usuário logado
        
        usuarioService.trocarEmailPorToken(emailAtual, trocarEmailRequest.getNovoEmail());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("mensagem", "Email alterado com sucesso de " + emailAtual + " para " + trocarEmailRequest.getNovoEmail());

        return ResponseEntity.ok(body);
    }



    @Operation(summary = "Deletar conta de usuário", 
               description = "Remove conta do sistema (próprio usuário ou admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conta deletada com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "\"Conta deletada com sucesso!\"")))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public String deletarConta(@PathVariable Long id) {
        return usuarioService.deletarConta(id);
    }

    
    @Operation(summary = "Listar usuários com filtros", 
               description = "Lista todos os usuários com filtros opcionais (apenas admins)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários filtrada",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[{\"id\": 1, \"nomeUsuario\": \"joao.silva\", \"email\": \"joao@email.com\", \"role\": \"USER\", \"userIsActive\": true}]")))
    })
    // Endpoint para listar usuários com filtros opcionais
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioListaResponseDTO>> listarUsuarios(
            @RequestParam(required = false) String nomeUsuario,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean userIsActive,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim) {
        try {
            List<Usuario> usuarios = usuarioService.listarComFiltros(
                nomeUsuario, email, cpf, role, userIsActive, dataInicio, dataFim
            );
            List<UsuarioListaResponseDTO> usuariosDTO = usuarios.stream()
                .map(UsuarioListaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Buscar usuário por ID", 
               description = "Obtém detalhes de um usuário específico (próprio usuário ou admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 2, \"nomeUsuario\": \"João da Silva\", \"email\": \"joao@email.com\", \"role\": \"ROLE_USER\", \"cpf\": 12345678909, \"userIsActive\": true}")))
    })
    @GetMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<Object> buscarUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuario(id);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
    }

    @Operation(summary = "Alternar status do usuário", 
               description = "Ativa ou desativa uma conta de usuário (próprio usuário ou admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Status de atividade atualizado com sucesso!\", \"ativo\": false}")))
    })
    @PatchMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<Object> alternarStatusUsuario(@PathVariable Long id) {
        try {
            Usuario usuarioAtualizado = usuarioService.alternarStatusUsuario(id);
            StatusAtivoResponseDTO responseDTO = new StatusAtivoResponseDTO(
                "Status de atividade atualizado com sucesso!",
                usuarioAtualizado.isUserIsActive()
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("erro", e.getMessage()));
        }
    }

    @Operation(summary = "Alterar dados do usuário (Admin)", 
               description = "Permite ao admin alterar todos os dados de qualquer usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados alterados com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Dados do usuário alterados com sucesso!\", \"timestamp\": \"2024-10-02T14:30:15\", \"status\": \"sucesso\"}")))
    })
    @PutMapping("/admin/alterar/{usuarioId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> alterarDadosUsuarioPorAdmin(
            @PathVariable Long usuarioId,
            @RequestBody AlterarUsuarioAdminRequestDTO request) {
        try {
            String mensagem = usuarioService.alterarDadosUsuarioPorAdmin(
                usuarioId,
                request.getNomeUsuario(),
                request.getEmail(),
                request.getCpf(),
                request.getDt_nascimento(),
                request.getSenha(),
                request.getUserIsActive(),
                request.getRole(),
                request.getTipo()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
