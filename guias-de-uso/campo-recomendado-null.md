# 🎯 Campo `recomendadoParaVoce` - Lógica com Null

## 📋 Resumo da Mudança

O campo `recomendadoParaVoce` agora retorna **três possíveis valores**:

| Valor | Significado | Quando ocorre |
|-------|-------------|---------------|
| `null` | Sem sistema de recomendação | Usuário **NÃO tem nenhuma** recomendação configurada |
| `true` | Investimento recomendado | Usuário **TEM recomendações** E este investimento está nelas |
| `false` | Investimento não recomendado | Usuário **TEM recomendações** MAS este investimento não está nelas |

## 🔍 Por Que Essa Mudança?

### ❌ **Problema Anterior:**
- Todo investimento retornava `true` ou `false`
- Não havia forma de saber se o usuário tinha ou não recomendações configuradas
- Usuários sem recomendações viam `false` para tudo (confuso)

### ✅ **Solução Atual:**
- `null` indica claramente: "Este usuário não tem recomendações"
- `true/false` indica: "Este usuário tem recomendações, e este investimento está/não está nelas"

## 📖 Exemplos Práticos

### 1. Usuário SEM Recomendações

**Request:**
```bash
GET /investimentos
Authorization: Bearer {token_user_sem_recomendacoes}
```

**Response:**
```json
[
  {
    "id": 1,
    "nome": "Tesouro Direto Selic",
    "simbolo": "TD-SELIC",
    "categoria": "RENDA_FIXA",
    "precoAtual": 102.50,
    "risco": "BAIXO",
    "recomendadoParaVoce": null  ← Usuário não tem recomendações
  },
  {
    "id": 2,
    "nome": "Petrobras PN",
    "simbolo": "PETR4",
    "categoria": "RENDA_VARIAVEL",
    "precoAtual": 28.50,
    "risco": "MEDIO",
    "recomendadoParaVoce": null  ← Usuário não tem recomendações
  }
]
```

### 2. Usuário COM Recomendações

**Request:**
```bash
GET /investimentos
Authorization: Bearer {token_user_com_recomendacoes}
```

**Response:**
```json
[
  {
    "id": 1,
    "nome": "Tesouro Direto Selic",
    "simbolo": "TD-SELIC",
    "categoria": "RENDA_FIXA",
    "precoAtual": 102.50,
    "risco": "BAIXO",
    "recomendadoParaVoce": true  ← FOI recomendado
  },
  {
    "id": 2,
    "nome": "Petrobras PN",
    "simbolo": "PETR4",
    "categoria": "RENDA_VARIAVEL",
    "precoAtual": 28.50,
    "risco": "MEDIO",
    "recomendadoParaVoce": false  ← NÃO foi recomendado
  }
]
```

### 3. Carteira com Recomendações

**Request:**
```bash
GET /carteira?incluirResumo=true
Authorization: Bearer {token_jwt}
```

**Response:**
```json
{
  "saldoDisponivel": 5000.00,
  "valorTotalInvestido": 15000.00,
  "posicoes": [
    {
      "id": 1,
      "nomeInvestimento": "Petrobras PN",
      "simboloInvestimento": "PETR4",
      "quantidadeTotal": 100,
      "valorAtual": 2800.00,
      "recomendadoParaVoce": true  ← Este investimento foi recomendado
    },
    {
      "id": 2,
      "nomeInvestimento": "Vale ON",
      "simboloInvestimento": "VALE3",
      "quantidadeTotal": 50,
      "valorAtual": 3260.00,
      "recomendadoParaVoce": false  ← Este NÃO foi recomendado
    }
  ]
}
```

### 4. Playlist com Investimentos

**Request:**
```bash
GET /playlists/1
Authorization: Bearer {token_jwt}
```

**Response:**
```json
{
  "id": 1,
  "nome": "Top Dividendos 2024 💰",
  "descricao": "As melhores ações e FIIs pagadores de dividendos",
  "investimentos": [
    {
      "id": 1,
      "nome": "Petrobras PN",
      "simbolo": "PETR4",
      "categoria": "Ações",
      "recomendadoParaVoce": true  ← Recomendado para você
    },
    {
      "id": 2,
      "nome": "Vale ON",
      "simbolo": "VALE3",
      "categoria": "Ações",
      "recomendadoParaVoce": null  ← Você não tem recomendações configuradas
    }
  ]
}
```

## 🔧 Como Funciona Tecnicamente

### 1. **Mudança nos DTOs**

Alteração de `boolean` (primitivo) para `Boolean` (nullable):

```java
// ANTES
private boolean recomendadoParaVoce;

// DEPOIS
private Boolean recomendadoParaVoce; // Pode ser null, true ou false
```

### 2. **Lógica no Service**

```java
// PASSO 1: Verificar se usuário TEM alguma recomendação
boolean usuarioTemRecomendacoes = 
    investimentoRecomendadoRepository.existsByUsuarioId(usuarioId);

// PASSO 2: Se NÃO tem recomendações → retornar null
if (!usuarioTemRecomendacoes) {
    dto.setRecomendadoParaVoce(null);
    return dto;
}

// PASSO 3: Se TEM recomendações → verificar cada investimento
boolean recomendado = investimentoRecomendadoRepository
    .existsByUsuarioIdAndInvestimentoId(usuarioId, investimentoId);

dto.setRecomendadoParaVoce(recomendado); // true ou false
```

### 3. **Novo Método no Repository**

Adicionado método para verificar se usuário tem recomendações:

```java
/**
 * Verifica se o usuário tem ALGUMA recomendação
 */
boolean existsByUsuarioId(Long usuarioId);
```

## 🎨 Como o Frontend Deve Tratar

### JavaScript/TypeScript

```typescript
interface Investimento {
  id: number;
  nome: string;
  recomendadoParaVoce: boolean | null;
}

function renderizarInvestimento(investimento: Investimento) {
  if (investimento.recomendadoParaVoce === null) {
    // Usuário não tem recomendações - não mostrar badge
    return `<div>${investimento.nome}</div>`;
  } else if (investimento.recomendadoParaVoce === true) {
    // Investimento foi recomendado
    return `
      <div>
        ${investimento.nome}
        <span class="badge badge-success">✨ Recomendado para você</span>
      </div>
    `;
  } else {
    // Investimento NÃO foi recomendado (usuário tem recomendações, mas não esta)
    return `<div>${investimento.nome}</div>`;
  }
}
```

### React

```jsx
function InvestimentoCard({ investimento }) {
  return (
    <div className="investimento-card">
      <h3>{investimento.nome}</h3>
      <p>{investimento.simbolo}</p>
      
      {/* Mostrar badge APENAS se foi recomendado */}
      {investimento.recomendadoParaVoce === true && (
        <span className="badge-recomendado">
          ✨ Recomendado para você
        </span>
      )}
      
      {/* Opcionalmente, mostrar mensagem se não tem recomendações */}
      {investimento.recomendadoParaVoce === null && (
        <small className="text-muted">
          Configure suas preferências para ver recomendações
        </small>
      )}
    </div>
  );
}
```

## 📊 Endpoints Afetados

Todos os seguintes endpoints agora retornam `recomendadoParaVoce` como `Boolean` (nullable):

| Endpoint | Método | Campo Incluído |
|----------|--------|----------------|
| `/investimentos` | GET | ✅ Sim |
| `/investimentos/{id}` | GET | ✅ Sim |
| `/investimentos` | POST | ✅ Sim |
| `/investimentos/{id}/toggle-ativo` | PATCH | ✅ Sim |
| `/investimentos/{id}/toggle-visibilidade` | PATCH | ✅ Sim |
| `/investimentos/favoritar/{investimentoId}/{usuarioId}` | POST | ✅ Sim |
| `/investimentos/favoritos/{usuarioId}` | GET | ✅ Sim |
| `/playlists/{id}` | GET | ✅ Sim (investimentos aninhados) |
| `/carteira` | GET | ✅ Sim (posições) |

## ✅ Benefícios

1. **Clareza**: Diferencia "sem recomendações" de "não recomendado"
2. **UX Melhor**: Frontend pode decidir não mostrar badges se usuário não tem recomendações
3. **Opcional**: Reconhece que recomendações são uma feature opcional
4. **Semântica**: `null` tem significado claro: "não aplicável"

## 🔬 Testes

### Teste 1: Usuário sem recomendações
```bash
# Login como usuário sem recomendações
POST /api/auth/login
{ "email": "novo@user.com", "password": "123456" }

# Verificar investimentos
GET /investimentos
# Espera: todos com "recomendadoParaVoce": null
```

### Teste 2: Usuário com recomendações
```bash
# Login como usuário com recomendações
POST /api/auth/login
{ "email": "user@user.com", "password": "123456" }

# Verificar investimentos
GET /investimentos
# Espera: alguns com true, outros com false, nenhum com null
```

### Teste 3: Admin cria recomendação
```bash
# Admin adiciona recomendação para usuário
POST /investimentos/recomendados
{
  "usuarioId": 1,
  "investimentoId": 5
}

# Usuário verifica investimento
GET /investimentos/5
# Espera: "recomendadoParaVoce": true
```

## 📦 Arquivos Modificados

| Arquivo | Mudança |
|---------|---------|
| `InvestimentoDTO.java` | `boolean` → `Boolean` |
| `InvestimentoPlaylistResponseDTO.java` | `boolean` → `Boolean` |
| `PosicaoCarteiraResponseDTO.java` | `boolean` → `Boolean` |
| `InvestimentoService.java` | Lógica com verificação de `existsByUsuarioId()` |
| `PlaylistService.java` | Lógica com verificação de `existsByUsuarioId()` |
| `CarteiraService.java` | Lógica com verificação de `existsByUsuarioId()` |
| `InvestimentoRecomendadoRepository.java` | Novo método `existsByUsuarioId(Long)` |

---

✅ **Sistema atualizado!** Agora o campo `recomendadoParaVoce` retorna `null` quando o usuário não tem recomendações, tornando a API mais semântica e facilitando o trabalho do frontend. 🎯
