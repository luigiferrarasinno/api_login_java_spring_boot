# 🔥 **ATUALIZAÇÃO: SUPORTE A PERÍODOS DE MESES**

## ✅ **Nova Funcionalidade Implementada!**

### 🎯 **Problema Resolvido:**
Antes você só podia filtrar por **mês individual**, agora você pode filtrar por **períodos/ranges de meses**!

---

## 📅 **Novos Parâmetros Disponíveis:**

### 🔥 **Para Períodos (Novos):**
| Parâmetro | Tipo | Descrição | Exemplo |
|-----------|------|-----------|---------|
| `mesInicio` | Integer | Mês de início do período (1-12) | `1` (Janeiro) |
| `mesFim` | Integer | Mês de fim do período (1-12) | `5` (Maio) |

### 📝 **Parâmetros Existentes:**
| Parâmetro | Tipo | Descrição | Exemplo |
|-----------|------|-----------|---------|
| `ano` | Integer | Ano para filtrar | `2025` |
| `mes` | Integer | Mês específico (não pode usar com período) | `10` |
| `investimentoId` | Long | ID do investimento específico | `1` |

---

## 🎯 **Exemplos Práticos do Seu Caso:**

### 📅 **Janeiro a Maio de 2025 (Todos os Investimentos):**
```http
GET /extrato/resumo?ano=2025&mesInicio=1&mesFim=5
```
**Retorna:** Análise completa de **todos os investimentos** de Janeiro a Maio de 2025

### 🎯 **Janeiro a Maio de 2025 (PETR4 apenas):**
```http
GET /extrato/resumo?ano=2025&mesInicio=1&mesFim=5&investimentoId=1
```
**Retorna:** Performance da **PETR4** especificamente de Janeiro a Maio de 2025

### 📊 **Outros Exemplos de Períodos:**
```http
# Primeiro trimestre de 2024
GET /extrato/resumo?ano=2024&mesInicio=1&mesFim=3

# Segundo semestre de 2024
GET /extrato/resumo?ano=2024&mesInicio=7&mesFim=12

# Últimos 3 meses de 2025
GET /extrato/resumo?ano=2025&mesInicio=10&mesFim=12
```

---

## 🛡️ **Validações Automáticas:**

### ✅ **Validações Implementadas:**
1. **mesInicio ≤ mesFim** - Início deve ser menor ou igual ao fim
2. **Ambos obrigatórios** - Se usar período, deve informar início E fim
3. **Não pode misturar** - Não pode usar `mes` junto com `mesInicio/mesFim`
4. **Ano obrigatório** - Se informar qualquer mês, deve informar ano
5. **Range válido** - Meses devem estar entre 1-12

### ❌ **Exemplos de Uso Inválido:**
```http
# ❌ Erro: mesInicio > mesFim
GET /extrato/resumo?ano=2025&mesInicio=8&mesFim=3

# ❌ Erro: só um dos parâmetros de período
GET /extrato/resumo?ano=2025&mesInicio=1

# ❌ Erro: misturando mes com período
GET /extrato/resumo?ano=2025&mes=10&mesInicio=1&mesFim=5

# ❌ Erro: mês sem ano
GET /extrato/resumo?mesInicio=1&mesFim=5
```

---

## 📊 **Exemplo de Resposta (Jan-Mai 2025):**

```json
{
  "periodo": "2025-01 a 05",
  "nomeUsuario": "João Silva",
  "emailUsuario": "user@user.com",
  "situacaoGeral": "LUCRO",
  "totalGeralInvestido": 15750.00,
  "totalGeralRecebido": 16200.00,
  "totalGeralDividendos": 850.50,
  "resultadoGeralLiquido": 1300.50,
  "percentualRetornoGeral": 8.25,
  "numeroTotalOperacoes": 28,
  "quantidadeInvestimentosComLucro": 6,
  "quantidadeInvestimentosComPrejuizo": 2,
  "quantidadeInvestimentosNeutros": 0,
  "investimentoMaisRentavel": "PETR4",
  "investimentoMenosRentavel": "VALE3",
  "investimentos": [
    {
      "simboloInvestimento": "PETR4",
      "nomeInvestimento": "Petróleo Brasileiro S.A.",
      "totalInvestido": 5700.00,
      "totalRecebido": 6150.00,
      "totalDividendos": 285.75,
      "resultladoLiquido": 735.75,
      "situacao": "LUCRO",
      "percentualRetorno": 12.91,
      "numeroOperacoes": 8
    }
    // ... outros investimentos
  ]
}
```

---

## 🎮 **Como Testar Agora:**

### 1. **Compile a aplicação:**
```bash
mvn clean compile
mvn spring-boot:run
```

### 2. **Acesse o Swagger:**
- 🌐 http://localhost:8080/swagger-ui.html
- Procure: **Extrato** → **Resumo unificado**

### 3. **Teste o período Jan-Mai 2025:**
- `ano`: 2025
- `mesInicio`: 1
- `mesFim`: 5
- `investimentoId`: (deixe vazio para todos)

### 4. **Faça login antes:**
- **Admin:** admin@admin.com / 123456
- **User:** user@user.com / 123456
- **Maria:** maria@investidora.com / 123456

---

## 🎯 **Resumo das Melhorias:**

### ✅ **Antes:**
- Só filtro por mês individual: `?ano=2024&mes=10`

### 🔥 **Agora:**
- **Mês individual:** `?ano=2024&mes=10`
- **Período de meses:** `?ano=2025&mesInicio=1&mesFim=5`
- **Período + investimento:** `?ano=2025&mesInicio=1&mesFim=5&investimentoId=1`

### 🚀 **Vantagens:**
1. **📊 Análise trimestral/semestral** - Períodos maiores de análise
2. **🔍 Mais flexibilidade** - Qualquer range de meses
3. **🎯 Combinações infinitas** - Período + investimento específico
4. **⚡ Performance** - Filtragem otimizada no backend
5. **🛡️ Validações robustas** - Previne erros de uso

**🎉 Agora você pode analisar Janeiro a Maio de 2025 (ou qualquer período) de forma fácil e precisa!**