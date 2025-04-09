package com.example.demo.user.controller;

import com.example.demo.user.dto.UsuarioDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;



@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.login(usuarioDTO.getNomeUsuario(), usuarioDTO.getSenha());
    }

    // Endpoint para criar uma nova conta
    @PostMapping("/criar")
    public String criarConta(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(usuarioDTO.getNomeUsuario());
        usuario.setSenha(usuarioDTO.getSenha());
        return usuarioService.criarConta(usuario);
    }
    
    // Endpoint para alterar a senha
    @PutMapping("/alterar-senha")
    @PreAuthorize("#nomeUsuario == authentication.name or hasAuthority('ROLE_ADMIN')")
    public String alterarSenha(@RequestParam String nomeUsuario, @RequestParam String novaSenha) {
        return usuarioService.alterarSenha(nomeUsuario, novaSenha);
    }

    // Endpoint para deletar conta
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
            Usuario usuario = usuarioService.buscarUsuario(id);  // Chama o serviço para buscar o usuário
            return ResponseEntity.ok(usuario);  // Retorna o usuário com status 200 OK
        } catch (RuntimeException e) {
            // Retorna 404 com a mensagem "Usuário não encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
    }
}
