package com.example.demo.feller.controller;

import com.example.demo.feller.dto.FellerPromptDTO;
import com.example.demo.feller.dto.FellerResponseDTO;
import com.example.demo.feller.service.FellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
}
