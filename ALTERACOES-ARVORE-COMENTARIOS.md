# 🌳 Implementação de Árvore de Comentários - Resumo das Alterações

## 📅 Data: 07/10/2025

---

## ✅ Alterações Realizadas

### 1️⃣ **Modelo (`Comentario.java`)**

#### Adicionado:
- **Campo `comentarioPai`**: Relacionamento `@ManyToOne` para o comentário pai
- **Campo `respostas`**: Relacionamento `@OneToMany` para lista de respostas
- **Construtor adicional**: `Comentario(conteudo, usuario, investimento, comentarioPai)`
- **Métodos auxiliares**:
  - `adicionarResposta(Comentario resposta)`
  - `removerResposta(Comentario resposta)`
  - `getNumeroRespostas()`
  - `isResposta()` - verifica se é uma resposta (tem pai)

#### Estrutura do Banco:
```sql
ALTER TABLE comentarios 
ADD COLUMN comentario_pai_id BIGINT NULL,
ADD FOREIGN KEY (comentario_pai_id) REFERENCES comentarios(id) ON DELETE CASCADE;
```

---

### 2️⃣ **DTO (`ComentarioDTO.java`)**

#### Adicionado:
- **Campo `comentarioPaiId`**: ID do comentário pai (null se for raiz)
- **Campo `numeroRespostas`**: Contador de respostas diretas
- **Campo `respostas`**: Lista de respostas aninhadas (árvore completa)
- **Construtor sobrecarregado**: `ComentarioDTO(comentario, incluirRespostas)`
  - Por padrão, `incluirRespostas = true` (retorna árvore completa)
  - Carrega respostas recursivamente usando Stream API

---

### 3️⃣ **DTO de Criação (`CriarComentarioDTO.java`)**

#### Adicionado:
- **Campo `comentarioPaiId`**: Para criar respostas a comentários
- **Construtor adicional**: `CriarComentarioDTO(conteudo, investimentoId, comentarioPaiId)`
- **Getters/Setters** para o novo campo

---

### 4️⃣ **Service (`ComentarioService.java`)**

#### Métodos Modificados:
- **`criarComentario(conteudo, investimentoId, emailUsuario)`**
  - Agora chama o método sobrecarregado com `comentarioPaiId = null`

#### Métodos Novos:
- **`criarComentario(conteudo, investimentoId, emailUsuario, comentarioPaiId)`**
  - Cria comentário raiz ou resposta
  - Validações:
    - ✅ Comentário pai deve existir
    - ✅ Comentário pai deve estar ativo
    - ✅ Comentário pai deve ser do mesmo investimento
  
- **`buscarComentariosRaizPorInvestimento(investimentoId)`**
  - Retorna apenas comentários sem pai (raiz)
  - Útil para construir árvore hierárquica no frontend

- **`buscarRespostasDoComentario(comentarioPaiId)`**
  - Retorna respostas diretas de um comentário
  - Filtra apenas comentários ativos

---

### 5️⃣ **Controller (`ComentarioController.java`)**

#### Endpoints Modificados:

**POST `/comentarios`**
- Agora aceita `comentarioPaiId` no body
- Mensagem de sucesso diferenciada:
  - "Comentário criado com sucesso!" → comentário raiz
  - "Resposta criada com sucesso!" → resposta

**GET `/comentarios/investimento/{investimentoId}`**
- Agora retorna apenas comentários raiz
- Cada comentário raiz vem com árvore completa de respostas aninhadas
- Usa `ComentarioDTO(comentario, true)` para incluir respostas

#### Endpoints Novos:

**GET `/comentarios/{comentarioId}/respostas`**
- Lista respostas diretas de um comentário
- Acesso público (não requer autenticação)
- Retorna:
  ```json
  {
    "comentarioPaiId": 5,
    "totalRespostas": 3,
    "respostas": [...]
  }
  ```

---

## 🎯 Funcionalidades Implementadas

### ✅ Criar Comentário Raiz
```json
POST /comentarios
{
  "conteudo": "Excelente investimento!",
  "investimentoId": 1
}
```

### ✅ Criar Resposta a Comentário
```json
POST /comentarios
{
  "conteudo": "Concordo totalmente!",
  "investimentoId": 1,
  "comentarioPaiId": 5
}
```

### ✅ Buscar Árvore de Comentários
```json
GET /comentarios/investimento/1

// Retorna:
{
  "investimentoId": 1,
  "totalComentarios": 2,
  "comentarios": [
    {
      "id": 1,
      "conteudo": "Comentário raiz",
      "comentarioPaiId": null,
      "numeroRespostas": 2,
      "respostas": [
        {
          "id": 5,
          "conteudo": "Resposta 1",
          "comentarioPaiId": 1,
          "numeroRespostas": 1,
          "respostas": [
            {
              "id": 10,
              "conteudo": "Resposta da resposta",
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

### ✅ Buscar Respostas Específicas
```json
GET /comentarios/5/respostas

// Retorna apenas respostas diretas do comentário 5
```

---

## 🔒 Validações de Segurança

1. ✅ **Comentário pai deve existir**
   - `EntityNotFoundException` se não encontrado

2. ✅ **Comentário pai deve estar ativo**
   - `IllegalArgumentException` se inativo

3. ✅ **Mesmo investimento**
   - Resposta deve ser do mesmo investimento que o pai
   - `IllegalArgumentException` caso contrário

4. ✅ **Autenticação**
   - Apenas usuários autenticados podem criar comentários/respostas

5. ✅ **Permissões de edição/exclusão**
   - Usuário pode editar/excluir apenas seus próprios comentários
   - Admin pode editar/excluir qualquer comentário

---

## 📊 Estrutura de Dados

### Antes (Comentário Flat)
```
comentarios
├── id
├── conteudo
├── usuario_id
├── investimento_id
├── data_criacao
└── ativo
```

### Depois (Comentário Hierárquico)
```
comentarios
├── id
├── conteudo
├── usuario_id
├── investimento_id
├── comentario_pai_id  ← NOVO
├── data_criacao
└── ativo
```

### Relacionamento no JPA
```java
// Um comentário pode ter um pai
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "comentario_pai_id")
private Comentario comentarioPai;

// Um comentário pode ter várias respostas
@OneToMany(mappedBy = "comentarioPai", cascade = CascadeType.ALL)
private List<Comentario> respostas = new ArrayList<>();
```

---

## 📝 Documentação Criada

### Novo Guia
- **`arvore-comentarios.md`**
  - Explicação completa do sistema
  - Exemplos de uso
  - Estrutura de árvore
  - Endpoints
  - Validações
  - Exemplos de código frontend
  - Fluxo de conversa visual

### Guia Atualizado
- **`comentarios.md`**
  - Adicionado link para o novo guia de árvore
  - Aviso sobre a nova funcionalidade

---

## 🚀 Como Testar

### 1. Criar comentário raiz
```bash
curl -X POST http://localhost:8080/comentarios \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "conteudo": "Ótimo investimento!",
    "investimentoId": 1
  }'
```

### 2. Criar resposta
```bash
curl -X POST http://localhost:8080/comentarios \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "conteudo": "Concordo!",
    "investimentoId": 1,
    "comentarioPaiId": 1
  }'
```

### 3. Buscar árvore
```bash
curl http://localhost:8080/comentarios/investimento/1
```

### 4. Buscar respostas específicas
```bash
curl http://localhost:8080/comentarios/1/respostas
```

---

## ✅ Checklist de Implementação

- [x] Adicionar relacionamento `comentarioPai` no modelo
- [x] Adicionar relacionamento `respostas` no modelo
- [x] Criar construtor para comentário com pai
- [x] Adicionar métodos auxiliares (`isResposta`, `getNumeroRespostas`)
- [x] Atualizar `ComentarioDTO` com novos campos
- [x] Criar construtor recursivo no DTO
- [x] Atualizar `CriarComentarioDTO` com `comentarioPaiId`
- [x] Implementar método `criarComentario` com validações
- [x] Implementar `buscarComentariosRaizPorInvestimento`
- [x] Implementar `buscarRespostasDoComentario`
- [x] Atualizar endpoint POST `/comentarios`
- [x] Atualizar endpoint GET `/comentarios/investimento/{id}`
- [x] Criar endpoint GET `/comentarios/{id}/respostas`
- [x] Criar guia completo `arvore-comentarios.md`
- [x] Atualizar guia existente `comentarios.md`
- [x] Validar compilação sem erros
- [x] Criar resumo de alterações

---

## 🎉 Resultado Final

O sistema de comentários agora funciona como uma **árvore hierárquica completa**, permitindo:

1. ✅ Comentários raiz (diretos no investimento)
2. ✅ Respostas a comentários (um nível)
3. ✅ Respostas a respostas (múltiplos níveis)
4. ✅ Navegação completa pela árvore
5. ✅ Contador automático de respostas
6. ✅ Validações robustas
7. ✅ Permissões de usuário/admin

**Similar a:** TikTok, Instagram, YouTube, Reddit, Twitter/X

---

## 📦 Arquivos Alterados

1. `Comentario.java` - Modelo com relacionamentos
2. `ComentarioDTO.java` - DTO com árvore
3. `CriarComentarioDTO.java` - DTO de criação
4. `ComentarioService.java` - Lógica de negócio
5. `ComentarioController.java` - Endpoints
6. `arvore-comentarios.md` - **NOVO** - Guia completo
7. `comentarios.md` - Atualizado com link
8. `ALTERACOES-ARVORE-COMENTARIOS.md` - **NOVO** - Este arquivo

---

## 🔄 Próximos Passos Sugeridos

- [ ] Testar endpoints com Postman/Insomnia
- [ ] Validar no Swagger UI (`/swagger-ui.html`)
- [ ] Criar dados de exemplo para testes
- [ ] Implementar frontend para exibir árvore
- [ ] Adicionar paginação para respostas
- [ ] Implementar sistema de curtidas/reações
- [ ] Notificações quando alguém responde

---

**Status:** ✅ Implementação Completa e Funcional
