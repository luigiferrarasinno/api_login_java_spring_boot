package com.example.demo.user.controller;

import com.example.demo.user.dto.UsuarioDTO;
import com.example.demo.user.dto.Responses.LoginResponseDTO;
import com.example.demo.user.dto.Responses.StatusAtivoResponseDTO;
import com.example.demo.user.dto.Responses.UsuarioResponseDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.service.UsuarioService;
import com.example.demo.user.dto.AlterarSenhaPorCpfEmailDTO;
import com.example.demo.user.dto.AlterarSenhaComSenhaAntiga;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import com.example.demo.user.dto.TrocarEmailRequest;

import java.time.LocalDateTime;
import java.util.Collections;
//import java.util.stream.StreamSupport;
//import java.util.List;
//import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

   @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.login(usuarioDTO.getEmail(), usuarioDTO.getSenha());
    }

    @PostMapping("/criar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String criarConta(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(usuarioDTO.getNomeUsuario());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDt_nascimento(usuarioDTO.getDt_nascimento());
        usuario.setCpf(usuarioDTO.getCpf());
        return usuarioService.criarConta(usuario);
    }

    @PutMapping("/alterar-senha")
    @PreAuthorize("#dto.email == authentication.name or hasAuthority('ROLE_ADMIN')")
    public String alterarSenha(@RequestBody AlterarSenhaComSenhaAntiga dto) {
        return usuarioService.alterarSenha(dto.getEmail(), dto.getSenhaAntiga(), dto.getSenhaNova());
    }



    @PostMapping("/criar-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody AlterarSenhaPorCpfEmailDTO dto) {
        String resultado = usuarioService.redefinirSenhaPorEmailCpf(
            dto.getEmail(),
            dto.getCpf(),
            dto.getDtNascimento(),
            dto.getSenhaNova()
        );
        
        if (resultado.equals("Senha redefinida com sucesso!")) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
    }

   @PutMapping("/trocar-email")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> trocarEmail(@RequestBody TrocarEmailRequest request) {
        usuarioService.trocarEmailPorCpf(request.getCpf(), request.getNovoEmail());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("mensagem", "Email do usuário com CPF " + request.getCpf() + " alterado com sucesso.");

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
    public Iterable<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
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

    @PutMapping("/{id}")
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

}
