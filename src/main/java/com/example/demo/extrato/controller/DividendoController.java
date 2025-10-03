package com.example.demo.extrato.controller;

import com.example.demo.extrato.service.DividendoService;
import com.example.demo.extrato.model.DividendoPendente;
import com.example.demo.extrato.dto.DividendoPendenteDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @GetMapping("/admin/pendentes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> listarDividendosPendentes() {
        try {
            List<DividendoPendente> pendentes = dividendoService.listarDividendosPendentes();
            
            List<DividendoPendenteDTO> pendentesDTO = pendentes.stream()
                .map(DividendoPendenteDTO::new)
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalPendentes", pendentesDTO.size());
            response.put("dividendos", pendentesDTO);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🔒 ADMIN: Liberar dividendo de um investimento específico por ID
     */
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
                Map<String, Object> response = new HashMap<>();
                response.put("mensagem", "Nenhum dividendo pendente encontrado para este investimento");
                response.put("investimentoId", investimentoId);
                return ResponseEntity.ok(response);
            }
            
            // Aprovar e pagar todos os dividendos deste investimento
            List<Long> dividendoIds = pendentes.stream().map(DividendoPendente::getId).collect(Collectors.toList());
            String resultadoPagamento = dividendoService.aprovarEPagarDividendos(dividendoIds, admin.getId(), "Liberado manualmente pelo admin");
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "✅ Dividendos liberados com sucesso para investimento " + investimentoId);
            response.put("criacao", resultadoCriacao);
            response.put("pagamento", resultadoPagamento);
            response.put("dividendosProcessados", pendentes.size());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 🔒 ADMIN: Liberar TODOS os dividendos pendentes
     */
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
                Map<String, Object> response = new HashMap<>();
                response.put("mensagem", "Nenhum dividendo pendente encontrado");
                return ResponseEntity.ok(response);
            }
            
            // Aprovar e pagar todos os dividendos
            List<Long> dividendoIds = pendentes.stream().map(DividendoPendente::getId).collect(Collectors.toList());
            String resultadoPagamento = dividendoService.aprovarEPagarDividendos(dividendoIds, admin.getId(), "Todos os dividendos liberados em lote pelo admin");
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "✅ TODOS os dividendos foram liberados com sucesso!");
            response.put("criacao", resultadoCriacao);
            response.put("pagamento", resultadoPagamento);
            response.put("totalDividendosProcessados", pendentes.size());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * 📈 PÚBLICO: Status do sistema de dividendos
     */
    @GetMapping("/status")
    public ResponseEntity<?> statusSistema() {
        long pendentes = dividendoService.contarDividendosPendentes();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sistemaAtivo", true);
        status.put("fluxo", "Admin cria pendências -> Admin revisa -> Admin aprova -> Usuários recebem");
        status.put("dividendosPendentes", pendentes);
        status.put("modoOperacao", "MANUAL - Apenas admins podem liberar dividendos");
        status.put("endpoints", Map.of(
            "listarPendentes", "GET /dividendos/admin/pendentes",
            "liberarPorInvestimento", "POST /dividendos/admin/liberar/{investimentoId}",
            "liberarTodos", "POST /dividendos/admin/liberar-todos"
        ));
        status.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(status);
    }
}