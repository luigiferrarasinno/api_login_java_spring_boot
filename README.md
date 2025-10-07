# 🏦 API de Investimentos Brasileira - Spring Boot

Uma API RESTful completa para gestão de investimentos com autenticação JWT, construída com **Spring Boot 3.2.2** e **Java 17**. 

**Sistema totalmente adaptado às regras do mercado brasileiro** com números inteiros de ações, controle administrativo de dividendos, sistema de comentários e o inovador sistema de playlists sociais.

---

## ✨ Principais Funcionalidades

- 🔐 **Autenticação JWT** com roles diferenciadas (USER/ADMIN)
- 📈 **Sistema Brasileiro de Ações** (apenas números inteiros, estoque limitado)
- 🎯 **Investimentos Recomendados** - Sistema personalizado de recomendações por perfil
- 💼 **Carteira Unificada** com GET consolidado e filtros avançados
- 💬 **Sistema de Comentários** nas ações com moderação
- 🔍 **Filtros Avançados** em todos os endpoints GET
- ⚙️ **Controle de Visibilidade** de investimentos para usuários
- 📊 **Preços Dinâmicos** com simulação de volatilidade por risco
- 🎵 **Sistema de Playlists Sociais** - "Spotify para investimentos"
- ✨ **Campo `recomendadoParaVoce`** - Indicador em todos os endpoints de investimentos
- 📋 **Extrato Completo** - Histórico detalhado de todas as transações

---

## 🎵 **NOVO! Sistema de Playlists Sociais**

> **"Crie, compartilhe e descubra playlists de investimentos como no Spotify!"**

### 🌟 Funcionalidades Sociais:
- 📝 **Criar playlists** personalizadas de investimentos
- 👥 **Seguir usuários** e suas playlists públicas
- 🤝 **Colaboração** - permita que outros contribuam
- 🔒 **Controle de privacidade** (públicas ou privadas)
- 🔍 **Descoberta** - explore playlists da comunidade
- 📤 **Compartilhamento direto** com usuários específicos

### 🎯 Sistema de Permissões:
- **🎨 Criador**: Controle total da playlist
- **🤝 Colaborador**: Pode adicionar/remover investimentos
- **👀 Seguidor**: Visualização e acompanhamento

📖 **[Ver Guia Completo de Playlists](GUIA-PLAYLIST.md)**

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Java 17** ou superior
- **Maven** instalado
- IDE de sua preferência

### Execução Rápida

**1. Clone e acesse:**
```bash
git clone https://github.com/luigiferrarasinno/api_login_java_spring_boot.git
cd api_login_java_spring_boot
```

**2. Execute a aplicação:**
```powershell
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**3. Acesse:**
- **API**: `http://localhost:8080`
- **Swagger**: `http://localhost:8080/swagger-ui.html`

---

## 🧪 Dados Automáticos Inicializados

### 👥 Usuários Criados:
| Persona | Email | Senha | Role | Descrição |
|---------|-------|-------|------|-----------|
| **Admin Sistema** | admin@admin.com | 123456 | ADMIN | Acesso completo ao sistema |
| **João Silva** | user@user.com | 123456 | USER | Investidor iniciante |
| **Maria Investidora** | maria@investidora.com | 123456 | USER | Investidora experiente |

### 📈 Portfólio Completo:
- **Ações**: PETR4, VALE3, ITUB4, BBAS3, ABEV3
- **FIIs**: HGLG11, MXRF11, XPLG11  
- **Renda Fixa**: TD-SELIC, CDB-INTER, LCI-NU

### 🎵 Playlists Sociais:
- **"Top Dividendos 2024 💰"** (Admin - Pública)
- **"Minha Carteira Conservadora 🛡️"** (Maria - Privada)
- **"FIIs para Iniciantes 🏢"** (Admin - Colaborativa)
- **"Apostas Arriscadas 🚀"** (João - Pública)

---

## 📚 Documentação e Ferramentas

### 🧪 Swagger (Documentação Interativa)
Teste todos os endpoints diretamente no navegador:
```
http://localhost:8080/swagger-ui.html
```

### 🛢️ Console do Banco H2
Acesse o banco de dados em memória para consultas SQL:
```
http://localhost:8080/h2-console
```

**Credenciais de Acesso:**
- **JDBC URL:** `jdbc:h2:mem:fellerdb`
- **Username:** `Admin`
- **Password:** `Fiap123`

> 💡 **Dicas úteis:**
> - `SELECT * FROM USUARIO;` - Ver todos os usuários
> - `SELECT * FROM INVESTIMENTO;` - Ver todos os investimentos
> - `SELECT * FROM COMENTARIO;` - Ver todos os comentários
> - `SELECT * FROM POSICAO_CARTEIRA;` - Ver posições dos usuários
> - `SELECT * FROM PLAYLIST;` - Ver todas as playlists
> - `SELECT * FROM PLAYLIST_SEGUIDORES;` - Ver relacionamentos sociais

---

## 🔑 Exemplo Prático no Postman

### Login Básico:
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "admin@admin.com",
  "senha": "123456"
}
```

**Resposta:**
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

### Usando o Token:
```http
GET http://localhost:8080/playlists/publicas
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

> 💡 **Para exemplos completos:** Consulte o **Swagger** ou os **guias específicos** abaixo!

---

## 📖 Guias Detalhados por Módulo

### 📚 Documentação Completa:

| 📋 Módulo | 🎯 Funcionalidades | 📎 Guia |
|-----------|-------------------|---------|
| 👤 **Usuários** | Login, cadastro, alteração de dados, filtros avançados | [usuarios.md](guias-de-uso/usuarios.md) |
| 📈 **Investimentos** | CRUD, favoritos, filtros, controle de visibilidade | [investimentos.md](guias-de-uso/investimentos.md) |
| 🎯 **Investimentos Recomendados** | Sistema de recomendações personalizadas, GET/POST/DELETE | [investimentos-recomendados.md](guias-de-uso/investimentos-recomendados.md) |
| 💬 **Comentários** | Sistema completo com moderação admin | [comentarios.md](guias-de-uso/comentarios.md) |
| � **Carteira Unificada** | GET consolidado com filtros, resumo e posições | [carteira-unificada.md](guias-de-uso/carteira-unificada.md) |
| � **Resumo de Investimentos** | Endpoint GET /resumo com métricas consolidadas | [resumo-investimentos.md](guias-de-uso/resumo-investimentos.md) |
| 🎵 **Playlists Sociais** | Sistema completo tipo Spotify | [playlist.md](guias-de-uso/playlist.md) |
| ✨ **Campo recomendadoParaVoce** | Lógica nullable em todos os endpoints | [campo-recomendado-null.md](guias-de-uso/campo-recomendado-null.md) |

> 💡 **Cada guia contém:**
> - Exemplos completos para Postman
> - Regras de negócio detalhadas
> - Diferenças USER vs ADMIN
> - Tratamento de erros
> - Casos de uso práticos

---

## 📁 Arquitetura do Projeto

```bash
src/main/java/com/example/demo
├── 🚀 DemoApplication.java               # Aplicação principal
├── 🔧 init/                              # Sistema de inicialização centralizado
│   └── SystemInitializer.java           # Cria usuários, investimentos, playlists e recomendações
├── 🛡️ security/                          # Segurança e autenticação
│   ├── JwtAuthenticationFilter.java     # Filtro JWT para requisições
│   ├── JwtUtil.java                     # Utilitários para geração/validação JWT
│   ├── SecurityConfig.java              # Configuração geral de segurança
│   └── CustomAccessDeniedHandler.java   # Tratamento de acesso negado
├── ⚠️ exception/                         # Tratamento de erros
│   ├── GlobalExceptionHandler.java      # Handler global de exceções
│   ├── EmailJaCadastradoException.java  # Exceção para email duplicado
│   └── RecursoNaoEncontradoException.java # Exceção para recursos não encontrados
├── 📝 logging/                           # Sistema de logs
│   └── PreSecurityLoggingFilter.java    # Log de requisições antes da autenticação
├── ⚙️ config/                            # Configurações gerais
│   └── SwaggerConfig.java               # Configuração do Swagger/OpenAPI
├── 👤 user/                              # Módulo de usuários
│   ├── controller/UsuarioController.java # Endpoints da API de usuários
│   ├── service/UsuarioService.java      # Lógica de negócio de usuários
│   ├── repository/UsuarioRepository.java # Acesso ao banco (usuários)
│   ├── dao/UsuarioDAO.java              # Operações complexas no banco
│   ├── model/Usuario.java               # Entidade JPA usuário
│   └── dto/                             # DTOs de entrada e saída
│       ├── UsuarioDTO.java              # DTO principal de usuário
│       ├── LoginRequestDTO.java         # DTO de requisição de login
│       ├── LoginResponseDTO.java        # Resposta do login
│       ├── RegisterRequestDTO.java      # DTO de registro de usuário
│       └── AlterarSenhaComSenhaAntiga.java # DTO para alteração de senha
├── 📈 investimento/                      # Módulo de investimentos
│   ├── controller/InvestimentoController.java # Endpoints CRUD investimentos
│   ├── service/                         # Serviços de negócio
│   │   ├── InvestimentoService.java     # Regras de negócio e validações
│   │   └── CotacaoService.java          # Serviço de cotações e preços
│   ├── repository/                      # Repositórios de dados
│   │   ├── InvestimentoRepository.java  # Queries personalizadas de investimentos
│   │   └── InvestimentoRecomendadoRepository.java # Queries de recomendações
│   ├── model/                           # Entidades do domínio
│   │   ├── Investimento.java            # Entidade principal
│   │   ├── InvestimentoRecomendado.java # Entidade de recomendações
│   │   ├── Categoria.java               # Enum de categorias
│   │   └── Risco.java                   # Enum de níveis de risco
│   ├── dto/                             # DTOs específicos
│   │   └── InvestimentoDTO.java         # DTO de investimento (com recomendadoParaVoce)
│   └── init/                            # Inicialização de dados
│       └── InvestimentoDataInitializer.java # Cria investimentos base
├── � investimento_recomendado/          # Sistema de recomendações personalizadas
│   ├── controller/InvestimentoRecomendadoController.java # Endpoints de recomendações
│   └── service/InvestimentoRecomendadoService.java # Lógica de recomendações
├── �🎵 playlist/                          # Módulo de playlists sociais
│   ├── controller/PlaylistController.java # 13 endpoints REST completos
│   ├── service/PlaylistService.java     # Lógica social e colaborativa
│   ├── repository/PlaylistRepository.java # Queries específicas de playlist
│   ├── model/                           # Entidades do domínio
│   │   ├── Playlist.java                # Entidade principal com M:N
│   │   └── PlaylistTipo.java            # Enum de tipos (PUBLICA/PRIVADA/COMPARTILHADA)
│   └── dto/                             # DTOs de request e response
│       ├── request/                     # DTOs de entrada
│       │   ├── CriarPlaylistRequestDTO.java
│       │   ├── AtualizarPlaylistRequestDTO.java
│       │   ├── AdicionarInvestimentoRequestDTO.java
│       │   └── CompartilharPlaylistRequestDTO.java
│       └── response/                    # DTOs de saída
│           ├── PlaylistResumoResponseDTO.java
│           ├── PlaylistDetalhadaResponseDTO.java
│           ├── InvestimentoPlaylistResponseDTO.java # DTO com recomendadoParaVoce
│           ├── UsuarioSeguidorResponseDTO.java
│           └── PlaylistOperacaoResponseDTO.java
├── 💬 comentarios/                       # Sistema de comentários
│   ├── controller/ComentarioController.java # CRUD e moderação
│   ├── service/ComentarioService.java   # Regras de permissão
│   ├── repository/ComentarioRepository.java # Soft delete
│   ├── model/Comentario.java            # Entidade com auditoria
│   └── dto/                             # DTOs específicos
│       ├── ComentarioDTO.java           # DTO principal
│       └── CriarComentarioRequestDTO.java # DTO de criação
├── 💼 carteira/                          # Sistema de carteira unificado
│   ├── controller/CarteiraController.java # Endpoint GET unificado com filtros
│   ├── service/CarteiraService.java     # Cálculos brasileiros e consolidação
│   ├── repository/PosicaoCarteiraRepository.java # Posições do usuário
│   ├── model/PosicaoCarteira.java       # Entidade de posições
│   └── dto/                             # DTOs específicos
│       ├── ResumoCarteiraResponseDTO.java # DTO principal de resposta
│       └── PosicaoCarteiraResponseDTO.java # DTO de posição (com recomendadoParaVoce)
├── 📊 extrato/                           # Sistema de extrato
│   ├── controller/ExtratoController.java # Consulta de transações
│   ├── service/ExtratoService.java      # Consolidação de movimentações
│   ├── repository/ExtratoRepository.java # Queries de histórico
│   ├── model/                           # Entidades do domínio
│   │   ├── Extrato.java                 # Histórico de transações
│   │   └── TipoTransacao.java           # Enum de tipos (COMPRA/VENDA/DIVIDENDO)
│   └── dto/                             # DTOs específicos
│       └── ExtratoDTO.java              # DTO de extrato
└── 💰 dividendo/                         # Sistema de dividendos (REMOVIDO - Funcionalidade descontinuada)
```

### 🏗️ Padrão Arquitetural MVC:

| Camada | Responsabilidade | Exemplo |
|--------|------------------|---------|
| **Controller** | Receber requisições HTTP e retornar respostas | `@RestController`, endpoints REST |
| **Service** | Regras de negócio e validações | Cálculos, validações, orquestração |
| **Repository** | Acesso ao banco de dados | Queries JPA, operações CRUD |
| **Model** | Entidades do domínio | Classes JPA, relacionamentos |
| **DTO** | Objetos de transferência | Request/Response, evitar exposição de entidades |

---

## 🛠️ Tecnologias e Dependências

- **☕ Java 17** - Linguagem base
- **🍃 Spring Boot 3.2.2** - Framework principal
- **🔐 Spring Security 6** - Autenticação JWT
- **💾 Spring Data JPA** - Persistência de dados
- **🗄️ H2 Database** - Banco em memória
- **📝 Bean Validation** - Validações automáticas
- **📚 Swagger/OpenAPI** - Documentação interativa
- **🔧 Maven** - Gerenciador de dependências

---

## 🏆 Principais Diferenciais

### 🇧🇷 **Regras Brasileiras:**
- ✅ Números inteiros de ações (sem frações)
- ✅ Validações de CPF e dados brasileiros
- ✅ Sistema de dividendos administrativo
- ✅ Controle de estoque realista

### 🔐 **Segurança Robusta:**
- ✅ JWT com expiração configurável
- ✅ Roles diferenciadas (USER/ADMIN)
- ✅ Validações granulares por endpoint
- ✅ Proteção contra exposição de dados

### 🎵 **Inovação Social:**
- ✅ Sistema de playlists tipo Spotify
- ✅ Colaboração em tempo real
- ✅ Descoberta de conteúdo
- ✅ Controles de privacidade avançados

---

## 👨‍💻 Equipe de Desenvolvimento

Desenvolvido por:

| Nome | RM | GitHub |
|------|-------|---------|
| **Luigi Ferrara Sinno** | RM98047 | [@luigiferrarasinno](https://github.com/luigiferrarasinno) |
| **Davi Passanha de Sousa Guerra** | RM551605 | |
| **Cauã Gonçalves de Jesus** | RM97648 | |
| **Luan Silveira Macea** | RM98290 | |
| **Rui Amorim Siqueira** | RM98436 | |

**🔗 Repositório:** [api_login_java_spring_boot](https://github.com/luigiferrarasinno/api_login_java_spring_boot)

---

## 🎯 Como Usar Esta Documentação

### 🚀 **Para Começar Rapidamente:**
1. **Execute** a aplicação: `mvnw spring-boot:run`
2. **Teste login** no Postman com as credenciais acima
3. **Explore** no Swagger: `http://localhost:8080/swagger-ui.html`

### 📚 **Para Aprender Específico:**
1. **Escolha o módulo** na tabela de guias
2. **Siga os exemplos** completos no guia
3. **Teste** no Postman ou Swagger

### 🔍 **Para Desenvolver:**
1. **Analise** a estrutura de pastas acima
2. **Entenda** o padrão MVC implementado
3. **Veja** os dados inicializados automaticamente

---

**🚀 Sistema completo, documentado e pronto para uso!**

**📖 Consulte os guias específicos para instruções detalhadas de cada funcionalidade.**