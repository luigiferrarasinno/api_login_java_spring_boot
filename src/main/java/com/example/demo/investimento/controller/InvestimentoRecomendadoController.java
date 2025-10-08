package com.example.demo.investimento.controller;

import com.example.demo.investimento.service.InvestimentoRecomendadoService;
import com.example.demo.investimento.dto.InvestimentoRecomendadoResponseDTO;
import com.example.demo.investimento.dto.AdicionarRecomendacoesRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/investimentos/recomendados")
@Tag(name = "Investimentos Recomendados", description = "Gerenciamento de investimentos recomendados para usuários")
public class InvestimentoRecomendadoController {
    
    private final InvestimentoRecomendadoService recomendadoService;
    
    public InvestimentoRecomendadoController(InvestimentoRecomendadoService recomendadoService) {
        this.recomendadoService = recomendadoService;
    }
    
    /**
     * 📋 Obter investimentos recomendados
     */
    @Operation(
        summary = "Obter investimentos recomendados",
        description = "Retorna a lista de investimentos recomendados do usuário autenticado. Admins podem usar usuarioId para ver recomendações de outros usuários.",
        tags = { "Investimentos Recomendados" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de investimentos recomendados retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = """
                        [
                          {
                            "id": 1,
                            "usuarioId": 5,
                            "investimentoId": 10,
                            "investimentoNome": "Petrobras PN",
                            "investimentoSimbolo": "PETR4",
                            "categoria": "Ações",
                            "risco": "Alto",
                            "dataRecomendacao": "2024-10-06T10:30:00"
                          }
                        ]
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - tentou acessar recomendações de outro usuário sem ser admin")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<InvestimentoRecomendadoResponseDTO>> obterRecomendados(
            @Parameter(description = "ID do usuário para consultar recomendações (apenas admin)")
            @RequestParam(required = false) Long usuarioId,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        
        List<InvestimentoRecomendadoResponseDTO> recomendados = recomendadoService.obterRecomendados(
            authentication.getName(),
            usuarioId,
            isAdmin
        );
        
        return ResponseEntity.ok(recomendados);
    }
    
    /**
     * ➕ Adicionar investimentos recomendados
     */
    @Operation(
        summary = "Adicionar investimentos recomendados",
        description = "Adiciona múltiplos investimentos como recomendados para um usuário. Usuários podem adicionar para si mesmos, admins podem adicionar para qualquer usuário.",
        tags = { "Investimentos Recomendados" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Investimentos recomendados adicionados com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = """
                        [
                          {
                            "id": 1,
                            "usuarioId": 5,
                            "investimentoId": 10,
                            "investimentoNome": "Petrobras PN",
                            "investimentoSimbolo": "PETR4",
                            "categoria": "Ações",
                            "risco": "Alto",
                            "dataRecomendacao": "2024-10-06T10:30:00"
                          }
                        ]
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou investimento já recomendado"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - tentou adicionar para outro usuário sem ser admin ou owner")
    })
    @PostMapping
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#request.usuarioId, authentication.name)")
    public ResponseEntity<List<InvestimentoRecomendadoResponseDTO>> adicionarRecomendacoes(
            @Valid @RequestBody AdicionarRecomendacoesRequestDTO request,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        
        List<InvestimentoRecomendadoResponseDTO> recomendados = recomendadoService.adicionarRecomendacoes(
            request.getUsuarioId(),
            request.getInvestimentoIds(),
            isAdmin
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(recomendados);
    }
    
    /**
     * 🗑️ Remover investimento recomendado
     */
    @Operation(
        summary = "Remover investimento recomendado",
        description = "Remove um investimento da lista de recomendados. Usuários podem remover apenas suas próprias recomendações, admins podem remover qualquer recomendação.",
        tags = { "Investimentos Recomendados" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Investimento recomendado removido com sucesso"
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - tentou remover recomendação de outro usuário"),
        @ApiResponse(responseCode = "404", description = "Recomendação não encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> removerRecomendacao(
            @Parameter(description = "ID do registro de recomendação")
            @PathVariable Long id,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        
        recomendadoService.removerRecomendacao(id, authentication.getName(), isAdmin);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 🗑️ Remover TODAS as recomendações de um usuário
     */
    @Operation(
        summary = "Remover todas as recomendações de um usuário",
        description = "Remove todos os investimentos recomendados de um usuário. Usuários podem remover apenas suas próprias recomendações, admins podem remover de qualquer usuário.",
        tags = { "Investimentos Recomendados" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Todas as recomendações removidas com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = """
                        {
                          "mensagem": "5 recomendações removidas com sucesso",
                          "quantidadeRemovida": 5
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - tentou remover recomendações de outro usuário"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/usuario/{usuarioId}/todas")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> removerTodasRecomendacoes(
            @Parameter(description = "ID do usuário cujas recomendações serão removidas")
            @PathVariable Long usuarioId,
            Authentication authentication) {
        
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        
        int quantidadeRemovida = recomendadoService.removerTodasRecomendacoes(
            usuarioId, 
            authentication.getName(), 
            isAdmin
        );
        
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("mensagem", quantidadeRemovida + " recomendações removidas com sucesso");
            put("quantidadeRemovida", quantidadeRemovida);
        }});
    }
}
