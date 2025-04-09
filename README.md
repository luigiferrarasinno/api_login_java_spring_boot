
# 🛡️ API de Autenticação com Spring Boot e JWT

Esta é uma API de autenticação construída com Java 17 e Spring Boot 3.2.2. A aplicação implementa controle de usuários com autenticação via JWT, suporte a diferentes roles (`USER` e `ADMIN`), e endpoints protegidos com base no papel do usuário.

---

## 📂 Estrutura do Projeto

```
src
└── main
    └── java
        └── com.example.demo
            ├── security               # Lógica de autenticação e JWT
            ├── user
            │   ├── controller         # Endpoints da API
            │   ├── dao                # Acesso ao banco de dados
            │   ├── dto                # Data Transfer Objects
            │   ├── exception          # Tratamento global de erros
            │   ├── init               # Inicializador com conta admin
            │   ├── model              # Entidades (JPA)
            │   ├── repository         # Repositórios (JPA)
            │   └── service            # Regras de negócio
```

---

## 🚀 Como rodar o projeto localmente

### Pré-requisitos

- Java 17
- Maven
- (Opcional) Postman para testar os endpoints

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/luigiferrarasinno/api_login_java_spring_boot.git
cd api_login_java_spring_boot
```

2. Execute o projeto via Maven ou diretamente pela sua IDE (VS Code, IntelliJ etc.).

3. Um usuário ADMIN será automaticamente criado:

```
Usuário: admin
Senha: admin123
```

---

## ✅ Endpoints Disponíveis

### 🔐 Autenticação

| Método | Endpoint               | Acesso        | Descrição                                  |
|--------|------------------------|---------------|--------------------------------------------|
| POST   | `/usuarios/login`      | Público       | Realiza login e retorna um token JWT       |
| POST   | `/usuarios/criar`      | Público       | Cria uma nova conta de usuário             |

#### Exemplo JSON para login/criação de conta:

```json
{
  "nomeUsuario": "usuario1",
  "senha": "senha123"
}
```

---

### 👤 Gerenciamento de Usuários

| Método | Endpoint                       | USER                 | ADMIN                | Descrição                                 |
|--------|--------------------------------|----------------------|----------------------|-------------------------------------------|
| PUT    | `/usuarios/alterar-senha`      | ✅ (dono da conta)   | ✅                   | Altera a senha do próprio usuário         |
| DELETE | `/usuarios/{id}`               | ✅ (dono da conta)   | ✅                   | Deleta a conta                            |
| GET    | `/usuarios`                    | ❌                   | ✅                   | Lista todos os usuários                   |
| GET    | `/usuarios/{id}`               | ✅ (dono da conta)   | ✅                   | Busca um usuário pelo ID                  |

---

## 🔐 Autenticação JWT

Após o login, a API retorna um token JWT. Esse token deve ser enviado no cabeçalho `Authorization` das requisições protegidas, com o prefixo `Bearer`.

### 🧾 Exemplo de uso no Postman

1. **Login**: `POST /usuarios/login`  
   → Receba o token no corpo da resposta

2. **Adicionar o token** nas próximas requisições protegidas:

```
Authorization: Bearer <seu_token_aqui>
```

---

## 🧠 Lógica de Permissões

### Controle de Acesso

| Endpoint                       | USER                 | ADMIN                |
|-------------------------------|----------------------|----------------------|
| `POST /usuarios/login`        | ✅                   | ✅                   |
| `POST /usuarios/criar`        | ✅                   | ✅                   |
| `PUT /usuarios/alterar-senha` | ✅ (dono da conta)   | ✅                   |
| `DELETE /usuarios/{id}`       | ✅ (dono da conta)   | ✅                   |
| `GET /usuarios`               | ❌                   | ✅                   |
| `GET /usuarios/{id}`          | ✅ (dono da conta)   | ✅                   |

---

## ⚠️ Tratamento de Erros

Erros são retornados com a seguinte estrutura:

```json
{
  "timestamp": "2025-04-09T14:32:00.123",
  "erro": "Usuário ou senha inválidos!",
  "status": 400
}
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.2
- Spring Security
- JWT (Java Web Tokens)
- H2 Database (memória)
- Maven

---

## 🧑‍💻 Autor

Feito por [Luigi Ferrara](https://github.com/luigiferrarasinno) 👨‍💻  
Repositório: [api_login_java_spring_boot](https://github.com/luigiferrarasinno/api_login_java_spring_boot.git)

---
