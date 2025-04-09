
# 🛡️ API de Autenticação com Spring Boot e JWT

Esta é uma API de autenticação e gerenciamento de usuários desenvolvida com **Java 17** e **Spring Boot 3.2.2**, utilizando autenticação via **JWT (JSON Web Token)**. A API possui controle de acesso baseado em **roles** (`USER` e `ADMIN`) e protege os endpoints adequadamente.

---

## 📁 Estrutura do Projeto

```bash
src
└── main
    └── java
        └── com.example.demo
            ├── security/              # Lógica de autenticação, JWT e segurança
            └── user/
                ├── controller/        # Endpoints da API
                ├── dao/               # Classe auxiliar para troca de senha
                ├── dto/               # Objetos de transferência de dados (entrada/saída)
                ├── exception/         # Tratamento global de erros e exceções personalizadas
                ├── init/              # Inicializador com criação do usuário admin
                ├── model/             # Entidades JPA (User e Role)
                ├── repository/        # Interfaces para acesso ao banco (UserRepository, RoleRepository)
                └── service/           # Lógica de negócio (cadastro, login, exclusão, etc.)
```

---

## 🔧 Camadas do Projeto

| Camada         | Função                                                                 |
|----------------|------------------------------------------------------------------------|
| `controller`   | Define os endpoints públicos e protegidos da API                       |
| `service`      | Contém as regras de negócio (ex: criação de conta, validações, etc.)   |
| `repository`   | Acesso ao banco de dados via Spring Data JPA                           |
| `dto`          | Objetos usados para entrada e saída de dados (evita expor entidades)   |
| `model`        | Entidades JPA que representam as tabelas no banco de dados             |
| `security`     | Configuração de autenticação e geração/validação de tokens JWT         |
| `exception`    | Gerencia erros com mensagens claras usando `@ControllerAdvice`         |
| `init`         | Cria automaticamente um usuário ADMIN no início da aplicação           |

---

## 🚀 Como rodar o projeto

### Pré-requisitos

- Java 17 instalado
- Maven instalado
- IDE de sua preferência (VS Code, IntelliJ...)

### Passos

```bash
git clone https://github.com/luigiferrarasinno/api_login_java_spring_boot.git
cd api_login_java_spring_boot
```

- Execute a aplicação com `./mvn spring-boot:run` ou através da sua IDE.
- A API sobe na porta padrão: `http://localhost:8080`

> ⚠️ Um usuário **ADMIN** será criado automaticamente:

```
Usuário: admin
Senha: admin123
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.2
- Spring Security
- JWT (JSON Web Token)
- H2 Database (em memória)
- Maven

---

## 🔐 Endpoints da API

### 1. Criar Conta

**POST** `/usuarios/criar`  
**Acesso**: Público

#### Requisição:

```json
{
  "nomeUsuario": "joao",
  "senha": "senha123"
}
```

#### Resposta:

- **201 Created**: Usuário criado com sucesso
- **400 Bad Request**: Usuário já existe ou dados inválidos

---

### 2. Login

**POST** `/usuarios/login`  
**Acesso**: Público

#### Requisição:

```json
{
  "nomeUsuario": "joao",
  "senha": "senha123"
}
```

#### Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

- **200 OK**: Token JWT válido
- **401 Unauthorized**: Credenciais inválidas

---

### 3. Alterar Senha

**PUT** `/usuarios/alterar-senha`  
**Acesso**: Protegido (USER ou ADMIN)

#### Requisição:

```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

#### Resposta:

- **200 OK**: Senha alterada com sucesso
- **400 Bad Request**: Senha atual incorreta

---

### 4. Deletar Usuário

**DELETE** `/usuarios/{id}`  
**Acesso**: 
- USER: pode deletar apenas sua própria conta  
- ADMIN: pode deletar qualquer usuário

#### Resposta:

- **200 OK**: Usuário deletado
- **403 Forbidden**: Tentativa de deletar outro usuário sem permissão

---

### 5. Listar Todos os Usuários

**GET** `/usuarios`  
**Acesso**: Apenas ADMIN

#### Resposta:

```json
[
  {
    "id": 1,
    "nomeUsuario": "admin"
  },
  {
    "id": 2,
    "nomeUsuario": "joao"
  }
]
```

---

### 6. Buscar Usuário por ID

**GET** `/usuarios/{id}`  
**Acesso**:
- USER: pode ver apenas seus próprios dados  
- ADMIN: pode ver qualquer usuário

#### Resposta:

```json
{
  "id": 2,
  "nomeUsuario": "joao"
}
```

---

## 🧠 Regras de Permissão

| Endpoint                   | USER           | ADMIN         |
|---------------------------|----------------|---------------|
| `/usuarios/criar`         | ✅              | ✅             |
| `/usuarios/login`         | ✅              | ✅             |
| `/usuarios/alterar-senha` | ✅ (próprio)    | ✅             |
| `/usuarios/{id}` (DELETE) | ✅ (próprio)    | ✅             |
| `/usuarios` (GET)         | ❌              | ✅             |
| `/usuarios/{id}` (GET)    | ✅ (próprio)    | ✅             |

---

## 📦 Exemplo de uso com Postman

1. Faça `POST /usuarios/login` com nome e senha
2. Copie o token da resposta
3. Nas requisições protegidas, adicione o header:

```
Authorization: Bearer <seu_token>
```

---

## ❌ Tratamento de Erros

A API retorna erros em formato padronizado:

```json
{
  "timestamp": "2025-04-09T15:10:22.491",
  "erro": "Usuário ou senha inválidos!",
  "status": 401
}
```

---

## 👤 Autor

Desenvolvido por [Luigi Ferrara Sinno](https://github.com/luigiferrarasinno)  
GitHub: [api_login_java_spring_boot](https://github.com/luigiferrarasinno/api_login_java_spring_boot.git)
