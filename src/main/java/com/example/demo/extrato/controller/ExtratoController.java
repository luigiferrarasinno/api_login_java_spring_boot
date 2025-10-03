package com.example.demo.extrato.controller;

import com.example.demo.extrato.service.ExtratoService;
import com.example.demo.extrato.service.DividendoService;
import com.example.demo.extrato.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/extrato")
public class ExtratoController {

    private final ExtratoService extratoService;
    private final DividendoService dividendoService;

    public ExtratoController(ExtratoService extratoService, DividendoService dividendoService) {
        this.extratoService = extratoService;
        this.dividendoService = dividendoService;
    }

    @PostMapping("/depositar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> depositar(@Valid @RequestBody DepositarRequestDTO request, 
                                      Authentication authentication) {
        try {
            String mensagem = extratoService.depositar(authentication.getName(), request.getValor());
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/sacar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> sacar(@Valid @RequestBody DepositarRequestDTO request, 
                                  Authentication authentication) {
        try {
            String mensagem = extratoService.sacar(authentication.getName(), request.getValor());
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/comprar-acao")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> comprarAcao(@Valid @RequestBody ComprarAcaoMercadoRequestDTO request, 
                                        Authentication authentication) {
        try {
            String mensagem = extratoService.comprarAcaoPrecoMercado(authentication.getName(), 
                                                                    request.getInvestimentoId(),
                                                                    request.getQuantidade());
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/vender-acao")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> venderAcao(@Valid @RequestBody VenderAcaoNovoRequestDTO request, 
                                       Authentication authentication) {
        try {
            String mensagem = extratoService.venderAcaoPrecoMercado(authentication.getName(), 
                                                                   request.getInvestimentoId(),
                                                                   request.getQuantidade());
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ExtratoResponseDTO>> obterExtrato(Authentication authentication) {
        List<ExtratoResponseDTO> extrato = extratoService.obterExtrato(authentication.getName());
        return ResponseEntity.ok(extrato);
    }

    @GetMapping("/investimento/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ExtratoResponseDTO>> obterExtratoPorInvestimento(
            @PathVariable Long investimentoId, 
            Authentication authentication) {
        List<ExtratoResponseDTO> extrato = extratoService.obterExtratoPorInvestimento(authentication.getName(), investimentoId);
        return ResponseEntity.ok(extrato);
    }

    // Endpoint simulado para dividendos (normalmente seria chamado por um job/scheduler)
    @PostMapping("/admin/simular-dividendo/{usuarioId}/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> simularDividendo(@PathVariable Long usuarioId,
                                             @PathVariable Long investimentoId,
                                             @RequestParam java.math.BigDecimal valor,
                                             Authentication authentication) {
        try {
            // Em um cenário real, isso seria feito por um processo automatizado
            // Aqui é apenas para demonstração
            String mensagem = extratoService.receberDividendo(authentication.getName(), investimentoId, valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/admin/distribuir-dividendos/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> distribuirDividendosAutomatico(@PathVariable Long investimentoId,
                                                           Authentication authentication) {
        try {
            // Integração com DividendoService - IMPLEMENTADO AGORA!
            String mensagem = dividendoService.distribuirDividendosComDetalhes(investimentoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            response.put("tipo", "DISTRIBUICAO_AUTOMATICA");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 🤖 NOVO: Criar todas as pendências de dividendos (admin deve aprovar depois)
    @PostMapping("/admin/distribuir-todos-dividendos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> distribuirTodosDividendos(Authentication authentication) {
        try {
            String mensagem = dividendoService.criarTodasPendenciasDividendos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", mensagem);
            response.put("timestamp", LocalDateTime.now());
            response.put("status", "sucesso");
            response.put("tipo", "DISTRIBUICAO_GERAL");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", "erro");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}