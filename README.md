
# 🛡️ API de java

Esta é uma API de autenticação e gerenciamento de usuários desenvolvida com **Java 17** e **Spring Boot 3.2.2**, utilizando autenticação via **JWT (JSON Web Token)**. A API possui controle de acesso baseado em **roles** (`USER` e `ADMIN`) e protege os endpoints adequadamente. A autenticação é feita por **email e senha**.

---

## 📁 Estrutura do Projeto

```bash
src
└── main
    └── java
        └── com.example.demo
            ├── security/              # Lógica de autenticação, JWT e segurança
            ├── user/                  # Pasta com tudo relacionado a entidade user 
            │   ├── controller/        # Endpoints da API
            │   ├── dao/               # Classe auxiliar para troca de senha
            │   ├── dto/               # Objetos de transferência de dados (entrada/saída)
            │   ├── exception/         # Tratamento global de erros e exceções personalizadas
            │   ├── init/              # Inicializador com criação do usuário admin
            │   ├── model/             # Entidades JPA (Usuario e Role)
            │   ├── repository/        # Interfaces para acesso ao banco
            │   └── service/           # Lógica de negócio (cadastro, login, exclusão, etc.)
            └── investimento/          # Pasta com tudo relacionado à entidade investimento
                ├── controller/        # Endpoints da API
                ├── dto/               # Objetos de transferência de dados (entrada/saída)
                ├── init/              # Inicializador para investimentos, se aplicável
                ├── model/             # Entidades JPA relacionadas a investimentos
                ├── repository/        # Interfaces para acesso ao banco
                └── service/           # Lógica de negócio (cadastro, consulta, atualização, etc.)

```

---

## 🔧 Camadas do Projeto

| Camada         | Função                                                                 |
|----------------|------------------------------------------------------------------------|
| `controller`   | Define os endpoints públicos e protegidos da API                       |
| `service`      | Contém as regras de negócio (ex: criação de conta, validações, etc.)   |
| `repository`   | Acesso ao banco de dados via Spring Data JPA                           |
| `dao`          | Realiza consultas personalizadas e operações mais complexas no banco   |
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

- Execute a aplicação com `mvn spring-boot:run` ou através da sua IDE.  
- A API sobe na porta padrão: `http://localhost:8080`

> ⚠️ Um usuário **ADMIN** será criado automaticamente:

```
Email: admin@admin.com
Senha: admin123
```

---

## 📚 Documentação e Ferramentas

### 🧪 Swagger (Documentação Interativa da API)

Acesse o Swagger UI para testar os endpoints diretamente pelo navegador:

```
http://localhost:8080/swagger-ui/index.html
```

---

### 🛢️ Console do Banco de Dados H2

A API utiliza o banco em memória H2. Acesse pelo navegador em:

```
http://localhost:8080/h2-console
```

**Credenciais de Acesso:**

- **JDBC URL:** `jdbc:h2:mem:fellerdb`
- **Username:** `Admin`
- **Password:** `Fiap123`

> 💡 Dica: após logar no H2 Console, use `SELECT * FROM USUARIO;` para visualizar os dados.

---

## 🛠️ Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.2.2  
- Spring Security  
- JWT (JSON Web Token)  
- H2 Database (em memória)  
- Maven  
- Swagger (OpenAPI)


## 🔐 Endpoints da API

### 1. Criar Conta

**POST** `/usuarios/criar`  
**Acesso**: Público

#### Requisição:

```json
{
  "nomeUsuario": "João da Silva",
  "senha": "senha123",
  "email": "joao.silva@email.com",
  "cpf": 12345678909,
  "dt_nascimento": "2006-05-20"
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
  "email": "joao@email.com",
  "senha": "senha123",
  "firstLogin": false

}
```

#### Resposta:

```json
{
    "token": "um token",
    "userId": "id do usuario"
}
```

- **200 OK**: Token JWT válido  
- **401 Unauthorized**: Credenciais inválidas

---

### 3. Alterar Senha

**PUT** `/usuarios/alterar-senha`  
**Acesso**: Protegido (o próprio usuário ou admin)

#### Requisição:

```json
{
  "email": "joao@email.com",
  "senhaAntiga": "uma senha",
  "senhaNova": "nova senha"
}
```

#### Resposta:

- **200 OK**: Senha alterada com sucesso  
- **403 Forbidden**: Tentativa de alterar senha de outro usuário

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
    "nomeUsuario": "admin",
    "email": "admin@email.com",
    "ativo": true
  },
  {
    "id": 2,
    "nomeUsuario": "joao",
    "email": "joao@email.com",
    "ativo": true
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
    "nomeUsuario": "João da Silva",
    "email": "joao.silva@email.com",
    "role": "ROLE_USER",
    "cpf": 12345678909,
    "dt_nascimento": "2006-05-20",
    "tipo_de_investidor": "nenhum por enquanto",
    "userIsActive": true,
    "user_permissions": "nenhuma por enquanto"
}
```

---

### 7. Alternar Status de Atividade

**PUT** `/usuarios/{id}`  
**Acesso**:
- USER: pode alterar apenas o próprio status  
- ADMIN: pode alterar qualquer usuário

#### Resposta:

```json
{
  "mensagem": "Status de atividade atualizado com sucesso!",
  "ativo": false
}
```
---

### 8. Criar Senha

**PUT** `/usuarios/criar-senha`
**Acesso**:

* Público: qualquer usuário que tenha CPF, email e data de nascimento corretos pode criar ou redefinir sua senha.

#### Corpo da Requisição (JSON):

```json
{
  "cpf": 12345678900,
  "email": "usuario@email.com",
  "dt_nascimento": "2006-05-20",
  "senhaNova": "minhaNovaSenhaSegura"
}
```

> **Observação:** o campo `dt_nascimento` deve estar no formato `"yyyy-MM-dd"`, e será convertido corretamente mesmo com o nome em snake\_case.

#### Resposta (200 OK):

```json
{
  "mensagem": "Senha redefinida com sucesso!"
}
```

---



## 🧠 Regras de Permissão

| Endpoint                           | USER                  | ADMIN |
| ---------------------------------- | ---------------------| ----- |
| `/usuarios/criar`                  | ❌                   | ✅     |
| `/usuarios/login`                  | ✅ (publico)         | ✅(publico)|
| `/usuarios/alterar-senha`          | ✅ (próprio)         | ✅     |
| `/usuarios/criar-senha`            | ✅ (publico)         | ✅(publico)|
| `/usuarios/{id}` (DELETE)          | ✅ (próprio)         | ✅     |
| `/usuarios` (GET)                  | ❌                   | ✅     |
| `/usuarios/{id}` (GET)             | ✅ (próprio)         | ✅     |
| `/usuarios/{id}` (PUT)             | ✅ (próprio)         | ✅     |


---

## 📦 Exemplo de uso com Postman

### 1. **Login como administrador**

Antes de criar qualquer conta, é necessário fazer login com um usuário administrador para obter o token JWT.

```
POST /usuarios/login
```

Corpo da requisição (JSON):

```json
{
  "email": "admin@admin.com",
  "senha": "admin123"
}
```

### 2. **Copie o token JWT da resposta**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

Você usará esse token para autenticar as requisições protegidas, como a criação de usuários.

---

### 3. **Criar uma nova conta de usuário**

```
POST /usuarios/criar
```

Corpo da requisição (JSON):

```json
{
  "nomeUsuario": "João da Silva",
  "senha": "senha123",
  "email": "joao.silva@email.com",
  "cpf": 12345678909,
  "dt_nascimento": "2006-05-20"
}
```

> **Atenção:** Este endpoint exige um token de **ADMIN** no cabeçalho da requisição.

---

### 4. **Fazer login com o novo usuário criado**

```
POST /usuarios/login
```

Corpo da requisição (JSON):

```json
{
  "email": "joao.silva@email.com",
  "senha": "senha123"
}
```

---

### 5. **Autenticação no Postman (Bearer Token)**

Para testar qualquer endpoint protegido:

* Vá até a aba **Authorization**
* Em **Type**, selecione **Bearer Token**
* No campo **Token**, cole o token JWT copiado
* O Postman automaticamente adicionará o cabeçalho:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
```

---

## 📈 Seção: Investimentos

Essa parte descreve como usar os endpoints de investimento após você obter o token JWT (veja na seção de login).


---

## 🧠 Regras de Permissão - Investimentos

| Endpoint                                                   | USER            | ADMIN           |
| ---------------------------------------------------------- | --------------- | --------------- |
| `GET /investimentos`                                       | ✅ (autenticado) | ✅ (autenticado) |
| `POST /investimentos`                                      | ❌               | ✅               |
| `GET /investimentos/{id}`                                  | ✅ (autenticado) | ✅               |
| `DELETE /investimentos/{id}`                               | ❌               | ✅               |
| `POST /investimentos/{investimentoId}/usuario/{usuarioId}` | ✅ (próprio)     | ✅               |
| `GET /investimentos/usuario/{usuarioId}`                   | ✅ (próprio)     | ✅               |

---

**Legenda:**

* ✅: Permitido
* ❌: Negado
* (próprio): Somente para o próprio usuário (via verificação customizada `@usuarioService.isOwnerOrAdmin`)
* (autenticado): Qualquer usuário autenticado

---

### ✅ Headers comuns para endpoints protegidos:

* **Authorization**: `Bearer SEU_TOKEN_JWT`
* **Content-Type**: `application/json`

---

### 📋 1. Listar todos os investimentos

* **Método:** `GET`
* **URL:** `http://localhost:8080/investimentos`
* **Pré-requisito:** qualquer usuário **autenticado**
* **Resposta (200 OK):** Lista JSON como:

```json
[
  {
    "id": 1,
    "nome": "Tesouro Direto",
    "categoria": "Renda Fixa",
    "valor": 1000.0,
    "descricao": "Investimento seguro em títulos do governo",
    "usuarioId": null
  },
  {
    "id": 2,
    "nome": "Ações Vale",
    "categoria": "Renda Variável",
    "valor": 5000.0,
    "descricao": "Investimento em ações da Vale",
    "usuarioId": null
  }
]
```

---

### 🔍 2. Buscar investimento por ID

* **Método:** `GET`
* **URL:** `http://localhost:8080/investimentos/{id}` (substitua `{id}` por um número)
* **Pré-requisito:** usuário autenticado
* **Resposta (200 OK):**

```json
{
  "id": 1,
  "nome": "Tesouro Direto",
  "categoria": "Renda Fixa",
  "valor": 1000.0,
  "descricao": "Investimento seguro em títulos do governo",
  "usuarioId": null
}
```

---

### ➕ 3. Criar um novo investimento

* **Método:** `POST`
* **URL:** `http://localhost:8080/investimentos`
* **Só ADMIN** pode executar
* **Body (JSON):**

```json
{
  "nome": "Fundo Imobiliário",
  "categoria": "Fundo",
  "valor": 3000.00,
  "descricao": "Investimento em fundos imobiliários"
}
```

* **Resposta (200 OK):**

```json
{
  "id": 3,
  "nome": "Fundo Imobiliário",
  "categoria": "Fundo",
  "valor": 3000.0,
  "descricao": "Investimento em fundos imobiliários",
  "usuarioId": null
}
```

---

### ❌ 4. Deletar um investimento

* **Método:** `DELETE`
* **URL:** `http://localhost:8080/investimentos/{id}`
* **Só ADMIN** pode executar
* **Resposta (204 No Content)**

---

### 🔄 5. Vincular / Desvincular investimento a usuário (Toggle)

* **Método:** `POST`
* **URL:** `http://localhost:8080/investimentos/{investimentoId}/usuario/{usuarioId}`

  * Exemplo: `http://localhost:8080/investimentos/1/usuario/2`
* **Acesso:**

  * Usuário com `ROLE_USER`: só pode vincular/desvincular seu próprio ID (ou seja, `{usuarioId}` = seu ID)
  * ADMIN: pode vincular/desvincular qualquer usuário
* **Resposta (200 OK):** mostra o investimento atualizado, incluindo o campo `usuarioId` (ou `null`, se tiver sido desvinculado)

---

### 👤 6. Listar investimentos vinculados a um usuário

* **Método:** `GET`
* **URL:** `http://localhost:8080/investimentos/usuario/{usuarioId}`
* **Acesso:**

  * Usuário: só pode acessar seus próprios investimentos
  * ADMIN: pode ver qualquer usuário
* **Resposta (200 OK):** lista somente os investimentos cujo `usuarioId` é o mesmo passado na URL

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

