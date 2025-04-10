package com.example.demo.user.service;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import org.springframework.stereotype.Service;
import com.example.demo.security.JwtUtil; 
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioDAO usuarioDAO, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
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
    
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return JwtUtil.gerarToken(nomeUsuario);
        } else {
            throw new RuntimeException("Usuário ou senha inválidos!");
        }
    }
    

    public String criarConta(Usuario usuario) {
        if (usuarioDAO.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            return "Nome de usuário já existe!";
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioDAO.save(usuario);
        usuario.setUserIsActive(true);
        return "Conta criada com sucesso!";
    }

    public String alterarSenha(String nomeUsuario, String novaSenha) {
        Optional<Usuario> usuarioExistente = usuarioDAO.findByNomeUsuario(nomeUsuario);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setSenha(passwordEncoder.encode(novaSenha)); // <- Criptografa nova senha
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
