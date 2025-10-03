# 📈 Guia de Investimentos - Endpoints

Este guia cobre todos os endpoints relacionados ao gerenciamento de investimentos.

---

## 🔐 Autenticação

Todos os endpoints protegidos requerem um token JWT no cabeçalho:
```
Authorization: Bearer SEU_TOKEN_JWT
```

---

## 💰 Investimentos Criados Automaticamente

> ⚠️ **11 investimentos criados automaticamente pelo sistema:**

### 📈 **Ações Brasileiras** (Categoria: RENDA_VARIAVEL)
| Símbolo | Nome | Preço Base | Risco | Dividend Yield | Frequência |
|---------|------|------------|-------|----------------|-----------|
| **PETR4** | Petróleo Brasileiro S.A. | R$ 28,50 | MEDIO | 8,5% | Trimestral |
| **VALE3** | Vale S.A. | R$ 65,20 | ALTO | 12,3% | Semestral |
| **ITUB4** | Itaú Unibanco Holding S.A. | R$ 32,40 | MEDIO | 6,8% | Trimestral |
| **BBAS3** | Banco do Brasil S.A. | R$ 45,30 | MEDIO | 9,2% | Trimestral |
| **ABEV3** | Ambev S.A. | R$ 14,80 | BAIXO | 4,5% | Semestral |

### 🏢 **Fundos Imobiliários** (Categoria: FUNDO_IMOBILIARIO)
| Símbolo | Nome | Preço Base | Risco | Dividend Yield | Frequência |
|---------|------|------------|-------|----------------|-----------|
| **HGLG11** | CSHG Real Estate Fund | R$ 105,40 | MEDIO | 0,85 | Mensal |
| **MXRF11** | Maxi Renda | R$ 9,85 | MEDIO | 0,08 | Mensal |
| **XPLG11** | XP Log | R$ 98,50 | MEDIO | 0,75 | Mensal |

### 💎 **Renda Fixa** (Categoria: RENDA_FIXA)
| Símbolo | Nome | Preço Base | Risco | Dividend Yield |
|---------|------|------------|-------|----------------|
| **TD-SELIC** | Tesouro Direto Selic | R$ 102,50 | BAIXO | 0,0% |
| **CDB-INTER** | CDB Banco Inter | R$ 1.000,00 | BAIXO | 0,0% |
| **LCI-NU** | LCI Nubank | R$ 5.000,00 | BAIXO | 0,0% |

> 💡 **Todos os investimentos estão ativos e visíveis para usuários por padrão**

---

## 🧠 Regras de Permissão

| Endpoint                                    | USER            | ADMIN           |
| ------------------------------------------- | --------------- | --------------- |
| `GET /investimentos`                        | ✅ (autenticado) | ✅ (autenticado) |
| `POST /investimentos`                       | ❌               | ✅               |
| `GET /investimentos/{id}`                   | ✅ (autenticado) | ✅               |
| `DELETE /investimentos/{id}`                | ❌               | ✅               |
| `PATCH /investimentos/{id}/toggle-ativo`    | ❌               | ✅               |
| `PATCH /investimentos/{id}/toggle-visibilidade` | ❌           | ✅               |
| `POST /investimentos/favoritar/{investimentoId}/{usuarioId}` | ✅ (próprio) | ✅ |
| `GET /investimentos/favoritos/{usuarioId}`  | ✅ (próprio)     | ✅               |

---

## 🛡️ Sistema de Visibilidade

**Flag `visivelParaUsuarios`**: Controla se usuários comuns podem ver o investimento
- **Admin**: Pode controlar a visibilidade através do endpoint toggle
- **Usuários comuns**: Só veem investimentos com `visivelParaUsuarios=true`

---

## 📋 Endpoints

### 1. Listar Investimentos com Filtros
**GET** `/investimentos`  
**Acesso**: Usuários autenticados

#### Parâmetros de Filtro (todos opcionais):

**Para Usuários Comuns:**
- `nome` - Busca parcial no nome (case-insensitive)
- `simbolo` - Busca parcial no símbolo (case-insensitive)
- `categoria` - Filtro por categoria (RENDA_VARIAVEL, FUNDO_IMOBILIARIO, RENDA_FIXA)
- `risco` - Filtro por risco (BAIXO, MEDIO, ALTO)
- `precoMin` - Preço mínimo
- `precoMax` - Preço máximo

**Para Admins (filtros adicionais):**
- `ativo` - Filtro por status ativo (true/false)
- `visivel` - Filtro por visibilidade (true/false)

#### Exemplos de Uso:

**Buscar ações com nome "vale":**
```
GET /investimentos?nome=vale&categoria=ACAO
```

**Buscar investimentos por faixa de preço:**
```
GET /investimentos?precoMin=50&precoMax=200
```

**Admin: Buscar investimentos invisíveis:**
```
GET /investimentos?visivel=false
```

**Combinar múltiplos filtros:**
```
GET /investimentos?categoria=FII&risco=MEDIO&precoMin=100
```

#### Resposta:
```json
[
  {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD",
    "categoria": "TESOURO_DIRETO",
    "precoBase": 100.00,
    "precoAtual": 102.50,
    "variacaoPercentual": 2.50,
    "descricao": "Investimento em títulos do governo",
    "ativo": true,
    "visivelParaUsuarios": true,
    "risco": "BAIXO",
    "quantidadeTotal": 1000000,
    "quantidadeDisponivel": 999500,
    "dividendYield": 0.0,
    "frequenciaDividendo": 0
  }
]
```

---

### 2. Buscar Investimento por ID
**GET** `/investimentos/{id}`  
**Acesso**: Usuários autenticados

#### Resposta:
```json
{
  "id": 2,
  "nome": "Ações Vale",
  "simbolo": "VALE3",
  "categoria": "ACAO",
  "precoBase": 65.00,
  "precoAtual": 68.75,
  "variacaoPercentual": 5.77,
  "descricao": "Ações da Vale S.A.",
  "ativo": true,
  "visivelParaUsuarios": true,
  "risco": "ALTO",
  "quantidadeTotal": 500000,
  "quantidadeDisponivel": 487350,
  "dividendYield": 8.5,
  "frequenciaDividendo": 4
}
```

---

### 3. Criar Investimento
**POST** `/investimentos`  
**Acesso**: Apenas ADMIN

#### Requisição:
```json
{
  "nome": "Fundo Imobiliário XYZ",
  "simbolo": "XYZF11",
  "categoria": "FII",
  "precoBase": 150.00,
  "descricao": "Fundo de investimento imobiliário",
  "risco": "MEDIO",
  "quantidadeTotal": 100000,
  "dividendYield": 7.2,
  "frequenciaDividendo": 12,
  "visivelParaUsuarios": true
}
```

---

### 4. Ativar/Desativar Investimento
**PATCH** `/investimentos/{id}/toggle-ativo`  
**Acesso**: Apenas ADMIN

#### Resposta:
```json
{
  "id": 1,
  "nome": "Tesouro Direto",
  "ativo": false,
  "mensagem": "Status alterado com sucesso"
}
```

---

### 5. Controlar Visibilidade
**PATCH** `/investimentos/{id}/toggle-visibilidade`  
**Acesso**: Apenas ADMIN

#### Resposta:
```json
{
  "id": 2,
  "nome": "Ações Vale",
  "visivelParaUsuarios": false,
  "mensagem": "Visibilidade alterada com sucesso"
}
```

---

### 6. Favoritar/Desfavoritar Investimento
**POST** `/investimentos/favoritar/{investimentoId}/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Exemplo:
```
POST /investimentos/favoritar/2/1
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
{
  "mensagem": "Investimento favoritado com sucesso",
  "investimento": {
    "id": 2,
    "nome": "Ações Vale",
    "favoritado": true
  }
}
```

---

### 7. Listar Investimentos Favoritos
**GET** `/investimentos/favoritos/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Resposta:
```json
[
  {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD",
    "categoria": "TESOURO_DIRETO",
    "precoAtual": 102.50,
    "risco": "BAIXO"
  },
  {
    "id": 2,
    "nome": "Ações Vale",
    "simbolo": "VALE3",
    "categoria": "ACAO",
    "precoAtual": 68.75,
    "risco": "ALTO"
  }
]
```

---

### 8. Deletar Investimento
**DELETE** `/investimentos/{id}`  
**Acesso**: Apenas ADMIN

#### Resposta:
```json
{
  "message": "Investimento com ID 3 foi excluído com sucesso."
}
```

---

## 📊 Sistema de Preços e Volatilidade

### Preços Dinâmicos:
- **Preço Base**: Valor de referência inicial
- **Preço Atual**: Cotação simulada com volatilidade
- **Variação %**: Percentual de mudança

### Volatilidade por Risco:
- **Baixo** (Tesouro): 0,5% de volatilidade
- **Médio** (FIIs): 1,5% de volatilidade
- **Alto** (Ações): 3,5% de volatilidade

---

## 💰 Sistema de Dividendos

### Características:
- **Dividend Yield**: Percentual anual de dividendos
- **Frequência**: Quantas vezes por ano paga dividendos
- **Cálculo**: Automático baseado no preço atual e yield

### Exemplos:
- **Tesouro Direto**: 0% yield (sem dividendos)
- **Ações Vale**: 8,5% ao ano, trimestral (4x/ano)
- **FII HGLG11**: 6,2% ao ano, mensal (12x/ano)

---

## 📦 Exemplo Completo no Postman

### 1. Listar Todos os Investimentos
```
GET /investimentos
Authorization: Bearer SEU_TOKEN
```

### 2. Filtrar Ações de Alto Risco
```
GET /investimentos?categoria=ACAO&risco=ALTO
Authorization: Bearer SEU_TOKEN
```

### 3. Admin: Ocultar Investimento dos Usuários
```
PATCH /investimentos/2/toggle-visibilidade
Authorization: Bearer TOKEN_ADMIN
```

### 4. Favoritar uma Ação
```
POST /investimentos/favoritar/2/1
Authorization: Bearer SEU_TOKEN
```

### 5. Ver Favoritos
```
GET /investimentos/favoritos/1
Authorization: Bearer SEU_TOKEN
```

---

## 🔍 Diferenças entre Roles

### Usuário Comum:
- ✅ Vê apenas investimentos visíveis (`visivelParaUsuarios=true`)
- ✅ Pode favoritar investimentos
- ❌ Não pode criar, editar ou deletar
- ❌ Não pode controlar visibilidade

### Admin:
- ✅ Vê TODOS os investimentos (visíveis e ocultos)
- ✅ Pode usar filtro `visivel=false`
- ✅ Pode criar, editar, deletar investimentos
- ✅ Pode controlar visibilidade para usuários
- ✅ Pode ativar/desativar investimentos

---

## ❌ Tratamento de Erros

### Erros Comuns:

#### 404 - Investimento Não Encontrado
```json
{
  "timestamp": "2025-10-02T22:10:22.491",
  "erro": "Investimento não encontrado",
  "status": 404
}
```

#### 403 - Investimento Oculto
```json
{
  "timestamp": "2025-10-02T22:10:22.491",
  "erro": "Investimento não disponível para visualização",
  "status": 403
}
```