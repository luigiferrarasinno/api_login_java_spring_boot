package com.example.demo.carteira.controller;

import com.example.demo.carteira.service.CarteiraService;
import com.example.demo.carteira.dto.ResumoCarteiraResponseDTO;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carteira")
@Tag(
    name = "Carteira", 
    description = "Gerenciamento de carteira de investimentos - mostra posições atuais e patrimônio atual do usuário"
)
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    /**
     * 📊 Endpoint unificado para obter dados da carteira com filtros opcionais
     */
    @Operation(
        summary = "Obter carteira de investimentos",
        description = "Retorna a carteira do usuário com filtros opcionais. Use incluirResumo=true para ver totais e métricas.",
        tags = { "Carteira" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dados da carteira retornados com sucesso",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Apenas Posições",
                        description = "Resposta quando incluirResumo=false (padrão)",
                        value = """
                            {
                              "posicoes": [
                                {
                                  "id": 1,
                                  "nomeInvestimento": "Petrobras PN",
                                  "simboloInvestimento": "PETR4",
                                  "categoria": "Ações",
                                  "risco": "Alto",
                                  "quantidadeTotal": 100,
                                  "precoMedio": 25.50,
                                  "valorInvestido": 2550.00,
                                  "precoAtual": 28.00,
                                  "valorAtual": 2800.00,
                                  "ganhoPerda": 250.00,
                                  "percentualGanhoPerda": 9.80,
                                  "totalDividendosRecebidos": 150.00,
                                  "dataPrimeiraCompra": "2024-01-15T10:30:00",
                                  "dataUltimaMovimentacao": "2024-09-20T14:20:00"
                                }
                              ]
                            }
                            """
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Com Resumo Completo",
                        description = "Resposta quando incluirResumo=true",
                        value = """
                            {
                              "saldoDisponivel": 5000.00,
                              "valorTotalInvestido": 15000.00,
                              "valorAtualCarteira": 17500.00,
                              "ganhoTotalCarteira": 2500.00,
                              "percentualGanhoCarteira": 16.67,
                              "totalDividendosCarteira": 850.50,
                              "quantidadePosicoes": 5,
                              "posicoes": [
                                {
                                  "id": 1,
                                  "nomeInvestimento": "Petrobras PN",
                                  "simboloInvestimento": "PETR4",
                                  "quantidadeTotal": 100,
                                  "precoMedio": 25.50,
                                  "valorInvestido": 2550.00,
                                  "precoAtual": 28.00,
                                  "valorAtual": 2800.00,
                                  "ganhoPerda": 250.00,
                                  "percentualGanhoPerda": 9.80,
                                  "totalDividendosRecebidos": 150.00
                                }
                              ]
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - tentou acessar carteira de outro usuário sem ser admin")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResumoCarteiraResponseDTO> obterCarteira(
            @Parameter(description = "Se true, inclui resumo financeiro completo. Se false, retorna apenas posições.")
            @RequestParam(defaultValue = "false") boolean incluirResumo,
            
            @Parameter(description = "ID do usuário para consultar carteira (⚠️ APENAS ADMIN). Deixe vazio para ver sua própria carteira.")
            @RequestParam(required = false) Long usuarioId,
            
            @Parameter(description = "Filtrar posições por ID do investimento")
            @RequestParam(required = false) Long investimentoId,
            
            @Parameter(description = "Buscar uma posição específica por ID")
            @RequestParam(required = false) Long posicaoId,
            
            Authentication authentication) {
        
        // Verificar se usuário é admin
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        
        ResumoCarteiraResponseDTO resultado = carteiraService.obterCarteiraComFiltros(
            authentication.getName(),
            usuarioId,
            investimentoId,
            posicaoId,
            incluirResumo,
            isAdmin
        );
        
        return ResponseEntity.ok(resultado);
    }
}