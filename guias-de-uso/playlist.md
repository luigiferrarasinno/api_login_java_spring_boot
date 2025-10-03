# 🎵 Guia Completo do Sistema de Playlists

> **"Spotify para Investimentos"** - Crie, compartilhe e descubra playlists de investimentos!

## 📋 Índice
1. [Visão Geral](#-visão-geral)
2. [Como Começar](#-como-começar)
3. [Funcionalidades](#-funcionalidades)
4. [API Reference](#-api-reference)
5. [Casos de Uso](#-casos-de-uso)
6. [Exemplos Práticos](#-exemplos-práticos)
7. [FAQ](#-faq)

---

## 🎯 Visão Geral

O **Sistema de Playlists** permite que usuários criem coleções organizadas de investimentos, similar ao funcionamento do Spotify com músicas. É uma ferramenta social e colaborativa para compartilhar estratégias de investimento.

### 🏗️ Arquitetura
```
📁 Playlist Module
├── 📝 Model (Playlist.java)
├── 🗄️ Repository (PlaylistRepository.java) 
├── 🔧 Service (PlaylistService.java)
├── 🌐 Controller (PlaylistController.java)
└── 📦 DTOs (Request/Response)
```

### 🔐 Sistema de Permissões
- **🎨 Criador**: Controle total (editar, excluir, gerenciar seguidores)
- **🤝 Colaborador**: Adicionar/remover investimentos (se permitido)
- **👀 Seguidor**: Visualizar e acompanhar atualizações

---

## 🚀 Como Começar

### 1️⃣ Pré-requisitos
- Sistema iniciado com `mvn spring-boot:run`
- Token JWT obtido via login
- Swagger UI disponível em: http://localhost:8080/swagger-ui.html

### 2️⃣ Playlists Criadas Automaticamente

> ⚠️ **4 playlists de exemplo já criadas no sistema:**

| Playlist | Criador | Tipo | Colaborativa | Conteúdo | Seguidores |
|----------|---------|------|--------------|----------|------------|
| **"Top Dividendos 2024 💰"** | Admin Sistema | Pública | ❌ | PETR4, VALE3, ITUB4, BBAS3 | João, Maria |
| **"Minha Carteira Conservadora 🛡️"** | Maria Investidora | Privada | ❌ | ITUB4, BBAS3, ABEV3 | - |
| **"FIIs para Iniciantes 🏢"** | Admin Sistema | Pública | ✅ | HGLG11, MXRF11, XPLG11 | João, Maria |
| **"Apostas Arriscadas 🚀"** | João Silva | Pública | ✅ | VALE3 | Admin |

### 2️⃣ Primeiro Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@user.com",
    "senha": "123456"
  }'
```

### 3️⃣ Sua Primeira Playlist
```bash
curl -X POST http://localhost:8080/playlists \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {seu-jwt-token}" \
  -d '{
    "nome": "Minha Primeira Playlist",
    "descricao": "Investimentos para iniciantes",
    "publica": true,
    "permiteColaboracao": false
  }'
```

---

## 🎵 Funcionalidades

### 📝 Gerenciamento de Playlists

| Funcionalidade | Descrição | Endpoint |
|----------------|-----------|----------|
| 🆕 **Criar** | Nova playlist personalizada | `POST /playlists` |
| 📋 **Listar** | Suas playlists e seguidas | `GET /playlists/minhas` |
| 👁️ **Visualizar** | Detalhes completos | `GET /playlists/{id}` |
| ✏️ **Editar** | Atualizar informações | `PUT /playlists/{id}` |
| 🗑️ **Excluir** | Remover permanentemente | `DELETE /playlists/{id}` |

### 🎶 Gerenciamento de Conteúdo

| Ação | Descrição | Endpoint |
|------|-----------|----------|
| ➕ **Adicionar** | Investimento à playlist | `POST /playlists/{id}/investimentos` |
| ➖ **Remover** | Investimento da playlist | `DELETE /playlists/{id}/investimentos/{invId}` |

### 👥 Funcionalidades Sociais

| Ação | Descrição | Endpoint |
|------|-----------|----------|
| ❤️ **Seguir** | Acompanhar playlist | `POST /playlists/{id}/seguir` |
| 💔 **Parar de Seguir** | Deixar de seguir | `DELETE /playlists/{id}/seguir` |
| 📤 **Compartilhar** | Enviar para usuário | `POST /playlists/{id}/compartilhar` |
| 🔍 **Buscar** | Encontrar por nome | `GET /playlists/buscar?nome={termo}` |
| 🌍 **Descobrir** | Playlists públicas | `GET /playlists/publicas` |

---

## 📚 API Reference

### 🎵 Criar Playlist
```http
POST /playlists
Content-Type: application/json
Authorization: Bearer {token}

{
  "nome": "Nome da Playlist",
  "descricao": "Descrição opcional",
  "publica": true,
  "permiteColaboracao": false
}
```

**Resposta:**
```json
{
  "mensagem": "Playlist 'Nome da Playlist' criada com sucesso!",
  "playlistId": 1,
  "nomePlaylist": "Nome da Playlist", 
  "timestamp": "2024-10-03T10:30:00",
  "status": "sucesso"
}
```

### 📋 Listar Playlists
```http
GET /playlists/minhas
Authorization: Bearer {token}
```

**Resposta:**
```json
[
  {
    "id": 1,
    "nome": "Minha Carteira Conservadora",
    "descricao": "Investimentos de baixo risco",
    "criadorNome": "João Silva",
    "publica": false,
    "totalInvestimentos": 5,
    "totalSeguidores": 0,
    "isCriador": true,
    "isFollowing": false
  }
]
```

### ➕ Adicionar Investimento
```http
POST /playlists/{id}/investimentos
Content-Type: application/json
Authorization: Bearer {token}

{
  "investimentoId": 2
}
```

### ❤️ Seguir Playlist
```http
POST /playlists/{id}/seguir
Authorization: Bearer {token}
```

---

## 💡 Casos de Uso

### 🎓 Para Investidores Iniciantes

#### 📚 Aprender com Especialistas
1. **Explore playlists públicas** de investidores experientes
2. **Siga playlists educativas** como "FIIs para Iniciantes"
3. **Acompanhe atualizações** de suas playlists favoritas

```bash
# Descobrir playlists públicas
curl -X GET http://localhost:8080/playlists/publicas \
  -H "Authorization: Bearer {token}"

# Seguir uma playlist interessante
curl -X POST http://localhost:8080/playlists/1/seguir \
  -H "Authorization: Bearer {token}"
```

#### 🎯 Organizar Seus Estudos
1. **Crie playlists temáticas**: "Ações de Bancos", "FIIs de Shopping"
2. **Mantenha privadas** enquanto aprende
3. **Torne públicas** quando ganhar confiança

```bash
# Playlist de estudos privada
curl -X POST http://localhost:8080/playlists \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "nome": "📚 Estudando FIIs",
    "descricao": "FIIs que estou pesquisando",
    "publica": false,
    "permiteColaboracao": false
  }'
```

### 🏆 Para Investidores Experientes

#### 📢 Compartilhar Conhecimento
1. **Crie playlists educativas** públicas
2. **Permita colaboração** da comunidade
3. **Compartilhe diretamente** com iniciantes

```bash
# Playlist colaborativa pública
curl -X POST http://localhost:8080/playlists \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "nome": "🎓 Dividendos Consistentes",
    "descricao": "Ações com histórico sólido de dividendos",
    "publica": true,
    "permiteColaboracao": true
  }'
```

#### 🎯 Estratégias Avançadas
1. **Organize por setores**: "Tech", "Energia", "Financeiro"
2. **Crie playlists sazonais**: "Aposentadoria 2030"
3. **Monitore performance** de diferentes estratégias

### 👨‍👩‍👧‍👦 Para Grupos e Comunidades

#### 🤝 Colaboração em Grupo
1. **Playlist do grupo de investimentos** da empresa
2. **Competição** entre amigos: "Melhor Carteira 2024"
3. **Educação familiar**: "Investimentos para Filhos"

```bash
# Compartilhar com membro do grupo
curl -X POST http://localhost:8080/playlists/1/compartilhar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "emailUsuario": "amigo@email.com"
  }'
```

---

## 🛠️ Exemplos Práticos

### 🎵 Playlists de Exemplo (Criadas Automaticamente)

#### 1. **"Top Dividendos 2024 💰"** 
- **Tipo**: Pública
- **Criador**: Admin Sistema
- **Foco**: Melhores pagadores de dividendos
- **Conteúdo**: PETR4, VALE3, ITUB4, BBAS3

```bash
# Visualizar detalhes
curl -X GET http://localhost:8080/playlists/1 \
  -H "Authorization: Bearer {token}"
```

#### 2. **"FIIs para Iniciantes 🏢"**
- **Tipo**: Pública Colaborativa ✨
- **Criador**: Admin Sistema
- **Foco**: Fundos Imobiliários introdutórios
- **Conteúdo**: HGLG11, MXRF11, XPLG11

```bash
# Adicionar sugestão (se você for seguidor)
curl -X POST http://localhost:8080/playlists/3/investimentos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"investimentoId": 5}'
```

#### 3. **"Apostas Arriscadas 🚀"**
- **Tipo**: Pública Colaborativa
- **Criador**: João Silva
- **Foco**: Alto risco, alto retorno
- **Aviso**: ⚠️ Invista por sua conta e risco!

### 🔍 Casos de Busca

```bash
# Buscar playlists sobre dividendos
curl -X GET "http://localhost:8080/playlists/buscar?nome=dividendos" \
  -H "Authorization: Bearer {token}"

# Buscar playlists sobre FIIs  
curl -X GET "http://localhost:8080/playlists/buscar?nome=fii" \
  -H "Authorization: Bearer {token}"
```

---

## ❓ FAQ

### 🤔 **Posso editar playlists de outros usuários?**
**Resposta**: Apenas o criador pode editar informações da playlist (nome, descrição, visibilidade). Colaboradores podem adicionar/remover investimentos se a playlist permitir colaboração.

### 🤔 **Como saber se posso colaborar numa playlist?**
**Resposta**: Verifique se:
1. A playlist `permiteColaboracao: true`
2. Você está seguindo a playlist
3. Você não é o criador (criador sempre pode)

### 🤔 **Playlists privadas podem ser compartilhadas?**
**Resposta**: Sim! O criador pode compartilhar diretamente com usuários específicos através do endpoint `/compartilhar`.

### 🤔 **Posso seguir minha própria playlist?**
**Resposta**: Não é necessário. Você já tem acesso total às suas playlists através de `/playlists/minhas`.

### 🤔 **O que acontece se eu parar de seguir uma playlist colaborativa?**
**Resposta**: Você perde a capacidade de adicionar/remover investimentos e não recebe mais atualizações.

### 🤔 **Como remover um seguidor da minha playlist?**
**Resposta**: Use o endpoint `DELETE /playlists/{id}/seguidores?emailSeguidor={email}`.

### 🤔 **Existe limite de investimentos por playlist?**
**Resposta**: Não há limite técnico definido. Recomenda-se manter entre 5-20 investimentos para facilitar análise.

### 🤔 **Posso renomear uma playlist depois de criada?**
**Resposta**: Sim! Use `PUT /playlists/{id}` para atualizar nome, descrição e configurações.

---

## 🚀 Próximos Passos

### 🔜 Funcionalidades Planejadas
- [ ] **📊 Analytics**: Métricas de performance das playlists
- [ ] **🔔 Notificações**: Alertas quando playlists seguidas são atualizadas  
- [ ] **💬 Comentários**: Sistema de comentários nas playlists
- [ ] **🏷️ Tags**: Categorização com tags personalizadas
- [ ] **📥 Import/Export**: Backup e restauração de playlists
- [ ] **📈 Histórico**: Acompanhar evolução das playlists

### 🎯 Dicas de Uso
1. **📝 Nomes descritivos**: Use nomes claros como "Dividendos Mensais" ao invés de "Lista 1"
2. **📄 Descrições detalhadas**: Explique a estratégia e critérios da playlist
3. **🔄 Mantenha atualizado**: Revise periodicamente suas playlists
4. **🤝 Colabore**: Participe de playlists colaborativas para aprender
5. **🎯 Seja específico**: Playlists focadas são mais úteis que genéricas

---

## 🎉 Divirta-se Criando suas Playlists!

O sistema está pronto para uso! Explore, crie e compartilhe suas estratégias de investimento de forma social e colaborativa. 

**Happy Investing! 🎵💰**

---

*📧 Dúvidas? Abra uma issue ou entre em contato!*