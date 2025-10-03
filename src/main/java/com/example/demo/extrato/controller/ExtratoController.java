package com.example.demo.extrato.controller;

import com.example.demo.extrato.service.ExtratoService;
import com.example.demo.extrato.service.DividendoService;
import com.example.demo.extrato.dto.*;
import com.example.demo.extrato.dto.response.*;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/extrato")
@Tag(name = "Extrato", description = "Endpoints para gerenciamento de extrato financeiro, operações de depósito, saque, compra/venda de ações e distribuição de dividendos")
public class ExtratoController {

    private final ExtratoService extratoService;
    private final DividendoService dividendoService;

    public ExtratoController(ExtratoService extratoService, DividendoService dividendoService) {
        this.extratoService = extratoService;
        this.dividendoService = dividendoService;
    }

    /**
     * 💰 Depositar dinheiro na conta
     */
    @Operation(
        summary = "Depositar dinheiro na conta",
        description = "Permite ao usuário depositar dinheiro em sua conta para realizar investimentos.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Depósito realizado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Depósito de R$ 1000.00 realizado com sucesso\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao realizar depósito")
    })
    @PostMapping("/depositar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> depositar(@Valid @RequestBody DepositarRequestDTO request, 
                                      Authentication authentication) {
        try {
            String mensagem = extratoService.depositar(authentication.getName(), request.getValor());
            
            OperacaoSucessoResponseDTO response = new OperacaoSucessoResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 💸 Sacar dinheiro da conta
     */
    @Operation(
        summary = "Sacar dinheiro da conta",
        description = "Permite ao usuário sacar dinheiro de sua conta, desde que tenha saldo suficiente.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Saque realizado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Saque de R$ 500.00 realizado com sucesso\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao realizar saque (saldo insuficiente)")
    })
    @PostMapping("/sacar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> sacar(@Valid @RequestBody DepositarRequestDTO request, 
                                  Authentication authentication) {
        try {
            String mensagem = extratoService.sacar(authentication.getName(), request.getValor());
            
            OperacaoSucessoResponseDTO response = new OperacaoSucessoResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 📈 Comprar ações pelo preço de mercado
     */
    @Operation(
        summary = "Comprar ações pelo preço de mercado",
        description = "Permite ao usuário comprar ações de um investimento específico pelo preço atual de mercado.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Compra de ações realizada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Compra de 10 ações de PETR4 realizada com sucesso por R$ 2500.00\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao comprar ações (saldo insuficiente ou investimento não encontrado)")
    })
    @PostMapping("/comprar-acao")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> comprarAcao(@Valid @RequestBody ComprarAcaoMercadoRequestDTO request, 
                                        Authentication authentication) {
        try {
            String mensagem = extratoService.comprarAcaoPrecoMercado(authentication.getName(), 
                                                                    request.getInvestimentoId(),
                                                                    request.getQuantidade());
            
            OperacaoSucessoResponseDTO response = new OperacaoSucessoResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 📉 Vender ações pelo preço de mercado
     */
    @Operation(
        summary = "Vender ações pelo preço de mercado",
        description = "Permite ao usuário vender ações de um investimento que possui em carteira pelo preço atual de mercado.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Venda de ações realizada com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Venda de 5 ações de VALE3 realizada com sucesso por R$ 1200.00\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao vender ações (quantidade insuficiente em carteira)")
    })
    @PostMapping("/vender-acao")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> venderAcao(@Valid @RequestBody VenderAcaoNovoRequestDTO request, 
                                       Authentication authentication) {
        try {
            String mensagem = extratoService.venderAcaoPrecoMercado(authentication.getName(), 
                                                                   request.getInvestimentoId(),
                                                                   request.getQuantidade());
            
            OperacaoSucessoResponseDTO response = new OperacaoSucessoResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 📋 Obter extrato completo do usuário
     */
    @Operation(
        summary = "Obter extrato completo",
        description = "Retorna o extrato completo de todas as operações financeiras do usuário autenticado.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Extrato retornado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 1, \"tipo\": \"DEPOSITO\", \"valor\": 1000.00, \"saldoAnterior\": 0.00, \"saldoAtual\": 1000.00, \"dataOperacao\": \"2024-10-02T10:30:00\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ExtratoResponseDTO>> obterExtrato(Authentication authentication) {
        List<ExtratoResponseDTO> extrato = extratoService.obterExtrato(authentication.getName());
        return ResponseEntity.ok(extrato);
    }

    /**
     * 📋 Obter extrato filtrado por investimento
     */
    @Operation(
        summary = "Obter extrato por investimento",
        description = "Retorna o extrato filtrado por um investimento específico, mostrando apenas operações relacionadas a esse ativo.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Extrato filtrado retornado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"id\": 2, \"tipo\": \"COMPRA_ACAO\", \"investimentoNome\": \"PETR4\", \"quantidade\": 10, \"valor\": 2500.00, \"dataOperacao\": \"2024-10-02T11:00:00\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/investimento/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ExtratoResponseDTO>> obterExtratoPorInvestimento(
            @PathVariable Long investimentoId, 
            Authentication authentication) {
        List<ExtratoResponseDTO> extrato = extratoService.obterExtratoPorInvestimento(authentication.getName(), investimentoId);
        return ResponseEntity.ok(extrato);
    }

    /**
     * 🎲 ADMIN: Simular recebimento de dividendo
     */
    @Operation(
        summary = "Simular recebimento de dividendo",
        description = "Endpoint ADMIN para simular o recebimento de dividendos por um usuário específico de um investimento. Normalmente seria executado por um processo automatizado.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dividendo simulado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Dividendo de R$ 50.00 recebido de PETR4\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao simular dividendo")
    })
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
            
            OperacaoSucessoResponseDTO response = new OperacaoSucessoResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 💰 ADMIN: Distribuir dividendos de um investimento específico
     */
    @Operation(
        summary = "Distribuir dividendos automáticos",
        description = "Endpoint ADMIN para distribuir dividendos automaticamente para todos os acionários de um investimento específico.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dividendos distribuídos com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Dividendos distribuídos para 15 acionários de PETR4\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\", \"tipo\": \"DISTRIBUICAO_AUTOMATICA\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao distribuir dividendos")
    })
    @PostMapping("/admin/distribuir-dividendos/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> distribuirDividendosAutomatico(@PathVariable Long investimentoId,
                                                           Authentication authentication) {
        try {
            // Integração com DividendoService - IMPLEMENTADO AGORA!
            String mensagem = dividendoService.distribuirDividendosComDetalhes(investimentoId);
            
            DistribuicaoDividendosResponseDTO response = new DistribuicaoDividendosResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso",
                "DISTRIBUICAO_AUTOMATICA"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 🤖 ADMIN: Criar pendências de dividendos para todos os investimentos
     */
    @Operation(
        summary = "Distribuir dividendos para todos os investimentos",
        description = "Endpoint ADMIN para criar pendências de dividendos para todos os investimentos. As pendências criadas devem ser aprovadas posteriormente.",
        tags = { "Extrato" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pendências de dividendos criadas com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"mensagem\": \"Pendências criadas para todos os investimentos\", \"timestamp\": \"2024-10-02T10:30:00\", \"status\": \"sucesso\", \"tipo\": \"DISTRIBUICAO_GERAL\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro ao criar pendências de dividendos")
    })
    @PostMapping("/admin/distribuir-todos-dividendos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> distribuirTodosDividendos(Authentication authentication) {
        try {
            String mensagem = dividendoService.criarTodasPendenciasDividendos();
            
            DistribuicaoDividendosResponseDTO response = new DistribuicaoDividendosResponseDTO(
                mensagem,
                LocalDateTime.now(),
                "sucesso",
                "DISTRIBUICAO_GERAL"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            OperacaoErroResponseDTO errorResponse = new OperacaoErroResponseDTO(
                e.getMessage(),
                LocalDateTime.now(),
                "erro"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}