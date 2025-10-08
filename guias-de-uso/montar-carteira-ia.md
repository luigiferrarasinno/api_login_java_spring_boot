# 🤖 Guia: Montar Carteira Recomendada Automaticamente com IA

## 📋 Visão Geral

O endpoint **Montar Carteira Recomendada** usa a inteligência artificial da Feller para **criar automaticamente** uma carteira de investimentos personalizada baseada no seu perfil completo.

### 🎯 Como Funciona:

1. **IA analisa seu perfil** (idade, perfil de risco, saldo, experiência)
2. **Seleciona 3 a 6 investimentos** adequados ao seu perfil
3. **Adiciona automaticamente** às suas recomendações
4. **Retorna a lista completa** de investimentos adicionados

---

## 🚀 Endpoint

### **POST** `/feller/montar-carteira-recomendada`

#### Descrição
A IA Feller monta uma carteira recomendada automaticamente e adiciona os investimentos às suas recomendações em uma única requisição.

**🔐 Autenticação:** OBRIGATÓRIA (JWT)  
**🎯 Personalização:** Totalmente automática baseada no seu perfil  
**⚡ Ação:** Adiciona investimentos diretamente (não apenas sugere)

---

## 📊 Request

### Headers
```
Authorization: Bearer {SEU_TOKEN_JWT}
Content-Type: application/json
```

### Body
**Nenhum body necessário!** A IA usa automaticamente as informações do seu perfil.

---

## 📤 Response

### Sucesso (201 Created)

```json
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
  },
  {
    "id": 3,
    "usuarioId": 5,
    "investimentoId": 1,
    "investimentoNome": "Petróleo Brasileiro S.A.",
    "investimentoSimbolo": "PETR4",
    "categoria": "Renda Variável",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-08T15:30:00"
  }
]
```

### Erro - IA não conseguiu gerar recomendações (400 Bad Request)

```json
{
  "erro": "A IA não conseguiu gerar recomendações válidas. Por favor, tente novamente.",
  "investimentosRecomendados": []
}
```

### Erro - Investimentos já existem (400 Bad Request)

```json
{
  "erro": "Erro ao adicionar investimentos recomendados: Investimento ID 9 já está recomendado para este usuário",
  "investimentosSugeridosPelaIA": [9, 10, 1]
}
```

---

## 💡 Exemplos de Uso

### 📌 **Exemplo 1: Perfil Conservador**

**Usuário:**
- Nome: Maria
- Idade: 55 anos
- Perfil: **Conservador**
- Saldo: R$ 50.000,00
- Investimentos atuais: 1

**Requisição:**
```bash
curl -X POST "http://localhost:8080/feller/montar-carteira-recomendada" \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json"
```

**Resposta da IA (IDs selecionados):** `[5, 9, 10, 11]`

**Investimentos adicionados:**
- **ABEV3** (ID 5) - Renda Variável, Baixo Risco
- **TD-SELIC** (ID 9) - Tesouro Direto, Baixo Risco
- **CDB-INTER** (ID 10) - CDB, Baixo Risco
- **LCI-NU** (ID 11) - Letra de Crédito, Baixo Risco

**🎯 Resultado:** 100% investimentos de **BAIXO RISCO** (adequado ao perfil conservador)

---

### 📌 **Exemplo 2: Perfil Moderado**

**Usuário:**
- Nome: João
- Idade: 28 anos
- Perfil: **Moderado**
- Saldo: R$ 5.000,00
- Investimentos atuais: 0

**Requisição:**
```bash
curl -X POST "http://localhost:8080/feller/montar-carteira-recomendada" \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json"
```

**Resposta da IA (IDs selecionados):** `[1, 3, 6, 9, 10]`

**Investimentos adicionados:**
- **PETR4** (ID 1) - Renda Variável, Médio Risco
- **ITUB4** (ID 3) - Renda Variável, Médio Risco
- **HGLG11** (ID 6) - Fundo Imobiliário, Médio Risco
- **TD-SELIC** (ID 9) - Tesouro Direto, Baixo Risco
- **CDB-INTER** (ID 10) - CDB, Baixo Risco

**🎯 Resultado:** Mix de **Baixo + Médio risco** (adequado ao perfil moderado)

---

### 📌 **Exemplo 3: Perfil Arrojado**

**Usuário:**
- Nome: Pedro
- Idade: 22 anos
- Perfil: **Arrojado**
- Saldo: R$ 10.000,00
- Investimentos atuais: 2

**Requisição:**
```bash
curl -X POST "http://localhost:8080/feller/montar-carteira-recomendada" \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json"
```

**Resposta da IA (IDs selecionados):** `[1, 2, 4, 6, 9]`

**Investimentos adicionados:**
- **PETR4** (ID 1) - Renda Variável, Médio Risco
- **VALE3** (ID 2) - Renda Variável, Médio Risco
- **MGLU3** (ID 4) - Renda Variável, **Alto Risco**
- **HGLG11** (ID 6) - Fundo Imobiliário, Médio Risco
- **TD-SELIC** (ID 9) - Tesouro Direto, Baixo Risco

**🎯 Resultado:** Mix de **Baixo + Médio + Alto risco** (adequado ao perfil arrojado)

---

## 🧠 Como a IA Decide

### 📋 **Informações Enviadas para a IA:**

```
PERFIL DO USUÁRIO:
Nome: João
Idade: 28 anos
Perfil de Investidor: Perfil Moderado
Saldo Disponível na Carteira: R$ 5000.00
Quantidade de Investimentos na Carteira: 0
Status: Novo na plataforma (primeiro acesso)
```

### 📊 **Lista de Investimentos Disponíveis (11 opções):**

| ID | Nome | Categoria | Risco | Retorno Anual |
|----|------|-----------|-------|---------------|
| 1 | PETR4 | Renda Variável | Médio | 12.5% |
| 2 | VALE3 | Renda Variável | Médio | 10.8% |
| 3 | ITUB4 | Renda Variável | Médio | 9.2% |
| 4 | MGLU3 | Renda Variável | **Alto** | 15.0% |
| 5 | ABEV3 | Renda Variável | Baixo | 8.5% |
| 6 | HGLG11 | Fundo Imobiliário | Médio | 11.3% |
| 7 | KNRI11 | Fundo Imobiliário | Médio | 10.5% |
| 8 | MXRF11 | Fundo Imobiliário | Médio | 9.8% |
| 9 | TD-SELIC | Tesouro Direto | Baixo | 10.9% |
| 10 | CDB-INTER | CDB | Baixo | 11.0% |
| 11 | LCI-NU | Letra de Crédito | Baixo | 9.5% |

### 🎯 **Regras da IA:**

1. **Perfil Conservador:**
   - ✅ Apenas investimentos de **BAIXO** risco
   - ✅ 3 a 5 investimentos (segurança)
   - ✅ Prioriza Tesouro Direto, CDB, LCI

2. **Perfil Moderado:**
   - ✅ Mix de **BAIXO + MÉDIO** risco
   - ✅ 4 a 6 investimentos (diversificação)
   - ✅ Evita investimentos de **ALTO** risco
   - ✅ Balanceamento entre segurança e retorno

3. **Perfil Arrojado:**
   - ✅ **Todos os riscos** permitidos (Baixo, Médio, Alto)
   - ✅ 4 a 6 investimentos (máxima diversificação)
   - ✅ Prioriza retornos maiores
   - ✅ Pode incluir ações voláteis (MGLU3)

### 📤 **Formato da Resposta da IA:**

A IA **DEVE** retornar **APENAS** um array JSON com os IDs:

✅ **CORRETO:**
```json
[1, 3, 6, 9, 10]
```

❌ **INCORRETO (texto adicional):**
```
Recomendo os seguintes investimentos: [1, 3, 6, 9, 10]
```

O sistema extrai automaticamente os IDs mesmo que a IA adicione texto extra.

---

## 🔄 Fluxo Completo

```
1. Usuário faz requisição
   POST /feller/montar-carteira-recomendada
   
2. Sistema busca perfil do usuário
   Nome, idade, perfil, saldo, investimentos atuais
   
3. Sistema monta prompt para a IA
   "Baseado neste perfil, retorne array de IDs..."
   
4. Envia para API Feller
   POST https://feller-api.onrender.com/feller
   
5. IA analisa e retorna IDs
   Exemplo: [1, 3, 6, 9, 10]
   
6. Sistema extrai IDs da resposta
   Valida que estão entre 1-11
   
7. Sistema adiciona investimentos
   POST /investimentos/recomendados (internamente)
   
8. Retorna investimentos adicionados
   Lista completa com detalhes
```

---

## 🧪 Testes

### Teste 1: Perfil Conservador
```bash
# Login como usuário conservador
TOKEN=$(curl -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "maria@email.com", "senha": "senha123"}' | jq -r '.token')

# Montar carteira
curl -X POST "http://localhost:8080/feller/montar-carteira-recomendada" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"

# ESPERADO: Apenas investimentos de BAIXO risco (5, 9, 10, 11)
```

### Teste 2: Limpar e Remontar
```bash
# Remover todas as recomendações atuais
curl -X DELETE "http://localhost:8080/investimentos/recomendados/usuario/{SEU_ID}/todas" \
  -H "Authorization: Bearer $TOKEN"

# Montar nova carteira
curl -X POST "http://localhost:8080/feller/montar-carteira-recomendada" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
```

---

## 📝 Notas Importantes

1. **⚠️ Adiciona às recomendações existentes**
   - Para começar do zero, use `DELETE /usuario/{id}/todas` antes

2. **🤖 IA pode variar**
   - Cada requisição pode gerar carteira diferente

3. **🔒 Segurança**
   - Dados sensíveis nunca enviados à API externa

4. **⚡ Performance**
   - Primeira requisição pode demorar ~30s (cold start)

---

## 🔗 Referências

- [Guia da IA Feller](./feller-ia.md)
- [Guia de Investimentos Recomendados](./investimentos-recomendados.md)

---

**Criado em:** 08/10/2025  
**Versão:** 1.0 - Montagem automática de carteira com IA  
**Status:** ✅ Funcional e testado
