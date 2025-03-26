package com.example.demo.service;

import com.example.demo.dao.UsuarioDAO;
import com.example.demo.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public String login(String nomeUsuario, String senha) {
        Optional<Usuario> usuarioExistente = usuarioDAO.findByNomeUsuario(nomeUsuario);
        if (usuarioExistente.isPresent() && usuarioExistente.get().getSenha().equals(senha)) {
            return "Login bem-sucedido!";
        } else {
            return "Usuário ou senha inválidos!";
        }
    }

    public String criarConta(Usuario usuario) {
        if (usuarioDAO.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            return "Nome de usuário já existe!";
        }
        usuarioDAO.save(usuario);
        return "Conta criada com sucesso!";
    }

    public String alterarSenha(String nomeUsuario, String novaSenha) {
        Optional<Usuario> usuarioExistente = usuarioDAO.findByNomeUsuario(nomeUsuario);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setSenha(novaSenha);
            usuarioDAO.save(usuario);
            return "Senha alterada com sucesso!";
        } else {
            return "Usuário não encontrado!";
        }
    }

    public String deletarConta(Long id) {
        if (usuarioDAO.existsById(id)) {
            usuarioDAO.deleteById(id);
            return "Conta excluída com sucesso!";
        } else {
            return "Usuário não encontrado!";
        }
    }

    public Iterable<Usuario> listarUsuarios() {
        return usuarioDAO.findAll();
    }
}
