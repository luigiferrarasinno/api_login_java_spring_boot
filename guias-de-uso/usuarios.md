# 👥 Guia de## 🧑‍💻 Usuários de Teste Criados Automaticamente

> ⚠️ **3 personas criadas automaticamente pelo sistema:**

| Persona | Nome | Email | **CPF (Login)** | Senha | Role | Perfil |
|---------|------|-------|-----------------|--------|------|--------|
| **👨‍💼 Admin Sistema** | Admin Sistema | `admin@admin.com` | **11111111111** | `123456` | ADMIN | Arrojado |
| **👤 João Silva** | João Silva | `user@user.com` | **22222222222** | `123456` | USER | Moderado |
| **👩‍💼 Maria Investidora** | Maria Investidora | `maria@investidora.com` | **33333333333** | `123456` | USER | Conservador |

> 🔑 **IMPORTANTE: Login agora usa CPF + Senha (não mais email + senha)**  
> 💡 **Todas as personas têm a mesma senha: `123456` para facilitar os testes** Endpoints

Este guia cobre todos os endpoints relacionados ao gerenciamento de usuários.

---

## 🚨 Mudanças Recentes

### 🔄 Login Atualizado
- **ANTES**: Login com `email` + `senha`
- **AGORA**: Login com `cpf` + `senha`
- **Endpoints afetados**: `/usuarios/login` e `/usuarios/login-admin`

### 📋 Criação de Usuário Melhorada
- **ANTES**: Retornava apenas mensagem de sucesso
- **AGORA**: Retorna dados completos do usuário criado (sem senha)
- **Campos automáticos**: role, tipo, saldoCarteira, userIsActive

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

### 1. Login com CPF
**POST** `/usuarios/login`  
**Acesso**: Público  
**🔄 MUDANÇA: Agora usa CPF + Senha (não mais email)**

#### Requisições de Exemplo:

**Login como Admin:**
```json
{
  "cpf": "11111111111",
  "senha": "123456"
}
```

**Login como João Silva (USER):**
```json
{
  "cpf": "22222222222", 
  "senha": "123456"
}
```

**Login como Maria Investidora (USER):**
```json
{
  "cpf": "33333333333",
  "senha": "123456"
}
```

#### Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "1",
  "firstLogin": false
}
```

---

### 1.1. Login Exclusivo para Administradores
**POST** `/usuarios/login-admin`  
**Acesso**: Apenas usuários com ROLE_ADMIN

> ⚡ **Login restrito**: Usuários comuns recebem erro 401 se tentarem usar este endpoint

#### Requisição:
```json
{
  "cpf": "11111111111",
  "senha": "123456"
}
```

#### Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "adminId": "1",
  "nomeAdmin": "Admin Sistema",
  "role": "ROLE_ADMIN"
}
```

---

### 2. Obter Dados do Usuário Logado
**GET** `/usuarios/logged`  
**Acesso**: Usuário autenticado  
**🆕 NOVO: Retorna dados completos do usuário pelo token**

#### Como usar:
```
GET /usuarios/logged
Authorization: Bearer SEU_TOKEN_JWT
Content-Type: application/json
```

#### Resposta de Sucesso (200 OK):
```json
{
  "id": 2,
  "nomeUsuario": "João Silva",
  "email": "user@user.com",
  "role": "ROLE_USER",
  "cpf": 22222222222,
  "userIsActive": true,
  "dt_nascimento": "1990-05-15",
  "tipo": "PERFIL_MODERADO",
  "saldoCarteira": 25000.00
}
```

#### Possíveis Erros:
- **401 Unauthorized**: Token inválido, expirado ou ausente
- **404 Not Found**: Usuário não encontrado no sistema
- **500 Internal Server Error**: Erro interno do servidor

---

### 3. Criar Conta
**POST** `/usuarios/criar`  
**Acesso**: Apenas ADMIN  
**🆕 NOVO: Retorna dados completos do usuário criado (sem senha)**

#### Requisição Testada e Funcional:
```json
{
  "nomeUsuario": "João da Silva",
  "senha": "123456",
  "email": "joao.silva@email.com",
  "cpf": 12345678909,
  "dt_nascimento": "2006-05-20"
}
```

#### Resposta de Sucesso (200 OK):
```json
{
  "id": 4,
  "nomeUsuario": "João da Silva",
  "email": "joao.silva@email.com",
  "role": "ROLE_USER",
  "cpf": 12345678909,
  "userIsActive": true,
  "dt_nascimento": "2006-05-20",
  "tipo": "PERFIL_CONSERVADOR",
  "saldoCarteira": 1000.00
}
```

#### Campos Automáticos:
- **role**: `"ROLE_USER"` (padrão para novos usuários)
- **tipo**: `"PERFIL_CONSERVADOR"` (perfil de investimento padrão)
- **saldoCarteira**: `1000.00` (saldo inicial)
- **userIsActive**: `true` (conta ativa por padrão)

#### Possíveis Erros:
- **400 Bad Request**: Dados inválidos, email já existe, CPF inválido, menor de 18 anos

---

### 4. Listar Usuários com Filtros
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

### 5. Buscar Usuário por ID
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

### 6. Alterar Senha
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

### 7. Criar/Redefinir Senha
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

### 8. Trocar Email
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

### 9. Alternar Status de Atividade
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

### 10. Deletar Usuário
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
  "cpf": "11111111111",
  "senha": "123456"
}
```

### 2. Copiar o Token da Resposta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "1",
  "firstLogin": false
}
```

### 2.1. Obter Dados do Usuário Logado
```
GET /usuarios/logged
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 3. Criar Novo Usuário (com token)
```
POST /usuarios/criar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nomeUsuario": "João da Silva",
  "senha": "123456",
  "email": "joao.silva@email.com",
  "cpf": 12345678909,
  "dt_nascimento": "2006-05-20"
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

---

## 💡 Dicas Importantes para Testes

### ✅ JSON Testado e Funcional (Criar Usuário)
```json
{
  "nomeUsuario": "João da Silva",
  "senha": "123456",
  "email": "joao.silva@email.com",
  "cpf": 12345678909,
  "dt_nascimento": "2006-05-20"
}
```

### 🔑 CPFs para Login Rápido
- **Admin**: `11111111111` + senha `123456`
- **Usuário 1**: `22222222222` + senha `123456` 
- **Usuário 2**: `33333333333` + senha `123456`

### 📝 Validações Ativas
- ✅ CPF deve ser válido (algoritmo brasileiro)
- ✅ Email deve ter formato válido (@domain.com)
- ✅ Usuário deve ter +18 anos
- ✅ Email não pode estar cadastrado
- ✅ Senha não pode ser vazia

### 🎯 Resposta de Criação
O endpoint `/usuarios/criar` agora retorna:
```json
{
  "id": 4,
  "nomeUsuario": "Nome do Usuário",
  "email": "email@dominio.com",
  "role": "ROLE_USER",
  "cpf": 12345678909,
  "userIsActive": true,
  "dt_nascimento": "YYYY-MM-DD",
  "tipo": "PERFIL_CONSERVADOR",
  "saldoCarteira": 1000.00
}
```

### 🔍 Endpoint /logged
**Uso**: Obter dados do usuário autenticado
**Token**: Obrigatório no header Authorization
**Resposta**: Dados completos (sem senha)

```bash
# Exemplo de uso
curl -H "Authorization: Bearer SEU_TOKEN" http://localhost:8080/usuarios/logged
```