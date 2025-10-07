# 🐳 Guia Docker - API de Investimentos Brasileira

## 📋 Pré-requisitos

- **Docker** instalado ([Download Docker Desktop](https://www.docker.com/products/docker-desktop))
- **Docker Compose** (já incluído no Docker Desktop)

## 🚀 Como Executar

### Opção 1: Usando Docker Compose (RECOMENDADO)

#### 1️⃣ Construir e Iniciar
```bash
docker-compose up --build
```

#### 2️⃣ Iniciar (após primeira build)
```bash
docker-compose up
```

#### 3️⃣ Iniciar em background (detached mode)
```bash
docker-compose up -d
```

#### 4️⃣ Parar a aplicação
```bash
docker-compose down
```

#### 5️⃣ Parar e remover volumes
```bash
docker-compose down -v
```

---

### Opção 2: Usando Docker diretamente

#### 1️⃣ Construir a imagem
```bash
docker build -t api-investimentos-brasileira .
```

#### 2️⃣ Executar o container
```bash
docker run -d -p 8080:8080 --name api-investimentos api-investimentos-brasileira
```

#### 3️⃣ Ver logs
```bash
docker logs -f api-investimentos
```

#### 4️⃣ Parar o container
```bash
docker stop api-investimentos
```

#### 5️⃣ Remover o container
```bash
docker rm api-investimentos
```

---

## 🔍 Verificar Status

### Ver containers rodando
```bash
docker ps
```

### Ver todos os containers (incluindo parados)
```bash
docker ps -a
```

### Ver logs em tempo real
```bash
# Docker Compose
docker-compose logs -f

# Docker direto
docker logs -f api-investimentos
```

### Healthcheck
```bash
# Verificar saúde do container
docker inspect --format='{{.State.Health.Status}}' api-investimentos-brasileira
```

---

## 🌐 Acessar a Aplicação

Após iniciar o container:

- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### Credenciais H2:
- **JDBC URL:** `jdbc:h2:mem:fellerdb`
- **Username:** `Admin`
- **Password:** `Fiap123`

---

## 🔧 Comandos Úteis

### Executar comando dentro do container
```bash
# Docker Compose
docker-compose exec api-investimentos sh

# Docker direto
docker exec -it api-investimentos sh
```

### Ver uso de recursos
```bash
docker stats api-investimentos-brasileira
```

### Reconstruir sem cache
```bash
docker-compose build --no-cache
docker-compose up
```

### Limpar imagens não utilizadas
```bash
docker image prune -a
```

---

## 📊 Estrutura do Projeto Docker

```
.
├── Dockerfile              # Definição da imagem
├── docker-compose.yml      # Orquestração de containers
├── .dockerignore          # Arquivos ignorados no build
└── DOCKER.md              # Este guia
```

---

## 🏗️ Arquitetura Docker

### Multi-stage Build

O Dockerfile usa **multi-stage build** para otimizar o tamanho da imagem:

1. **Stage 1 (build)**: Maven + JDK 17
   - Compila o código
   - Gera o JAR

2. **Stage 2 (runtime)**: JRE 17 Alpine
   - Imagem mínima
   - Apenas JRE necessário
   - Copia JAR compilado

### Benefícios:
- ✅ Imagem final pequena (~200MB vs ~700MB)
- ✅ Apenas dependências de runtime
- ✅ Mais seguro (menos surface de ataque)
- ✅ Build mais rápido com cache

---

## ⚙️ Variáveis de Ambiente

Você pode customizar o comportamento via variáveis de ambiente:

```yaml
# No docker-compose.yml
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - JAVA_OPTS=-Xmx512m -Xms256m
  - SERVER_PORT=8080
```

Ou via linha de comando:

```bash
docker run -e JAVA_OPTS="-Xmx1g" -p 8080:8080 api-investimentos-brasileira
```

---

## 🩺 Healthcheck

O container inclui healthcheck automático:

- **Intervalo**: 30 segundos
- **Timeout**: 10 segundos
- **Retries**: 5 tentativas
- **Start Period**: 40 segundos (tempo de inicialização)

Status possíveis:
- `starting` - Iniciando
- `healthy` - Saudável
- `unhealthy` - Não saudável

---

## 🐛 Troubleshooting

### Container não inicia
```bash
# Ver logs detalhados
docker-compose logs

# Verificar se a porta 8080 está livre
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac
```

### Build falha
```bash
# Limpar cache e rebuildar
docker-compose down
docker system prune -a
docker-compose build --no-cache
```

### Aplicação lenta
```bash
# Aumentar memória Java
docker-compose down
# Editar docker-compose.yml: JAVA_OPTS=-Xmx1g -Xms512m
docker-compose up
```

### Container para sozinho
```bash
# Verificar logs
docker logs api-investimentos-brasileira

# Verificar healthcheck
docker inspect api-investimentos-brasileira | grep -A 10 Health
```

---

## 📦 Publicar Imagem

### Docker Hub

```bash
# Login
docker login

# Tag
docker tag api-investimentos-brasileira luigiferrarasinno/api-investimentos:latest

# Push
docker push luigiferrarasinno/api-investimentos:latest
```

### GitHub Container Registry

```bash
# Login
echo $CR_PAT | docker login ghcr.io -u USERNAME --password-stdin

# Tag
docker tag api-investimentos-brasileira ghcr.io/luigiferrarasinno/api-investimentos:latest

# Push
docker push ghcr.io/luigiferrarasinno/api-investimentos:latest
```

---

## 🔒 Boas Práticas

✅ **Implementadas:**
- Multi-stage build
- Imagem base Alpine (mínima)
- .dockerignore configurado
- Healthcheck ativo
- Non-root user (Alpine default)
- Variáveis de ambiente
- Restart policy

🎯 **Recomendações Futuras:**
- [ ] Adicionar banco de dados PostgreSQL
- [ ] Configurar volumes persistentes
- [ ] Adicionar Redis para cache
- [ ] Configurar nginx como reverse proxy
- [ ] Implementar Docker Swarm ou Kubernetes

---

## 📝 Logs

### Ver últimas 100 linhas
```bash
docker-compose logs --tail=100
```

### Ver logs de um serviço específico
```bash
docker-compose logs api-investimentos
```

### Salvar logs em arquivo
```bash
docker-compose logs > logs.txt
```

---

## 🚀 Deploy em Produção

### Railway
```bash
# Instalar Railway CLI
npm install -g @railway/cli

# Login
railway login

# Deploy
railway up
```

### Heroku
```bash
# Login
heroku login

# Criar app
heroku create api-investimentos-brasileira

# Push
git push heroku main
```

### AWS ECS / Azure Container Instances
```bash
# Seguir documentação específica da plataforma
```

---

## 🎓 Comandos para Desenvolvimento

### Rebuild após mudanças no código
```bash
docker-compose down
docker-compose build
docker-compose up
```

### Live reload (requer Spring DevTools)
```bash
# Montar volume do código
docker-compose -f docker-compose.dev.yml up
```

### Debug remoto
```bash
# Adicionar ao docker-compose.yml:
# JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
# ports:
#   - "5005:5005"
```

---

## ✅ Checklist de Deploy

- [ ] Dockerfile criado
- [ ] docker-compose.yml configurado
- [ ] .dockerignore adicionado
- [ ] Build local funcionando
- [ ] Healthcheck testado
- [ ] Logs acessíveis
- [ ] Variáveis de ambiente configuradas
- [ ] Documentação atualizada
- [ ] Imagem publicada (opcional)

---

**🐳 Aplicação dockerizada e pronta para deploy!**
