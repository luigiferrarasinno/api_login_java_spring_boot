package com.example.demo.investimento.controller;

import com.example.demo.investimento.dto.InvestimentoDTO;
import com.example.demo.investimento.dto.response.VincularResponseDTO;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.service.InvestimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "📈 Investimentos", description = "Gestão de investimentos com controle de favoritos, visibilidade e filtros avançados")
@RestController
@RequestMapping("/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    @Operation(summary = "Listar investimentos", 
               description = "Lista investimentos com filtros avançados. Admin vê todos, usuário vê apenas visíveis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de investimentos",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[{\"id\": 1, \"nome\": \"Tesouro Direto\", \"simbolo\": \"TD\", \"categoria\": \"TESOURO_DIRETO\", \"precoAtual\": 102.50, \"risco\": \"BAIXO\", \"ativo\": true}]")))
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<InvestimentoDTO> listarTodos(
            Authentication auth,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String simbolo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String risco,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Boolean visivel,
            @RequestParam(required = false) String precoMin,
            @RequestParam(required = false) String precoMax) {
        
        boolean incluirUsuarios = isAdmin(auth);
        boolean ehAdmin = isAdmin(auth);
        
        return investimentoService.listarComFiltros(
                nome, simbolo, categoria, risco, ativo, visivel, 
                precoMin, precoMax, ehAdmin
        ).stream()
                .map(inv -> new InvestimentoDTO(inv, incluirUsuarios))
                .collect(Collectors.toList());
    }

    @Operation(summary = "[ADMIN] Criar investimento", 
               description = "Cria um novo investimento no sistema (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Investimento criado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"Tesouro Direto\", \"simbolo\": \"TD\", \"categoria\": \"TESOURO_DIRETO\", \"precoAtual\": 102.50, \"ativo\": true, \"visivelParaUsuarios\": true}")))
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<InvestimentoDTO> criar(@RequestBody Investimento investimento) {
        Investimento salvo = investimentoService.salvar(investimento);
        return ResponseEntity.ok(new InvestimentoDTO(salvo));
    }

    @Operation(summary = "[ADMIN] Alternar status ativo", 
               description = "Ativa ou desativa um investimento (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"Tesouro Direto\", \"ativo\": false, \"visivelParaUsuarios\": true}")))
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/toggle-ativo")
    public ResponseEntity<InvestimentoDTO> toggleAtivo(@PathVariable Long id) {
        Investimento investimentoAtualizado = investimentoService.toggleAtivo(id);
        return ResponseEntity.ok(new InvestimentoDTO(investimentoAtualizado));
    }


    @Operation(summary = "Buscar investimento por ID", 
               description = "Obtém detalhes de um investimento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Investimento encontrado",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"Tesouro Direto\", \"simbolo\": \"TD\", \"categoria\": \"TESOURO_DIRETO\", \"precoAtual\": 102.50, \"descricao\": \"Investimento de renda fixa\"}")))
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InvestimentoDTO> buscar(@PathVariable Long id, Authentication auth) {
        boolean incluirUsuarios = isAdmin(auth);
        Investimento investimento = investimentoService.buscarPorId(id);
        return ResponseEntity.ok(new InvestimentoDTO(investimento, incluirUsuarios));
    }

  
    @Operation(summary = "[ADMIN] Alternar visibilidade", 
               description = "Torna um investimento visível ou invisível para usuários (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visibilidade alterada com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1, \"nome\": \"Tesouro Direto\", \"visivelParaUsuarios\": false, \"ativo\": true}")))
    })
    @PatchMapping("/{id}/toggle-visibilidade")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<InvestimentoDTO> toggleVisibilidade(@PathVariable Long id) {
        Investimento investimentoAtualizado = investimentoService.toggleVisibilidade(id);
        return ResponseEntity.ok(new InvestimentoDTO(investimentoAtualizado, true));
    }

    @Operation(summary = "[ADMIN] Excluir investimento", 
               description = "Remove um investimento do sistema permanentemente (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Investimento excluído com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"message\": \"Investimento com ID 1 foi excluído com sucesso.\"}")))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deletarInvestimento(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.ok().body(
            Map.of("message", "Investimento com ID " + id + " foi excluído com sucesso.")
        );
    }




    @Operation(summary = "Favoritar/Desfavoritar investimento", 
               description = "Adiciona ou remove investimento dos favoritos do usuário (próprio usuário ou admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"investimento\": {\"id\": 1, \"nome\": \"Tesouro Direto\", \"favoritado\": true}, \"mensagem\": \"Investimento favoritado com sucesso.\"}")))
    })
    @PostMapping("/favoritar/{investimentoId}/{usuarioId}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#usuarioId, authentication.name)")
    public ResponseEntity<VincularResponseDTO> favoritar(@PathVariable Long investimentoId, @PathVariable Long usuarioId, Authentication auth) {
        boolean incluirUsuarios = isAdmin(auth);
        Investimento investimento = investimentoService.vincularInvestimentoAUsuario(investimentoId, usuarioId);

        String mensagem;
        if (investimento.getUsuarios() != null && 
            investimento.getUsuarios().stream().anyMatch(usuario -> usuario.getId().equals(usuarioId))) {
            mensagem = "Investimento favoritado com sucesso.";
        } else {
            mensagem = "Investimento desfavoritado com sucesso.";
        }

        VincularResponseDTO responseDTO = new VincularResponseDTO(new InvestimentoDTO(investimento, incluirUsuarios), mensagem);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(summary = "Listar investimentos favoritos", 
               description = "Lista todos os investimentos favoritados pelo usuário (próprio usuário ou admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de investimentos favoritos",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[{\"id\": 1, \"nome\": \"Tesouro Direto\", \"simbolo\": \"TD\", \"categoria\": \"TESOURO_DIRETO\", \"precoAtual\": 102.50, \"risco\": \"BAIXO\"}]\n")))
    })
    @GetMapping("/favoritos/{usuarioId}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#usuarioId, authentication.name)")
    public List<InvestimentoDTO> listarFavoritos(@PathVariable Long usuarioId, Authentication auth) {
        boolean incluirUsuarios = isAdmin(auth);
        return investimentoService.listarPorUsuario(usuarioId).stream()
                .map(inv -> new InvestimentoDTO(inv, incluirUsuarios))
                .collect(Collectors.toList());
    }
}
