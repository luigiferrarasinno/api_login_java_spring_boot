package com.example.demo.extrato.controller;

import com.example.demo.extrato.service.ExtratoService;
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

    public ExtratoController(ExtratoService extratoService) {
        this.extratoService = extratoService;
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




    
    // ===== ENDPOINT UNIFICADO DE RESUMO DE INVESTIMENTOS =====
    
    /**
     * 📊 Gerar resumo completo de investimentos com filtros flexíveis
     */
    @Operation(
        summary = "Resumo unificado de investimentos com análise de lucro/prejuízo",
        description = "Gera um resumo completo dos investimentos do usuário com cálculos de lucro/prejuízo, " +
                     "dividendos recebidos e estatísticas detalhadas. Suporta filtros opcionais por mês, ano e investimento específico. " +
                     "Todos os parâmetros são opcionais - sem filtros retorna resumo completo de todos os investimentos.",
        tags = { "Extrato", }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Resumo gerado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Resumo Geral",
                        description = "Sem filtros - todos os investimentos",
                        value = "{\"periodo\": \"Todo período\", \"situacaoGeral\": \"LUCRO\", \"resultadoGeralLiquido\": 2850.75, \"percentualRetornoGeral\": 12.35, \"quantidadeInvestimentosComLucro\": 5, \"quantidadeInvestimentosComPrejuizo\": 2}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Resumo Mensal",
                        description = "Filtro: ?ano=2024&mes=10",
                        value = "{\"periodo\": \"2024-10\", \"situacaoGeral\": \"LUCRO\", \"resultadoGeralLiquido\": 1250.75, \"percentualRetornoGeral\": 8.35, \"numeroTotalOperacoes\": 15}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Período de Meses",
                        description = "Filtro: ?ano=2025&mesInicio=1&mesFim=5 (Janeiro a Maio 2025)",
                        value = "{\"periodo\": \"2025-01 a 05\", \"situacaoGeral\": \"LUCRO\", \"resultadoGeralLiquido\": 3250.80, \"percentualRetornoGeral\": 15.20, \"numeroTotalOperacoes\": 42}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Investimento Específico",
                        description = "Filtro: ?investimentoId=1",
                        value = "{\"periodo\": \"Todo período - Investimento ID 1\", \"investimentos\": [{\"simboloInvestimento\": \"PETR4\", \"situacao\": \"LUCRO\", \"resultadoLiquido\": 750.50, \"percentualRetorno\": 14.04}]}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Período + Investimento",
                        description = "Filtro: ?ano=2025&mesInicio=1&mesFim=5&investimentoId=1 (PETR4 de Jan-Mai)",
                        value = "{\"periodo\": \"2025-01 a 05 - Investimento ID 1\", \"investimentos\": [{\"simboloInvestimento\": \"PETR4\", \"totalInvestido\": 2850.00, \"totalRecebido\": 3200.00, \"situacao\": \"LUCRO\"}]}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Filtro Mensal + Investimento",
                        description = "Filtro: ?ano=2024&mes=10&investimentoId=1",
                        value = "{\"periodo\": \"2024-10 - Investimento ID 1\", \"investimentos\": [{\"simboloInvestimento\": \"PETR4\", \"totalInvestido\": 850.00, \"totalRecebido\": 920.00, \"situacao\": \"LUCRO\"}]}"
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos (ex: mês fora do range 1-12)"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "404", description = "Usuário ou investimento não encontrado")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/resumo")
    public ResponseEntity<com.example.demo.extrato.dto.ResumoCompletoDTO> gerarResumoInvestimentos(
            Authentication authentication,
            @RequestParam(required = false, name = "ano") 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "Ano para filtrar (ex: 2024). Opcional - sem filtro retorna todo período.",
                example = "2024"
            ) Integer ano,
            
            @RequestParam(required = false, name = "mes") 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "Mês específico para filtrar (1-12). Requer o parâmetro 'ano'. Não pode ser usado junto com mesInicio/mesFim.",
                example = "10"
            ) Integer mes,
            
            @RequestParam(required = false, name = "mesInicio") 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "Mês de início do período (1-12). Requer 'ano' e 'mesFim'. Para períodos como Jan-Mai: mesInicio=1&mesFim=5",
                example = "1"
            ) Integer mesInicio,
            
            @RequestParam(required = false, name = "mesFim") 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "Mês de fim do período (1-12). Requer 'ano' e 'mesInicio'. Para períodos como Jan-Mai: mesInicio=1&mesFim=5",
                example = "5"
            ) Integer mesFim,
            
            @RequestParam(required = false, name = "investimentoId") 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "ID do investimento específico para filtrar. Opcional - sem filtro retorna todos os investimentos.",
                example = "1"
            ) Long investimentoId) {
        
        try {
            // Validação de parâmetros
            if (mes != null && (mes < 1 || mes > 12)) {
                return ResponseEntity.badRequest().build();
            }
            
            if (mesInicio != null && (mesInicio < 1 || mesInicio > 12)) {
                return ResponseEntity.badRequest().build();
            }
            
            if (mesFim != null && (mesFim < 1 || mesFim > 12)) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validações de combinações de parâmetros
            if (mes != null && (mesInicio != null || mesFim != null)) {
                return ResponseEntity.badRequest().build(); // Não pode usar mes com mesInicio/mesFim
            }
            
            if (mesInicio != null && mesFim != null && mesInicio > mesFim) {
                return ResponseEntity.badRequest().build(); // Início deve ser menor que fim
            }
            
            if ((mesInicio != null || mesFim != null) && (mesInicio == null || mesFim == null)) {
                return ResponseEntity.badRequest().build(); // Ambos ou nenhum
            }
            
            // Se qualquer mês foi informado, ano também deve ser informado
            if ((mes != null || mesInicio != null || mesFim != null) && ano == null) {
                return ResponseEntity.badRequest().build();
            }
            
            String email = authentication.getName();
            com.example.demo.extrato.dto.ResumoCompletoDTO resumo = extratoService.gerarResumo(email, ano, mes, mesInicio, mesFim, investimentoId);
            return ResponseEntity.ok(resumo);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}