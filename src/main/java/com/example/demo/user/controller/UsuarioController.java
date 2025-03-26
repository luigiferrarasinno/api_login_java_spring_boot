package com.example.demo.user.controller;

import com.example.demo.user.model.Usuario;
import com.example.demo.user.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para login
    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario.getNomeUsuario(), usuario.getSenha());
    }

    // Endpoint para criar uma nova conta
    @PostMapping("/criar")
    public String criarConta(@RequestBody Usuario usuario) {
        return usuarioService.criarConta(usuario);
    }

    // Endpoint para alterar a senha
    @PutMapping("/alterar-senha")
    public String alterarSenha(@RequestParam String nomeUsuario, @RequestParam String novaSenha) {
        return usuarioService.alterarSenha(nomeUsuario, novaSenha);
    }

    // Endpoint para deletar conta
    @DeleteMapping("/{id}")
    public String deletarConta(@PathVariable Long id) {
        return usuarioService.deletarConta(id);
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public Iterable<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
}
