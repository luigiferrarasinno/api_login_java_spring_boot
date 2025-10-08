# 🎯 Guia de Investimentos Recomendados

## 📋 Visão Geral

O sistema de **Investimentos Recomendados** permite que investimentos sejam sugeridos/marcados para usuários específicos baseados em seu perfil de risco e objetivos.

---

## 🔐 Permissões

| Ação | Usuário Normal | Admin |
|------|----------------|-------|
| Ver suas próprias recomendações | ✅ | ✅ |
| Ver recomendações de outros | ❌ | ✅ |
| Adicionar recomendações para si | ✅ | ✅ |
| Adicionar recomendações para outros | ❌ | ✅ |
| Remover uma recomendação própria | ✅ | ✅ |
| Remover recomendação de outros | ❌ | ✅ |
| Remover TODAS suas recomendações | ✅ | ✅ |
| Remover TODAS de outro usuário | ❌ | ✅ |

---

## 📊 Endpoints

### 1️⃣ **Obter Investimentos Recomendados**

**GET** `/investimentos/recomendados`

#### Descrição
Retorna a lista de investimentos recomendados do usuário autenticado.

#### Headers
```
Authorization: Bearer {TOKEN}
```

#### Query Parameters (Opcional - Apenas Admin)
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `usuarioId` | Long | ID do usuário para consultar recomendações (admin only) |

#### Exemplo 1: Ver suas próprias recomendações (USER/ADMIN)
```bash
GET /investimentos/recomendados
```

#### Exemplo 2: Admin ver recomendações de outro usuário
```bash
GET /investimentos/recomendados?usuarioId=5
```

#### Resposta de Sucesso (200 OK)
```json
[
  {
    "id": 1,
    "usuarioId": 5,
    "investimentoId": 1,
    "investimentoNome": "Petróleo Brasileiro S.A.",
    "investimentoSimbolo": "PETR4",
    "categoria": "Renda Variável",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T10:30:00"
  },
  {
    "id": 2,
    "usuarioId": 5,
    "investimentoId": 6,
    "investimentoNome": "CSHG Real Estate Fund",
    "investimentoSimbolo": "HGLG11",
    "categoria": "Fundo Imobiliário",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T10:32:15"
  }
]
```

#### Possíveis Erros
| Código | Descrição |
|--------|-----------|
| 401 | Usuário não autenticado |
| 403 | Tentou acessar recomendações de outro usuário sem ser admin |
| 404 | Usuário não encontrado (quando usa usuarioId) |

---

### 2️⃣ **Adicionar Investimentos Recomendados**

**POST** `/investimentos/recomendados`

#### Descrição
Adiciona múltiplos investimentos como recomendados para um usuário.

**⚠️ VALIDAÇÃO:** Não permite duplicatas - se tentar adicionar um investimento já recomendado, retorna erro 400.

#### Headers
```
Authorization: Bearer {TOKEN}
Content-Type: application/json
```

#### Body
```json
{
  "usuarioId": 5,
  "investimentoIds": [1, 3, 6, 9]
}
```

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `usuarioId` | Long | ✅ | ID do usuário que receberá as recomendações |
| `investimentoIds` | List<Long> | ✅ | Lista de IDs dos investimentos a serem recomendados |

#### Exemplo 1: Adicionar para si mesmo (USER)
```bash
POST /investimentos/recomendados
Content-Type: application/json

{
  "usuarioId": 5,
  "investimentoIds": [1, 3, 6]
}
```

#### Exemplo 2: Admin adicionar para outro usuário
```bash
POST /investimentos/recomendados
Content-Type: application/json

{
  "usuarioId": 10,
  "investimentoIds": [9, 10, 11]
}
```

#### Resposta de Sucesso (201 Created)
```json
[
  {
    "id": 15,
    "usuarioId": 5,
    "investimentoId": 1,
    "investimentoNome": "Petróleo Brasileiro S.A.",
    "investimentoSimbolo": "PETR4",
    "categoria": "Renda Variável",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T14:20:00"
  },
  {
    "id": 16,
    "usuarioId": 5,
    "investimentoId": 3,
    "investimentoNome": "Itaú Unibanco Holding S.A.",
    "investimentoSimbolo": "ITUB4",
    "categoria": "Renda Variável",
    "risco": "Médio",
    "dataRecomendacao": "2024-10-06T14:20:00"
  }
]
```

#### Possíveis Erros
| Código | Descrição |
|--------|-----------|
| 400 | Investimento já está recomendado para este usuário (duplicata) |
| 400 | Investimento ID não existe ou está invisível |
| 401 | Usuário não autenticado |
| 403 | Tentou adicionar para outro usuário sem ser admin/owner |
| 404 | Usuário ou investimento não encontrado |

---

### 3️⃣ **Remover Um Investimento Recomendado**

**DELETE** `/investimentos/recomendados/{id}`

#### Descrição
Remove um investimento específico da lista de recomendados.

#### Headers
```
Authorization: Bearer {TOKEN}
```

#### Path Parameters
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do registro de recomendação (não é o ID do investimento!) |

#### Exemplo
```bash
DELETE /investimentos/recomendados/15
```

#### Resposta de Sucesso (204 No Content)
```
(sem corpo de resposta)
```

#### Possíveis Erros
| Código | Descrição |
|--------|-----------|
| 401 | Usuário não autenticado |
| 403 | Tentou remover recomendação de outro usuário |
| 404 | Recomendação não encontrada |

---

### 4️⃣ **Remover TODAS as Recomendações de um Usuário** ⭐ NOVO

**DELETE** `/investimentos/recomendados/usuario/{usuarioId}/todas`

#### Descrição
Remove **todos** os investimentos recomendados de um usuário de uma vez.

**Regras:**
- ✅ Usuário pode remover TODAS suas próprias recomendações
- ✅ Admin pode remover TODAS as recomendações de qualquer usuário
- ❌ Usuário NÃO pode remover recomendações de outros

#### Headers
```
Authorization: Bearer {TOKEN}
```

#### Path Parameters
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `usuarioId` | Long | ID do usuário cujas recomendações serão removidas |

#### Exemplo 1: Remover todas suas recomendações
```bash
# Se você é o usuário ID 5
DELETE /investimentos/recomendados/usuario/5/todas
```

#### Exemplo 2: Admin remover todas as recomendações de outro usuário
```bash
# Admin removendo recomendações do usuário ID 10
DELETE /investimentos/recomendados/usuario/10/todas
```

#### Resposta de Sucesso (200 OK)
```json
{
  "mensagem": "5 recomendações removidas com sucesso",
  "quantidadeRemovida": 5
}
```

#### Resposta quando não há recomendações
```json
{
  "mensagem": "0 recomendações removidas com sucesso",
  "quantidadeRemovida": 0
}
```

#### Possíveis Erros
| Código | Descrição |
|--------|-----------|
| 401 | Usuário não autenticado |
| 403 | Tentou remover recomendações de outro usuário sem ser admin |
| 404 | Usuário não encontrado |

---

## 🛡️ Validações Implementadas

### ✅ Validação de Duplicatas
O sistema **não permite** que o mesmo investimento seja recomendado duas vezes para o mesmo usuário.

**Exemplo de erro:**
```bash
POST /investimentos/recomendados
{
  "usuarioId": 5,
  "investimentoIds": [1, 1, 3]  # ID 1 duplicado!
}
```

**Resposta (400 Bad Request):**
```json
{
  "erro": "Investimento ID 1 já está recomendado para este usuário"
}
```

### ✅ Validação de Visibilidade
Usuários comuns só podem recomendar investimentos **visíveis** (campo `visivelParaUsuarios = true`).

### ✅ Validação de Permissões
- Usuário só pode gerenciar suas próprias recomendações
- Admin pode gerenciar recomendações de qualquer usuário

---

## 💡 Casos de Uso

### 📌 Caso 1: Recomendar Investimentos para Iniciantes (Admin)

```bash
# Admin recomenda investimentos conservadores para usuário iniciante
POST /investimentos/recomendados
{
  "usuarioId": 20,
  "investimentoIds": [5, 9, 10, 11]
}

# IDs:
# 5 = ABEV3 (Baixo Risco)
# 9 = TD-SELIC (Baixo Risco)
# 10 = CDB-INTER (Baixo Risco)
# 11 = LCI-NU (Baixo Risco)
```

### 📌 Caso 2: Usuário Quer Limpar Todas Suas Recomendações

```bash
# Usuário ID 5 quer começar do zero
DELETE /investimentos/recomendados/usuario/5/todas

# Resposta:
{
  "mensagem": "8 recomendações removidas com sucesso",
  "quantidadeRemovida": 8
}
```

### 📌 Caso 3: Admin Audita Recomendações de Usuários

```bash
# Ver recomendações do usuário 5
GET /investimentos/recomendados?usuarioId=5

# Ver recomendações do usuário 10
GET /investimentos/recomendados?usuarioId=10

# Remover todas as recomendações obsoletas do usuário 10
DELETE /investimentos/recomendados/usuario/10/todas
```

### 📌 Caso 4: Sistema Automático de Recomendações

```bash
# Admin ou sistema automatizado adiciona recomendações baseadas no perfil

# Perfil CONSERVADOR → Apenas baixo risco
POST /investimentos/recomendados
{
  "usuarioId": 15,
  "investimentoIds": [5, 9, 10, 11]
}

# Perfil MODERADO → Baixo e médio risco
POST /investimentos/recomendados
{
  "usuarioId": 16,
  "investimentoIds": [1, 3, 4, 5, 6, 7, 8, 9, 10, 11]
}

# Perfil ARROJADO → Todos os riscos
POST /investimentos/recomendados
{
  "usuarioId": 17,
  "investimentoIds": [1, 2, 3, 4, 5, 6, 7, 8]
}
```

---

## 📊 Estrutura de Dados

### InvestimentoRecomendado (Entidade)
```java
{
  id: Long,                    // ID único da recomendação
  usuario: Usuario,            // Usuário que recebe a recomendação
  investimento: Investimento,  // Investimento recomendado
  dataRecomendacao: LocalDateTime  // Quando foi recomendado
}
```

### InvestimentoRecomendadoResponseDTO
```json
{
  "id": 1,
  "usuarioId": 5,
  "investimentoId": 1,
  "investimentoNome": "Petróleo Brasileiro S.A.",
  "investimentoSimbolo": "PETR4",
  "categoria": "Renda Variável",
  "risco": "Médio",
  "dataRecomendacao": "2024-10-06T10:30:00"
}
```

---

## 🧪 Testes com cURL

### Obter suas recomendações
```bash
curl -X GET "http://localhost:8080/investimentos/recomendados" \
  -H "Authorization: Bearer {TOKEN}"
```

### Adicionar recomendações
```bash
curl -X POST "http://localhost:8080/investimentos/recomendados" \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 5,
    "investimentoIds": [1, 3, 6, 9]
  }'
```

### Remover uma recomendação
```bash
curl -X DELETE "http://localhost:8080/investimentos/recomendados/15" \
  -H "Authorization: Bearer {TOKEN}"
```

### Remover TODAS as recomendações
```bash
curl -X DELETE "http://localhost:8080/investimentos/recomendados/usuario/5/todas" \
  -H "Authorization: Bearer {TOKEN}"
```

---

## 📝 Notas Importantes

1. **IDs de Recomendação vs IDs de Investimento**
   - ID da recomendação é o identificador único do relacionamento
   - ID do investimento é o identificador do ativo em si
   - Ao deletar, use o ID da recomendação (campo `id` no response)

2. **Validação de Duplicatas**
   - Sistema bloqueia automaticamente duplicatas
   - Retorna erro 400 com mensagem clara

3. **Remoção em Lote**
   - Use `/usuario/{usuarioId}/todas` para limpar todas de uma vez
   - Retorna quantidade de recomendações removidas

4. **Perfis de Risco**
   - Use recomendações para guiar usuários baseado em seu perfil
   - Conservador: Apenas BAIXO risco
   - Moderado: BAIXO e MÉDIO risco
   - Arrojado: Todos os riscos (incluindo ALTO)

---

## 🔗 Referências

- [Guia de Investimentos](./investimentos.md)
- [Guia de Investimentos Populados (IDs)](./investimentos-populados.md)
- [Guia de Usuários](./usuarios.md)

---

**Atualizado em:** 08/10/2025  
**Versão:** 1.1 - Adicionado endpoint de remoção em lote
