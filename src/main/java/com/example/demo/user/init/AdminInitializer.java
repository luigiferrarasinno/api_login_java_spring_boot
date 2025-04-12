package com.example.demo.user.init;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    private final UsuarioDAO usuarioDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioDAO usuarioDAO, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    //Usuário: admin
    //Senha: admin123
    //Email: admin@admin.com
    @PostConstruct
    public void criarAdmin() {
        String nomeAdmin = "admin";
        String emailAdmin = "admin@admin.com";

        if (usuarioDAO.findByEmail(emailAdmin).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario(nomeAdmin);
            admin.setEmail(emailAdmin);
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setUserIsActive(true);
            admin.setUser_permissions(null);
            admin.setTipo_de_investidor(null); // Tipo de investidor padrão

            // Campos adicionados
            admin.setCpf(99999999999L); // CPF fictício válido (não será verificado aqui)
            admin.setDt_nascimento(LocalDate.of(1990, 1, 1)); // Data de nascimento padrão

            usuarioDAO.save(admin);
            System.out.println("Usuário ADMIN criado: admin@admin.com / admin123");
        }
    }

}
