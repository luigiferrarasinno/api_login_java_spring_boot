# 🌳 Sistema de Árvore de Comentários (Respostas)

## 📝 Descrição
O sistema de comentários agora suporta **árvore de respostas** (comentários hierárquicos), similar ao TikTok, Instagram e outras redes sociais. Usuários podem comentar em investimentos e também **responder a comentários de outros usuários**.

---

## 🔑 Principais Recursos

### ✅ Comentários Raiz
- Comentários diretos sobre um investimento
- Não possuem `comentarioPaiId`
- Podem ter múltiplas respostas

### ✅ Respostas (Comentários Filhos)
- Respostas a outros comentários
- Possuem `comentarioPaiId` apontando para o comentário pai
- Podem ter suas próprias respostas (árvore completa)

### ✅ Estrutura Hierárquica
- Suporte completo para múltiplos níveis de respostas
- Contador automático de respostas em cada comentário
- Validações para manter integridade da árvore

---

## 🛠️ Endpoints

### 1️⃣ Criar Comentário ou Resposta
**POST** `/comentarios`

#### Criar Comentário Raiz
```json
{
  "conteudo": "Excelente investimento em renda fixa!",
  "investimentoId": 1
}
```

#### Criar Resposta a um Comentário
```json
{
  "conteudo": "Concordo! Também investi nisso.",
  "investimentoId": 1,
  "comentarioPaiId": 5
}
```

**Autenticação:** Requerida  
**Resposta de Sucesso:**
```json
{
  "mensagem": "Resposta criada com sucesso!",
  "comentario": {
    "id": 10,
    "conteudo": "Concordo! Também investi nisso.",
    "usuarioId": 2,
    "nomeUsuario": "Maria Silva",
    "emailUsuario": "maria@email.com",
    "investimentoId": 1,
    "nomeInvestimento": "Tesouro Selic 2025",
    "simboloInvestimento": "TESOURO_SELIC",
    "dataCriacao": "07/10/2025 14:30",
    "dataAtualizacao": null,
    "editado": false,
    "ativo": true,
    "comentarioPaiId": 5,
    "numeroRespostas": 0,
    "respostas": []
  },
  "timestamp": "2025-10-07T14:30:00"
}
```

---

### 2️⃣ Buscar Comentários de um Investimento (com Árvore)
**GET** `/comentarios/investimento/{investimentoId}`

**Exemplo:** `/comentarios/investimento/1`

**Autenticação:** Não requerida (público)

**Resposta:**
```json
{
  "investimentoId": 1,
  "totalComentarios": 3,
  "comentarios": [
    {
      "id": 1,
      "conteudo": "Ótimo investimento!",
      "usuarioId": 1,
      "nomeUsuario": "João",
      "emailUsuario": "joao@email.com",
      "investimentoId": 1,
      "nomeInvestimento": "Tesouro Selic 2025",
      "simboloInvestimento": "TESOURO_SELIC",
      "dataCriacao": "01/10/2025 10:00",
      "dataAtualizacao": null,
      "editado": false,
      "ativo": true,
      "comentarioPaiId": null,
      "numeroRespostas": 2,
      "respostas": [
        {
          "id": 5,
          "conteudo": "Também acho!",
          "usuarioId": 2,
          "nomeUsuario": "Maria",
          "emailUsuario": "maria@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "02/10/2025 11:00",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 1,
          "numeroRespostas": 1,
          "respostas": [
            {
              "id": 10,
              "conteudo": "Concordo com vocês!",
              "usuarioId": 3,
              "nomeUsuario": "Pedro",
              "emailUsuario": "pedro@email.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "03/10/2025 12:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 5,
              "numeroRespostas": 0,
              "respostas": []
            }
          ]
        }
      ]
    }
  ]
}
```

**📌 Observação:** Este endpoint retorna apenas comentários raiz com todas as suas respostas aninhadas (árvore completa).

---

### 3️⃣ Buscar Respostas de um Comentário Específico
**GET** `/comentarios/{comentarioId}/respostas`

**Exemplo:** `/comentarios/5/respostas`

**Autenticação:** Não requerida (público)

**Resposta:**
```json
{
  "comentarioPaiId": 5,
  "totalRespostas": 2,
  "respostas": [
    {
      "id": 10,
      "conteudo": "Ótima análise!",
      "usuarioId": 3,
      "nomeUsuario": "Pedro",
      "emailUsuario": "pedro@email.com",
      "investimentoId": 1,
      "nomeInvestimento": "Tesouro Selic 2025",
      "simboloInvestimento": "TESOURO_SELIC",
      "dataCriacao": "03/10/2025 12:00",
      "dataAtualizacao": null,
      "editado": false,
      "ativo": true,
      "comentarioPaiId": 5,
      "numeroRespostas": 0,
      "respostas": []
    }
  ]
}
```

---

### 4️⃣ Editar Comentário ou Resposta
**PUT** `/comentarios/{comentarioId}`

```json
{
  "conteudo": "Conteúdo atualizado do comentário"
}
```

**Autenticação:** Requerida  
**Permissões:**
- ✅ Usuário pode editar apenas seus próprios comentários/respostas
- ✅ Admin pode editar qualquer comentário/resposta

---

### 5️⃣ Excluir Comentário ou Resposta
**DELETE** `/comentarios/{comentarioId}`

**Autenticação:** Requerida  
**Permissões:**
- ✅ Usuário pode excluir apenas seus próprios comentários/respostas
- ✅ Admin pode excluir qualquer comentário/resposta

**📌 Nota:** Exclusão é do tipo "soft delete" (comentário permanece no banco mas `ativo = false`)

---

## 🎯 Validações Implementadas

### ✅ Ao Criar Resposta
1. **Comentário pai deve existir**
   - Se `comentarioPaiId` não existe, retorna erro 404

2. **Comentário pai deve estar ativo**
   - Não é possível responder a comentários excluídos/inativos

3. **Mesmo investimento**
   - Resposta deve ser do mesmo investimento que o comentário pai

4. **Autenticação obrigatória**
   - Apenas usuários autenticados podem comentar/responder

---

## 📊 Campos Importantes

### ComentarioDTO
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | ID do comentário |
| `conteudo` | String | Texto do comentário (1-1000 caracteres) |
| `comentarioPaiId` | Long | ID do comentário pai (null se for raiz) |
| `numeroRespostas` | int | Quantidade de respostas diretas |
| `respostas` | List | Lista de respostas aninhadas (árvore) |

---

## 🔐 Permissões

| Ação | Usuário Normal | Admin |
|------|----------------|-------|
| Criar comentário/resposta | ✅ | ✅ |
| Ver comentários públicos | ✅ | ✅ |
| Editar próprio comentário | ✅ | ✅ |
| Editar comentário de outros | ❌ | ✅ |
| Excluir próprio comentário | ✅ | ✅ |
| Excluir comentário de outros | ❌ | ✅ |
| Ver comentários inativos | ❌ | ✅ |

---

## 💡 Exemplos de Uso

### Fluxo Completo de Conversa

```
Investimento: Tesouro Selic 2025

└─ 📝 João: "Excelente investimento para iniciantes!" [ID: 1]
   │
   ├─ 💬 Maria: "Concordo! Comecei com esse." [ID: 5, pai: 1]
   │  │
   │  └─ 💬 Pedro: "Eu também! Qual a rentabilidade de vocês?" [ID: 10, pai: 5]
   │     │
   │     └─ 💬 Maria: "Estou com 10% ao ano." [ID: 15, pai: 10]
   │
   └─ 💬 Ana: "Melhor que poupança, com certeza." [ID: 6, pai: 1]

└─ 📝 Carlos: "Alguém sabe sobre o prazo de resgate?" [ID: 2]
   │
   └─ 💬 Admin: "É de D+0! Cai na conta no mesmo dia." [ID: 7, pai: 2]
```

### Exemplo de Código Frontend (JavaScript)

```javascript
// Criar comentário raiz
async function criarComentario(investimentoId, conteudo) {
  const response = await fetch('/comentarios', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      conteudo: conteudo,
      investimentoId: investimentoId
    })
  });
  return await response.json();
}

// Responder a um comentário
async function responderComentario(investimentoId, comentarioPaiId, conteudo) {
  const response = await fetch('/comentarios', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      conteudo: conteudo,
      investimentoId: investimentoId,
      comentarioPaiId: comentarioPaiId
    })
  });
  return await response.json();
}

// Buscar comentários com árvore
async function buscarComentariosComRespostas(investimentoId) {
  const response = await fetch(`/comentarios/investimento/${investimentoId}`);
  const data = await response.json();
  
  // data.comentarios já vem com a árvore completa de respostas
  renderizarArvoreComentarios(data.comentarios);
}

// Renderizar árvore recursivamente
function renderizarArvoreComentarios(comentarios, nivel = 0) {
  comentarios.forEach(comentario => {
    console.log(`${'  '.repeat(nivel)}${comentario.nomeUsuario}: ${comentario.conteudo}`);
    
    // Renderizar respostas recursivamente
    if (comentario.respostas && comentario.respostas.length > 0) {
      renderizarArvoreComentarios(comentario.respostas, nivel + 1);
    }
  });
}
```

---

## 🗃️ Estrutura do Banco de Dados

### Tabela: `comentarios`
```sql
CREATE TABLE comentarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  conteudo VARCHAR(1000) NOT NULL,
  usuario_id BIGINT NOT NULL,
  investimento_id BIGINT NOT NULL,
  comentario_pai_id BIGINT NULL,           -- NOVO: Referência ao comentário pai
  data_criacao TIMESTAMP NOT NULL,
  data_atualizacao TIMESTAMP,
  editado BOOLEAN DEFAULT FALSE,
  ativo BOOLEAN DEFAULT TRUE,
  
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  FOREIGN KEY (investimento_id) REFERENCES investimentos(id),
  FOREIGN KEY (comentario_pai_id) REFERENCES comentarios(id) ON DELETE CASCADE
);
```

**📌 Nota:** A coluna `comentario_pai_id` permite a árvore de comentários. Se for `NULL`, é um comentário raiz.

---

## ✅ Checklist de Implementação

- [x] Modelo `Comentario` com relacionamento `@ManyToOne` para `comentarioPai`
- [x] Modelo `Comentario` com relacionamento `@OneToMany` para `respostas`
- [x] DTO `ComentarioDTO` com campos `comentarioPaiId`, `numeroRespostas` e `respostas`
- [x] DTO `CriarComentarioDTO` com campo `comentarioPaiId`
- [x] Service com método `criarComentario` que aceita `comentarioPaiId`
- [x] Service com método `buscarComentariosRaizPorInvestimento` (apenas raiz)
- [x] Service com método `buscarRespostasDoComentario`
- [x] Controller com endpoint para criar comentário/resposta
- [x] Controller com endpoint para buscar comentários raiz com árvore
- [x] Controller com endpoint para buscar respostas de um comentário
- [x] Validações de integridade (comentário pai ativo, mesmo investimento, etc.)
- [x] Métodos auxiliares na entidade (`isResposta()`, `getNumeroRespostas()`, etc.)

---

## 🚀 Melhorias Futuras (Sugestões)

- [ ] Paginação para comentários e respostas
- [ ] Sistema de curtidas/reações em comentários
- [ ] Notificações quando alguém responde seu comentário
- [ ] Marcar comentários como "Melhor resposta"
- [ ] Filtros por data, popularidade, etc.
- [ ] Limitador de profundidade da árvore (ex: máximo 5 níveis)
- [ ] Menções de usuários com `@usuario`

---

## 📞 Suporte

Para dúvidas ou problemas, consulte:
- README.md do projeto
- Documentação Swagger em `/swagger-ui.html`
- Outros guias em `/guias-de-uso/`
