package com.example.demo.feller.controller;

import com.example.demo.feller.dto.FellerPromptDTO;
import com.example.demo.feller.dto.FellerResponseDTO;
import com.example.demo.feller.dto.MontarCarteiraRecomendadaResponseDTO;
import com.example.demo.feller.service.FellerService;
import com.example.demo.investimento.dto.InvestimentoRecomendadoResponseDTO;
import com.example.demo.investimento.service.InvestimentoRecomendadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para endpoints da IA Feller
 * Feller é uma assistente especializada em investimentos
 */
@RestController
@RequestMapping("/feller")
@Tag(name = "Feller IA", description = "Endpoints para interação com a assistente de investimentos Feller")
public class FellerController {

    @Autowired
    private FellerService fellerService;

    @Autowired
    private InvestimentoRecomendadoService investimentoRecomendadoService;

    @PostMapping("/chat")
    @Operation(
            summary = "Conversar com a IA Feller (Personalizada)",
            description = "Envia uma mensagem/prompt para a assistente de investimentos Feller e recebe uma resposta inteligente PERSONALIZADA. " +
                    "A Feller utiliza informações do seu perfil (nome, tipo de perfil, saldo) para fornecer recomendações mais precisas. " +
                    "⚠️ Requer autenticação. A IA recebe apenas informações não sensíveis (sem CPF, senha ou email).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Prompt/mensagem para a IA Feller",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FellerPromptDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Pergunta sobre investimentos",
                                            value = "{\"prompt\": \"Quais são os investimentos de baixo risco disponíveis?\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Análise de ativo",
                                            value = "{\"prompt\": \"Me explique sobre o investimento PETR4\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Recomendação personalizada",
                                            value = "{\"prompt\": \"O que você recomenda para mim?\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Análise de saldo",
                                            value = "{\"prompt\": \"Como devo investir meu saldo disponível?\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Comparação",
                                            value = "{\"prompt\": \"Qual a diferença entre CDB e Tesouro Direto?\"}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resposta personalizada da IA recebida com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FellerResponseDTO.class),
                                    examples = @ExampleObject(
                                            value = "{\"response\": \"Olá João! Considerando seu perfil MODERADO e saldo de R$ 5.000,00, recomendo: 60% em TD-SELIC (baixo risco), 30% em PETR4 (médio risco) e 10% em HGLG11 (FII)...\", \"timestamp\": 1728384000000}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Prompt inválido ou vazio"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro ao comunicar com a API da IA Feller"
                    )
            }
    )
    public ResponseEntity<FellerResponseDTO> chat(
            @Valid @RequestBody FellerPromptDTO promptDTO,
            Authentication authentication) {
        
        String email = authentication.getName();
        FellerResponseDTO response = fellerService.enviarPromptComContexto(promptDTO, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/montar-carteira-recomendada")
    @Operation(
            summary = "Montar Carteira Recomendada Automaticamente com IA",
            description = "A IA Feller analisa seu perfil completo (idade, perfil de investidor, saldo) e monta AUTOMATICAMENTE " +
                    "uma carteira recomendada personalizada. Os investimentos são adicionados diretamente às suas recomendações. " +
                    "⚠️ Requer autenticação. A IA escolhe entre 3 a 6 investimentos baseados no seu perfil de risco.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Carteira montada e investimentos recomendados adicionados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InvestimentoRecomendadoResponseDTO.class),
                                    examples = @ExampleObject(
                                            value = """
                                                [
                                                  {
                                                    "id": 1,
                                                    "usuarioId": 5,
                                                    "investimentoId": 9,
                                                    "investimentoNome": "Tesouro Direto - SELIC",
                                                    "investimentoSimbolo": "TD-SELIC",
                                                    "categoria": "Tesouro Direto",
                                                    "risco": "Baixo",
                                                    "dataRecomendacao": "2024-10-08T15:30:00"
                                                  },
                                                  {
                                                    "id": 2,
                                                    "usuarioId": 5,
                                                    "investimentoId": 10,
                                                    "investimentoNome": "CDB Banco Inter",
                                                    "investimentoSimbolo": "CDB-INTER",
                                                    "categoria": "CDB",
                                                    "risco": "Baixo",
                                                    "dataRecomendacao": "2024-10-08T15:30:00"
                                                  }
                                                ]
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "IA não conseguiu gerar recomendações válidas"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro ao comunicar com a API da IA Feller"
                    )
            }
    )
    public ResponseEntity<?> montarCarteiraRecomendada(Authentication authentication) {
        // 1. Usar a IA para obter IDs dos investimentos recomendados
        MontarCarteiraRecomendadaResponseDTO carteiraIA = fellerService.montarCarteiraRecomendada(
                authentication.getName()
        );

        // 2. Verificar se a IA retornou investimentos válidos
        if (carteiraIA.getInvestimentosAdicionados() == null || carteiraIA.getInvestimentosAdicionados().isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                    "erro", carteiraIA.getMensagem(),
                    "investimentosRecomendados", java.util.List.of()
            ));
        }

        // 3. Obter ID do usuário atual
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));

        // 4. Buscar o usuário para pegar o ID
        try {
            Long usuarioId = investimentoRecomendadoService.obterUsuarioIdPorEmail(email);

            // 5. Adicionar os investimentos recomendados pela IA COM DETALHAMENTO
            java.util.Map<String, java.util.List<Long>> resultado = 
                    investimentoRecomendadoService.adicionarRecomendacoesComDetalhes(
                            usuarioId,
                            carteiraIA.getInvestimentosAdicionados(),
                            isAdmin
                    );

            // 6. Criar resposta detalhada
            String mensagem;
            int quantidadeAdicionados = resultado.get("adicionados").size();
            int quantidadeJaExistentes = resultado.get("jaExistentes").size();
            
            if (quantidadeJaExistentes == 0) {
                mensagem = String.format("Carteira montada com sucesso! %d investimento(s) adicionado(s).", 
                        quantidadeAdicionados);
            } else {
                mensagem = String.format(
                        "Carteira montada! %d investimento(s) adicionado(s), %d já existia(m) nas suas recomendações.",
                        quantidadeAdicionados, quantidadeJaExistentes);
            }
            
            MontarCarteiraRecomendadaResponseDTO response = new MontarCarteiraRecomendadaResponseDTO(
                    resultado.get("adicionados"),
                    resultado.get("jaExistentes"),
                    mensagem
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                    "erro", "Erro ao adicionar investimentos recomendados: " + e.getMessage(),
                    "investimentosSugeridosPelaIA", carteiraIA.getInvestimentosAdicionados()
            ));
        }
    }
}
