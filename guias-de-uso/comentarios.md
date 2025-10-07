# 💬 Guia de Comentários - Endpoints

Este guia cobre todos os endpoints relacionados ao sistema de comentários dos investimentos.

> 🌳 **NOVO:** Sistema de Árvore de Comentários (Respostas)!  
> Agora é possível responder comentários criando uma árvore hierárquica, similar ao TikTok.  
> **📖 Guia completo:** [`arvore-comentarios.md`](./arvore-comentarios.md)

---

## 🔐 Autenticação

Todos os endpoints requerem um token JWT no cabeçalho:
```
Authorization: Bearer SEU_TOKEN_JWT
```

---

## 🧠 Sistema de Permissões

| Endpoint                                | USER                    | ADMIN           |
| --------------------------------------- | ----------------------- | --------------- |
| `GET /comentarios/investimento/{id}`    | ✅ (comentários ativos)  | ✅ (todos)       |
| `POST /comentarios`                     | ✅                      | ✅               |
| `PUT /comentarios/{id}`                 | ✅ (próprios)           | ✅ (todos)       |
| `DELETE /comentarios/{id}`              | ✅ (próprios)           | ✅ (todos)       |
| `GET /comentarios/meus/{usuarioId}`     | ✅ (próprios)           | ✅ (todos)       |
| `GET /comentarios`                      | ❌                      | ✅               |
| `GET /comentarios/ativo/{id}`           | ✅                      | ✅               |
| `PATCH /comentarios/{id}/toggle-ativo` | ❌                      | ✅               |

---

## � Comentários Criados Automaticamente

> ⚠️ **7 comentários de exemplo criados automaticamente:**

### 📈 **Comentários sobre PETR4:**
- **Admin Sistema**: "Excelente oportunidade com os preços atuais do petróleo! 📈"
- **João Silva**: "Boa para dividendos, mas atenção à volatilidade do setor."
- **Maria Investidora**: "Prefiro manter uma posição pequena, muito risco geopolítico."

### ⛏️ **Comentários sobre VALE3:**
- **Maria Investidora**: "A demanda por minério está forte, especialmente da China."
- **Admin Sistema**: "ESG melhorou muito, mas ainda há trabalho a fazer."

### 🏢 **Comentários sobre HGLG11 (FII):**
- **João Silva**: "Meu primeiro FII! Dividendos mensais são ótimos 💰"
- **Maria Investidora**: "Shopping centers estão se recuperando bem pós-pandemia."

> 💡 **Comentários têm datas aleatórias dos últimos 30 dias para simular atividade real**

---

## �💡 Sistema de Soft Delete

- **Comentários deletados** não são removidos do banco de dados
- São marcados com `ativo=false`
- Usuários comuns não veem comentários inativos
- Admins podem reativar comentários deletados

---

## 📋 Endpoints

### 1. Listar Comentários de um Investimento
**GET** `/comentarios/investimento/{investimentoId}`  
**Acesso**: Usuários autenticados

#### Comportamento:
- **Usuários**: Veem apenas comentários ativos
- **Admins**: Veem todos (ativos e inativos)

#### Exemplo:
```
GET /comentarios/investimento/1
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
[
  {
    "id": 1,
    "conteudo": "Ótima oportunidade de investimento!",
    "dataComentario": "2024-10-02T14:30:15",
    "dataUltimaEdicao": null,
    "ativo": true,
    "investimento": {
      "id": 1,
      "nome": "Tesouro Direto",
      "simbolo": "TD"
    },
    "usuario": {
      "id": 2,
      "nomeUsuario": "joao.silva",
      "email": "joao@email.com"
    }
  },
  {
    "id": 2,
    "conteudo": "Rendimentos consistentes, recomendo.",
    "dataComentario": "2024-10-02T15:45:22",
    "dataUltimaEdicao": "2024-10-02T16:00:30",
    "ativo": true,
    "investimento": {
      "id": 1,
      "nome": "Tesouro Direto",
      "simbolo": "TD"
    },
    "usuario": {
      "id": 3,
      "nomeUsuario": "maria.santos",
      "email": "maria@email.com"
    }
  }
]
```

---

### 2. Criar Comentário
**POST** `/comentarios`  
**Acesso**: Usuários autenticados

#### Requisição:
```json
{
  "conteudo": "Este investimento tem bons resultados históricos.",
  "investimentoId": 1,
  "usuarioId": 2
}
```

#### Validações:
- **Conteudo**: Obrigatório, máximo 1000 caracteres
- **InvestimentoId**: Deve existir e estar ativo
- **UsuarioId**: Deve existir e estar ativo
- **Permissão**: Usuário só pode comentar em nome próprio

#### Resposta:
```json
{
  "id": 3,
  "conteudo": "Este investimento tem bons resultados históricos.",
  "dataComentario": "2024-10-02T16:20:45",
  "dataUltimaEdicao": null,
  "ativo": true,
  "investimento": {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD"
  },
  "usuario": {
    "id": 2,
    "nomeUsuario": "joao.silva",
    "email": "joao@email.com"
  }
}
```

---

### 3. Editar Comentário
**PUT** `/comentarios/{comentarioId}`  
**Acesso**: Dono do comentário ou ADMIN

#### Requisição:
```json
{
  "conteudo": "Atualizando minha opinião: excelente investimento para longo prazo!"
}
```

#### Validações:
- **Conteudo**: Obrigatório, máximo 1000 caracteres
- **Comentário**: Deve existir e estar ativo
- **Permissão**: Apenas o dono ou admin pode editar

#### Resposta:
```json
{
  "id": 1,
  "conteudo": "Atualizando minha opinião: excelente investimento para longo prazo!",
  "dataComentario": "2024-10-02T14:30:15",
  "dataUltimaEdicao": "2024-10-02T17:10:20",
  "ativo": true,
  "investimento": {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD"
  },
  "usuario": {
    "id": 2,
    "nomeUsuario": "joao.silva",
    "email": "joao@email.com"
  }
}
```

---

### 4. Deletar Comentário (Soft Delete)
**DELETE** `/comentarios/{comentarioId}`  
**Acesso**: Dono do comentário ou ADMIN

#### Comportamento:
- Comentário é marcado como `ativo=false`
- Não é removido fisicamente do banco
- Admins podem reativar posteriormente

#### Resposta:
```json
{
  "message": "Comentário deletado com sucesso (soft delete)."
}
```

---

### 5. Listar Meus Comentários
**GET** `/comentarios/meus/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Comportamento:
- **Usuário**: Vê apenas seus próprios comentários ativos
- **Admin**: Pode ver comentários de qualquer usuário

#### Exemplo:
```
GET /comentarios/meus/2
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
[
  {
    "id": 1,
    "conteudo": "Atualizando minha opinião: excelente investimento para longo prazo!",
    "dataComentario": "2024-10-02T14:30:15",
    "dataUltimaEdicao": "2024-10-02T17:10:20",
    "ativo": true,
    "investimento": {
      "id": 1,
      "nome": "Tesouro Direto",
      "simbolo": "TD"
    }
  },
  {
    "id": 3,
    "conteudo": "Este investimento tem bons resultados históricos.",
    "dataComentario": "2024-10-02T16:20:45",
    "dataUltimaEdicao": null,
    "ativo": true,
    "investimento": {
      "id": 1,
      "nome": "Tesouro Direto",
      "simbolo": "TD"
    }
  }
]
```

---

### 6. Listar Todos os Comentários (Admin Only)
**GET** `/comentarios`  
**Acesso**: Apenas ADMIN

#### Comportamento:
- Lista TODOS os comentários (ativos e inativos)
- Útil para moderação e auditoria

#### Resposta:
```json
[
  {
    "id": 4,
    "conteudo": "Comentário deletado pelo usuário",
    "dataComentario": "2024-10-02T10:15:30",
    "dataUltimaEdicao": null,
    "ativo": false,
    "investimento": {
      "id": 2,
      "nome": "Ações Vale",
      "simbolo": "VALE3"
    },
    "usuario": {
      "id": 3,
      "nomeUsuario": "maria.santos",
      "email": "maria@email.com"
    }
  }
]
```

---

### 7. Buscar Comentário Específico
**GET** `/comentarios/ativo/{comentarioId}`  
**Acesso**: Usuários autenticados

#### Comportamento:
- Retorna apenas se o comentário estiver ativo
- Usado para verificar existência antes de operações

#### Resposta:
```json
{
  "id": 1,
  "conteudo": "Atualizando minha opinião: excelente investimento para longo prazo!",
  "dataComentario": "2024-10-02T14:30:15",
  "dataUltimaEdicao": "2024-10-02T17:10:20",
  "ativo": true,
  "investimento": {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD"
  },
  "usuario": {
    "id": 2,
    "nomeUsuario": "joao.silva",
    "email": "joao@email.com"
  }
}
```

---

### 8. Reativar Comentário (Admin Only)
**PATCH** `/comentarios/{comentarioId}/toggle-ativo`  
**Acesso**: Apenas ADMIN

#### Comportamento:
- Alterna o status `ativo` do comentário
- Permite recuperar comentários deletados

#### Resposta:
```json
{
  "id": 4,
  "ativo": true,
  "message": "Status do comentário alterado com sucesso."
}
```

---

## 🔍 Diferenças entre Roles

### Usuário Comum:
- ✅ Pode comentar em investimentos
- ✅ Pode editar apenas seus próprios comentários
- ✅ Pode deletar apenas seus próprios comentários
- ✅ Vê apenas comentários ativos
- ❌ Não pode moderar outros comentários

### Admin:
- ✅ Pode fazer tudo que usuário comum faz
- ✅ Pode editar qualquer comentário
- ✅ Pode deletar qualquer comentário
- ✅ Vê todos os comentários (ativos e inativos)
- ✅ Pode reativar comentários deletados
- ✅ Pode acessar endpoint de moderação

---

## 📦 Exemplos Completos no Postman

### 1. Usuário Comenta em uma Ação
```
POST /comentarios
Authorization: Bearer TOKEN_USER
Content-Type: application/json

{
  "conteudo": "VALE3 está com boa perspectiva para 2024!",
  "investimentoId": 2,
  "usuarioId": 2
}
```

### 2. Ver Comentários de um Investimento
```
GET /comentarios/investimento/2
Authorization: Bearer TOKEN_USER
```

### 3. Editar Próprio Comentário
```
PUT /comentarios/5
Authorization: Bearer TOKEN_USER
Content-Type: application/json

{
  "conteudo": "Revisando: VALE3 precisa de análise mais cuidadosa."
}
```

### 4. Admin Modera Comentário
```
DELETE /comentarios/5
Authorization: Bearer TOKEN_ADMIN
```

### 5. Admin Reativa Comentário
```
PATCH /comentarios/5/toggle-ativo
Authorization: Bearer TOKEN_ADMIN
```

---

## 🎯 Casos de Uso no Frontend

### Página de Investimento:
1. **Carregar comentários**: `GET /comentarios/investimento/{id}`
2. **Novo comentário**: `POST /comentarios`
3. **Editar comentário**: Botão "Editar" → `PUT /comentarios/{id}`
4. **Deletar comentário**: Botão "Excluir" → `DELETE /comentarios/{id}`

### Painel do Usuário:
1. **Meus comentários**: `GET /comentarios/meus/{usuarioId}`
2. **Editar/Deletar**: Links diretos para cada comentário

### Painel Admin:
1. **Moderação**: `GET /comentarios` (todos os comentários)
2. **Reativar deletados**: `PATCH /comentarios/{id}/toggle-ativo`

---

## ⚠️ Regras de Negócio

### Validações:
- **Conteúdo**: 1 a 1000 caracteres
- **Investimento**: Deve existir e estar ativo
- **Usuário**: Deve existir e estar ativo
- **Autoria**: Usuários só podem comentar em nome próprio

### Auditoria:
- **dataComentario**: Timestamp de criação
- **dataUltimaEdicao**: Timestamp da última modificação
- **Soft Delete**: Comentários nunca são perdidos

### Moderação:
- Admins podem editar conteúdo de qualquer comentário
- Admins podem desativar/reativar comentários
- Histórico completo mantido para auditoria

---

## ❌ Tratamento de Erros

### Erros Comuns:

#### 404 - Comentário Não Encontrado
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Comentário não encontrado",
  "status": 404
}
```

#### 403 - Sem Permissão
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Você só pode editar seus próprios comentários",
  "status": 403
}
```

#### 400 - Conteúdo Inválido
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Conteúdo do comentário não pode estar vazio",
  "status": 400
}
```

#### 400 - Investimento Inativo
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Não é possível comentar em investimentos inativos",
  "status": 400
}
```