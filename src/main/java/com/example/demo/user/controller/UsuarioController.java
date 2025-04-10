package com.example.demo.user.controller;

import com.example.demo.user.dto.UsuarioDTO;
import com.example.demo.user.dto.UsuarioResponseDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
//import java.util.stream.StreamSupport;
//import java.util.List;
//import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.login(usuarioDTO.getNomeUsuario(), usuarioDTO.getSenha());
    }

    @PostMapping("/criar")
    public String criarConta(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(usuarioDTO.getNomeUsuario());
        usuario.setSenha(usuarioDTO.getSenha());
        return usuarioService.criarConta(usuario);
    }

    @PutMapping("/alterar-senha")
    @PreAuthorize("#usuarioDTO.nomeUsuario == authentication.name or hasAuthority('ROLE_ADMIN')")
    public String alterarSenha(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.alterarSenha(usuarioDTO.getNomeUsuario(), usuarioDTO.getSenha());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public String deletarConta(@PathVariable Long id) {
        return usuarioService.deletarConta(id);
    }

      //@GetMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //public List<UsuarioResponseDTO> listarUsuarios() {
     //   Iterable<Usuario> usuarios = usuarioService.listarUsuarios();

        //return StreamSupport.stream(usuarios.spliterator(), false)
      //          .map(UsuarioResponseDTO::new)
      //          .collect(Collectors.toList());
   // }

    // Endpoint para listar todos os usuários
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Iterable<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<Object> buscarUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuario(id);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<Object> alternarStatusUsuario(@PathVariable Long id) {
        try {
            Usuario usuarioAtualizado = usuarioService.alternarStatusUsuario(id);
            return ResponseEntity.ok().body(
                Map.of(
                    "mensagem", "Status de atividade atualizado com sucesso!",
                    "ativo", usuarioAtualizado.isUserIsActive()
                )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", e.getMessage())
            );
        }
    }
    

}
