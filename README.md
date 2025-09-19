
# 🛡️ API de java

Esta é uma API de autenticação e gerenciamento de usuários e gerenciamento de investimentos desenvolvida com **Java 17** e **Spring Boot 3.2.2**, utilizando autenticação via **JWT (JSON Web Token)**. A API possui controle de acesso baseado em **roles** (`USER` e `ADMIN`) e protege os endpoints adequadamente. A autenticação é feita por **email e senha**.

---

## 📁 Estrutura do Projeto

```bash
src
└── main
    └── java
        └── com.example.demo
            ├── exception/             # Tratamento global de erros e exceções personalizadas
            ├── security/              # Lógica de autenticação, JWT e segurança
            ├── logging/               # Filtro para log de requisições e respostas da API
            ├── user/                  # Pasta com tudo relacionado a entidade user 
            │   ├── controller/        # Endpoints da API
            │   ├── dao/               # Classe auxiliar para troca de senha
            │   ├── dto/               # Objetos de transferência de dados (entrada/saída)
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
| `exception`    | Gerencia erros com mensagens personalizadas                            |
| `init`         | Cria automaticamente algumas coisas no início da aplicação             |


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

> 👤 Um usuário **comum (ROLE\_USER)** também será criado automaticamente:

```
Email: usuario@teste.com
Senha: teste123
```

> ⚠️ Alguns investimentos são criados automaticamente tambem
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

> 💡 Dica: após logar no H2 Console, use `SELECT * FROM Investimento;` para visualizar os dados.


---

## 🛠️ Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.2.2  
- Spring Security  
- JWT (JSON Web Token)  
- H2 Database (em memória)  
- Maven  
- Swagger (OpenAPI)

---

## 🏗️ Arquitetura e diagramas (peso 10%)

### 📐 Diagramas de arquitetura (camadas e componentes)

```mermaid
graph TB
    subgraph "Presentation Layer"
        UC[Usuario Controller]
        IC[Investimento Controller]
    end
    
    subgraph "Security Layer"
        JWT[JWT Filter]
        SEC[Security Config]
        AUTH[Authentication]
    end
    
    subgraph "Service Layer"
        US[Usuario Service]
        IS[Investimento Service]
    end
    
    subgraph "Data Access Layer"
        UR[Usuario Repository]
        IR[Investimento Repository]
        DAO[Usuario DAO]
    end
    
    subgraph "Model Layer"
        UE[Usuario Entity]
        IE[Investimento Entity]
    end
    
    subgraph "Database"
        H2[(H2 Database)]
    end
    
    subgraph "Exception Handling"
        GEH[Global Exception Handler]
        CUSTOM[Custom Exceptions]
    end
    
    UC --> JWT
    IC --> JWT
    JWT --> SEC
    SEC --> AUTH
    UC --> US
    IC --> IS
    US --> UR
    IS --> IR
    US --> DAO
    UR --> UE
    IR --> IE
    UE --> H2
    IE --> H2
    US --> GEH
    IS --> GEH
    GEH --> CUSTOM
```

### 🗂️ Diagrama de entidades (ER)

```mermaid
erDiagram
    USUARIO {
        Long id PK
        String nomeUsuario
        String email UK
        String senha
        Long cpf UK
        LocalDate dt_nascimento
        String role
        Boolean userIsActive
        String tipo_de_investidor
        String user_permissions
    }
    
    INVESTIMENTO {
        Long id PK
        String nome
        String categoria
        Double valor
        String descricao
        LocalDate data
        String risco
        Boolean ativo
        Long usuarioId FK
    }
    
    USUARIO ||--o{ INVESTIMENTO : "possui"
```

### 🔄 Casos de uso implementados como serviços

```mermaid
graph LR
    subgraph "Usuario Service Cases"
        UC1[Cadastrar Usuario]
        UC2[Autenticar Usuario]
        UC3[Alterar Senha]
        UC4[Redefinir Senha]
        UC5[Trocar Email]
        UC6[Ativar/Desativar]
        UC7[Consultar Usuario]
        UC8[Excluir Usuario]
    end
    
    subgraph "Investimento Service Cases"
        IC1[Listar Investimentos]
        IC2[Criar Investimento]
        IC3[Consultar por ID]
        IC4[Vincular Usuario]
        IC5[Desvincular Usuario]
        IC6[Investimentos do Usuario]
        IC7[Toggle Ativo]
        IC8[Excluir Investimento]
    end
    
    subgraph "Security Cases"
        SC1[Gerar JWT]
        SC2[Validar Token]
        SC3[Autorizar Acesso]
        SC4[Filtrar Requisições]
    end
    
    UC2 --> SC1
    UC7 --> SC2
    IC1 --> SC3
    UC1 --> SC4
```

### 🏛️ Arquitetura de Camadas Detalhada

```mermaid
graph TD
    subgraph "Client Layer"
        WEB[Web Browser]
        MOBILE[Mobile App]
        API_CLIENT[API Client]
    end
    
    subgraph "API Gateway"
        CORS[CORS Filter]
        LOG[Logging Filter]
    end
    
    subgraph "Controller Layer"
        subgraph "Usuario Module"
            UCONTROLLER[Usuario Controller]
        end
        subgraph "Investimento Module"
            ICONTROLLER[Investimento Controller]
        end
    end
    
    subgraph "Security Layer"
        JWTFILTER[JWT Authentication Filter]
        AUTHPROVIDER[Authentication Provider]
        ACCESSHANDLER[Access Denied Handler]
    end
    
    subgraph "Business Layer"
        subgraph "Usuario Domain"
            USERVICE[Usuario Service]
            UDTO[Usuario DTOs]
        end
        subgraph "Investimento Domain"
            ISERVICE[Investimento Service]
            IDTO[Investimento DTOs]
        end
    end
    
    subgraph "Data Layer"
        subgraph "Repository Layer"
            UREPO[Usuario Repository]
            IREPO[Investimento Repository]
            UDAO[Usuario DAO]
        end
        subgraph "Entity Layer"
            UENTITY[Usuario Entity]
            IENTITY[Investimento Entity]
        end
    end
    
    subgraph "Database Layer"
        H2DB[(H2 Database)]
    end
    
    WEB --> CORS
    MOBILE --> CORS
    API_CLIENT --> CORS
    
    CORS --> LOG
    LOG --> UCONTROLLER
    LOG --> ICONTROLLER
    
    UCONTROLLER --> JWTFILTER
    ICONTROLLER --> JWTFILTER
    
    JWTFILTER --> AUTHPROVIDER
    AUTHPROVIDER --> ACCESSHANDLER
    
    UCONTROLLER --> USERVICE
    ICONTROLLER --> ISERVICE
    
    USERVICE --> UDTO
    ISERVICE --> IDTO
    
    USERVICE --> UREPO
    ISERVICE --> IREPO
    USERVICE --> UDAO
    
    UREPO --> UENTITY
    IREPO --> IENTITY
    
    UENTITY --> H2DB
    IENTITY --> H2DB
```

---

# 🔐 Endpoints da API de manipulação de usuario
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

**PATCH** `/usuarios/{id}`  
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

### 9. Trocar Email

**PUT** `/usuarios/trocar-email`
**Acesso**:

* Apenas **ADMINs** com token **Bearer**.

#### Corpo da Requisição (JSON):

```json
{
  "cpf": 12345678900,
  "novoEmail": "novo.email@email.com"
}
```

> **Observação:** o CPF deve estar cadastrado no sistema. O novo email **não pode** ser igual ao atual nem já estar em uso por outro usuário.

#### Resposta (200 OK):

```json
{
  "mensagem": "Email do usuário com CPF 12345678900 alterado com sucesso para novo.email@email.com"
}
```

#### Respostas de Erro:

* **400 Bad Request** – Quando o novo email já está cadastrado:

```json
{
  "timestamp": "2025-06-10T14:22:00",
  "erro": "Email já cadastrado",
  "status": 400
}
```

* **404 Not Found** – Quando o CPF informado não existe:

```json
{
  "timestamp": "2025-06-10T14:25:00",
  "erro": "Usuário não encontrado para o CPF informado",
  "status": 404
}
```

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

Essa parte descreve como usar os endpoints de investimento após você obter o token JWT (veja na seção de manipulação de usuário).

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
    "categoria": "RENDA_FIXA",
    "valor": 1000.0,
    "descricao": "Investimento seguro em títulos do governo",
    "usuarioId": null,
    "data": "2025-06-10",
    "risco": "ALTO"
  },
  {
    "id": 2,
    "nome": "Ações Vale",
    "categoria": "RENDA_VARIAVEL",
    "valor": 5000.0,
    "descricao": "Investimento em ações da Vale",
    "usuarioId": null,
    "data": "2025-06-10",
    "risco": "ALTO"
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
  "categoria": "RENDA_FIXA",
  "valor": 1000.0,
  "descricao": "Investimento seguro em títulos do governo",
  "usuarioId": null,
  "data": "2025-06-10",
  "risco": "ALTO"
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
  "categoria": "FUNDO",
  "valor": 3000.00,
  "descricao": "Investimento em fundos imobiliários",
  "data": "2025-06-10",
  "risco": "ALTO"
}
```

* **Resposta (200 OK):**

```json
{
  "id": 3,
  "nome": "Fundo Imobiliário",
  "categoria": "FUNDO",
  "valor": 3000.0,
  "descricao": "Investimento em fundos imobiliários",
  "usuarioId": null,
  "data": "2025-06-10",
  "risco": "ALTO"
}
```

---

### ✅ 4. Deletar um investimento

* **Método:** `DELETE`

* **URL:** `http://localhost:8080/investimentos/{id}`

* **Autorização:** Apenas usuários com `ROLE_ADMIN` podem executar

* **Resposta (204 No Content):** Nenhum conteúdo retornado

* **Erros possíveis:**

  * `404 Not Found`: Se o investimento com o ID informado não existir (caso esteja implementado no serviço)

---

### 🔄 5. Vincular / Desvincular investimento a usuário (Toggle)

* **Método:** `POST`
* **URL:** `http://localhost:8080/investimentos/{investimentoId}/usuario/{usuarioId}`

  * Exemplo: `http://localhost:8080/investimentos/1/usuario/2`
* **Acesso:**

  * Usuário com `ROLE_USER`: só pode vincular/desvincular seu próprio ID (ou seja, `{usuarioId}` = seu ID)
  * ADMIN: pode vincular/desvincular qualquer usuário
* **Resposta (200 OK):** mostra o investimento atualizado, incluindo o campo `usuarioId` (ou `null`) além de incluir uma `message` que informa se houve vinculação ou desvinculação do usuário com o investimento

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

### 🔄 7. Ativar / Desativar um investimento (toggle ativo)

* **Método:** `PATCH`
* **URL:** `http://localhost:8080/investimentos/{id}/toggle-ativo`
* **Autorização:** Apenas usuários com `ROLE_ADMIN` podem executar
* **Descrição:** Alterna o status ativo/inativo do investimento com o ID informado
* **Resposta (200 OK):** Retorna o investimento atualizado, exemplo:

```json
{
  "id": 1,
  "nome": "Tesouro Direto",
  "categoria": "RENDA_FIXA",
  "valor": 1000.0,
  "descricao": "Investimento seguro em títulos do governo",
  "usuarioId": null,
  "data": "2025-06-10",
  "risco": "ALTO",
  "ativo": false
}
```

---

## 👤 Autor

Desenvolvido por:

• Davi Passanha de Sousa Guerra - RM551605

• Cauã Gonçalves de Jesus - RM97648

• Luan Silveira Macea - RM98290

• Rui Amorim Siqueira - RM98436

• Luigi Ferrara Sinno -RM98047

GitHub: [api_login_java_spring_boot](https://github.com/luigiferrarasinno/api_login_java_spring_boot.git)

