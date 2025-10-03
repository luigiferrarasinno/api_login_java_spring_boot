package com.example.demo.extrato.controller;

import com.example.demo.extrato.service.DividendoService;
import com.example.demo.extrato.model.DividendoPendente;
import com.example.demo.extrato.dto.DividendoPendenteDTO;
import com.example.demo.extrato.dto.response.*;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller para sistema completo de dividendos com aprovação administrativa
 */
@RestController
@RequestMapping("/dividendos")
public class DividendoController {

    private final DividendoService dividendoService;
    private final UsuarioRepository usuarioRepository;

    public DividendoController(DividendoService dividendoService, UsuarioRepository usuarioRepository) {
        this.dividendoService = dividendoService;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * � ADMIN: Ver todos os dividendos pendentes
     */
    @Operation(
        summary = "Listar todos os dividendos pendentes",
        description = "Endpoint ADMIN para visualizar todos os dividendos pendentes aguardando aprovação.",
        tags = { "Dividendos" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de dividendos pendentes retornada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"totalPendentes\": 2, \"dividendos\": [{\"id\": 1, \"investimentoNome\": \"PETR4\", \"valorTotal\": 150.50, \"dataCriacao\": \"2024-10-02T10:30:00\"}], \"timestamp\": \"2024-10-02T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao buscar dividendos pendentes")
    })
    @GetMapping("/admin/pendentes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> listarDividendosPendentes() {
        try {
            List<DividendoPendente> pendentes = dividendoService.listarDividendosPendentes();
            
            List<DividendoPendenteDTO> pendentesDTO = pendentes.stream()
                .map(DividendoPendenteDTO::new)
                .collect(Collectors.toList());
            
            DividendosPendentesResponseDTO response = new DividendosPendentesResponseDTO(
                pendentesDTO.size(),
                pendentesDTO,
                LocalDateTime.now()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🔒 ADMIN: Liberar dividendo de um investimento específico por ID
     */
    @Operation(
        summary = "Liberar dividendo de um investimento específico",
        description = "Endpoint ADMIN para liberar manualmente os dividendos pendentes de um investimento pelo seu ID.",
        tags = { "Dividendos" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dividendos liberados com sucesso para o investimento",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"✅ Dividendos liberados com sucesso para investimento 1\", \"criacao\": \"Pendências criadas\", \"pagamento\": \"Dividendos pagos\", \"dividendosProcessados\": 3, \"timestamp\": \"2024-10-02T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao liberar dividendos para o investimento")
    })
    @PostMapping("/admin/liberar/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> liberarDividendoPorInvestimento(@PathVariable Long investimentoId, Authentication auth) {
        try {
            Usuario admin = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));
            
            // Primeiro cria as pendências para este investimento
            String resultadoCriacao = dividendoService.criarPendenciasDividendos(investimentoId);
            
            // Depois busca as pendências criadas para este investimento
            List<DividendoPendente> pendentes = dividendoService.listarDividendosPendentes()
                .stream()
                .filter(p -> p.getInvestimento().getId().equals(investimentoId))
                .collect(Collectors.toList());
            
            if (pendentes.isEmpty()) {
                MensagemSimplesDividendoResponseDTO response = new MensagemSimplesDividendoResponseDTO(
                    "Nenhum dividendo pendente encontrado para este investimento",
                    investimentoId,
                    LocalDateTime.now()
                );
                return ResponseEntity.ok(response);
            }
            
            // Aprovar e pagar todos os dividendos deste investimento
            List<Long> dividendoIds = pendentes.stream().map(DividendoPendente::getId).collect(Collectors.toList());
            String resultadoPagamento = dividendoService.aprovarEPagarDividendos(dividendoIds, admin.getId(), "Liberado manualmente pelo admin");
            
            LiberacaoInvestimentoResponseDTO response = new LiberacaoInvestimentoResponseDTO(
                "✅ Dividendos liberados com sucesso para investimento " + investimentoId,
                resultadoCriacao,
                resultadoPagamento,
                pendentes.size(),
                LocalDateTime.now()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🔒 ADMIN: Liberar TODOS os dividendos pendentes
     */
    @Operation(
        summary = "Liberar TODOS os dividendos pendentes",
        description = "Endpoint ADMIN para liberar manualmente todos os dividendos pendentes do sistema.",
        tags = { "Dividendos" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Todos os dividendos pendentes liberados com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"✅ TODOS os dividendos foram liberados com sucesso!\", \"criacao\": \"Todas as pendências criadas\", \"pagamento\": \"Todos os dividendos pagos\", \"totalDividendosProcessados\": 15, \"timestamp\": \"2024-10-02T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao liberar todos os dividendos pendentes")
    })
    @PostMapping("/admin/liberar-todos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> liberarTodosDividendos(Authentication auth) {
        try {
            Usuario admin = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));
            
            // Primeiro cria todas as pendências
            String resultadoCriacao = dividendoService.criarTodasPendenciasDividendos();
            
            // Depois busca todas as pendências
            List<DividendoPendente> pendentes = dividendoService.listarDividendosPendentes();
            
            if (pendentes.isEmpty()) {
                MensagemSimplesDividendoResponseDTO response = new MensagemSimplesDividendoResponseDTO(
                    "Nenhum dividendo pendente encontrado",
                    LocalDateTime.now()
                );
                return ResponseEntity.ok(response);
            }
            
            // Aprovar e pagar todos os dividendos
            List<Long> dividendoIds = pendentes.stream().map(DividendoPendente::getId).collect(Collectors.toList());
            String resultadoPagamento = dividendoService.aprovarEPagarDividendos(dividendoIds, admin.getId(), "Todos os dividendos liberados em lote pelo admin");
            
            LiberacaoTodosDividendosResponseDTO response = new LiberacaoTodosDividendosResponseDTO(
                "✅ TODOS os dividendos foram liberados com sucesso!",
                resultadoCriacao,
                resultadoPagamento,
                pendentes.size(),
                LocalDateTime.now()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 📈 PÚBLICO: Status do sistema de dividendos
     */
    @Operation(
        summary = "Status do sistema de dividendos",
        description = "Endpoint público para consultar o status atual do sistema de dividendos, incluindo quantidade de pendências e modo de operação.",
        tags = { "Dividendos" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Status do sistema retornado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"sistemaAtivo\": true, \"fluxo\": \"Admin cria pendências -> Admin revisa -> Admin aprova -> Usuários recebem\", \"dividendosPendentes\": 5, \"modoOperacao\": \"MANUAL - Apenas admins podem liberar dividendos\", \"endpoints\": {\"listarPendentes\": \"GET /dividendos/admin/pendentes\", \"liberarPorInvestimento\": \"POST /dividendos/admin/liberar/{investimentoId}\", \"liberarTodos\": \"POST /dividendos/admin/liberar-todos\"}, \"timestamp\": \"2024-10-02T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao consultar status do sistema")
    })
    @GetMapping("/status")
    public ResponseEntity<?> statusSistema() {
        long pendentes = dividendoService.contarDividendosPendentes();
        
        StatusSistemaResponseDTO status = new StatusSistemaResponseDTO(
            true,
            "Admin cria pendências -> Admin revisa -> Admin aprova -> Usuários recebem",
            pendentes,
            "MANUAL - Apenas admins podem liberar dividendos",
            Map.of(
                "listarPendentes", "GET /dividendos/admin/pendentes",
                "liberarPorInvestimento", "POST /dividendos/admin/liberar/{investimentoId}",
                "liberarTodos", "POST /dividendos/admin/liberar-todos"
            ),
            LocalDateTime.now()
        );
        
        return ResponseEntity.ok(status);
    }
}