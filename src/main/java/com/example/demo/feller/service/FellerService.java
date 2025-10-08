package com.example.demo.feller.service;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.feller.dto.FellerPromptDTO;
import com.example.demo.feller.dto.FellerResponseDTO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

/**
 * Service para comunicação com a API da IA Feller
 */
@Service
public class FellerService {

    private static final String FELLER_API_URL = "https://feller-api.onrender.com/feller";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public FellerService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Envia um prompt ENRIQUECIDO com informações do usuário para a IA Feller
     * O usuário não vê as informações adicionadas - apenas a resposta personalizada
     *
     * @param promptDTO DTO contendo o prompt original do usuário
     * @param email Email do usuário autenticado
     * @return DTO com a resposta personalizada da IA
     */
    public FellerResponseDTO enviarPromptComContexto(FellerPromptDTO promptDTO, String email) {
        // 1. Buscar usuário no banco
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        // 2. Criar resumo seguro do usuário (SEM informações sensíveis)
        String resumoUsuario = construirResumoUsuario(usuario);

        // 3. Enriquecer o prompt original com o contexto do usuário
        String promptEnriquecido = construirPromptEnriquecido(promptDTO.getPrompt(), resumoUsuario);

        // 4. Enviar para a API Feller
        return enviarParaAPIFeller(promptEnriquecido);
    }

    /**
     * Constrói um resumo SEGURO do usuário (sem dados sensíveis)
     */
    private String construirResumoUsuario(Usuario usuario) {
        StringBuilder resumo = new StringBuilder();
        
        // Nome (primeiro nome apenas)
        String primeiroNome = usuario.getNomeUsuario().split(" ")[0];
        resumo.append("Nome: ").append(primeiroNome).append("\n");
        
        // Idade (calculada a partir da data de nascimento)
        if (usuario.getDt_nascimento() != null) {
            int idade = Period.between(usuario.getDt_nascimento(), LocalDate.now()).getYears();
            resumo.append("Idade: ").append(idade).append(" anos\n");
        }
        
        // Perfil de investidor
        if (usuario.getTipo() != null) {
            resumo.append("Perfil de Investidor: ").append(usuario.getTipo().getDescricao()).append("\n");
        } else {
            resumo.append("Perfil de Investidor: Não definido ainda\n");
        }
        
        // Tipo de investidor (campo legado)
        if (usuario.getTipo_de_investidor() != null && !usuario.getTipo_de_investidor().equals("nenhum por enquanto")) {
            resumo.append("Tipo: ").append(usuario.getTipo_de_investidor()).append("\n");
        }
        
        // Saldo disponível na carteira
        BigDecimal saldo = usuario.getSaldoCarteira();
        if (saldo != null && saldo.compareTo(BigDecimal.ZERO) > 0) {
            resumo.append("Saldo Disponível na Carteira: R$ ").append(String.format("%.2f", saldo)).append("\n");
        } else {
            resumo.append("Saldo Disponível na Carteira: R$ 0,00\n");
        }
        
        // Quantidade de investimentos na carteira
        int quantidadeInvestimentos = usuario.getInvestimentos() != null ? usuario.getInvestimentos().size() : 0;
        resumo.append("Quantidade de Investimentos na Carteira: ").append(quantidadeInvestimentos);
        
        // Status de primeiro login (iniciante ou experiente na plataforma)
        if (usuario.isFirstLogin()) {
            resumo.append("\nStatus: Novo na plataforma (primeiro acesso)");
        }
        
        return resumo.toString();
    }

    /**
     * Constrói o prompt enriquecido de forma transparente para o usuário
     */
    private String construirPromptEnriquecido(String promptOriginal, String resumoUsuario) {
        return String.format(
            "Responda a pergunta deste usuário: \"%s\"\n\n" +
            "Aqui estão algumas informações do usuário para você personalizar sua resposta:\n" +
            "%s\n\n" +
            "IMPORTANTE: Use essas informações para dar uma resposta mais personalizada e relevante. " +
            "Não mencione explicitamente que recebeu essas informações - use-as naturalmente na conversa.",
            promptOriginal,
            resumoUsuario
        );
    }

    /**
     * Envia o prompt para a API externa da Feller
     */
    private FellerResponseDTO enviarParaAPIFeller(String prompt) {
        try {
            // Preparar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Preparar body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("prompt", prompt);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // Fazer requisição POST para a API Feller
            ResponseEntity<String> response = restTemplate.postForEntity(
                    FELLER_API_URL,
                    request,
                    String.class
            );

            String responseBody = response.getBody();
            String responseText;

            // Tentar parsear como JSON primeiro
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                // Se é JSON válido, extrair o campo "response"
                responseText = jsonNode.has("response") 
                        ? jsonNode.get("response").asText() 
                        : responseBody;
            } catch (Exception jsonException) {
                // Se não é JSON válido, usar o corpo da resposta diretamente (texto puro)
                responseText = responseBody;
            }

            return new FellerResponseDTO(responseText);

        } catch (Exception e) {
            // Em caso de erro, retornar mensagem de erro
            return new FellerResponseDTO(
                    "Desculpe, ocorreu um erro ao processar sua solicitação. " +
                    "A IA Feller está temporariamente indisponível. Erro: " + e.getMessage()
            );
        }
    }
}
