# 📊 Guia de Uso - API Unificada de Carteira

## 🎯 Diferença entre CARTEIRA e EXTRATO

### **CARTEIRA** = Patrimônio ATUAL (Snapshot do Presente)
- ✅ Mostra apenas as posições que você **TEM AGORA**
- ✅ Ganho/perda calculado com base no **preço ATUAL** de mercado (não realizado)
- ✅ Dividendos apenas das ações que você **ainda POSSUI**
- 🎯 **Use para:** Ver seu patrimônio atual, tomar decisões de venda/compra

### **EXTRATO** = Histórico COMPLETO (Timeline do Passado)
- ✅ Mostra **TODAS** as transações que já aconteceram
- ✅ Ganho/perda calculado com base em vendas **REALIZADAS**
- ✅ Dividendos de **TODAS** as ações, mesmo as já vendidas
- 🎯 **Use para:** Ver histórico completo, calcular lucros realizados, imposto de renda

---

## 🆕 Endpoint Unificado

### **GET /carteira**

Endpoint único que substitui os antigos `/carteira/resumo` e `/carteira/posicoes`.

---

## 📋 Parâmetros Disponíveis

### 1️⃣ **incluirResumo** (boolean, padrão: false)
Controla se deve retornar o resumo financeiro completo.

| Valor | Retorno |
|-------|---------|
| `false` (padrão) | Apenas lista de posições |
| `true` | Resumo completo (saldo, totais, percentuais) + posições |

**Exemplos:**
```http
GET /carteira
GET /carteira?incluirResumo=true
```

---

### 2️⃣ **usuarioId** (Long, opcional) ⚠️ **APENAS ADMIN**

Permite visualizar a carteira de outro usuário.

| Quem pode usar | Comportamento |
|----------------|---------------|
| Usuário comum | ❌ Retorna 403 Forbidden |
| Admin | ✅ Pode ver carteira de qualquer usuário |

**Exemplos:**
```http
# Admin vendo carteira do usuário ID 10
GET /carteira?usuarioId=10&incluirResumo=true

# Usuário comum tentando ver outro usuário (ERRO 403)
GET /carteira?usuarioId=5  # ❌ Acesso negado
```

---

### 3️⃣ **investimentoId** (Long, opcional)

Filtra posições de um investimento específico.

**Exemplos:**
```http
# Ver apenas posições de PETR4 (ID 5)
GET /carteira?investimentoId=5

# Ver posições de VALE3 com resumo
GET /carteira?investimentoId=8&incluirResumo=true
```

---

### 4️⃣ **posicaoId** (Long, opcional)

Retorna uma posição específica da carteira.

**Exemplos:**
```http
# Detalhes da posição ID 25
GET /carteira?posicaoId=25

# Posição com resumo
GET /carteira?posicaoId=25&incluirResumo=true
```

---

## 📚 Exemplos de Uso Completos

### Exemplo 1: Listar minhas posições (sem resumo)
```http
GET /carteira
Authorization: Bearer <token>
```

**Resposta:**
```json
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
```

---

### Exemplo 2: Resumo completo da carteira
```http
GET /carteira?incluirResumo=true
Authorization: Bearer <token>
```

**Resposta:**
```json
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
    },
    {
      "id": 2,
      "nomeInvestimento": "Vale ON",
      "simboloInvestimento": "VALE3",
      "quantidadeTotal": 50,
      "precoMedio": 60.00,
      "valorInvestido": 3000.00,
      "precoAtual": 65.00,
      "valorAtual": 3250.00,
      "ganhoPerda": 250.00,
      "percentualGanhoPerda": 8.33,
      "totalDividendosRecebidos": 200.00
    }
  ]
}
```

---

### Exemplo 3: Filtrar por investimento específico
```http
GET /carteira?investimentoId=5&incluirResumo=true
Authorization: Bearer <token>
```

**Resposta:**
```json
{
  "saldoDisponivel": 5000.00,
  "valorTotalInvestido": 2550.00,
  "valorAtualCarteira": 2800.00,
  "ganhoTotalCarteira": 250.00,
  "percentualGanhoCarteira": 9.80,
  "totalDividendosCarteira": 150.00,
  "quantidadePosicoes": 1,
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
```

---

### Exemplo 4: Admin visualizando carteira de outro usuário
```http
GET /carteira?usuarioId=10&incluirResumo=true
Authorization: Bearer <admin-token>
```

**Resposta:** (mesma estrutura, mas com dados do usuário ID 10)

---

### Exemplo 5: Detalhes de uma posição específica
```http
GET /carteira?posicaoId=25
Authorization: Bearer <token>
```

**Resposta:**
```json
{
  "posicoes": [
    {
      "id": 25,
      "nomeInvestimento": "Itaú Unibanco PN",
      "simboloInvestimento": "ITUB4",
      "categoria": "Ações",
      "risco": "Médio",
      "quantidadeTotal": 200,
      "precoMedio": 24.80,
      "valorInvestido": 4960.00,
      "precoAtual": 26.50,
      "valorAtual": 5300.00,
      "ganhoPerda": 340.00,
      "percentualGanhoPerda": 6.85,
      "totalDividendosRecebidos": 85.50,
      "dataPrimeiraCompra": "2024-03-10T09:15:00",
      "dataUltimaMovimentacao": "2024-10-01T16:45:00"
    }
  ]
}
```

---

## 🔒 Controle de Acesso

### Usuário Comum
- ✅ Pode ver apenas sua **própria** carteira
- ❌ Não pode usar o parâmetro `usuarioId`
- ❌ Tentativa de usar `usuarioId` retorna **403 Forbidden**

### Admin
- ✅ Pode ver **qualquer** carteira usando `usuarioId`
- ✅ Pode combinar `usuarioId` com outros filtros
- ✅ Pode ver própria carteira (sem `usuarioId`)

---

## 🎨 Casos de Uso Práticos

### 1. Dashboard do usuário
```http
GET /carteira?incluirResumo=true
```
Mostra visão completa: saldo + posições + totais

---

### 2. Listar apenas ações
```http
GET /carteira
```
Lista simples de posições (sem overhead do resumo)

---

### 3. Análise de investimento específico
```http
GET /carteira?investimentoId=5&incluirResumo=true
```
Foca em um ativo específico com métricas

---

### 4. Admin auditando usuário
```http
GET /carteira?usuarioId=15&incluirResumo=true
```
Visão completa da carteira de outro usuário

---

### 5. Detalhes para decisão de venda
```http
GET /carteira?posicaoId=30&incluirResumo=false
```
Dados detalhados de uma posição (preço médio, dividendos, ganho/perda)

---

## 📊 Campos Retornados

### Resumo (quando `incluirResumo=true`)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `saldoDisponivel` | BigDecimal | Dinheiro disponível para investir |
| `valorTotalInvestido` | BigDecimal | Soma do custo de compra de todas posições |
| `valorAtualCarteira` | BigDecimal | Valor atual de mercado de todas posições |
| `ganhoTotalCarteira` | BigDecimal | Diferença entre valor atual e investido |
| `percentualGanhoCarteira` | BigDecimal | Percentual de ganho/perda |
| `totalDividendosCarteira` | BigDecimal | Soma de dividendos de todas posições |
| `quantidadePosicoes` | Integer | Número de posições ativas |

### Posição Individual
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | ID da posição |
| `nomeInvestimento` | String | Nome completo do ativo |
| `simboloInvestimento` | String | Ticker (ex: PETR4) |
| `categoria` | String | Categoria do investimento |
| `risco` | String | Nível de risco |
| `quantidadeTotal` | BigDecimal | Quantidade de ações |
| `precoMedio` | BigDecimal | Preço médio de compra |
| `valorInvestido` | BigDecimal | Custo total de aquisição |
| `precoAtual` | BigDecimal | Preço atual de mercado |
| `valorAtual` | BigDecimal | Valor atual da posição |
| `ganhoPerda` | BigDecimal | Ganho/perda não realizado |
| `percentualGanhoPerda` | BigDecimal | Percentual de ganho/perda |
| `totalDividendosRecebidos` | BigDecimal | Dividendos recebidos desta ação |
| `dataPrimeiraCompra` | LocalDateTime | Data da primeira compra |
| `dataUltimaMovimentacao` | LocalDateTime | Data da última movimentação |

---

## ⚠️ Erros Comuns

### 403 Forbidden - Acesso Negado
```json
{
  "message": "Apenas administradores podem visualizar carteira de outros usuários",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Usuário comum tentou usar `usuarioId`. Remova o parâmetro.

---

### 404 Not Found - Posição não encontrada
```json
{
  "message": "Posição não encontrada: 999",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Verifique se o `posicaoId` existe e pertence ao usuário.

---

### 403 Forbidden - Posição de outro usuário
```json
{
  "message": "Você não tem permissão para acessar esta posição",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** A posição existe, mas pertence a outro usuário. Apenas admins podem acessar.

---

## 🔄 Migração dos Endpoints Antigos

### Antes (2 endpoints)
```http
# Resumo
GET /carteira/resumo

# Posições
GET /carteira/posicoes
```

### Agora (1 endpoint unificado)
```http
# Apenas posições (equivalente ao antigo /posicoes)
GET /carteira

# Resumo completo (equivalente ao antigo /resumo)
GET /carteira?incluirResumo=true
```

---

## ✨ Vantagens da Unificação

1. ✅ **Menos endpoints** = API mais simples
2. ✅ **Filtros poderosos** = Maior flexibilidade
3. ✅ **Controle granular** = Escolha incluir ou não o resumo
4. ✅ **Admin pode auditar** = Visualizar carteira de qualquer usuário
5. ✅ **Performance** = Buscar apenas o que precisa
6. ✅ **Documentação clara** = Diferença entre carteira e extrato explicada

---

## 🎯 Resumo Rápido

| Quero... | Endpoint |
|----------|----------|
| Minhas posições (simples) | `GET /carteira` |
| Meu resumo completo | `GET /carteira?incluirResumo=true` |
| Só minhas ações PETR4 | `GET /carteira?investimentoId=5` |
| Detalhes de uma posição | `GET /carteira?posicaoId=25` |
| Ver carteira de outro usuário (admin) | `GET /carteira?usuarioId=10&incluirResumo=true` |

---

## 💡 Dicas

1. **Performance:** Use `incluirResumo=false` se não precisar dos totais
2. **Admin:** Sempre use `usuarioId` quando auditar outros usuários
3. **Filtros:** Combine filtros para buscas específicas
4. **Dividendos:** Lembre-se que mostra apenas dividendos de posições atuais

---

**Documentação gerada em:** 06/10/2025  
**Versão da API:** 1.0 (Endpoint Unificado)
