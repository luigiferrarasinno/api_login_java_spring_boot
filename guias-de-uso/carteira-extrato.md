# 💼 Guia de Carteira e Extrato - Endpoints

Este guia cobre todos os endpoints relacionados ao gerenciamento de carteira e extrato financeiro.

---

## 🔐 Autenticação

Todos os endpoints requerem um token JWT no cabeçalho:
```
Authorization: Bearer SEU_TOKEN_JWT
```

---

## 🧠 Sistema de Permissões

| Endpoint                                    | USER              | ADMIN           |
| ------------------------------------------- | ----------------- | --------------- |
| `GET /carteira/{usuarioId}`                 | ✅ (própria)       | ✅ (qualquer)    |
| `POST /carteira/comprar`                    | ✅                | ✅               |
| `POST /carteira/vender`                     | ✅                | ✅               |
| `GET /carteira/{usuarioId}/valor-total`     | ✅ (própria)       | ✅ (qualquer)    |
| `GET /carteira/{usuarioId}/posicao/{investimentoId}` | ✅ (própria) | ✅ (qualquer) |
| `GET /extrato/{usuarioId}`                  | ✅ (próprio)       | ✅ (qualquer)    |

---

## � Usuários e Saldos Iniciais

> ⚠️ **3 usuários criados com carteiras vazias:**

| Usuário | Email | Saldo Inicial | Posições Iniciais | Observação |
|---------|-------|---------------|-------------------|-------------|
| **Admin Sistema** | admin@admin.com | R$ 0,00 | Vazia | Para testes administrativos |
| **João Silva** | user@user.com | R$ 0,00 | Vazia | Usuário para testes de compra |
| **Maria Investidora** | maria@investidora.com | R$ 0,00 | Vazia | Usuário experiente para testes |

> 💡 **Para testar compras**: Use endpoint de alteração de saldo (ADMIN) ou implemente funcionalidade de depósito

> 📊 **Dados Disponíveis**: 11 investimentos com preços variados (R$ 9,85 a R$ 5.000,00)

---

## �💡 Sistema Brasileiro de Ações

### Regras Implementadas:
- ✅ **Números Inteiros**: Só é possível comprar/vender quantidades inteiras
- ✅ **Estoque Limitado**: Investimentos têm `quantidadeDisponivel` finita
- ✅ **Validação de Saldo**: Usuário precisa ter saldo suficiente
- ✅ **Validação de Posição**: Só pode vender o que possui

---

## 📋 Endpoints de Carteira

### 1. Ver Carteira Completa
**GET** `/carteira/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Função:
Mostra todas as posições ativas (quantidade > 0) do usuário com informações detalhadas.

#### Exemplo:
```
GET /carteira/2
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
{
  "usuario": {
    "id": 2,
    "nomeUsuario": "joao.silva",
    "saldo": 15000.00
  },
  "posicoes": [
    {
      "investimento": {
        "id": 1,
        "nome": "Tesouro Direto",
        "simbolo": "TD",
        "categoria": "TESOURO_DIRETO",
        "precoAtual": 102.50,
        "risco": "BAIXO"
      },
      "quantidade": 50,
      "precoMedio": 100.75,
      "valorInvestido": 5037.50,
      "valorAtual": 5125.00,
      "lucroPreju": 87.50,
      "percentualGanho": 1.74
    },
    {
      "investimento": {
        "id": 2,
        "nome": "Ações Vale",
        "simbolo": "VALE3",
        "categoria": "ACAO",
        "precoAtual": 68.75,
        "risco": "ALTO"
      },
      "quantidade": 25,
      "precoMedio": 65.20,
      "valorInvestido": 1630.00,
      "valorAtual": 1718.75,
      "lucroPreju": 88.75,
      "percentualGanho": 5.44
    }
  ],
  "resumo": {
    "valorTotalInvestido": 6667.50,
    "valorAtualCarteira": 6843.75,
    "lucroTotalCarteira": 176.25,
    "percentualGanhoGeral": 2.64,
    "saldoDisponivel": 15000.00
  }
}
```

---

### 2. Comprar Investimento
**POST** `/carteira/comprar`  
**Acesso**: Usuários autenticados

#### Requisição:
```json
{
  "usuarioId": 2,
  "investimentoId": 1,
  "quantidade": 10
}
```

#### Validações Automáticas:
- ✅ **Usuário existe** e está ativo
- ✅ **Investimento existe** e está ativo  
- ✅ **Quantidade é inteira** (sistema brasileiro)
- ✅ **Quantidade disponível** suficiente no estoque
- ✅ **Saldo suficiente** para a compra
- ✅ **Permissão**: Usuário só pode comprar para si mesmo

#### Resposta Sucesso:
```json
{
  "message": "Compra realizada com sucesso!",
  "transacao": {
    "tipo": "COMPRA",
    "investimento": {
      "id": 1,
      "nome": "Tesouro Direto",
      "simbolo": "TD"
    },
    "quantidade": 10,
    "precoUnitario": 102.50,
    "valorTotal": 1025.00,
    "dataTransacao": "2024-10-02T16:30:45"
  },
  "novaPositacao": {
    "quantidade": 60,
    "precoMedio": 101.08,
    "valorAtual": 6150.00
  },
  "saldoAtualizado": 13975.00
}
```

---

### 3. Vender Investimento
**POST** `/carteira/vender`  
**Acesso**: Usuários autenticados

#### Requisição:
```json
{
  "usuarioId": 2,
  "investimentoId": 2,
  "quantidade": 5
}
```

#### Validações Automáticas:
- ✅ **Posição existe** e tem quantidade suficiente
- ✅ **Quantidade é inteira** (sistema brasileiro)
- ✅ **Não pode vender mais** do que possui
- ✅ **Permissão**: Usuário só pode vender próprias ações

#### Resposta Sucesso:
```json
{
  "message": "Venda realizada com sucesso!",
  "transacao": {
    "tipo": "VENDA",
    "investimento": {
      "id": 2,
      "nome": "Ações Vale",
      "simbolo": "VALE3"
    },
    "quantidade": 5,
    "precoUnitario": 68.75,
    "valorTotal": 343.75,
    "dataTransacao": "2024-10-02T16:45:20"
  },
  "posicaoAtualizada": {
    "quantidade": 20,
    "precoMedio": 65.20,
    "valorAtual": 1375.00
  },
  "saldoAtualizado": 14318.75,
  "lucroVenda": 17.75
}
```

---

### 4. Valor Total da Carteira
**GET** `/carteira/{usuarioId}/valor-total`  
**Acesso**: Próprio usuário ou ADMIN

#### Função:
Retorna apenas o valor total da carteira atualizado com preços atuais.

#### Resposta:
```json
{
  "usuarioId": 2,
  "valorTotalCarteira": 6843.75,
  "saldoDisponivel": 15000.00,
  "patrimonioTotal": 21843.75,
  "dataConsulta": "2024-10-02T17:00:15"
}
```

---

### 5. Posição Específica
**GET** `/carteira/{usuarioId}/posicao/{investimentoId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Função:
Mostra detalhes de uma posição específica do usuário.

#### Exemplo:
```
GET /carteira/2/posicao/1
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
{
  "investimento": {
    "id": 1,
    "nome": "Tesouro Direto",
    "simbolo": "TD",
    "categoria": "TESOURO_DIRETO",
    "precoAtual": 102.50,
    "risco": "BAIXO"
  },
  "quantidade": 60,
  "precoMedio": 101.08,
  "valorInvestido": 6064.80,
  "valorAtual": 6150.00,
  "lucroPreju": 85.20,
  "percentualGanho": 1.41,
  "ultimaTransacao": "2024-10-02T16:30:45"
}
```

---

## 📊 Endpoints de Extrato

### 1. Extrato Completo
**GET** `/extrato/{usuarioId}`  
**Acesso**: Próprio usuário ou ADMIN

#### Função:
Mostra todas as transações (compras, vendas, dividendos) ordenadas por data decrescente.

#### Exemplo:
```
GET /extrato/2
Authorization: Bearer SEU_TOKEN
```

#### Resposta:
```json
{
  "usuario": {
    "id": 2,
    "nomeUsuario": "joao.silva"
  },
  "transacoes": [
    {
      "id": 15,
      "tipo": "VENDA",
      "investimento": {
        "id": 2,
        "nome": "Ações Vale",
        "simbolo": "VALE3"
      },
      "quantidade": 5,
      "precoUnitario": 68.75,
      "valorTotal": 343.75,
      "dataTransacao": "2024-10-02T16:45:20",
      "observacoes": "Venda para realização de lucro"
    },
    {
      "id": 14,
      "tipo": "COMPRA",
      "investimento": {
        "id": 1,
        "nome": "Tesouro Direto",
        "simbolo": "TD"
      },
      "quantidade": 10,
      "precoUnitario": 102.50,
      "valorTotal": 1025.00,
      "dataTransacao": "2024-10-02T16:30:45",
      "observacoes": "Diversificação da carteira"
    },
    {
      "id": 13,
      "tipo": "DIVIDENDO",
      "investimento": {
        "id": 2,
        "nome": "Ações Vale",
        "simbolo": "VALE3"
      },
      "quantidade": 25,
      "precoUnitario": 1.46,
      "valorTotal": 36.50,
      "dataTransacao": "2024-10-01T14:20:30",
      "observacoes": "Dividendo trimestral"
    }
  ],
  "resumo": {
    "totalTransacoes": 25,
    "valorTotalCompras": 12500.00,
    "valorTotalVendas": 2843.75,
    "totalDividendosRecebidos": 245.80,
    "saldoAtual": 15000.00
  }
}
```

---

## 🔢 Cálculos Detalhados

### Preço Médio:
```
Novo Preço Médio = (Quantidade Atual × Preço Médio Atual + Quantidade Compra × Preço Compra) 
                   ÷ (Quantidade Atual + Quantidade Compra)
```

### Lucro/Prejuízo:
```
Lucro = (Preço Atual - Preço Médio) × Quantidade Atual
Percentual = (Lucro ÷ Valor Investido) × 100
```

### Valor da Carteira:
```
Valor Atual = Σ(Quantidade × Preço Atual) para todas as posições
```

---

## 🎯 Exemplos Práticos

### Simulação de Compras:

#### 1ª Compra - 50 ações VALE3 a R$ 65,00:
```json
{
  "usuarioId": 2,
  "investimentoId": 2,
  "quantidade": 50
}
```
- **Valor**: R$ 3.250,00
- **Preço Médio**: R$ 65,00

#### 2ª Compra - 25 ações VALE3 a R$ 70,00:
```json
{
  "usuarioId": 2,
  "investimentoId": 2,
  "quantidade": 25
}
```
- **Valor**: R$ 1.750,00
- **Novo Preço Médio**: (50×65 + 25×70) ÷ 75 = R$ 66,67

#### Venda - 25 ações VALE3 a R$ 68,75:
```json
{
  "usuarioId": 2,
  "investimentoId": 2,
  "quantidade": 25
}
```
- **Valor Recebido**: R$ 1.718,75
- **Lucro na Venda**: (68,75 - 66,67) × 25 = R$ 52,00

---

## 📦 Fluxo Completo no Postman

### 1. Ver Carteira Inicial
```
GET /carteira/2
Authorization: Bearer TOKEN_USER
```

### 2. Comprar 10 Tesouro Direto
```
POST /carteira/comprar
Authorization: Bearer TOKEN_USER
Content-Type: application/json

{
  "usuarioId": 2,
  "investimentoId": 1,
  "quantidade": 10
}
```

### 3. Verificar Nova Posição
```
GET /carteira/2/posicao/1
Authorization: Bearer TOKEN_USER
```

### 4. Ver Extrato Atualizado
```
GET /extrato/2
Authorization: Bearer TOKEN_USER
```

### 5. Vender Parte da Posição
```
POST /carteira/vender
Authorization: Bearer TOKEN_USER
Content-Type: application/json

{
  "usuarioId": 2,
  "investimentoId": 1,
  "quantidade": 5
}
```

---

## 🔍 Diferenças entre Roles

### Usuário Comum:
- ✅ Pode ver apenas sua própria carteira
- ✅ Pode comprar/vender apenas para si mesmo
- ✅ Pode ver apenas seu próprio extrato
- ❌ Não pode acessar carteiras de outros usuários

### Admin:
- ✅ Pode ver carteira de qualquer usuário
- ✅ Pode comprar/vender para qualquer usuário
- ✅ Pode ver extrato de qualquer usuário
- ✅ Útil para suporte e auditoria

---

## ⚙️ Configurações do Sistema

### Estoque de Investimentos:
```json
{
  "quantidadeTotal": 1000000,
  "quantidadeDisponivel": 987650
}
```

### Atualizações Automáticas:
- **Preços**: Simulação com volatilidade por risco
- **Estoque**: Reduzido a cada compra, aumentado a cada venda
- **Preço Médio**: Recalculado automaticamente

---

## ❌ Tratamento de Erros

### Erros de Compra:

#### Saldo Insuficiente:
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Saldo insuficiente. Necessário: R$ 1025,00, Disponível: R$ 800,00",
  "status": 400
}
```

#### Estoque Insuficiente:
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Quantidade disponível insuficiente. Solicitado: 100, Disponível: 45",
  "status": 400
}
```

### Erros de Venda:

#### Posição Insuficiente:
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Quantidade insuficiente para venda. Possuído: 25, Tentando vender: 50",
  "status": 400
}
```

#### Sem Posição:
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Usuário não possui posição neste investimento",
  "status": 404
}
```

### Erros de Permissão:

#### Acesso Negado:
```json
{
  "timestamp": "2024-10-02T22:10:22.491",
  "erro": "Você só pode acessar sua própria carteira",
  "status": 403
}
```

---

## 🎯 Resumo Executivo

### Funcionalidades Principais:
- ✅ **Sistema Brasileiro**: Apenas números inteiros
- ✅ **Estoque Realista**: Quantidades limitadas
- ✅ **Cálculo Automático**: Preço médio e lucros
- ✅ **Extrato Completo**: Todas as transações
- ✅ **Segurança**: Permissões adequadas por role

### Casos de Uso:
- **Portfolio Management**: Acompanhar posições e performance
- **Trading**: Comprar e vender com validações
- **Auditoria**: Extrato completo de transações
- **Suporte**: Admin pode visualizar qualquer carteira