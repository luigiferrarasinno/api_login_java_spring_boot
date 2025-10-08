package com.example.demo.playlist.repository;

import com.example.demo.playlist.model.Playlist;
import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    // Buscar playlists do usuário
    List<Playlist> findByCriadorAndAtivaTrue(Usuario criador);

    // Buscar playlists que o usuário segue
    @Query("SELECT p FROM Playlist p JOIN p.seguidores s WHERE s = :usuario AND p.ativa = true")
    List<Playlist> findPlaylistsSeguidasPorUsuario(@Param("usuario") Usuario usuario);

    // Buscar playlists públicas
    @Query("SELECT p FROM Playlist p WHERE p.tipo = 'PUBLICA' AND p.ativa = true")
    List<Playlist> findByTipoPublicaAndAtivaTrue();

    // Buscar playlist por ID e verificar se está ativa
    Optional<Playlist> findByIdAndAtivaTrue(Long id);

    // Buscar playlists por nome (busca parcial, case-insensitive) - apenas públicas
    @Query("SELECT p FROM Playlist p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.ativa = true AND p.tipo = 'PUBLICA'")
    List<Playlist> findByNomeContainingIgnoreCaseAndTipoPublicaAndAtivaTrue(@Param("nome") String nome);

    // Verificar se usuário tem acesso à playlist (é criador, seguidor ou é pública)
    @Query("SELECT p FROM Playlist p WHERE p.id = :playlistId AND p.ativa = true AND " +
           "(p.criador = :usuario OR :usuario MEMBER OF p.seguidores OR p.tipo = 'PUBLICA')")
    Optional<Playlist> findPlaylistComAcesso(@Param("playlistId") Long playlistId, @Param("usuario") Usuario usuario);

    // Contar playlists do usuário
    long countByCriadorAndAtivaTrue(Usuario criador);

    // Playlists mais seguidas (top playlists públicas)
    @Query("SELECT p FROM Playlist p WHERE p.tipo = 'PUBLICA' AND p.ativa = true ORDER BY SIZE(p.seguidores) DESC")
    List<Playlist> findTopPlaylistsPublicas();

    // Verificar se usuário já segue uma playlist
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Playlist p " +
           "WHERE p.id = :playlistId AND :usuario MEMBER OF p.seguidores")
    boolean usuarioSeguePlaylist(@Param("playlistId") Long playlistId, @Param("usuario") Usuario usuario);

    // Buscar playlists que contêm um investimento específico (apenas públicas)
    @Query("SELECT p FROM Playlist p JOIN p.investimentos i WHERE i.id = :investimentoId AND p.tipo = 'PUBLICA' AND p.ativa = true")
    List<Playlist> findPlaylistsComInvestimento(@Param("investimentoId") Long investimentoId);

    // Buscar playlists compartilhadas
    @Query("SELECT p FROM Playlist p WHERE p.tipo = 'COMPARTILHADA' AND p.ativa = true AND :usuario MEMBER OF p.seguidores")
    List<Playlist> findPlaylistsCompartilhadasPorUsuario(@Param("usuario") Usuario usuario);
    
    // Buscar TODAS as playlists acessíveis por um usuário (criadas por ele, seguindo, públicas)
    @Query("SELECT DISTINCT p FROM Playlist p WHERE p.ativa = true AND " +
           "(p.criador = :usuario OR :usuario MEMBER OF p.seguidores OR p.tipo = 'PUBLICA')")
    List<Playlist> findAllAcessiveisPorUsuario(@Param("usuario") Usuario usuario);
    
    // Buscar TODAS as playlists do sistema (ADMIN ONLY - inclui privadas de todos)
    List<Playlist> findByAtivaTrue();
}