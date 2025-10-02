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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        return usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());
    }

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



    @DeleteMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public String deletarConta(@PathVariable Long id) {
        return usuarioService.deletarConta(id);
    }

    
    // Endpoint para listar todos os usuários
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioListaResponseDTO>> listarUsuarios() {
        try {
            List<Usuario> usuarios = (List<Usuario>) usuarioService.listarUsuarios();
            List<UsuarioListaResponseDTO> usuariosDTO = usuarios.stream()
                .map(UsuarioListaResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
