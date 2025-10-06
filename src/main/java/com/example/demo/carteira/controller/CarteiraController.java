package com.example.demo.carteira.controller;

import com.example.demo.carteira.service.CarteiraService;
import com.example.demo.carteira.dto.PosicaoCarteiraResponseDTO;
import com.example.demo.carteira.dto.ResumoCarteiraResponseDTO;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carteira")
@Tag(name = "Carteira", description = "Endpoints para gerenciamento de carteira de investimentos, incluindo resumo financeiro e posições detalhadas")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    /**
     * 📊 Obter resumo da carteira de investimentos
     */
    @Operation(
        summary = "Obter resumo da carteira",
        description = "Retorna um resumo consolidado da carteira do usuário, incluindo saldo disponível, valor total investido, valorização total, total de dividendos recebidos e outras métricas financeiras importantes.",
        tags = { "Carteira" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Resumo da carteira retornado com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"saldoDisponivel\": 5000.00, \"valorTotalInvestido\": 15000.00, \"valorAtualCarteira\": 17500.00, \"ganhoTotalCarteira\": 2500.00, \"percentualGanhoCarteira\": 16.67, \"totalDividendosCarteira\": 850.50, \"quantidadePosicoes\": 5}"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResumoCarteiraResponseDTO> obterResumoCarteira(Authentication authentication) {
        ResumoCarteiraResponseDTO resumo = carteiraService.obterResumoCarteira(authentication.getName());
        return ResponseEntity.ok(resumo);
    }

    /**
     * 📈 Obter posições detalhadas da carteira
     */
    @Operation(
        summary = "Obter posições detalhadas da carteira",
        description = "Retorna a lista detalhada de todas as posições de investimentos na carteira do usuário, incluindo quantidade de ações, preço médio, valor atual, performance de cada ativo e total de dividendos recebidos.",
        tags = { "Carteira" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Posições da carteira retornadas com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "[{\"investimentoId\": 1, \"investimentoNome\": \"PETR4\", \"quantidade\": 100, \"precoMedio\": 25.50, \"precoAtual\": 28.00, \"valorInvestido\": 2550.00, \"valorAtual\": 2800.00, \"valorizacao\": 250.00, \"percentualValorizacao\": 9.80, \"totalDividendosRecebidos\": 150.00}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/posicoes")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PosicaoCarteiraResponseDTO>> obterPosicoesCarteira(Authentication authentication) {
        List<PosicaoCarteiraResponseDTO> posicoes = carteiraService.obterPosicoesCarteira(authentication.getName());
        return ResponseEntity.ok(posicoes);
    }
}