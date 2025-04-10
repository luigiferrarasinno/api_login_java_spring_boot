package com.example.demo.user.service;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import org.springframework.stereotype.Service;
import com.example.demo.security.JwtUtil; 
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean isOwnerOrAdmin(Long id, String nomeUsuarioAuth) {
        Optional<Usuario> usuarioAuth = usuarioDAO.findByNomeUsuario(nomeUsuarioAuth);
    
        if (usuarioAuth.isEmpty()) return false; // Se o usuário autenticado não existir, acesso negado
    
        Usuario usuarioLogado = usuarioAuth.get();
    
        if (usuarioLogado.getRole().equals("ROLE_ADMIN")) {
            return true; // Admin pode acessar qualquer coisa, mesmo que o usuário alvo não exista
        }
    
        Optional<Usuario> usuarioTarget = usuarioDAO.findbyid(id);
    
        return usuarioTarget.isPresent() &&
               usuarioLogado.getId().equals(usuarioTarget.get().getId());
    }
    
    
    

    public String login(String nomeUsuario, String senha) {
        Optional<Usuario> usuario = usuarioDAO.findByNomeUsuario(nomeUsuario);
    
        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            // Se usuário e senha batem, gerar token JWT
            return JwtUtil.gerarToken(nomeUsuario);
        } else {
            throw new RuntimeException("Usuário ou senha inválidos!");
        }
    }

    public String criarConta(Usuario usuario) {
        if (usuarioDAO.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            return "Nome de usuário já existe!";
        }
        usuarioDAO.save(usuario);
        usuario.setUserIsActive(true);
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

    //mostrar informações do usuário pelo id
    public Usuario buscarUsuario(Long id) {
    Optional<Usuario> usuarioExistente = usuarioDAO.findbyid(id);
    if (usuarioExistente.isPresent()) {
        return usuarioExistente.get();  // Retorna o objeto Usuario
    } else {
        throw new RuntimeException("Usuário não encontrado!"); // Exceção personalizada
    }
    }

    public Usuario alternarStatusUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findbyid(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado!");
        }
    
        Usuario usuario = usuarioOpt.get();
        usuario.setUserIsActive(!usuario.isUserIsActive()); // alterna true/false
        return usuarioDAO.save(usuario);
    }
    
    
}
