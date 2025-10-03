package com.example.demo.user.init;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.model.TipoPerfil;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminInitializer {

    private final UsuarioDAO usuarioDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioDAO usuarioDAO, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void inicializarUsuarios() {
        criarAdmin();
        criarUsuarioComum();
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
}
