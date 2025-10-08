package com.example.demo.feller.service;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.feller.dto.FellerPromptDTO;
import com.example.demo.feller.dto.FellerResponseDTO;
import com.example.demo.feller.dto.MontarCarteiraRecomendadaResponseDTO;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Monta uma carteira recomendada automaticamente usando a IA
     * A IA retorna APENAS os IDs dos investimentos recomendados
     * 
     * @param email Email do usuário autenticado
     * @return DTO com lista de IDs dos investimentos recomendados
     */
    public MontarCarteiraRecomendadaResponseDTO montarCarteiraRecomendada(String email) {
        try {
            // 1. Buscar usuário no banco
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

            // 2. Criar resumo do usuário
            String resumoUsuario = construirResumoUsuario(usuario);

            // 3. Criar prompt ESPECÍFICO para retornar apenas IDs
            String prompt = construirPromptMontarCarteira(resumoUsuario);

            // 4. Enviar para a API Feller
            String respostaIA = enviarParaAPIFellerTexto(prompt);

            // 5. Extrair IDs da resposta
            List<Long> idsRecomendados = extrairIDsInvestimentos(respostaIA);

            // 6. Validar que temos IDs válidos
            if (idsRecomendados.isEmpty()) {
                return new MontarCarteiraRecomendadaResponseDTO(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    "A IA não conseguiu gerar recomendações válidas. Por favor, tente novamente."
                );
            }

            return new MontarCarteiraRecomendadaResponseDTO(
                idsRecomendados,
                new ArrayList<>(),  // Ainda não sabemos quais são duplicatas (controller vai descobrir)
                "Carteira recomendada montada com sucesso! " + idsRecomendados.size() + " investimentos selecionados pela IA Feller."
            );

        } catch (RecursoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            return new MontarCarteiraRecomendadaResponseDTO(
                new ArrayList<>(),
                new ArrayList<>(),
                "Erro ao montar carteira recomendada: " + e.getMessage()
            );
        }
    }

    /**
     * Constrói um prompt ESPECÍFICO para a IA retornar APENAS array de IDs
     */
    private String construirPromptMontarCarteira(String resumoUsuario) {
        return """
            TAREFA: Você deve recomendar investimentos para este usuário baseado no perfil dele.
            
            PERFIL DO USUÁRIO:
            %s
            
            INVESTIMENTOS DISPONÍVEIS (ID | Nome | Categoria | Risco | Dividend Yield):
            1 | Petróleo Brasileiro S.A. (PETR4) | Renda Variável | MÉDIO | 8.5%%
            2 | Vale S.A. (VALE3) | Renda Variável | ALTO | 12.3%%
            3 | Itaú Unibanco Holding S.A. (ITUB4) | Renda Variável | MÉDIO | 6.8%%
            4 | Banco do Brasil S.A. (BBAS3) | Renda Variável | MÉDIO | 9.2%%
            5 | Ambev S.A. (ABEV3) | Renda Variável | BAIXO | 4.5%%
            6 | CSHG Real Estate Fund (HGLG11) | Fundo Imobiliário | MÉDIO | 10.2%%
            7 | Maxi Renda (MXRF11) | Fundo Imobiliário | MÉDIO | 9.6%%
            8 | XP Log (XPLG11) | Fundo Imobiliário | MÉDIO | 8.8%%
            9 | Tesouro Direto Selic (TD-SELIC) | Renda Fixa | BAIXO | 0%% (rendimento por valorização)
            10 | CDB Banco Inter (CDB-INTER) | Renda Fixa | BAIXO | 0%% (rendimento por juros)
            11 | LCI Nubank (LCI-NU) | Renda Fixa | BAIXO | 0%% (rendimento por juros)
            
            REGRAS IMPORTANTES:
            1. Analise o perfil do usuário (Conservador, Moderado ou Arrojado)
            2. Escolha entre 3 a 6 investimentos adequados ao perfil
            3. Diversifique entre categorias quando possível
            4. CONSERVADOR: Apenas investimentos de BAIXO risco (IDs: 5, 9, 10, 11)
            5. MODERADO: Mix de BAIXO e MÉDIO risco - EVITE ALTO (IDs recomendados: 1, 3, 4, 5, 6, 7, 8, 9, 10, 11)
            6. ARROJADO: Todos os riscos permitidos, incluindo VALE3 (ID 2 - único de ALTO risco)
            
            ATENÇÃO - ÚNICO INVESTIMENTO DE ALTO RISCO:
            - ID 2 (VALE3) é o ÚNICO investimento de ALTO risco
            - Conservadores NÃO podem ter VALE3
            - Moderados NÃO devem ter VALE3
            - Arrojados PODEM ter VALE3
            
            FORMATO DA RESPOSTA (EXTREMAMENTE IMPORTANTE):
            Você DEVE retornar APENAS um array JSON com os IDs dos investimentos.
            NÃO escreva explicações, NÃO escreva texto adicional, APENAS o array.
            
            EXEMPLOS DE RESPOSTAS VÁLIDAS:
            
            Para CONSERVADOR: [5, 9, 10, 11] ou [5, 9, 10] ou [9, 10, 11]
            Para MODERADO: [1, 3, 6, 9, 10] ou [1, 4, 5, 7, 9, 10]
            Para ARROJADO: [1, 2, 6, 9, 10] ou [2, 3, 4, 6, 8, 9]
            
            RESPONDA AGORA APENAS COM O ARRAY JSON DE IDs:
            """.formatted(resumoUsuario);
    }

    /**
     * Extrai IDs de investimentos da resposta da IA
     * Suporta diferentes formatos: [1,2,3], [1, 2, 3], ou texto com números
     */
    private List<Long> extrairIDsInvestimentos(String respostaIA) {
        List<Long> ids = new ArrayList<>();
        
        // Remove espaços em branco extras
        String resposta = respostaIA.trim();
        
        try {
            // Tentar parsear como array JSON direto
            if (resposta.startsWith("[") && resposta.endsWith("]")) {
                // Remove colchetes e espaços
                String numeros = resposta.substring(1, resposta.length() - 1).trim();
                
                if (!numeros.isEmpty()) {
                    // Split por vírgula
                    String[] partes = numeros.split(",");
                    
                    for (String parte : partes) {
                        try {
                            Long id = Long.parseLong(parte.trim());
                            // Validar que o ID está entre 1 e 11 (investimentos disponíveis)
                            if (id >= 1 && id <= 11) {
                                ids.add(id);
                            }
                        } catch (NumberFormatException e) {
                            // Ignora valores inválidos
                        }
                    }
                }
            } else {
                // Se não é array JSON, tentar encontrar números na resposta usando regex
                Pattern pattern = Pattern.compile("\\b([1-9]|1[01])\\b");
                Matcher matcher = pattern.matcher(resposta);
                
                while (matcher.find()) {
                    Long id = Long.parseLong(matcher.group(1));
                    if (id >= 1 && id <= 11 && !ids.contains(id)) {
                        ids.add(id);
                    }
                }
            }
        } catch (Exception e) {
            // Em caso de erro, retorna lista vazia
            return new ArrayList<>();
        }
        
        return ids;
    }

    /**
     * Envia prompt e retorna texto puro (sem wrapper de DTO)
     */
    private String enviarParaAPIFellerTexto(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("prompt", prompt);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    FELLER_API_URL,
                    request,
                    String.class
            );

            String responseBody = response.getBody();

            // Tentar parsear como JSON
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.has("response") 
                        ? jsonNode.get("response").asText() 
                        : responseBody;
            } catch (Exception e) {
                return responseBody;
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com a API Feller: " + e.getMessage());
        }
    }
}
