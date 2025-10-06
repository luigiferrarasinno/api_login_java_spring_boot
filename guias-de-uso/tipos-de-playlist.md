# 🎵 Guia dos Tipos de Playlist - Sistema de Playlists de Investimentos

## Tipos de Playlist Disponíveis

### 1. 📱 PRIVADA
- **Visibilidade**: Apenas o criador pode ver e acessar
- **Descoberta**: Não aparece em buscas ou listas públicas
- **Acesso**: Somente o criador
- **Uso**: Para playlists pessoais e estratégias confidenciais

### 2. 🌍 PUBLICA
- **Visibilidade**: Visível para todos os usuários da plataforma
- **Descoberta**: Aparece em buscas e listas de playlists públicas
- **Acesso**: Qualquer usuário pode ver e seguir
- **Uso**: Para compartilhar estratégias com toda a comunidade

### 3. 👥 COMPARTILHADA ⭐ **NOVO!**
- **Visibilidade**: Visível apenas para usuários específicos convidados
- **Descoberta**: Não aparece em buscas públicas
- **Acesso**: Apenas criador e usuários convidados
- **Uso**: Para compartilhar com usuários selecionados (ex: clientes VIP, equipe, amigos)

## Funcionalidades por Tipo

| Ação | PRIVADA | PUBLICA | COMPARTILHADA |
|------|---------|---------|---------------|
| Criar | ✅ | ✅ | ✅ |
| Ver (criador) | ✅ | ✅ | ✅ |
| Ver (outros) | ❌ | ✅ | 👥 Apenas convidados |
| Buscar | ❌ | ✅ | ❌ |
| Seguir | ❌ | ✅ | 👥 Apenas convidados |
| Colaborar | 🔧 Se permitir | 🔧 Se permitir | 🔧 Se permitir |
| Convidar usuários | ❌ | ❌ | ✅ |

## Novos Endpoints

### Listar Playlists Compartilhadas Comigo
```
GET /playlists/compartilhadas
```
Retorna todas as playlists COMPARTILHADAS que foram especificamente compartilhadas com você.

### Tornar Playlist Compartilhada
```
PUT /playlists/{id}/tornar-compartilhada
```
Converte uma playlist PRIVADA ou PUBLICA para COMPARTILHADA.

### Compartilhar com Usuário Específico
```
POST /playlists/{id}/compartilhar-com
Body: {"emailUsuario": "usuario@email.com"}
```
Compartilha a playlist com um usuário específico e converte automaticamente para COMPARTILHADA.

## Casos de Uso Práticos

### 💼 Scenario 1: Consultoria de Investimentos
```
1. Criar playlist PRIVADA para cada cliente
2. Converter para COMPARTILHADA
3. Compartilhar apenas com o cliente específico
```

### 👨‍👩‍👧‍👦 Scenario 2: Família/Grupo de Amigos
```
1. Criar playlist COMPARTILHADA "Estratégia Familiar"
2. Convidar membros da família
3. Permitir colaboração para todos contribuírem
```

### 🏢 Scenario 3: Equipe de Trabalho
```
1. Criar playlist COMPARTILHADA "Análise Setorial Tech"
2. Convidar analistas da equipe
3. Colaboração ativa para research em grupo
```

### 📚 Scenario 4: Educação/Mentoria
```
1. Playlist PUBLICA para conteúdo geral
2. Playlist COMPARTILHADA para alunos premium
3. Playlist PRIVADA para preparação de material
```

## Exemplos de Requests

### Criar Playlist Compartilhada
```json
POST /playlists
{
  "nome": "Carteira VIP Exclusiva",
  "descricao": "Estratégias premium para clientes selecionados",
  "tipo": "COMPARTILHADA",
  "permiteColaboracao": false
}
```

### Atualizar Tipo de Playlist
```json
PUT /playlists/1
{
  "tipo": "COMPARTILHADA"
}
```

### Compartilhar com Usuário
```json
POST /playlists/1/compartilhar-com
{
  "emailUsuario": "cliente-vip@email.com"
}
```

## Migração de Dados Existentes

Para playlists existentes:
- `publica: true` → `tipo: "PUBLICA"`
- `publica: false` → `tipo: "PRIVADA"`

Execute o script `migration_playlist_tipo.sql` para migrar dados existentes.

## Compatibilidade

Os endpoints antigos continuam funcionando:
- `getPublica()` retorna `true` se `tipo == "PUBLICA"`
- Filtros por "playlists públicas" agora filtram por `tipo == "PUBLICA"`

## Benefícios do Novo Sistema

1. **🎯 Controle Granular**: Compartilhe com quem você quiser
2. **🔒 Privacidade**: Playlists COMPARTILHADAS não aparecem publicamente
3. **💼 Casos Comerciais**: Ideal para consultores e educadores
4. **👥 Colaboração Seletiva**: Permita colaboração apenas entre pessoas escolhidas
5. **📊 Organização**: Separe conteúdo público, privado e compartilhado

---

**🎵 Agora você tem controle total sobre quem pode ver suas playlists de investimentos!**