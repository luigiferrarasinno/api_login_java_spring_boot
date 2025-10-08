# Configuração de CORS - Instruções Importantes

## O que foi feito?

Foi adicionada a configuração de CORS (Cross-Origin Resource Sharing) para permitir que o frontend acesse a API de diferentes origens.

### Arquivos modificados:

1. **CorsConfig.java** (NOVO) - Configuração centralizada de CORS
2. **SecurityConfig.java** - Atualizado para incluir a configuração de CORS

## Configuração Atual

### Desenvolvimento (Configuração Atual)
A configuração atual permite **todas as origens** com `allowedOriginPatterns("*")`. Isso é útil para desenvolvimento e testes.

### IMPORTANTE: Segurança em Produção

⚠️ **ATENÇÃO**: Para produção, você DEVE especificar as URLs exatas do seu frontend por questões de segurança!

#### Como configurar para produção:

Edite o arquivo `src/main/java/com/example/demo/config/CorsConfig.java` e substitua:

```java
configuration.setAllowedOriginPatterns(Arrays.asList("*"));
```

Por:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "https://seu-frontend.com",
    "https://www.seu-frontend.com",
    "https://seu-app.vercel.app"  // Se usar Vercel, por exemplo
));
```

**OU** use variável de ambiente:

```java
String[] allowedOrigins = System.getenv("ALLOWED_ORIGINS") != null 
    ? System.getenv("ALLOWED_ORIGINS").split(",")
    : new String[]{"http://localhost:3000"}; // fallback para dev

configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
```

E configure a variável de ambiente `ALLOWED_ORIGINS` no seu servidor:
```
ALLOWED_ORIGINS=https://seu-frontend.com,https://www.seu-frontend.com
```

## O que a configuração permite:

✅ Requisições de qualquer origem (atualmente - deve ser restringido em produção)
✅ Todos os métodos HTTP (GET, POST, PUT, DELETE, PATCH, OPTIONS)
✅ Todos os headers personalizados
✅ Credenciais (cookies, tokens de autorização)
✅ Headers Authorization e Content-Type expostos
✅ Cache de requisições preflight por 1 hora

## Próximos passos:

1. **Fazer rebuild da aplicação**
2. **Fazer redeploy**
3. **Testar as requisições do frontend**
4. **Configurar origens específicas para produção** (importante!)

## Comandos para rebuild (escolha um):

### Maven:
```bash
mvn clean package
```

### Maven Wrapper (Windows):
```powershell
.\mvnw.cmd clean package
```

### Docker (se estiver usando):
```bash
docker-compose build
docker-compose up -d
```

## Verificação

Após o deploy, verifique se os headers CORS estão sendo retornados:

```bash
curl -H "Origin: https://seu-frontend.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type, Authorization" \
  -X OPTIONS \
  --verbose \
  https://sua-api.com/playlists
```

Você deve ver headers como:
- `Access-Control-Allow-Origin`
- `Access-Control-Allow-Methods`
- `Access-Control-Allow-Headers`
- `Access-Control-Allow-Credentials`

## Troubleshooting

### Ainda recebe erro de CORS?

1. Verifique se fez rebuild e redeploy
2. Limpe o cache do navegador
3. Verifique se está usando HTTPS em produção
4. Confirme que o header `Authorization` está sendo enviado corretamente
5. Verifique os logs do servidor para erros de configuração

### Erro "Credentials mode is 'include'"?

Se usar `credentials: 'include'` no fetch, não pode usar `"*"` em `allowedOrigins`. Use URLs específicas.

## Mais informações

- [Spring CORS Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-cors)
- [MDN CORS Guide](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
