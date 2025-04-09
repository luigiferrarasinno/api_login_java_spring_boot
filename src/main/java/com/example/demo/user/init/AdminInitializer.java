package com.example.demo.user.init;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {
    private final UsuarioDAO usuarioDAO;

    public AdminInitializer(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    //Usuário: admin
    //Senha: admin123
    @PostConstruct
    public void criarAdmin() {
        String nomeAdmin = "admin";
        if (usuarioDAO.findByNomeUsuario(nomeAdmin).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario(nomeAdmin);
            admin.setSenha("admin123"); // uma senha generica que eu fiz para adm 
            admin.setRole("ROLE_ADMIN");
            usuarioDAO.save(admin);
            System.out.println("Usuário ADMIN criado: admin/admin123");
        }
    }
}

// pra criar e logar na conta pelo postman que é por onde eu testo precisa de um json tipo assim em raw no body da requisção , anotação para eu não esquecer dps
//{
 // "nomeUsuario": "novoUsuario1",
 // "senha": "novaSenha1"
//}

