package com.example.demo.user.init;

import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.repository.ComentarioRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.model.TipoPerfil;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class AdminInitializer {

    private final UsuarioDAO usuarioDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ComentarioRepository comentarioRepository;
    private final InvestimentoRepository investimentoRepository;

    public AdminInitializer(UsuarioDAO usuarioDAO, 
                          BCryptPasswordEncoder passwordEncoder,
                          ComentarioRepository comentarioRepository,
                          InvestimentoRepository investimentoRepository) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
        this.comentarioRepository = comentarioRepository;
        this.investimentoRepository = investimentoRepository;
    }

    @PostConstruct
    public void inicializarUsuarios() {
        criarAdmin();
        criarUsuarioComum();
        criarComentariosIniciais();
    }

    private void criarAdmin() {
        String emailAdmin = "admin@admin.com";

        if (usuarioDAO.findByEmail(emailAdmin).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario("admin");
            admin.setEmail(emailAdmin);
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setUserIsActive(true);
            admin.setUser_permissions(null);
            admin.setTipo_de_investidor(null);
            admin.setCpf(99999999999L);
            admin.setDt_nascimento(LocalDate.of(1990, 1, 1));
            admin.setFirstLogin(false);
            admin.setSaldoCarteira(new java.math.BigDecimal("100000.00")); // R$ 100.000 para admin

            usuarioDAO.save(admin);
            System.out.println("Usuário ADMIN criado: admin@admin.com / admin123");
        }
    }

    private void criarUsuarioComum() {
        String emailUsuario = "usuario@teste.com";

        if (usuarioDAO.findByEmail(emailUsuario).isEmpty()) {
            Usuario user = new Usuario();
            user.setNomeUsuario("Usuário Comum");
            user.setEmail(emailUsuario);
            user.setSenha(passwordEncoder.encode("teste123"));
            user.setRole("ROLE_USER");
            user.setUserIsActive(true);
            user.setUser_permissions("nenhuma por enquanto");
            user.setTipo_de_investidor("nenhum por enquanto");
            user.setCpf(88888888888L);
            user.setDt_nascimento(LocalDate.of(2000, 5, 15));
            user.setFirstLogin(true);
            user.setTipo(TipoPerfil.PERFIL_CONSERVADOR); // Definindo perfil conservador como padrão
            user.setSaldoCarteira(new java.math.BigDecimal("10000.00")); // R$ 10.000 para usuário comum

            usuarioDAO.save(user);
            System.out.println("Usuário COMUM criado: usuario@teste.com / teste123");
        }
    }

    private void criarComentariosIniciais() {
        if (comentarioRepository.count() == 0) {
            // Buscar usuário teste e admin
            Optional<Usuario> usuarioTeste = usuarioDAO.findByEmail("usuario@teste.com");
            Optional<Usuario> admin = usuarioDAO.findByEmail("admin@admin.com");
            
            if (usuarioTeste.isEmpty() || admin.isEmpty()) {
                System.out.println("Usuários não encontrados para criar comentários iniciais");
                return;
            }

            // Buscar alguns investimentos
            java.util.List<Investimento> investimentos = investimentoRepository.findAll();
            if (investimentos.isEmpty()) {
                System.out.println("Nenhum investimento encontrado para criar comentários");
                return;
            }

            Usuario usuario = usuarioTeste.get();
            Usuario adminUser = admin.get();

            // Comentários do usuário teste
            if (investimentos.size() > 0) {
                Comentario c1 = new Comentario(
                    "Excelente opção de investimento! Tenho visto bons retornos neste ativo.",
                    usuario,
                    investimentos.get(0)
                );
                comentarioRepository.save(c1);
            }

            if (investimentos.size() > 1) {
                Comentario c2 = new Comentario(
                    "Estou considerando diversificar minha carteira com este investimento. Alguém tem experiência?",
                    usuario,
                    investimentos.get(1)
                );
                comentarioRepository.save(c2);
            }

            if (investimentos.size() > 0) {
                Comentario c3 = new Comentario(
                    "Para investidores iniciantes, recomendo estudar bem antes de investir neste ativo.",
                    usuario,
                    investimentos.get(0)
                );
                comentarioRepository.save(c3);
            }

            // Comentários do admin
            if (investimentos.size() > 1) {
                Comentario c4 = new Comentario(
                    "Este investimento apresenta boa liquidez e é adequado para perfil conservador.",
                    adminUser,
                    investimentos.get(1)
                );
                comentarioRepository.save(c4);
            }

            if (investimentos.size() > 2) {
                Comentario c5 = new Comentario(
                    "Atenção para a volatilidade deste ativo. Adequado apenas para perfil arrojado.",
                    adminUser,
                    investimentos.get(2)
                );
                comentarioRepository.save(c5);
            }

            if (investimentos.size() > 0) {
                Comentario c6 = new Comentario(
                    "Investimento seguro e com boa rentabilidade histórica. Recomendado para carteiras conservadoras.",
                    adminUser,
                    investimentos.get(0)
                );
                comentarioRepository.save(c6);
            }

            System.out.println("Comentários iniciais criados com sucesso!");
            System.out.println("- 3 comentários do usuário teste (usuario@teste.com)");
            System.out.println("- 3 comentários do admin (admin@admin.com)");
        }
    }
}
