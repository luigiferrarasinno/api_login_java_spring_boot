package com.example.demo.user.service;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.dto.Responses.LoginResponseDTO;
import com.example.demo.user.dto.Responses.LoginAdminResponseDTO;
import com.example.demo.user.model.Usuario;
import org.springframework.stereotype.Service;

import com.example.demo.exception.EmailJaCadastradoException;
import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.security.JwtUtil;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    
    
    
    public LoginResponseDTO login(String cpf, String senha) {
        // Validações básicas
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser vazio!");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha não pode ser vazia!");
        }

        // Converte CPF para Long
        Long cpfLong;
        try {
            cpfLong = Long.parseLong(cpf.replaceAll("[^0-9]", "")); // Remove caracteres não numéricos
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter apenas números!");
        }

        // Busca usuário por CPF
        Optional<Usuario> usuario = usuarioDAO.findByCpf(cpfLong);
        
        if (usuario.isEmpty()) {
            throw new RuntimeException("CPF ou senha inválidos!");
        }

        Usuario user = usuario.get();

        // Verifica se está ativo
        if (!user.isUserIsActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta desativada!");
        }

        // Verifica se o usuário tem senha cadastrada
        if (user.getSenha() == null || user.getSenha().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Usuário não possui senha cadastrada. Por favor, cadastre uma senha antes de fazer login!");
        }

        // Verifica senha
        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "CPF ou senha inválidos!");
        }

        // Se for o primeiro login, atualizar para false no banco
        boolean wasFirstLogin = user.isFirstLogin();
        if (wasFirstLogin) {
            user.setFirstLogin(false);
            usuarioDAO.save(user);
        }

        // Gera token usando email (padrão do sistema JWT)
        String token = JwtUtil.gerarToken(user.getEmail());
        return new LoginResponseDTO(token, user.getId(), wasFirstLogin);
    }

    /**
     * Login exclusivo para administradores usando CPF
     * Apenas usuários com ROLE_ADMIN podem utilizar este endpoint
     */
    public LoginAdminResponseDTO loginAdmin(String cpf, String senha) {
        // Validações básicas
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser vazio!");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha não pode ser vazia!");
        }

        // Converte CPF para Long
        Long cpfLong;
        try {
            cpfLong = Long.parseLong(cpf.replaceAll("[^0-9]", "")); // Remove caracteres não numéricos
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter apenas números!");
        }

        // Busca usuário por CPF
        Optional<Usuario> usuario = usuarioDAO.findByCpf(cpfLong);
        
        if (usuario.isEmpty()) {
            throw new RuntimeException("CPF ou senha inválidos!");
        }

        Usuario user = usuario.get();

        // Verifica se é administrador
        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                "Acesso negado! Este login é exclusivo para administradores.");
        }

        // Verifica se está ativo
        if (!user.isUserIsActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta de administrador desativada!");
        }

        // Verifica senha
        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "CPF ou senha inválidos!");
        }

        // Se for primeiro login, atualizar
        if (user.isFirstLogin()) {
            user.setFirstLogin(false);
            usuarioDAO.save(user);
        }

        // Gera token usando email (padrão do sistema)
        String token = JwtUtil.gerarToken(user.getEmail());
        
        return new LoginAdminResponseDTO(
            token, 
            user.getId().toString(), 
            user.getNomeUsuario(), 
            user.getRole()
        );
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

    /**
     * Lista usuários com filtros opcionais
     */
    public java.util.List<Usuario> listarComFiltros(String nomeUsuario, String email, String cpf, 
                                                   String role, Boolean userIsActive, 
                                                   String dataInicio, String dataFim) {
        try {
            // Nota: campos de data ignorados pois Usuario não tem createdAt
            return usuarioDAO.findUsuariosComFiltros(
                nomeUsuario, email, cpf, role, userIsActive
            );
        } catch (Exception e) {
            // Em caso de erro nos filtros, retorna todos os usuários
            return (java.util.List<Usuario>) usuarioDAO.findAll();
        }
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
    @Transactional
    public void trocarEmailPorCpf(Long cpf, String novoEmail) {
        Usuario usuario = usuarioDAO.findByCpf(cpf)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para o CPF informado"));

        if (usuario.getEmail().equalsIgnoreCase(novoEmail)) {
            throw new EmailJaCadastradoException("O email informado já está cadastrado para este usuário.");
        }

        usuarioDAO.findByEmail(novoEmail).ifPresent(u -> {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário.");
        });

        usuario.setEmail(novoEmail);
        usuarioDAO.save(usuario);
    }

    // Método simplificado para redefinir senha apenas com CPF
    public String redefinirSenhaPorCpf(Long cpf, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByCpf(cpf);

        if (usuarioOpt.isEmpty()) {
            return "Usuário com esse CPF não encontrado!";
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioDAO.save(usuario);

        return "Senha redefinida com sucesso!";
    }

    // Método para trocar email usando o token JWT (email atual)
    public void trocarEmailPorToken(String emailAtual, String novoEmail) {
        Usuario usuario = usuarioDAO.findByEmail(emailAtual)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (usuario.getEmail().equalsIgnoreCase(novoEmail)) {
            throw new EmailJaCadastradoException("O email informado já está cadastrado para este usuário.");
        }

        usuarioDAO.findByEmail(novoEmail).ifPresent(u -> {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário.");
        });

        usuario.setEmail(novoEmail);
        usuarioDAO.save(usuario);
    }

    // Método para alterar senha usando o token JWT (sem precisar do email)
    public String alterarSenhaPorToken(String emailUsuario, String senhaAntiga, String senhaNova) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(emailUsuario);

        if (usuarioOpt.isEmpty()) {
            return "Usuário não encontrado!";
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(senhaAntiga, usuario.getSenha())) {
            return "Senha antiga não confere!";
        }

        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioDAO.save(usuario);

        return "Senha alterada com sucesso!";
    }

    @Transactional
    public String alterarDadosUsuarioPorAdmin(Long usuarioId, String nomeUsuario, String email, 
                                            Long cpf, LocalDate dt_nascimento, String senha, 
                                            Boolean userIsActive, String role, com.example.demo.user.model.TipoPerfil tipo) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findbyid(usuarioId);
        
        if (usuarioOpt.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + usuarioId);
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar se email já existe (se foi alterado)
        if (email != null && !email.equals(usuario.getEmail())) {
            Optional<Usuario> usuarioExistente = usuarioDAO.findByEmail(email);
            if (usuarioExistente.isPresent()) {
                throw new EmailJaCadastradoException("Email já está em uso: " + email);
            }
            usuario.setEmail(email);
        }
        
        // Verificar se CPF já existe (se foi alterado)
        if (cpf != null && !cpf.equals(usuario.getCpf())) {
            Optional<Usuario> usuarioExistente = usuarioDAO.findByCpf(cpf);
            if (usuarioExistente.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já está em uso: " + cpf);
            }
            usuario.setCpf(cpf);
        }
        
        // Atualizar campos se foram fornecidos
        if (nomeUsuario != null) {
            usuario.setNomeUsuario(nomeUsuario);
        }
        
        if (dt_nascimento != null) {
            usuario.setDt_nascimento(dt_nascimento);
        }
        
        if (senha != null) {
            usuario.setSenha(passwordEncoder.encode(senha));
        }
        
        if (userIsActive != null) {
            usuario.setUserIsActive(userIsActive);
        }
        
        if (role != null) {
            usuario.setRole(role);
        }
        
        if (tipo != null) {
            usuario.setTipo(tipo);
        }
        
        usuarioDAO.save(usuario);
        
        return "Dados do usuário alterados com sucesso!";
    }

}
