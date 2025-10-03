package com.example.demo.investimento.controller;

import com.example.demo.investimento.dto.response.AtualizacaoPrecoResponseDTO;
import com.example.demo.investimento.dto.response.AtualizacaoTodosPrecoResponseDTO;
import com.example.demo.investimento.dto.response.CotacaoResponseDTO;
import com.example.demo.investimento.dto.response.ErrorResponseDTO;
import com.example.demo.investimento.service.CotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Tag(name = " Cotações", description = "Sistema de cotações e atualização de preços dos investimentos")
@RestController
@RequestMapping("/cotacao")
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    @Operation(summary = "Obter preço atual", 
               description = "Obtém o preço atual de mercado de um investimento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preço obtido com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"investimentoId\": 1, \"precoAtual\": 102.50, \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "400", description = "Erro ao obter preço",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"erro\": \"Investimento não encontrado\", \"timestamp\": \"2024-01-01T10:00:00\"}"))) 
    })
    @GetMapping("/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obterPrecoAtual(@PathVariable Long investimentoId) {
        try {
            BigDecimal precoAtual = cotacaoService.obterPrecoAtual(investimentoId);
            
            CotacaoResponseDTO response = new CotacaoResponseDTO(investimentoId, precoAtual, LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "[ADMIN] Atualizar preço", 
               description = "Força a atualização do preço de mercado de um investimento específico (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preço atualizado com sucesso",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Preço atualizado com sucesso\", \"investimentoId\": 1, \"novoPreco\": 105.75, \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "400", description = "Erro na atualização",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"erro\": \"Falha ao conectar com API de mercado\", \"timestamp\": \"2024-01-01T10:00:00\"}"))) 
    })
    @PostMapping("/atualizar/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> atualizarPreco(@PathVariable Long investimentoId) {
        try {
            cotacaoService.atualizarPrecoMercado(investimentoId);
            BigDecimal novoPreco = cotacaoService.obterPrecoAtual(investimentoId);
            
            AtualizacaoPrecoResponseDTO response = new AtualizacaoPrecoResponseDTO(
                "Preço atualizado com sucesso", investimentoId, novoPreco, LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "[ADMIN] Atualizar todos os preços", 
               description = "Força a atualização de preços de mercado de todos os investimentos (apenas admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos os preços atualizados",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"mensagem\": \"Todos os preços foram atualizados\", \"timestamp\": \"2024-01-01T10:00:00\"}"))),
        @ApiResponse(responseCode = "400", description = "Erro na atualização em lote",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"erro\": \"Falha parcial na atualização dos preços\", \"timestamp\": \"2024-01-01T10:00:00\"}"))) 
    })
    @PostMapping("/atualizar-todos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> atualizarTodosPrecos() {
        try {
            cotacaoService.atualizarTodosPrecos();
            
            AtualizacaoTodosPrecoResponseDTO response = new AtualizacaoTodosPrecoResponseDTO(
                "Todos os preços foram atualizados", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}