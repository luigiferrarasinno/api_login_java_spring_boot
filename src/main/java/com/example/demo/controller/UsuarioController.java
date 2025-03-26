package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

        // Criar um usuário de teste com nome 'teste' e senha 'senha1234' caso ainda não exista
        if (!usuarioRepository.findByNomeUsuario("teste").isPresent()) {
            Usuario usuarioTeste = new Usuario();
            usuarioTeste.setNomeUsuario("teste");
            usuarioTeste.setSenha("senha1234");
            usuarioRepository.save(usuarioTeste);
        }
    }

    // Endpoint para login
    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByNomeUsuario(usuario.getNomeUsuario());
        if (usuarioExistente.isPresent() && usuarioExistente.get().getSenha().equals(usuario.getSenha())) {
            return "Login bem-sucedido!";
        } else {
            return "Usuário ou senha inválidos!";
        }
    }

    // Endpoint para criar uma nova conta
    @PostMapping("/criar")
    public String criarConta(@RequestBody Usuario usuario) {
        // Verifica se o nome de usuário já existe
        if (usuarioRepository.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            return "Nome de usuário já existe!";
        }
        usuarioRepository.save(usuario);
        return "Conta criada com sucesso!";
    }

    // Endpoint para alterar a senha
    @PutMapping("/alterar-senha")
    public String alterarSenha(@RequestParam String nomeUsuario, @RequestParam String novaSenha) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setSenha(novaSenha);
            usuarioRepository.save(usuario);
            return "Senha alterada com sucesso!";
        } else {
            return "Usuário não encontrado!";
        }
    }

    // Endpoint para deletar conta
    @DeleteMapping("/{id}")
    public String deletarConta(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return "Conta excluída com sucesso!";
        } else {
            return "Usuário não encontrado!";
        }
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
