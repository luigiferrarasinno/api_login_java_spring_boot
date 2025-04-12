package com.example.demo.user.repository;

import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCpf(Long cpf);
}
