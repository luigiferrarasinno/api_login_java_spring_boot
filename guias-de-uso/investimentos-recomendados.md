# 📚 Guia de Uso - Investimentos Recomendados

## 🎯 Visão Geral

Sistema para gerenciar investimentos recomendados para usuários. Permite que usuários e administradores criem, visualizem e removam recomendações de investimentos.

---

## 📊 Estrutura da Tabela

### **investimento_recomendado**

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | ID do registro (PK) |
| `usuario_id` | Long | ID do usuário (FK) |
| `investimento_id` | Long | ID do investimento (FK) |
| `data_recomendacao` | LocalDateTime | Data/hora da recomendação |

---

## 🔐 Controle de Acesso

### **Usuário Normal:**
- ✅ Pode ver suas **próprias** recomendações
- ✅ Pode adicionar recomendações para **si mesmo**
- ✅ Pode remover suas **próprias** recomendações
- ❌ Não pode ver/adicionar/remover recomendações de outros

### **Admin:**
- ✅ Pode ver recomendações de **qualquer** usuário
- ✅ Pode adicionar recomendações para **qualquer** usuário
- ✅ Pode remover **qualquer** recomendação

---

## 📋 Endpoints Disponíveis

### 1️⃣ **GET /investimentos/recomendados** - Obter Recomendações

Retorna a lista de investimentos recomendados.

**Parâmetros:**
- `usuarioId` (Long, opcional) - ⚠️ **Apenas para admin**

**Exemplos:**

```http
# Usuário vendo suas próprias recomendações
GET /investimentos/recomendados
Authorization: Bearer <user-token>
```

**Resposta:**
```json
[
  {
    "id": 1,
    "usuarioId": 5,
    "investimentoId": 10,
    "investimentoNome": "Petrobras PN",
    "investimentoSimbolo": "PETR4",
    "categoria": "Ações",
    "risco": "Alto",
    "dataRecomendacao": "2024-10-06T10:30:00"
  },
  {
    "id": 2,
    "usuarioId": 5,
    "investimentoId": 15,
    "investimentoNome": "Vale ON",
    "investimentoSimbolo": "VALE3",
    "categoria": "Ações",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T11:45:00"
  }
]
```

---

```http
# Admin vendo recomendações do usuário ID 10
GET /investimentos/recomendados?usuarioId=10
Authorization: Bearer <admin-token>
```

**Resposta:**
```json
[
  {
    "id": 5,
    "usuarioId": 10,
    "investimentoId": 8,
    "investimentoNome": "Itaú Unibanco PN",
    "investimentoSimbolo": "ITUB4",
    "categoria": "Ações",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-05T09:20:00"
  }
]
```

---

### 2️⃣ **POST /investimentos/recomendados** - Adicionar Recomendações

Adiciona múltiplos investimentos como recomendados para um usuário.

**Validação de Acesso:** `@usuarioService.isOwnerOrAdmin`
- Usuário pode adicionar apenas para si mesmo
- Admin pode adicionar para qualquer usuário

**Body (JSON):**
```json
{
  "usuarioId": 5,
  "investimentoIds": [10, 15, 20]
}
```

**Exemplo - Usuário adicionando para si mesmo:**

```http
POST /investimentos/recomendados
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "usuarioId": 5,
  "investimentoIds": [10, 15, 20]
}
```

**Resposta (201 Created):**
```json
[
  {
    "id": 1,
    "usuarioId": 5,
    "investimentoId": 10,
    "investimentoNome": "Petrobras PN",
    "investimentoSimbolo": "PETR4",
    "categoria": "Ações",
    "risco": "Alto",
    "dataRecomendacao": "2024-10-06T10:30:00"
  },
  {
    "id": 2,
    "usuarioId": 5,
    "investimentoId": 15,
    "investimentoNome": "Vale ON",
    "investimentoSimbolo": "VALE3",
    "categoria": "Ações",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T10:30:01"
  },
  {
    "id": 3,
    "usuarioId": 5,
    "investimentoId": 20,
    "investimentoNome": "Magazine Luiza ON",
    "investimentoSimbolo": "MGLU3",
    "categoria": "Ações",
    "risco": "Alto",
    "dataRecomendacao": "2024-10-06T10:30:02"
  }
]
```

---

**Exemplo - Admin adicionando para outro usuário:**

```http
POST /investimentos/recomendados
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "usuarioId": 10,
  "investimentoIds": [8, 12]
}
```

**Resposta (201 Created):**
```json
[
  {
    "id": 5,
    "usuarioId": 10,
    "investimentoId": 8,
    "investimentoNome": "Itaú Unibanco PN",
    "investimentoSimbolo": "ITUB4",
    "categoria": "Ações",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T11:00:00"
  },
  {
    "id": 6,
    "usuarioId": 10,
    "investimentoId": 12,
    "investimentoNome": "Bradesco PN",
    "investimentoSimbolo": "BBDC4",
    "categoria": "Ações",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T11:00:01"
  }
]
```

---

### 3️⃣ **DELETE /investimentos/recomendados/{id}** - Remover Recomendação

Remove uma recomendação específica pelo ID do registro.

**Validação:**
- Usuário normal: Só pode remover suas próprias recomendações
- Admin: Pode remover qualquer recomendação

**Exemplo - Usuário removendo sua recomendação:**

```http
DELETE /investimentos/recomendados/1
Authorization: Bearer <user-token>
```

**Resposta (204 No Content):**
```
(sem body)
```

---

**Exemplo - Admin removendo qualquer recomendação:**

```http
DELETE /investimentos/recomendados/5
Authorization: Bearer <admin-token>
```

**Resposta (204 No Content):**
```
(sem body)
```

---

## ⚠️ Erros Comuns

### 400 Bad Request - Investimento já recomendado
```json
{
  "message": "Investimento ID 10 já está recomendado para este usuário",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Não pode adicionar o mesmo investimento duas vezes para o mesmo usuário.

---

### 403 Forbidden - Acesso negado (GET)
```json
{
  "message": "Apenas administradores podem visualizar recomendações de outros usuários",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Usuário comum tentou usar `usuarioId` no GET. Remova o parâmetro.

---

### 403 Forbidden - Acesso negado (POST)
```json
{
  "message": "Access Denied",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Usuário comum tentou adicionar recomendações para outro usuário. Use seu próprio ID.

---

### 403 Forbidden - Acesso negado (DELETE)
```json
{
  "message": "Você não tem permissão para remover esta recomendação",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** A recomendação pertence a outro usuário. Apenas o dono ou admin pode remover.

---

### 404 Not Found - Recomendação não encontrada
```json
{
  "message": "Recomendação não encontrada: 999",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** O ID da recomendação não existe no banco de dados.

---

### 404 Not Found - Investimento não encontrado
```json
{
  "message": "Investimento não encontrado: 999",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** Um dos IDs de investimento no POST não existe.

---

### 404 Not Found - Usuário não encontrado
```json
{
  "message": "Usuário não encontrado com ID: 999",
  "timestamp": "2024-10-06T11:20:00"
}
```
**Solução:** O `usuarioId` fornecido não existe.

---

## 🎨 Casos de Uso Práticos

### Caso 1: Usuário criando sua lista de favoritos

```http
# 1. Adicionar investimentos favoritos
POST /investimentos/recomendados
{
  "usuarioId": 5,
  "investimentoIds": [10, 15, 20, 25]
}

# 2. Ver lista de favoritos
GET /investimentos/recomendados

# 3. Remover um investimento
DELETE /investimentos/recomendados/2
```

---

### Caso 2: Admin gerenciando recomendações para cliente

```http
# 1. Ver recomendações atuais do cliente
GET /investimentos/recomendados?usuarioId=10

# 2. Adicionar novas recomendações
POST /investimentos/recomendados
{
  "usuarioId": 10,
  "investimentoIds": [8, 12, 18]
}

# 3. Remover recomendação específica
DELETE /investimentos/recomendados/5
```

---

### Caso 3: Construir portfólio personalizado

```http
# Estratégia conservadora
POST /investimentos/recomendados
{
  "usuarioId": 5,
  "investimentoIds": [1, 2, 3]  // Tesouro, CDBs, Fundos
}

# Estratégia agressiva
POST /investimentos/recomendados
{
  "usuarioId": 5,
  "investimentoIds": [10, 15, 20, 25]  // Ações de crescimento
}
```

---

## 📊 Campos do Response DTO

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | ID único do registro de recomendação |
| `usuarioId` | Long | ID do usuário dono da recomendação |
| `investimentoId` | Long | ID do investimento recomendado |
| `investimentoNome` | String | Nome completo do investimento |
| `investimentoSimbolo` | String | Ticker/Símbolo (ex: PETR4) |
| `categoria` | String | Categoria do investimento |
| `risco` | String | Nível de risco |
| `dataRecomendacao` | LocalDateTime | Quando foi recomendado |

---

## 🔄 Fluxo Típico de Uso

```
1. Usuário autentica → Recebe token JWT

2. Usuário vê investimentos disponíveis
   GET /investimentos

3. Usuário adiciona favoritos
   POST /investimentos/recomendados
   { usuarioId: 5, investimentoIds: [10, 15] }

4. Usuário consulta seus favoritos
   GET /investimentos/recomendados

5. Usuário decide investir em um favorito
   POST /extrato/comprar-acao
   { investimentoId: 10, quantidade: 100 }

6. Usuário remove da lista de favoritos
   DELETE /investimentos/recomendados/1
```

---

## ✨ Vantagens do Sistema

1. ✅ **Personalização** - Cada usuário tem sua lista única
2. ✅ **Flexibilidade** - Adiciona múltiplos de uma vez
3. ✅ **Segurança** - Validação rigorosa de permissões
4. ✅ **Rastreabilidade** - Data de quando foi recomendado
5. ✅ **Admin-friendly** - Admins podem gerenciar para clientes
6. ✅ **Informações completas** - Retorna dados do investimento junto

---

## 🎯 Resumo Rápido

| Quero... | Endpoint | Método |
|----------|----------|--------|
| Ver meus favoritos | `/investimentos/recomendados` | GET |
| Ver favoritos de outro (admin) | `/investimentos/recomendados?usuarioId=10` | GET |
| Adicionar favoritos | `/investimentos/recomendados` | POST |
| Remover favorito | `/investimentos/recomendados/{id}` | DELETE |

---

**Documentação gerada em:** 07/10/2025  
**Versão da API:** 1.0
