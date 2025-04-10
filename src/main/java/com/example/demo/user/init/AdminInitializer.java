package com.example.demo.user.init;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import jakarta.annotation.PostConstruct;
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
        if (usuarioDAO.findByNomeUsuario(nomeAdmin).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario(nomeAdmin);
            admin.setEmail("admin@admin.com"); // 
            admin.setSenha(passwordEncoder.encode("admin123")); 
            admin.setRole("ROLE_ADMIN");
            admin.setUserIsActive(true);
            usuarioDAO.save(admin);
            System.out.println("Usuário ADMIN criado: admin@admin.com / admin123");
        }
    }
}
