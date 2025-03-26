

# **API de Usuários - Sistema de Login e Cadastro**

Esta é uma API simples para gerenciar usuários, incluindo funcionalidades de login, cadastro de contas, alteração de senha, deleção de conta e visualização de todos os usuários. A API foi construída com **Spring Boot** e utiliza um banco de dados H2 (embutido).

## **Funcionalidades**
A API permite as seguintes operações:

1. **Login**: Verificar se o usuário e a senha são válidos.
2. **Criar Conta**: Criar um novo usuário no sistema.
3. **Alterar Senha**: Alterar a senha de um usuário.
4. **Deletar Conta**: Excluir um usuário do sistema.
5. **Listar Usuários**: Retornar todos os usuários cadastrados.

## **Endpoints da API**

### **1. Login de Usuário**
- **URL**: `/usuarios/login`
- **Método**: `POST`
- **Descrição**: Realiza o login de um usuário, verificando a combinação de nome de usuário e senha.
- **Corpo da Requisição**:
  ```json
  {
    "nomeUsuario": "usuarioTeste",
    "senha": "senha1234"
  }
  ```
- **Resposta**:
  - `200 OK`: `"Login bem-sucedido!"`
  - `400 Bad Request`: `"Usuário ou senha inválidos!"`

### **2. Criar Conta**
- **URL**: `/usuarios/criar`
- **Método**: `POST`
- **Descrição**: Cria um novo usuário no sistema.
- **Corpo da Requisição**:
  ```json
  {
    "nomeUsuario": "novoUsuario",
    "senha": "novaSenha123"
  }
  ```
- **Resposta**:
  - `201 Created`: `"Conta criada com sucesso!"`
  - `400 Bad Request`: `"Nome de usuário já existe!"`

### **3. Alterar Senha**
- **URL**: `/usuarios/alterar-senha`
- **Método**: `PUT`
- **Descrição**: Permite que um usuário altere sua senha.
- **Parâmetros**:
  - `nomeUsuario` (Query Parameter): Nome do usuário.
  - `novaSenha` (Query Parameter): Nova senha do usuário.
- **Exemplo de URL**: `http://localhost:8080/usuarios/alterar-senha?nomeUsuario=usuarioTeste&novaSenha=novaSenha1234`
- **Resposta**:
  - `200 OK`: `"Senha alterada com sucesso!"`
  - `404 Not Found`: `"Usuário não encontrado!"`

### **4. Deletar Conta**
- **URL**: `/usuarios/{id}`
- **Método**: `DELETE`
- **Descrição**: Exclui um usuário do sistema pelo ID.
- **Parâmetro**:
  - `id` (Path Variable): ID do usuário a ser excluído.
- **Exemplo de URL**: `http://localhost:8080/usuarios/1`
- **Resposta**:
  - `200 OK`: `"Conta excluída com sucesso!"`
  - `404 Not Found`: `"Usuário não encontrado!"`

### **5. Listar Todos os Usuários**
- **URL**: `/usuarios`
- **Método**: `GET`
- **Descrição**: Retorna todos os usuários cadastrados no sistema.
- **Resposta**:
  ```json
  [
    {
      "id": 1,
      "nomeUsuario": "usuarioTeste",
      "senha": "senha1234"
    }
  ]
  ```

---

## **Instruções para Rodar o Projeto**

### **Pré-requisitos**

1. **Java 8+**: A API foi desenvolvida com Java 8 ou versões superiores.
2. **Maven**: O projeto usa o Maven para gerenciamento de dependências.
3. **Postman** (ou outro cliente REST): Para testar os endpoints da API.

### **Passos para Rodar a API**

1. **Clonar o Repositório**:
   ```bash
   git clone [https://github.com/usuario/projeto-api.git](https://github.com/luigiferrarasinno/api_login_java_spring_boot.git)
   cd projeto-api
   ```

2. **Construir o Projeto**:
   Caso não tenha o Maven instalado, você pode usar o Maven Wrapper:
   ```bash
   ./mvnw clean install
   ```

3. **Rodar a Aplicação**:
   Após compilar o projeto, execute o seguinte comando para iniciar a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar a API**:
   A API estará rodando em `http://localhost:8080`. Use o **Postman** ou outro cliente REST para interagir com os endpoints.

### **Usuário de Teste**
Ao rodar a aplicação pela primeira vez, será criado automaticamente um usuário de teste com o nome de usuário **`teste`** e senha **`senha1234`**. Você pode usar esse usuário para testar o endpoint de login.

---

## **Tecnologias Utilizadas**
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **H2 Database**: Banco de dados embutido para persistência dos usuários.
- **Maven**: Gerenciador de dependências e build automation.
- **Postman**: Para testar os endpoints.

---

## **Conclusão**
Essa API oferece funcionalidades básicas de autenticação e gerenciamento de usuários, com endpoints para login, criação de conta, alteração de senha, exclusão de conta e listagem de usuários. Ela pode ser expandida facilmente para atender a requisitos mais complexos.

Se você tiver dúvidas ou sugestões, sinta-se à vontade para contribuir ou abrir um problema no repositório!

