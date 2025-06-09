package com.example.demo.user.service;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.dto.Responses.LoginResponseDTO;
import com.example.demo.user.model.Usuario;
import org.springframework.stereotype.Service;
import com.example.demo.security.JwtUtil;

import java.time.LocalDate;
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
        Optional<Usuario> usuarioAuth = usuarioDAO.findByEmail(nomeUsuarioAuth);
    
        if (usuarioAuth.isEmpty()) return false; // Se o usuário autenticado não existir, acesso negado
    
        Usuario usuarioLogado = usuarioAuth.get();
    
        if (usuarioLogado.getRole().equals("ROLE_ADMIN")) {
            return true; // Admin pode acessar qualquer coisa, mesmo que o usuário alvo não exista
        }
    
        Optional<Usuario> usuarioTarget = usuarioDAO.findbyid(id);
    
        return usuarioTarget.isPresent() &&
               usuarioLogado.getId().equals(usuarioTarget.get().getId());
    }
    
    
    
    public LoginResponseDTO login(String email, String senha) {
        Optional<Usuario> usuario = usuarioDAO.findByEmail(email);

        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            Usuario user = usuario.get();
            boolean wasFirstLogin = user.isFirstLogin(); // guarda valor original

            // Se for o primeiro login, atualizar para false no banco
            if (wasFirstLogin) {
                user.setFirstLogin(false);
                usuarioDAO.save(user);
            }

            String token = JwtUtil.gerarToken(email);
            return new LoginResponseDTO(token, user.getId(), wasFirstLogin);
        } else {
            throw new RuntimeException("Email ou senha inválidos!");
        }
    }

    // Método para validar CPF
    // O CPF deve ter 11 dígitos e não pode conter todos os dígitos iguais (ex: 111.111.111-11)

    private boolean isCpfValido(Long cpf) {
        if (cpf == null) return false;
        String cpfStr = String.format("%011d", cpf); // completa com zeros à esquerda se necessário
    
        if (cpfStr.matches("(\\d)\\1{10}")) return false; // rejeita CPFs com todos os dígitos iguais
    
        int soma = 0, resto;
        for (int i = 1; i <= 9; i++) {
            soma += Integer.parseInt(cpfStr.substring(i - 1, i)) * (11 - i);
        }
        resto = (soma * 10) % 11;
        if ((resto == 10) || (resto == 11)) resto = 0;
        if (resto != Integer.parseInt(cpfStr.substring(9, 10))) return false;
    
        soma = 0;
        for (int i = 1; i <= 10; i++) {
            soma += Integer.parseInt(cpfStr.substring(i - 1, i)) * (12 - i);
        }
        resto = (soma * 10) % 11;
        if ((resto == 10) || (resto == 11)) resto = 0;
        return resto == Integer.parseInt(cpfStr.substring(10));
    }

    // Método para verificar se o usuário tem mais de 18 anos
    // O método verifica se a data de nascimento é válida e se o usuário tem pelo menos 18 anos
    
    private boolean temMaisDe18Anos(LocalDate nascimento) {
        if (nascimento == null) return false;
        LocalDate hoje = LocalDate.now();
        return nascimento.plusYears(18).isBefore(hoje) || nascimento.plusYears(18).isEqual(hoje);
    }
    
    // Método para criar uma nova conta de usuário  


    public String criarConta(Usuario usuario) {
        String email = usuario.getEmail();
        Long cpf = usuario.getCpf();
        LocalDate nascimento = usuario.getDt_nascimento();

        // Validação simples de e-mail
        if (email == null || !email.contains("@") || email.length() < 5 || email.indexOf("@") == email.length() - 1) {
            return "Email inválido! Informe um email com '@' e ao menos 5 caracteres.";
        }

        // Validação do CPF
        if (!isCpfValido(cpf)) {
            return "CPF inválido! Verifique se o número está correto.";
        }

        // Verificação de idade
        if (!temMaisDe18Anos(nascimento)) {
            return "Você precisa ter pelo menos 18 anos para criar uma conta.";
        }

        // Verifica se já existe um usuário com esse e-mail
        if (usuarioDAO.findByEmail(email).isPresent()) {
            return "Email já cadastrado!";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setUserIsActive(true);
        usuarioDAO.save(usuario);

        return "Conta criada com sucesso!";
    }

    //para criraar a senha
    public String redefinirSenhaPorEmailCpf(String email, Long cpf, LocalDate dtNascimento, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return "Usuário com esse e-mail não encontrado!";
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getCpf().equals(cpf)) {
            return "CPF não confere com o e-mail fornecido!";
        }

        if (!usuario.getDt_nascimento().isEqual(dtNascimento)) {
            return "Data de nascimento não confere com o e-mail e CPF fornecidos!";
        }


        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioDAO.save(usuario);

        return "Senha redefinida com sucesso!";
    }


    
    //para alterar a senha do usuário
    public String alterarSenha(String email, String senhaAntiga, String senhaNova) {
        Optional<Usuario> usuarioExistente = usuarioDAO.findByEmail(email);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            if (!passwordEncoder.matches(senhaAntiga, usuario.getSenha())) {
                return "Senha antiga incorreta!";
            }
            usuario.setSenha(passwordEncoder.encode(senhaNova));
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

    public Usuario buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));
    }
    
}
