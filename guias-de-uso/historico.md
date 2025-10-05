# Guia de Uso - API de Histórico de Investimentos

## Visão Geral
A API de Histórico permite gerenciar e acompanhar o desempenho dos investimentos dos usuários ao longo do tempo. Cada registro armazena o valor investido e o valor de r### 4. Análise por Período
```
GET /api/historico/periodo?usuarioId=2&dataInicio=2024-01&dataFim=2024-10
```

### 5. 🆕 Histórico dos Últimos 12 Meses com Lucro/Prejuízo
```
GET /api/historico/ultimos12meses/2
```no para um investimento específico em um determinado mês/ano.

## 🔐 Controle de Acesso e Segurança

### Usuários Comuns (ROLE_USER)
- ✅ Podem criar, visualizar, editar e deletar **apenas seus próprios** registros de histórico
- ✅ Podem consultar seus próprios investimentos com lucro/prejuízo
- ❌ **NÃO** podem acessar dados de outros usuários
- ❌ **NÃO** podem fazer consultas gerais (por investimento, mês/ano)

### Administradores (ROLE_ADMIN)
- ✅ Podem criar, visualizar, editar e deletar histórico de **qualquer usuário**
- ✅ Podem fazer **todas as consultas**, incluindo:
  - Buscar por investimento específico
  - Consultas gerais por mês/ano
  - Dados de qualquer usuário
- ✅ Acesso total a toda a API

### Autenticação Obrigatória
🔑 **TODOS** os endpoints requerem autenticação válida (token JWT)

## Estrutura da Tabela Historico
- **ID**: Identificador único do registro
- **ID Investimento**: Referência ao investimento
- **ID Usuário**: Referência ao usuário proprietário
- **Total Investido**: Valor total investido no período
- **Total Retornando**: Valor total de retorno do investimento
- **Mês/Ano de Registro**: Período de referência do registro
- **Data de Registro**: Data de criação do registro no sistema

## Endpoints Disponíveis

### 1. Criar Histórico
**POST** `/api/historico`

Cria um novo registro de histórico. Se já existir um registro para o mesmo usuário, investimento e mês/ano, o registro será atualizado.

**Corpo da Requisição:**
```json
{
    "investimentoId": 1,
    "usuarioId": 1,
    "totalInvestido": 1000.00,
    "totalRetornando": 1100.00,
    "mesAnoRegistro": "2024-10"
}
```

**Resposta (201 Created):**
```json
{
    "id": 1,
    "investimentoId": 1,
    "nomeInvestimento": "Ações Petrobras",
    "simboloInvestimento": "PETR4",
    "usuarioId": 1,
    "nomeUsuario": "João Silva",
    "totalInvestido": 1000.00,
    "totalRetornando": 1100.00,
    "retorno": 100.00,
    "percentualRetorno": 10.00,
    "lucroPrejuizo": "LUCRO",
    "mesAnoRegistro": "2024-10",
    "dataRegistro": "2024-10-05"
}
```

### 2. Buscar Histórico por ID
**GET** `/api/historico/{id}`

Busca um registro específico de histórico pelo ID.

### 3. Buscar Histórico por Usuário
**GET** `/api/historico/usuario/{usuarioId}`
🔐 **Permissão:** Próprio usuário ou Admin

Busca todo o histórico de investimentos de um usuário, ordenado pela data de registro mais recente.

### 4. Buscar Histórico por Investimento
**GET** `/api/historico/investimento/{investimentoId}`
🔐 **Permissão:** Apenas Admin

Busca todo o histórico de um investimento específico.

### 5. Buscar Histórico por Período
**GET** `/api/historico/periodo?usuarioId={id}&dataInicio={yyyy-MM}&dataFim={yyyy-MM}`
🔐 **Permissão:** Próprio usuário ou Admin

Busca o histórico de um usuário em um período específico.

**Exemplo:**
```
GET /api/historico/periodo?usuarioId=1&dataInicio=2024-01&dataFim=2024-12
```

### 6. Buscar Histórico por Mês/Ano
**GET** `/api/historico/mes-ano/{yyyy-MM}`
🔐 **Permissão:** Apenas Admin

Busca todos os registros de um mês/ano específico.

**Exemplo:**
```
GET /api/historico/mes-ano/2024-10
```

### 7. Buscar Investimentos com Lucro
**GET** `/api/historico/lucro/{usuarioId}`
🔐 **Permissão:** Próprio usuário ou Admin

Busca todos os investimentos que tiveram performance positiva (lucro) para um usuário.

### 8. Buscar Investimentos com Prejuízo
**GET** `/api/historico/prejuizo/{usuarioId}`
🔐 **Permissão:** Próprio usuário ou Admin

Busca todos os investimentos que tiveram performance negativa (prejuízo) para um usuário.

### 9. Atualizar Histórico
**PUT** `/api/historico/{id}`
🔐 **Permissão:** Proprietário do histórico ou Admin

Atualiza um registro de histórico existente.

**Corpo da Requisição:** (mesmo formato do POST)

### 10. Deletar Histórico
**DELETE** `/api/historico/{id}`
🔐 **Permissão:** Proprietário do histórico ou Admin

Remove um registro de histórico.

**Resposta:** 204 No Content

### 11. 🆕 Buscar Histórico dos Últimos 12 Meses
**GET** `/api/historico/ultimos12meses/{usuarioId}`
🔐 **Permissão:** Próprio usuário ou Admin

Busca o histórico de investimentos de um usuário nos últimos 12 meses com indicador de lucro/prejuízo.

**Exemplo:**
```
GET /api/historico/ultimos12meses/2
```

**Resposta:**
```json
[
    {
        "id": 15,
        "investimentoId": 3,
        "nomeInvestimento": "Itaú Unibanco",
        "simboloInvestimento": "ITUB4",
        "usuarioId": 2,
        "nomeUsuario": "João Silva",
        "totalInvestido": 2000.00,
        "totalRetornando": 2150.00,
        "retorno": 150.00,
        "percentualRetorno": 7.50,
        "lucroPrejuizo": "LUCRO",
        "mesAnoRegistro": "2024-10",
        "dataRegistro": "2024-10-15"
    },
    {
        "id": 12,
        "investimentoId": 1,
        "nomeInvestimento": "Petróleo Brasileiro",
        "simboloInvestimento": "PETR4",
        "usuarioId": 2,
        "nomeUsuario": "João Silva",
        "totalInvestido": 1500.00,
        "totalRetornando": 1400.00,
        "retorno": -100.00,
        "percentualRetorno": -6.67,
        "lucroPrejuizo": "PREJUIZO",
        "mesAnoRegistro": "2024-09",
        "dataRegistro": "2024-09-20"
    }
]
```

## Funcionalidades Especiais

### Cálculos Automáticos
- **Retorno**: Calculado automaticamente como `totalRetornando - totalInvestido`
- **Percentual de Retorno**: Calculado como `((totalRetornando - totalInvestido) / totalInvestido) * 100`
- **🆕 Lucro/Prejuízo**: Determinado automaticamente baseado no retorno:
  - `"LUCRO"` se retorno >= 0
  - `"PREJUIZO"` se retorno < 0

### Prevenção de Duplicatas
O sistema verifica automaticamente se já existe um registro para:
- Mesmo usuário
- Mesmo investimento  
- Mesmo mês/ano

Se existir, o registro é atualizado ao invés de criar um duplicado.

### Validações
- **Total Investido**: Deve ser maior ou igual a zero
- **Usuario ID**: Deve existir na base de dados
- **Investimento ID**: Deve existir na base de dados
- **Mês/Ano**: Deve estar no formato correto (YYYY-MM)

## Casos de Uso Comuns

### 1. Registrar Performance Mensal
```json
POST /api/historico
{
    "investimentoId": 5,
    "usuarioId": 2,
    "totalInvestido": 5000.00,
    "totalRetornando": 5250.00,
    "mesAnoRegistro": "2024-10"
}
```

### 2. Acompanhar Evolução de um Investimento
```
GET /api/historico/investimento/5
```

### 3. Análise de Performance de um Usuário
```
GET /api/historico/usuario/2
```

### 4. Relatório de Lucros/Perdas
```
GET /api/historico/lucro/2
GET /api/historico/prejuizo/2
```

### 5. Análise por Período
```
GET /api/historico/periodo?usuarioId=2&dataInicio=2024-01&dataFim=2024-10
```

## Observações Importantes

1. **Formato de Data**: Use o formato `YYYY-MM` para mês/ano (ex: "2024-10")
2. **Valores Monetários**: Use formato decimal com até 2 casas decimais
3. **Relacionamentos**: Os IDs de usuário e investimento devem existir previamente
4. **Ordenação**: Os resultados são ordenados pela data de registro mais recente por padrão
5. **Transações**: Operações de criação e atualização são transacionais para garantir consistência

## Status Codes Comuns

- **200 OK**: Operação bem-sucedida
- **201 Created**: Histórico criado com sucesso
- **204 No Content**: Histórico deletado com sucesso
- **400 Bad Request**: Dados inválidos na requisição
- **401 Unauthorized**: Token de autenticação inválido ou ausente
- **403 Forbidden**: Acesso negado - usuário não tem permissão para esta operação
- **404 Not Found**: Recurso não encontrado (histórico, usuário ou investimento)
- **500 Internal Server Error**: Erro interno do servidor

## Exemplos de Respostas de Erro de Segurança

### Tentativa de acessar dados de outro usuário (403 Forbidden)
```json
{
    "error": "Access Denied",
    "message": "Acesso negado: Você não tem permissão para acessar os dados deste usuário",
    "timestamp": "2024-10-05T10:30:00Z",
    "status": 403
}
```

### Usuário comum tentando acessar endpoint de admin (403 Forbidden)
```json
{
    "error": "Access Denied", 
    "message": "Acesso negado: Esta operação requer privilégios de administrador",
    "timestamp": "2024-10-05T10:30:00Z",
    "status": 403
}
```