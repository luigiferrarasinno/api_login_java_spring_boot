package com.example.demo.investimento.controller;

import com.example.demo.investimento.service.CotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cotacao")
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    @GetMapping("/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obterPrecoAtual(@PathVariable Long investimentoId) {
        try {
            BigDecimal precoAtual = cotacaoService.obterPrecoAtual(investimentoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("investimentoId", investimentoId);
            response.put("precoAtual", precoAtual);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/atualizar/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> atualizarPreco(@PathVariable Long investimentoId) {
        try {
            cotacaoService.atualizarPrecoMercado(investimentoId);
            BigDecimal novoPreco = cotacaoService.obterPrecoAtual(investimentoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Preço atualizado com sucesso");
            response.put("investimentoId", investimentoId);
            response.put("novoPreco", novoPreco);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/atualizar-todos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> atualizarTodosPrecos() {
        try {
            cotacaoService.atualizarTodosPrecos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Todos os preços foram atualizados");
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}