# 🎯 **API DE RESUMO DE INVESTIMENTOS - ENDPOINT UNIFICADO**

## ✅ **Implementação Concluída com Sucesso!**

### 🔥 **Endpoint Único Consolidado:**
```http
GET /extrato/resumo
```

---

## 📋 **Parâmetros Disponíveis (Todos Opcionais)**

| Parâmetro | Tipo | Descrição | Exemplo |
|-----------|------|-----------|---------|
| `ano` | Integer | Filtrar por ano específico | `2024` |
| `mes` | Integer | Filtrar por mês (1-12) - requer `ano` | `10` |
| `investimentoId` | Long | Filtrar por investimento específico | `1` |

---

## 🎯 **Exemplos de Uso Práticos**

### 📊 **1. Resumo Geral** (Sem filtros)
```bash
GET /extrato/resumo
```
**Retorna:** Análise completa de todos os investimentos de todos os períodos

### 📅 **2. Resumo Anual**
```bash
GET /extrato/resumo?ano=2024
```
**Retorna:** Todos os investimentos realizados em 2024

### 🗓️ **3. Resumo Mensal**
```bash
GET /extrato/resumo?ano=2024&mes=10
```
**Retorna:** Todos os investimentos de outubro de 2024

### 🎯 **4. Investimento Específico**
```bash
GET /extrato/resumo?investimentoId=1
```
**Retorna:** Análise completa da PETR4 em todo período

### 🔍 **5. Filtro Combinado**
```bash
GET /extrato/resumo?ano=2024&mes=10&investimentoId=1
```
**Retorna:** Performance da PETR4 especificamente em outubro de 2024

### 📅 **6. Período de Meses** (🔥 NOVO!)
```bash
GET /extrato/resumo?ano=2025&mesInicio=1&mesFim=5
```
**Retorna:** Análise de **Janeiro a Maio de 2025** (todos os investimentos)

### 🎯 **7. Período + Investimento Específico** (🔥 NOVO!)
```bash
GET /extrato/resumo?ano=2025&mesInicio=1&mesFim=5&investimentoId=1
```
**Retorna:** Performance da **PETR4 de Janeiro a Maio de 2025**

---

## 📈 **Dados Retornados no Resumo**

### 🎯 **Informações Gerais:**
- **Período analisado**
- **Nome e email do usuário**
- **Situação geral:** LUCRO, PREJUIZO ou NEUTRO

### 💰 **Totais Consolidados:**
- **Total Investido:** Soma de todas as compras
- **Total Recebido:** Soma de todas as vendas
- **Total Dividendos:** Soma de todos os dividendos recebidos
- **Resultado Líquido:** (Recebido + Dividendos) - Investido
- **Percentual de Retorno:** Percentual de lucro/prejuízo sobre o investido

### 📊 **Estatísticas Avançadas:**
- **Número total de operações**
- **Quantidade de investimentos com lucro**
- **Quantidade de investimentos com prejuízo**
- **Maior lucro individual**
- **Maior prejuízo individual**
- **Investimento mais rentável**
- **Investimento menos rentável**

### 📋 **Detalhamento por Investimento:**
- **Símbolo e nome do investimento**
- **Total investido no período**
- **Total recebido no período**
- **Dividendos recebidos**
- **Resultado líquido (lucro/prejuízo)**
- **Percentual de retorno**
- **Número de operações realizadas**
- **Lista detalhada de todas as transações**

---

## 🧮 **Validações Implementadas**

✅ **Mês válido:** Entre 1 e 12 (mes, mesInicio, mesFim)  
✅ **Ano obrigatório:** Se qualquer mês for informado  
✅ **Período válido:** mesInicio deve ser ≤ mesFim  
✅ **Parâmetros completos:** mesInicio e mesFim devem ser informados juntos  
✅ **Sem conflito:** não pode usar `mes` junto com `mesInicio/mesFim`  
✅ **Token JWT:** Identificação automática do usuário  
✅ **Investimento existente:** Validação de IDs  

---

## 🎮 **Como Testar**

### 🚀 **1. Inicie a Aplicação:**
```bash
mvn spring-boot:run
```

### 🌐 **2. Acesse o Swagger:**
- URL: http://localhost:8080/swagger-ui.html
- Procure por: **Extrato** → **Resumo unificado**

### 🔑 **3. Faça Login:**
- **Admin:** admin@admin.com / 123456
- **User:** user@user.com / 123456  
- **Maria:** maria@investidora.com / 123456

### 📊 **4. Teste o Endpoint:**
```http
Authorization: Bearer SEU_TOKEN_JWT
GET /extrato/resumo?ano=2024&mes=9
```

---

## 🎯 **Vantagens da Consolidação**

### ✅ **Antes:** 4 endpoints diferentes
- `/extrato/resumo`
- `/extrato/resumo/{ano}/{mes}`
- `/extrato/resumo/investimento/{investimentoId}`
- `/extrato/resumo/{ano}/{mes}/investimento/{investimentoId}`

### 🔥 **Agora:** 1 endpoint flexível
- `/extrato/resumo` com parâmetros opcionais

### 🚀 **Benefícios:**
1. **📋 API mais limpa** - Menos endpoints para documentar
2. **🔍 Mais flexível** - Combinações infinitas de filtros
3. **🧠 Mais intuitivo** - Parâmetros de query são mais claros
4. **⚡ Manutenção simplificada** - Um só lugar para lógica
5. **📚 Documentação unificada** - Swagger mais organizado

---

## 💾 **Dados de Teste Pré-Populados**

### 🎯 **Sistema já contém:**
- **📊 299 registros de extrato** nos últimos 12 meses
- **👥 3 usuários** com perfis diferentes de investimento
- **💰 11 investimentos** ativos (ações, FIIs e renda fixa)
- **🎵 4 playlists** com relacionamentos
- **💬 7 comentários** nos investimentos

### 📈 **Perfis de Usuário:**
- **👤 João (Conservador):** 2-3 operações/mês, evita alto risco
- **👩‍💼 Maria (Moderado):** 3-5 operações/mês, mix equilibrado
- **👨‍💼 Admin (Agressivo):** 4-7 operações/mês, todos os investimentos

---

## 🎉 **Resultado Final**

### ✅ **Sistema Completo Implementado:**
1. **📊 Remoção do histórico** - Migração bem-sucedida para extratos
2. **🔥 Endpoint unificado** - Máxima flexibilidade de consulta  
3. **💰 Análise de lucro/prejuízo** - Cálculos automáticos e precisos
4. **📈 299 registros** - Dados realistas para 12 meses
5. **🎯 Estatísticas avançadas** - Performance detalhada por investimento

**🚀 Pronto para uso em produção com dados realistas e análises completas de performance de investimentos!**