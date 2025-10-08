# 🤖 Guia da IA Feller

## 📋 Visão Geral

**Feller** é uma assistente de investimentos intel#### Exemplo 1: Perguntar sobre investimentos de baixo risc#### Exemplo 3: Recomendação personalizada (SUPER PERSONALIZADA!)

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {SEU_TOKEN_JWT}" \
  -d '{
    "prompt": "O que você recomenda para mim?"
  }'
```

**Resposta esperada (ULTRA PERSONALIZADA!):**
```json
{
  "response": "Olá Maria! Vi que você tem 55 anos e perfil conservador - perfeito! Com seus R$ 50.000,00 disponíveis, recomendo uma estratégia SUPER segura: 70% em TD-SELIC (R$ 35.000 - liquidez total), 20% em CDB-INTER (R$ 10.000 - 11% ao ano) e 10% em LCI-NU (R$ 5.000 - isento de IR). Você já tem 1 investimento, então essa diversificação complementa bem! Quer que eu explique cada um?",
  "timestamp": 1728384015000
}
```

**🎯 Diferença:** Você só pergunta "o que você recomenda?" e a IA já sabe TUDO sobre você!

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {SEU_TOKEN_JWT}" \
  -d '{
    "prompt": "Quais são os investimentos de baixo risco disponíveis?"
  }'
```

**Resposta esperada (PERSONALIZADA!):**
```json
{
  "response": "Olá João! Para seu perfil conservador, os investimentos de baixo risco ideais são: ABEV3 (ações estáveis), TD-SELIC (Tesouro Direto), CDB-INTER (11% ao ano) e LCI-NU (isento de IR). Com seus R$ 5.000,00 disponíveis, recomendo dividir: 40% TD-SELIC, 30% CDB-INTER, 20% LCI-NU e 10% ABEV3.",
  "timestamp": 1728384000000
}
```

**🎯 Diferença:** A IA sabe seu nome, perfil e saldo - resposta muito mais útil!especificamente sobre os investimentos disponíveis na plataforma. Ela pode responder perguntas, fazer análises, comparar ativos e fornecer **recomendações PERSONALIZADAS** baseadas no seu perfil.

### 🎯 **Diferencial: Personalização Automática**

A Feller recebe automaticamente informações do seu perfil (nome, idade, perfil de investidor, saldo) para fornecer respostas **ultra personalizadas**, sem que você precise fornecer contexto!

**📖 Quer entender como funciona?** Leia: [Guia de Personalização](./feller-personalizacao.md)

---

## 🏗️ Estrutura

```
feller/
├── controller/
│   └── FellerController.java      # Endpoints da IA
├── dto/
│   ├── FellerPromptDTO.java       # Request (prompt do usuário)
│   └── FellerResponseDTO.java     # Response (resposta da IA)
└── service/
    └── FellerService.java         # Comunicação com API externa
```

---

## 🔌 API Externa

**URL da IA Feller:** `https://feller-api.onrender.com/feller`

### Requisição
```bash
curl -X POST https://feller-api.onrender.com/feller \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Olá mundo"}'
```

---

## 📊 Endpoints

### 1️⃣ **Conversar com Feller (Personalizada)** ⭐

**POST** `/feller/chat`

#### Descrição
Envia uma mensagem/pergunta para a assistente Feller e recebe uma resposta **inteligente e PERSONALIZADA** sobre investimentos.

**🔐 Autenticação:** OBRIGATÓRIA (requer JWT token)  
**🎯 Personalização:** A IA recebe automaticamente informações do seu perfil (nome, idade, perfil de investidor, saldo) para respostas mais precisas.  
**🔒 Segurança:** Apenas dados não sensíveis são enviados (sem CPF, senha ou email completo).

#### Headers
```
Content-Type: application/json
```

#### Body
```json
{
  "prompt": "Quais são os investimentos de baixo risco disponíveis?"
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `prompt` | String | ✅ | Pergunta/mensagem para a IA Feller |

#### Resposta de Sucesso (200 OK)
```json
{
  "response": "Com base nos investimentos disponíveis, recomendo para baixo risco: ABEV3 (Renda Variável), TD-SELIC (Tesouro Direto), CDB-INTER (CDB) e LCI-NU (Letra de Crédito). Esses ativos possuem menor volatilidade e são ideais para investidores conservadores...",
  "timestamp": 1728384000000
}
```

#### Resposta em Caso de Erro (200 OK com mensagem de erro)
```json
{
  "response": "Desculpe, ocorreu um erro ao processar sua solicitação. A IA Feller está temporariamente indisponível. Erro: Connection timeout",
  "timestamp": 1728384005000
}
```

#### Possíveis Erros
| Código | Descrição |
|--------|-----------|
| 400 | Prompt vazio ou inválido |
| 500 | Erro interno do servidor |

---

## 💡 Exemplos de Uso

### 📌 Exemplo 1: Perguntar sobre investimentos de baixo risco

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Quais são os investimentos de baixo risco disponíveis?"
  }'
```

**Resposta esperada:**
```json
{
  "response": "Os investimentos de baixo risco são: ABEV3, TD-SELIC, CDB-INTER e LCI-NU...",
  "timestamp": 1728384000000
}
```

---

### 📌 Exemplo 2: Análise de um ativo específico

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Me explique sobre o investimento PETR4"
  }'
```

**Resposta esperada:**
```json
{
  "response": "PETR4 é a ação preferencial da Petróleo Brasileiro S.A. (Petrobras). É um investimento de Renda Variável com risco médio, focado no setor de energia. Tem um rendimento histórico de 12.5% ao ano...",
  "timestamp": 1728384010000
}
```

---

### 📌 Exemplo 3: Recomendação personalizada

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Sou investidor iniciante com perfil conservador, o que você recomenda?"
  }'
```

**Resposta esperada:**
```json
{
  "response": "Para investidores iniciantes com perfil conservador, recomendo começar com: 1) Tesouro Direto SELIC (TD-SELIC) - liquidez diária e baixo risco; 2) CDB-INTER - retorno de 11% ao ano com garantia do FGC; 3) LCI-NU - isento de IR...",
  "timestamp": 1728384015000
}
```

---

### 📌 Exemplo 4: Comparação entre investimentos

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Qual a diferença entre CDB e Tesouro Direto?"
  }'
```

**Resposta esperada:**
```json
{
  "response": "CDB (Certificado de Depósito Bancário) e Tesouro Direto são ambos investimentos de renda fixa, mas têm diferenças importantes: CDB é emitido por bancos privados, enquanto o Tesouro Direto é emitido pelo governo federal...",
  "timestamp": 1728384020000
}
```

---

### 📌 Exemplo 5: Perguntas sobre estratégias

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Como devo diversificar minha carteira com R$ 10.000?"
  }'
```

---

### 📌 Exemplo 6: Informações sobre categorias

```bash
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "O que são Fundos Imobiliários?"
  }'
```

---

## 🧠 Capacidades da IA Feller

A Feller foi treinada com informações sobre **todos os 11 investimentos** da plataforma:

### ✅ Conhecimento sobre:
1. **PETR4** - Petróleo Brasileiro S.A. (Renda Variável, Médio Risco)
2. **VALE3** - Vale S.A. (Renda Variável, Médio Risco)
3. **ITUB4** - Itaú Unibanco (Renda Variável, Médio Risco)
4. **MGLU3** - Magazine Luiza (Renda Variável, Alto Risco)
5. **ABEV3** - Ambev S.A. (Renda Variável, Baixo Risco)
6. **HGLG11** - CSHG Real Estate Fund (FII, Médio Risco)
7. **KNRI11** - Kinea Renda Imobiliária (FII, Médio Risco)
8. **MXRF11** - Maxi Renda (FII, Médio Risco)
9. **TD-SELIC** - Tesouro Direto SELIC (Tesouro Direto, Baixo Risco)
10. **CDB-INTER** - CDB Banco Inter (CDB, Baixo Risco)
11. **LCI-NU** - LCI Nubank (Letra de Crédito, Baixo Risco)

### ✅ Pode responder sobre:
- ✅ Características de cada investimento
- ✅ Níveis de risco e retorno esperado
- ✅ Comparações entre ativos
- ✅ Recomendações por perfil de investidor
- ✅ Estratégias de diversificação
- ✅ Diferenças entre categorias
- ✅ Liquidez e prazos
- ✅ Tributação (IR)

---

## 🛡️ Tratamento de Erros

### ✅ Validação de Prompt Vazio
```bash
POST /feller/chat
{
  "prompt": ""
}
```

**Resposta (400 Bad Request):**
```json
{
  "erro": "O prompt não pode estar vazio"
}
```

### ✅ API Externa Indisponível
Quando a API da Feller está fora do ar ou com problemas, o sistema retorna uma mensagem amigável:

```json
{
  "response": "Desculpe, ocorreu um erro ao processar sua solicitação. A IA Feller está temporariamente indisponível. Erro: Connection timeout",
  "timestamp": 1728384025000
}
```

---

## 🔧 Implementação Técnica

### 🏗️ Arquitetura

```
Cliente
   ↓
FellerController (POST /feller/chat)
   ↓
FellerService (envia para API externa)
   ↓
API Externa: https://feller-api.onrender.com/feller
   ↓
Resposta → FellerResponseDTO
```

### 🔌 Comunicação Externa

O `FellerService` usa **RestTemplate** para comunicação HTTP:

```java
// Prepara requisição
Map<String, String> requestBody = new HashMap<>();
requestBody.put("prompt", promptDTO.getPrompt());

// Envia POST para API externa
ResponseEntity<String> response = restTemplate.postForEntity(
    "https://feller-api.onrender.com/feller",
    request,
    String.class
);

// Parseia resposta JSON
JsonNode jsonNode = objectMapper.readTree(response.getBody());
String responseText = jsonNode.get("response").asText();
```

### 📦 DTOs

**FellerPromptDTO (Request):**
```java
{
  "prompt": "string"  // @NotBlank
}
```

**FellerResponseDTO (Response):**
```java
{
  "response": "string",
  "timestamp": long    // milliseconds
}
```

---

## 🧪 Testes

### Teste Local com cURL
```bash
# Teste básico
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Olá Feller, me ajude com investimentos!"}'

# Teste com pergunta complexa
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Compare PETR4 com VALE3 em termos de risco e retorno"}'

# Teste de validação (deve retornar erro 400)
curl -X POST "http://localhost:8080/feller/chat" \
  -H "Content-Type: application/json" \
  -d '{"prompt": ""}'
```

### Teste com Postman
```
POST http://localhost:8080/feller/chat
Content-Type: application/json

{
  "prompt": "Quais investimentos você recomenda para quem tem R$ 5.000?"
}
```

---

## 📝 Notas Importantes

1. **⚠️ REQUER AUTENTICAÇÃO**
   - Endpoint protegido com JWT
   - Necessário passar `Authorization: Bearer {TOKEN}` no header
   - A IA usa seu perfil para personalizar respostas

2. **API Externa pode estar lenta**
   - A API Feller está em servidor gratuito (Render)
   - Primeira requisição após inatividade pode demorar ~30s (cold start)

3. **Respostas podem variar**
   - A IA é não-determinística
   - Mesma pergunta pode gerar respostas diferentes

4. **Personalização Automática** ⭐
   - Sistema enriquece o prompt com dados do seu perfil automaticamente
   - Você não vê isso acontecendo - é transparente!
   - Leia mais: [Guia de Personalização](./feller-personalizacao.md)

5. **Limitações**
   - Não armazena histórico de conversas (stateless)
   - Cada requisição é independente
   - Não possui contexto de mensagens anteriores

---

## 🚀 Melhorias Futuras

### 🔜 Possíveis Evoluções:
1. **Histórico de Conversas**
   - Salvar conversas no banco
   - Contexto entre mensagens
   - Relatório de uso

2. **Autenticação**
   - Restringir acesso a usuários logados
   - Limitar quantidade de requisições

3. **Cache de Respostas**
   - Cachear perguntas frequentes
   - Reduzir chamadas à API externa

4. **Integração com Carteira**
   - Feller analisa investimentos do usuário
   - Recomendações personalizadas baseadas em dados reais

5. **Análise de Sentimento**
   - Detectar tom da conversa
   - Respostas mais contextualizadas

---

## 🔗 Referências

- **[📖 Guia de Personalização da IA](./feller-personalizacao.md)** ⭐ Como funciona a personalização automática
- [Guia de Investimentos](./investimentos.md)
- [Guia de Investimentos Populados](./investimentos-populados.md)
- [Guia de Usuários](./usuarios.md)
- [API Externa Feller](https://feller-api.onrender.com/feller)

---

## 📊 Swagger/OpenAPI

A documentação interativa está disponível em:
```
http://localhost:8080/swagger-ui.html
```

Procure pela tag **"Feller IA"** para testar o endpoint diretamente no navegador! 🌐

---

**Criado em:** 08/10/2025  
**Atualizado em:** 08/10/2025  
**Versão:** 2.0 - Adicionada personalização automática de prompts com dados do usuário  
**Status:** ✅ Funcional, testado e SUPER PERSONALIZADO!
