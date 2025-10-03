package com.example.demo.user.repository;

import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCpf(Long cpf);
    
    // Filtros individuais
    List<Usuario> findByNomeUsuarioContainingIgnoreCase(String nomeUsuario);
    List<Usuario> findByEmailContainingIgnoreCase(String email);
    List<Usuario> findByUserIsActive(Boolean userIsActive);
    List<Usuario> findByRole(String role);
    
    // Busca por CPF como string parcial (para filtros)
    @Query("SELECT u FROM Usuario u WHERE CAST(u.cpf AS string) LIKE CONCAT('%', :cpf, '%')")
    List<Usuario> findByCpfContaining(@Param("cpf") String cpf);
    
    // Filtro combinado para admin (sem filtros de data pois não temos createdAt no Usuario)
    @Query("SELECT u FROM Usuario u WHERE " +
           "(:nomeUsuario IS NULL OR LOWER(u.nomeUsuario) LIKE LOWER(CONCAT('%', :nomeUsuario, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:cpf IS NULL OR CAST(u.cpf AS string) LIKE CONCAT('%', :cpf, '%')) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:userIsActive IS NULL OR u.userIsActive = :userIsActive)")
    List<Usuario> findUsuariosComFiltros(@Param("nomeUsuario") String nomeUsuario,
                                        @Param("email") String email,
                                        @Param("cpf") String cpf,
                                        @Param("role") String role,
                                        @Param("userIsActive") Boolean userIsActive);
}
