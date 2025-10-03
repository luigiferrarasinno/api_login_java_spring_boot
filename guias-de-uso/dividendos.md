# 💰 Guia de Dividendos - Endpoints

Este guia cobre todos os endpoints relacionados ao sistema de dividendos administrativo.

---

## 🔐 Autenticação

Todos os endpoints requerem um token JWT no cabeçalho:
```
Authorization: Bearer SEU_TOKEN_JWT
```

---

## 🧠 Sistema de Permissões

| Endpoint                                           | USER | ADMIN |
| -------------------------------------------------- | ---- | ----- |
| `GET /dividendos/pendentes`                        | ❌    | ✅     |
| `POST /dividendos/liberar/{investimentoId}`        | ❌    | ✅     |
| `POST /dividendos/liberar-todos`                   | ❌    | ✅     |
| `GET /dividendos/historico/{usuarioId}`            | ✅*   | ✅     |

*✅ = Apenas histórico próprio para usuários comuns

---

## � Investimentos que Pagam Dividendos

> ⚠️ **8 investimentos configurados para pagar dividendos:**

### 📈 **Ações (RENDA_VARIAVEL):**
| Símbolo | Nome | Yield | Frequência | Valor por Ação* |
|---------|------|--------|------------|------------------|
| **PETR4** | Petróleo Brasileiro | 8,5% | Trimestral (4x/ano) | R$ 0,61 |
| **VALE3** | Vale S.A. | 12,3% | Semestral (2x/ano) | R$ 4,01 |
| **ITUB4** | Itaú Unibanco | 6,8% | Trimestral (4x/ano) | R$ 0,55 |
| **BBAS3** | Banco do Brasil | 9,2% | Trimestral (4x/ano) | R$ 1,04 |
| **ABEV3** | Ambev S.A. | 4,5% | Semestral (2x/ano) | R$ 0,33 |

### 🏢 **FIIs (FUNDO_IMOBILIARIO):**
| Símbolo | Nome | Yield | Frequência | Valor por Cota* |
|---------|------|--------|------------|------------------|
| **HGLG11** | CSHG Real Estate | R$ 0,85 | Mensal (12x/ano) | R$ 0,85 |
| **MXRF11** | Maxi Renda | R$ 0,08 | Mensal (12x/ano) | R$ 0,08 |
| **XPLG11** | XP Log | R$ 0,75 | Mensal (12x/ano) | R$ 0,75 |

> 💡 ***Valor por Ação/Cota**: Calculado automaticamente baseado no preço atual e yield configurado*

### 💎 **Renda Fixa** (Não paga dividendos):
- TD-SELIC, CDB-INTER, LCI-NU têm `dividendYield: 0` e `frequencia: 0`

---

## �💡 Como Funciona o Sistema

### Processo de Dividendos:
1. **Investimentos** têm `dividendYield` e `frequenciaDividendo` configurados
2. **Admin** escolhe quando liberar dividendos através dos endpoints
3. **Dividendos Pendentes** são criados automaticamente para usuários com posições
4. **Pagamento** é processado instantaneamente ao liberar

### Cálculo Automático:
- **Valor por Ação**: `(precoAtual * dividendYield / 100) / frequenciaDividendo`
- **Valor Total**: `valorPorAcao * quantidadeAcoes`
- **Apenas** usuários com posições ativas recebem

---

## 📋 Endpoints

### 1. Ver Dividendos Pendentes
**GET** `/dividendos/pendentes`  
**Acesso**: Apenas ADMIN

#### Função:
Mostra todos os dividendos que foram criados mas ainda não liberados para pagamento.

#### Resposta:
```json
[
  {
    "id": 1,
    "investimento": {
      "id": 2,
      "nome": "Ações Vale",
      "simbolo": "VALE3",
      "dividendYield": 8.5,
      "frequenciaDividendo": 4
    },
    "usuario": {
      "id": 2,
      "nomeUsuario": "joao.silva",
      "email": "joao@email.com"
    },
    "valorPorAcao": 1.46,
    "quantidadeAcoes": 50,
    "valorTotal": 73.00,
    "dataCriacao": "2024-10-02T14:30:15",
    "processado": false
  },
  {
    "id": 2,
    "investimento": {
      "id": 2,
      "nome": "Ações Vale",
      "simbolo": "VALE3",
      "dividendYield": 8.5,
      "frequenciaDividendo": 4
    },
    "usuario": {
      "id": 3,
      "nomeUsuario": "maria.santos",
      "email": "maria@email.com"
    },
    "valorPorAcao": 1.46,
    "quantidadeAcoes": 25,
    "valorTotal": 36.50,
    "dataCriacao": "2024-10-02T14:30:15",
    "processado": false
  }
]
```

---

### 2. Liberar Dividendos por Investimento
**POST** `/dividendos/liberar/{investimentoId}`  
**Acesso**: Apenas ADMIN

#### Função:
Cria e libera imediatamente dividendos para todos os usuários que possuem ações deste investimento específico.

#### Exemplo:
```
POST /dividendos/liberar/2
Authorization: Bearer TOKEN_ADMIN
```

#### Processo Automático:
1. **Busca** todas as posições ativas do investimento
2. **Calcula** valor do dividendo baseado no yield e preço atual
3. **Cria** registro de dividendo pendente
4. **Processa** imediatamente o pagamento
5. **Atualiza** saldo dos usuários

#### Resposta:
```json
{
  "message": "Dividendos liberados com sucesso para o investimento: Ações Vale",
  "investimento": {
    "id": 2,
    "nome": "Ações Vale",
    "simbolo": "VALE3"
  },
  "detalhes": {
    "usuariosAtendidos": 5,
    "valorTotalPago": 1250.75,
    "valorPorAcao": 1.46,
    "dataProcessamento": "2024-10-02T16:45:30"
  },
  "dividendosProcessados": [
    {
      "usuario": "joao.silva",
      "quantidadeAcoes": 50,
      "valorRecebido": 73.00
    },
    {
      "usuario": "maria.santos",
      "quantidadeAcoes": 25,
      "valorRecebido": 36.50
    }
  ]
}
```

---

### 3. Liberar Todos os Dividendos
**POST** `/dividendos/liberar-todos`  
**Acesso**: Apenas ADMIN

#### Função:
Libera dividendos para TODOS os investimentos que têm `dividendYield > 0` e usuários com posições ativas.

#### Exemplo:
```
POST /dividendos/liberar-todos
Authorization: Bearer TOKEN_ADMIN
```

#### Resposta:
```json
{
  "message": "Dividendos liberados com sucesso para todos os investimentos",
  "resumoGeral": {
    "investimentosProcessados": 3,
    "usuariosAtendidos": 12,
    "valorTotalPago": 4567.80,
    "dataProcessamento": "2024-10-02T17:00:45"
  },
  "detalhePorInvestimento": [
    {
      "investimento": {
        "id": 2,
        "nome": "Ações Vale",
        "simbolo": "VALE3"
      },
      "usuariosAtendidos": 5,
      "valorTotalPago": 1250.75,
      "valorPorAcao": 1.46
    },
    {
      "investimento": {
        "id": 3,
        "nome": "FII HGLG11",
        "simbolo": "HGLG11"
      },
      "usuariosAtendidos": 7,
      "valorTotalPago": 3317.05,
      "valorPorAcao": 0.98
    }
  ]
}
```

---

### 4. Histórico de Dividendos do Usuário
**GET** `/dividendos/historico/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Função:
Mostra todos os dividendos já recebidos por um usuário específico.

#### Exemplo:
```
GET /dividendos/historico/2
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
[
  {
    "id": 5,
    "investimento": {
      "id": 2,
      "nome": "Ações Vale",
      "simbolo": "VALE3"
    },
    "valorPorAcao": 1.46,
    "quantidadeAcoes": 50,
    "valorTotal": 73.00,
    "dataCriacao": "2024-10-02T16:45:30",
    "dataProcessamento": "2024-10-02T16:45:30",
    "processado": true
  },
  {
    "id": 8,
    "investimento": {
      "id": 3,
      "nome": "FII HGLG11",
      "simbolo": "HGLG11"
    },
    "valorPorAcao": 0.98,
    "quantidadeAcoes": 100,
    "valorTotal": 98.00,
    "dataCriacao": "2024-09-15T14:20:10",
    "dataProcessamento": "2024-09-15T14:20:10",
    "processado": true
  }
]
```

---

## 🔢 Cálculos Detalhados

### Fórmula do Dividendo:
```
Valor por Ação = (Preço Atual × Dividend Yield ÷ 100) ÷ Frequência
Valor Total = Valor por Ação × Quantidade de Ações Possuídas
```

### Exemplos Práticos:

#### Ações Vale (VALE3):
- **Preço Atual**: R$ 68,75
- **Dividend Yield**: 8,5% ao ano
- **Frequência**: 4 vezes ao ano (trimestral)
- **Cálculo**: (68,75 × 8,5 ÷ 100) ÷ 4 = R$ 1,46 por ação

#### FII HGLG11:
- **Preço Atual**: R$ 117,50  
- **Dividend Yield**: 6,2% ao ano
- **Frequência**: 12 vezes ao ano (mensal)
- **Cálculo**: (117,50 × 6,2 ÷ 100) ÷ 12 = R$ 0,61 por cota

---

## 🎯 Fluxo de Trabalho Admin

### 1. Verificar Dividendos Pendentes
```
GET /dividendos/pendentes
Authorization: Bearer TOKEN_ADMIN
```

### 2. Liberar Dividendo de Investimento Específico
```
POST /dividendos/liberar/2
Authorization: Bearer TOKEN_ADMIN
```

### 3. Liberar Todos os Dividendos (Mensal/Trimestral)
```
POST /dividendos/liberar-todos
Authorization: Bearer TOKEN_ADMIN
```

### 4. Verificar Histórico de um Usuário
```
GET /dividendos/historico/2
Authorization: Bearer TOKEN_ADMIN
```

---

## 📊 Dashboard Sugerido

### Para Admin:
1. **Botão**: "Ver Dividendos Pendentes"
2. **Botão**: "Liberar Todos os Dividendos"
3. **Lista**: Investimentos individuais com botão "Liberar Dividendos"
4. **Relatório**: Resumo mensal/trimestral de pagamentos

### Para Usuário:
1. **Página**: "Meu Histórico de Dividendos"
2. **Dashboard**: Soma total recebida no mês/ano
3. **Gráfico**: Evolução dos dividendos ao longo do tempo

---

## ⚙️ Configuração de Investimentos

Para que um investimento pague dividendos, deve ter:

### Ações (Trimestral):
```json
{
  "dividendYield": 8.5,
  "frequenciaDividendo": 4
}
```

### FIIs (Mensal):
```json
{
  "dividendYield": 6.2,
  "frequenciaDividendo": 12
}
```

### Tesouro Direto (Sem Dividendos):
```json
{
  "dividendYield": 0.0,
  "frequenciaDividendo": 0
}
```

---

## 🔄 Estados do Sistema

### DividendoPendente:
- **processado: false** → Aguardando liberação admin
- **processado: true** → Já pago ao usuário

### Quando Liberar:
- **Ações**: Normalmente trimestral (4x/ano)
- **FIIs**: Normalmente mensal (12x/ano)
- **Admin decide**: Não há automação por data

---

## ❌ Tratamento de Erros

### Erros Comuns:

#### 404 - Investimento Não Encontrado
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Investimento não encontrado",
  "status": 404
}
```

#### 400 - Investimento Sem Dividendos
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Investimento não paga dividendos (dividendYield = 0)",
  "status": 400
}
```

#### 404 - Nenhuma Posição Ativa
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Nenhum usuário possui ações deste investimento",
  "status": 404
}
```

#### 403 - Acesso Negado
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Apenas administradores podem liberar dividendos",
  "status": 403
}
```

---

## 🎯 Resumo Executivo

### Vantagens do Sistema:
- ✅ **Controle Total**: Admin decide quando pagar
- ✅ **Cálculo Automático**: Baseado em yield e preço atual
- ✅ **Transparência**: Histórico completo para usuários
- ✅ **Flexibilidade**: Por investimento ou todos juntos

### Casos de Uso:
- **Mensal**: Liberar dividendos de FIIs
- **Trimestral**: Liberar dividendos de ações
- **Especial**: Dividendos extraordinários por investimento
- **Auditoria**: Verificar histórico de pagamentos