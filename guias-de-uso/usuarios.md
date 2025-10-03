# 👥 Guia de Usuários - Endpoints

Este guia cobre todos os endpoints relacionados ao gerenciamento de usuários.

---

## 🔐 Autenticação

Todos os endpoints protegidos requerem um token JWT no cabeçalho:
```
Authorization: Bearer SEU_TOKEN_JWT
```
## 🧑‍💻 Usuários de Teste Criados Automaticamente

> ⚠️ **3 personas criadas automaticamente pelo sistema:**

| Persona | Nome | Email | Senha | Role | CPF | Perfil |
|---------|------|-------|--------|------|-----|--------|
| **👨‍💼 Admin Sistema** | Admin Sistema | `admin@admin.com` | `123456` | ADMIN | 11111111111 | Arrojado |
| **👤 João Silva** | João Silva | `user@user.com` | `123456` | USER | 22222222222 | Moderado |
| **👩‍💼 Maria Investidora** | Maria Investidora | `maria@investidora.com` | `123456` | USER | 33333333333 | Conservador |

> 💡 **Todas as personas têm a mesma senha: `123456` para facilitar os testes**
---

## 🧠 Regras de Permissão

| Endpoint                       | USER        | ADMIN       |
| ------------------------------ | ----------- | ----------- |
| `/usuarios/criar`              | ❌           | ✅           |
| `/usuarios/login`              | ✅ (público) | ✅ (público) |
| `/usuarios/alterar-senha`      | ✅ (próprio) | ✅           |
| `/usuarios/criar-senha`        | ✅ (público) | ✅ (público) |
| `/usuarios/{id}` (DELETE)      | ✅ (próprio) | ✅           |
| `/usuarios` (GET)              | ❌           | ✅           |
| `/usuarios/{id}` (GET)         | ✅ (próprio) | ✅           |
| `/usuarios/{id}` (PATCH)       | ✅ (próprio) | ✅           |
| `/usuarios/trocar-email` (PUT) | ❌           | ✅           |

---

## 📋 Endpoints

### 1. Login
**POST** `/usuarios/login`  
**Acesso**: Público

#### Requisições de Exemplo:

**Login como Admin:**
```json
{
  "email": "admin@admin.com",
  "senha": "123456"
}
```

**Login como João Silva (USER):**
```json
{
  "email": "user@user.com", 
  "senha": "123456"
}
```

**Login como Maria Investidora (USER):**
```json
{
  "email": "maria@investidora.com",
  "senha": "123456"
}
```

#### Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "nomeUsuario": "Admin Sistema",
    "email": "admin@admin.com",
    "role": "ROLE_ADMIN"
  }
}
```

---

### 2. Criar Conta
**POST** `/usuarios/criar`  
**Acesso**: Apenas ADMIN

#### Requisição:
```json
{
  "nomeUsuario": "Novo Usuário",
  "senha": "123456",
  "email": "novo@usuario.com",
  "cpf": 44444444444,
  "dt_nascimento": "2000-05-15"
}
```

#### Resposta:
- **201 Created**: Usuário criado com sucesso
- **400 Bad Request**: Dados inválidos ou usuário já existe

---

### 3. Listar Usuários com Filtros
**GET** `/usuarios`  
**Acesso**: Apenas ADMIN

#### Parâmetros de Filtro (todos opcionais):
- `nomeUsuario` - Busca parcial no nome (case-insensitive)
- `email` - Busca parcial no email (case-insensitive)
- `cpf` - Busca parcial no CPF
- `role` - Filtro por role (ROLE_ADMIN, ROLE_USER)
- `userIsActive` - Filtro por status ativo (true/false)

#### Exemplos de Uso:

**Buscar usuários com nome "João":**
```
GET /usuarios?nomeUsuario=joão
```

**Buscar apenas admins ativos:**
```
GET /usuarios?role=ROLE_ADMIN&userIsActive=true
```

**Buscar por CPF parcial:**
```
GET /usuarios?cpf=123456
```

**Combinar múltiplos filtros:**
```
GET /usuarios?nomeUsuario=silva&email=gmail&role=ROLE_USER
```

#### Resposta:
```json
[
  {
    "id": 1,
    "nomeUsuario": "admin",
    "email": "admin@admin.com",
    "role": "ROLE_ADMIN",
    "cpf": 12345678900,
    "userIsActive": true,
    "dt_nascimento": "1990-01-01"
  }
]
```

---

### 4. Buscar Usuário por ID
**GET** `/usuarios/{id}`  
**Acesso**: Próprio usuário ou ADMIN

#### Resposta:
```json
{
  "id": 2,
  "nomeUsuario": "João Silva",
  "email": "joao@exemplo.com",
  "role": "ROLE_USER",
  "cpf": 88888888888,
  "dt_nascimento": "2000-05-15",
  "tipo_de_investidor": "nenhum por enquanto",
  "userIsActive": true,
  "user_permissions": "nenhuma por enquanto",
  "saldoCarteira": 10000.00
}
```

---

### 5. Alterar Senha
**PUT** `/usuarios/alterar-senha`  
**Acesso**: Próprio usuário ou ADMIN

#### Requisição:
```json
{
  "email": "joao@exemplo.com",
  "senhaAntiga": "123456",
  "senhaNova": "novaSenha456"
}
```

---

### 6. Criar/Redefinir Senha
**PUT** `/usuarios/criar-senha`  
**Acesso**: Público

#### Requisição:
```json
{
  "cpf": 88888888888,
  "email": "joao@exemplo.com",
  "dt_nascimento": "2000-05-15",
  "senhaNova": "minhaNovaSenhaSegura"
}
```

---

### 7. Trocar Email
**PUT** `/usuarios/trocar-email`  
**Acesso**: Apenas ADMIN

#### Requisição:
```json
{
  "cpf": 88888888888,
  "novoEmail": "novo.email@email.com"
}
```

---

### 8. Alternar Status de Atividade
**PATCH** `/usuarios/{id}`  
**Acesso**: Próprio usuário ou ADMIN

#### Resposta:
```json
{
  "mensagem": "Status de atividade atualizado com sucesso!",
  "ativo": false
}
```

---

### 9. Deletar Usuário
**DELETE** `/usuarios/{id}`  
**Acesso**: Próprio usuário ou ADMIN

#### Resposta:
- **200 OK**: Usuário deletado com sucesso
- **403 Forbidden**: Sem permissão para deletar

---

## 📦 Exemplo Completo no Postman

### 1. Login como Admin
```
POST /usuarios/login
Content-Type: application/json

{
  "email": "admin@admin.com",
  "senha": "123456"
}
```

### 2. Copiar o Token da Resposta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Criar Novo Usuário (com token)
```
POST /usuarios/criar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nomeUsuario": "Novo Usuário",
  "senha": "novasenha123",
  "email": "novo.usuario@email.com",
  "cpf": 11111111111,
  "dt_nascimento": "1995-03-10"
}
```

### 4. Listar Usuários com Filtros
```
GET /usuarios?nomeUsuario=usuario&userIsActive=true
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## ❌ Tratamento de Erros

### Erros Comuns:

#### 401 - Não Autorizado
```json
{
  "timestamp": "2025-10-02T22:10:22.491",
  "erro": "Token JWT inválido ou expirado",
  "status": 401
}
```

#### 403 - Acesso Negado
```json
{
  "timestamp": "2025-10-02T22:10:22.491",
  "erro": "Acesso negado para este recurso",
  "status": 403
}
```

#### 400 - Dados Inválidos
```json
{
  "timestamp": "2025-10-02T22:10:22.491",
  "erro": "Email já cadastrado",
  "status": 400
}
```