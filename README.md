

# **API de Usuários - Sistema de Login, Cadastro e Autenticação JWT**

Esta API foi construída com **Spring Boot** e oferece funcionalidades para gerenciar usuários, incluindo login, cadastro, alteração de senha, deleção de conta e visualização de todos os usuários. A API usa um banco de dados H2 embutido e foi estruturada com camadas **Controller**, **Service** e **DAO**.

Agora a API conta com **autenticação JWT**, que protege os endpoints e garante que apenas usuários autenticados possam acessar funcionalidades específicas.

---

## **Funcionalidades**
A API permite as seguintes operações:

1. **Login**: Verificar se o usuário e a senha são válidos e gerar um token JWT.
2. **Criar Conta**: Criar um novo usuário no sistema.
3. **Alterar Senha**: Alterar a senha de um usuário autenticado.
4. **Deletar Conta**: Excluir um usuário do sistema (autorizado).
5. **Listar Usuários**: Retornar todos os usuários cadastrados (admin).
6. **Ver Usuário por ID**: Retornar os dados de um usuário específico (autorizado).

---

## **Autenticação com JWT**

A autenticação da API é baseada em **JWT (JSON Web Token)**. Após o login bem-sucedido, um token é gerado e deve ser utilizado para acessar endpoints protegidos.

### 🔐 Como funciona:

- Ao fazer login em `/usuarios/login`, um **token JWT** é retornado.
- Esse token deve ser incluído no header das requisições:
  ```
  Authorization: Bearer <seu_token>
  ```

- O token identifica o usuário e seu papel (usuário comum ou admin), sendo usado para validar permissões.

---

## **Controle de Acesso**

A API diferencia dois tipos de usuários:

- `USER` → usuário comum.
- `ADMIN` → usuário administrador, com acesso total.

### 🔐 Regras de Acesso:

| Endpoint                       | USER                 | ADMIN                |
|-------------------------------|----------------------|----------------------|
| `POST /usuarios/login`        | ✅                   | ✅                   |
| `POST /usuarios/criar`        | ✅                   | ✅                   |
| `PUT /usuarios/alterar-senha` | ✅ (dono da conta)   | ✅                   |
| `DELETE /usuarios/{id}`       | ✅ (dono da conta)   | ✅ (⚠️ ver observação) |
| `GET /usuarios`               | ❌                   | ✅                   |
| `GET /usuarios/{id}`          | ✅ (dono da conta)   | ✅                   |

> ❗ **Observação:** Atualmente, o admin **não consegue deletar contas de outros usuários**, mas isso pode ser ajustado facilmente no método de verificação de acesso.

---

## **Verificação de Permissões com `@PreAuthorize`**

A segurança de cada endpoint é controlada com anotações como:

- `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`  
  → apenas admins têm acesso.

- `@PreAuthorize("@usuarioService.isOwnerOrAdmin(#id, authentication.name)")`  
  → permite acesso ao dono da conta ou ao admin.

### Exemplo do método `isOwnerOrAdmin`:

```java
public boolean isOwnerOrAdmin(Long id, String nomeUsuarioAuth) {
    Optional<Usuario> usuarioAuth = usuarioDAO.findByNomeUsuario(nomeUsuarioAuth);
    Optional<Usuario> usuarioTarget = usuarioDAO.findById(id);

    if (usuarioAuth.isEmpty() || usuarioTarget.isEmpty()) return false;

    return usuarioAuth.get().getRole().equalsIgnoreCase("ADMIN") ||
           usuarioAuth.get().getId().equals(usuarioTarget.get().getId());
}
```

---

## **Endpoints da API**

### **1. Login de Usuário**
- **URL**: `/usuarios/login`
- **Método**: `POST`
- **Descrição**: Realiza o login e retorna um token JWT.
- **Corpo**:
  ```json
  {
    "nomeUsuario": "usuarioTeste",
    "senha": "senha1234"
  }
  ```
- **Resposta**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp..."
  }
  ```

### **2. Criar Conta**
- **URL**: `/usuarios/criar`
- **Método**: `POST`
- **Descrição**: Cria uma nova conta.

### **3. Alterar Senha**
- **URL**: `/usuarios/alterar-senha`
- **Método**: `PUT`
- **Descrição**: Apenas o dono da conta ou o admin podem alterar a senha.
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```

### **4. Deletar Conta**
- **URL**: `/usuarios/{id}`
- **Método**: `DELETE`
- **Descrição**: Apenas o dono da conta pode deletar. Admin ainda não consegue.
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```

### **5. Listar Todos os Usuários**
- **URL**: `/usuarios`
- **Método**: `GET`
- **Acesso**: Apenas para admins.

### **6. Ver usuário pelo ID**
- **URL**: `/usuarios/{id}`
- **Método**: `GET`
- **Descrição**: Acesso permitido ao próprio usuário ou admin.

---

## **Testando com Postman**

1. Envie um `POST` para `/usuarios/login` com suas credenciais.
2. Copie o `token` retornado.
3. Em qualquer endpoint protegido, adicione no header:
   ```
   Authorization: Bearer <seu_token>
   ```

---

## **Instruções para Rodar o Projeto**

### **Pré-requisitos**
- Java 17+
- Maven
- Postman

### **Passos**
```bash
git clone https://github.com/luigiferrarasinno/api_login_java_spring_boot.git
cd projeto-api
./mvnw clean install
./mvnw spring-boot:run
```

Acesse a API em `http://localhost:8080`.

---

## **Tecnologias Utilizadas**
- Spring Boot
- Spring Security
- JWT (jjwt)
- H2 Database
- Maven

---

## **Conclusão**
Essa API oferece um sistema completo de autenticação e autorização com base em tokens JWT. Com controle de permissões para usuários comuns e administradores, ela pode ser usada como base para aplicações seguras com autenticação robusta.
```
