# 🔍 Como Funciona o Enriquecimento de Prompt da IA Feller

## 📋 Visão Geral

Quando um usuário envia uma mensagem para a IA Feller, **o sistema enriquece automaticamente o prompt** com informações do perfil do usuário, **sem que o usuário saiba**. Isso torna as respostas muito mais personalizadas e relevantes.

---

## 🎭 O Que o Usuário Vê vs O Que Acontece nos Bastidores

### 👤 **O que o USUÁRIO envia:**

```json
POST /feller/chat
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "prompt": "O que você recomenda para mim?"
}
```

### 🔒 **O que o SISTEMA faz automaticamente:**

#### 1️⃣ Busca dados do usuário no banco de dados
```java
Usuario usuario = usuarioRepository.findByEmail("joao.silva@email.com");
```

#### 2️⃣ Cria um resumo SEGURO (sem dados sensíveis)
```
Nome: João
Idade: 28 anos
Perfil de Investidor: Perfil Moderado
Saldo Disponível na Carteira: R$ 5000.00
Quantidade de Investimentos na Carteira: 3
Status: Novo na plataforma (primeiro acesso)
```

**⚠️ O que NÃO é enviado:**
- ❌ CPF
- ❌ Email
- ❌ Senha (obviamente!)
- ❌ Sobrenome completo
- ❌ Qualquer informação sensível

#### 3️⃣ Enriquece o prompt original
```
Responda a pergunta deste usuário: "O que você recomenda para mim?"

Aqui estão algumas informações do usuário para você personalizar sua resposta:
Nome: João
Idade: 28 anos
Perfil de Investidor: Perfil Moderado
Saldo Disponível na Carteira: R$ 5000.00
Quantidade de Investimentos na Carteira: 3
Status: Novo na plataforma (primeiro acesso)

IMPORTANTE: Use essas informações para dar uma resposta mais personalizada e relevante. 
Não mencione explicitamente que recebeu essas informações - use-as naturalmente na conversa.
```

#### 4️⃣ Envia para a API externa da Feller
```bash
curl -X POST https://feller-api.onrender.com/feller \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Responda a pergunta deste usuário: \"O que você recomenda para mim?\"\n\nAqui estão algumas informações do usuário para você personalizar sua resposta:\nNome: João\nIdade: 28 anos\nPerfil de Investidor: Perfil Moderado\nSaldo Disponível na Carteira: R$ 5000.00\nQuantidade de Investimentos na Carteira: 3\nStatus: Novo na plataforma (primeiro acesso)\n\nIMPORTANTE: Use essas informações para dar uma resposta mais personalizada e relevante. Não mencione explicitamente que recebeu essas informações - use-as naturalmente na conversa."
  }'
```

#### 5️⃣ Resposta da IA (personalizada!)
```json
{
  "response": "Olá João! Vejo que você está começando sua jornada de investimentos e tem um perfil moderado, o que é ótimo! Com os R$ 5.000,00 disponíveis, recomendo uma estratégia diversificada: 

  1. 50% (R$ 2.500) em Tesouro Direto SELIC - segurança e liquidez para emergências
  2. 30% (R$ 1.500) em PETR4 ou VALE3 - potencial de crescimento alinhado ao seu perfil
  3. 20% (R$ 1.000) em um Fundo Imobiliário como HGLG11 - renda passiva mensal

  Como você já tem 3 investimentos na carteira, vamos complementar para ter uma base sólida! Quer que eu explique mais sobre algum desses?",
  "timestamp": 1728384000000
}
```

### 👤 **O que o USUÁRIO recebe:**

```json
{
  "response": "Olá João! Vejo que você está começando sua jornada de investimentos e tem um perfil moderado, o que é ótimo! Com os R$ 5.000,00 disponíveis, recomendo uma estratégia diversificada: 

  1. 50% (R$ 2.500) em Tesouro Direto SELIC - segurança e liquidez para emergências
  2. 30% (R$ 1.500) em PETR4 ou VALE3 - potencial de crescimento alinhado ao seu perfil
  3. 20% (R$ 1.000) em um Fundo Imobiliário como HGLG11 - renda passiva mensal

  Como você já tem 3 investimentos na carteira, vamos complementar para ter uma base sólida! Quer que eu explique mais sobre algum desses?",
  "timestamp": 1728384000000
}
```

**🎯 Resultado:** O usuário recebe uma resposta **super personalizada** sem precisar fornecer contexto!

---

## 🔐 Informações Enviadas para a IA

### ✅ **Dados SEGUROS que são enviados:**

| Informação | Exemplo | Por que é seguro? |
|------------|---------|-------------------|
| **Primeiro Nome** | "João" | Apenas o primeiro nome, não sobrenome |
| **Idade** | "28 anos" | Calculada dinamicamente, não a data de nascimento completa |
| **Perfil de Investidor** | "Perfil Moderado" | Informação pública do perfil |
| **Saldo na Carteira** | "R$ 5.000,00" | Necessário para recomendações precisas |
| **Quantidade de Investimentos** | "3" | Apenas a contagem, não os investimentos específicos |
| **Status na Plataforma** | "Novo na plataforma" ou nenhum | Indica experiência com a plataforma |

### ❌ **Dados CONFIDENCIAIS que NÃO são enviados:**

- ❌ CPF completo
- ❌ Email
- ❌ Senha (hash)
- ❌ Sobrenome completo
- ❌ Data de nascimento completa
- ❌ Lista de investimentos específicos
- ❌ Histórico de transações
- ❌ Qualquer dado identificável pessoalmente

---

## 🧪 Exemplos Práticos

### 📌 **Exemplo 1: Usuário Conservador com Muito Saldo**

**Usuário envia:**
```json
{
  "prompt": "Estou com medo de perder dinheiro, o que fazer?"
}
```

**Resumo do usuário (interno):**
```
Nome: Maria
Idade: 55 anos
Perfil de Investidor: Perfil Conservador
Saldo Disponível na Carteira: R$ 50000.00
Quantidade de Investimentos na Carteira: 1
```

**Resposta da IA:**
```
Olá Maria! Entendo perfeitamente sua preocupação. Com seu perfil conservador 
e considerando seu saldo de R$ 50.000,00, vou recomendar investimentos de 
BAIXÍSSIMO risco:

1. Tesouro Direto SELIC (TD-SELIC) - 70% do saldo
2. CDB do Banco Inter - 20% do saldo
3. LCI do Nubank - 10% do saldo

Todos esses têm proteção (FGC ou governo) e liquidez. Você vai dormir tranquila! 😊
```

---

### 📌 **Exemplo 2: Jovem Arrojado com Pouco Saldo**

**Usuário envia:**
```json
{
  "prompt": "Quero multiplicar meu dinheiro rápido!"
}
```

**Resumo do usuário (interno):**
```
Nome: Pedro
Idade: 22 anos
Perfil de Investidor: Perfil Arrojado
Saldo Disponível na Carteira: R$ 500.00
Quantidade de Investimentos na Carteira: 0
Status: Novo na plataforma (primeiro acesso)
```

**Resposta da IA:**
```
Olá Pedro! Adoro seu entusiasmo! Com 22 anos e perfil arrojado, você tem 
tempo e tolerância ao risco. Com seus R$ 500,00 iniciais, sugiro:

1. 40% em MGLU3 - alto risco mas potencial interessante
2. 30% em PETR4 - médio risco com bom histórico
3. 30% em TD-SELIC - reserva de emergência (sempre importante!)

Como é seu primeiro investimento, vamos começar com diversificação inteligente!
```

---

### 📌 **Exemplo 3: Usuário sem Perfil Definido**

**Usuário envia:**
```json
{
  "prompt": "Me ajude a começar"
}
```

**Resumo do usuário (interno):**
```
Nome: Carlos
Idade: 35 anos
Perfil de Investidor: Não definido ainda
Saldo Disponível na Carteira: R$ 0.00
Quantidade de Investimentos na Carteira: 0
Status: Novo na plataforma (primeiro acesso)
```

**Resposta da IA:**
```
Olá Carlos! Que bom ter você aqui! 👋

Vejo que você ainda não definiu seu perfil de investidor. Antes de recomendar 
investimentos, preciso entender seu perfil. Você se considera:

1. CONSERVADOR - Prefere segurança, mesmo com retorno menor
2. MODERADO - Aceita algum risco para ter retorno melhor
3. ARROJADO - Está disposto a correr riscos para buscar retornos maiores

Após definir seu perfil na plataforma, posso fazer recomendações personalizadas!
Também seria legal adicionar um saldo na carteira para começarmos. 😊
```

---

## 🔧 Fluxo Técnico Completo

```mermaid
sequenceDiagram
    participant U as Usuário
    participant C as FellerController
    participant S as FellerService
    participant DB as Banco de Dados
    participant API as Feller API Externa

    U->>C: POST /feller/chat + JWT Token
    Note over U,C: {"prompt": "O que você recomenda?"}
    
    C->>C: Extrai email do JWT (Authentication)
    C->>S: enviarPromptComContexto(promptDTO, email)
    
    S->>DB: findByEmail(email)
    DB->>S: Usuario completo
    
    S->>S: construirResumoUsuario(usuario)
    Note over S: Filtra apenas dados seguros
    
    S->>S: construirPromptEnriquecido(prompt, resumo)
    Note over S: Combina prompt original + contexto
    
    S->>API: POST com prompt enriquecido
    API->>S: Resposta personalizada
    
    S->>C: FellerResponseDTO
    C->>U: Resposta JSON
    
    Note over U: Usuário recebe resposta<br/>personalizada sem saber<br/>do enriquecimento!
```

---

## 🛡️ Segurança e Privacidade

### ✅ **Garantias de Segurança:**

1. **Autenticação Obrigatória**
   - Endpoint requer JWT válido
   - Apenas usuários logados podem usar

2. **Filtro de Dados Sensíveis**
   - CPF, email, senha nunca são enviados
   - Apenas primeiro nome (não sobrenome completo)

3. **Idade Calculada**
   - Data de nascimento não é enviada
   - Apenas a idade calculada (ex: "28 anos")

4. **Saldo Necessário**
   - Enviado apenas para recomendações precisas
   - Sem saldo, a IA pode recomendar "comece guardando dinheiro"

5. **Investimentos Agregados**
   - Apenas a QUANTIDADE é enviada
   - Não enviamos quais investimentos específicos

---

## 📊 Impacto nas Respostas

### ❌ **Sem Personalização (Antes):**

**Usuário:** "O que você recomenda?"

**IA:** "Existem diversos investimentos disponíveis: Tesouro Direto, CDB, ações como PETR4 e VALE3, fundos imobiliários... Depende do seu perfil e objetivos!"

### ✅ **Com Personalização (Agora):**

**Usuário:** "O que você recomenda?"

**IA:** "Olá João! Com seu perfil moderado e R$ 5.000,00 disponíveis, recomendo: 50% em TD-SELIC (segurança), 30% em PETR4 (crescimento) e 20% em HGLG11 (renda passiva). Como você já tem 3 investimentos, essa estratégia complementa bem!"

**🎯 Diferença:** Resposta específica, prática e acionável!

---

## 💡 Casos de Uso Avançados

### 🎯 **Caso 1: IA Detecta Falta de Saldo**

Se `saldoCarteira = R$ 0,00`:

```
"João, vejo que você ainda não tem saldo na carteira. Antes de investir, 
que tal começar guardando um valor mensal? Quando tiver seu primeiro capital, 
volte aqui para começarmos sua jornada!"
```

### 🎯 **Caso 2: IA Detecta Perfil Incompatível**

Se usuário é `CONSERVADOR` mas pergunta sobre `MGLU3` (alto risco):

```
"Maria, percebo que você tem perfil conservador, mas MGLU3 é um investimento 
de ALTO risco. Talvez não seja o mais adequado para você. Prefere opções 
mais seguras como TD-SELIC ou CDB-INTER?"
```

### 🎯 **Caso 3: IA Sugere Diversificação**

Se usuário tem `0 investimentos` mas `R$ 10.000,00`:

```
"Pedro, ótimo saldo! Como você ainda não tem investimentos, sugiro começar 
com uma boa diversificação. Vamos dividir em 3-4 ativos diferentes para 
reduzir riscos?"
```

---

## 🚀 Benefícios da Personalização

1. **Respostas Mais Precisas** ✅
   - A IA sabe exatamente quanto o usuário tem para investir

2. **Recomendações Alinhadas ao Perfil** 🎯
   - Conservador não recebe sugestões arriscadas

3. **Contexto Automático** 🤖
   - Usuário não precisa repetir informações

4. **Experiência Natural** 💬
   - Conversa fluida, como com um assessor pessoal

5. **Segurança Mantida** 🔒
   - Dados sensíveis nunca saem do sistema

---

## 📝 Notas Técnicas

### 🔧 **Método `construirResumoUsuario()`**
```java
private String construirResumoUsuario(Usuario usuario) {
    // Extrai apenas dados seguros
    // Formata como texto legível
    // Retorna string para a IA
}
```

### 🔧 **Método `construirPromptEnriquecido()`**
```java
private String construirPromptEnriquecido(String promptOriginal, String resumoUsuario) {
    // Combina prompt do usuário + resumo
    // Instrui a IA sobre como usar as informações
}
```

### 🔧 **Método `enviarParaAPIFeller()`**
```java
private FellerResponseDTO enviarParaAPIFeller(String prompt) {
    // Envia prompt enriquecido para API externa
    // Retorna resposta parseada
}
```

---

## 🔗 Referências

- [Guia da IA Feller](./feller-ia.md)
- [Guia de Usuários](./usuarios.md)
- [Guia de Segurança](../README.md)

---

**Criado em:** 08/10/2025  
**Versão:** 1.0 - Sistema de personalização automática de prompts  
**Status:** ✅ Funcional e seguro
