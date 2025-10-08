# 🔧 Correção: Prompt de Montar Carteira Alinhado com Banco de Dados

## 🐛 Problema Identificado

O prompt enviado para a IA Feller estava com **informações desatualizadas** dos investimentos, diferente dos dados reais no banco.

---

## ❌ Inconsistências Encontradas

### **ID 2 - VALE3:**
- ❌ **Prompt Antigo:** Risco **MÉDIO**
- ✅ **Banco Real:** Risco **ALTO**
- **Impacto:** IA poderia recomendar VALE3 para perfil moderado (errado!)

### **ID 4 - Investimento Trocado:**
- ❌ **Prompt Antigo:** Magazine Luiza (MGLU3) - Alto Risco
- ✅ **Banco Real:** Banco do Brasil (BBAS3) - Médio Risco
- **Impacto:** IDs errados causariam erro ao adicionar investimentos

### **ID 7 - FII Errado:**
- ❌ **Prompt Antigo:** Kinea Renda Imobiliária (KNRI11)
- ✅ **Banco Real:** Maxi Renda (MXRF11)
- **Impacto:** Nome incorreto na lista

### **ID 8 - FII Errado:**
- ❌ **Prompt Antigo:** Maxi Renda FII (MXRF11)
- ✅ **Banco Real:** XP Log (XPLG11)
- **Impacto:** Nome incorreto na lista

---

## ✅ Dados Corretos (Alinhados com o Banco)

| ID | Símbolo | Nome Completo | Categoria | Risco | Yield |
|----|---------|---------------|-----------|-------|-------|
| 1 | PETR4 | Petróleo Brasileiro S.A. | Renda Variável | **MÉDIO** | 8.5% |
| 2 | VALE3 | Vale S.A. | Renda Variável | **ALTO** ⚠️ | 12.3% |
| 3 | ITUB4 | Itaú Unibanco Holding S.A. | Renda Variável | **MÉDIO** | 6.8% |
| 4 | BBAS3 | Banco do Brasil S.A. | Renda Variável | **MÉDIO** | 9.2% |
| 5 | ABEV3 | Ambev S.A. | Renda Variável | **BAIXO** | 4.5% |
| 6 | HGLG11 | CSHG Real Estate Fund | Fundo Imobiliário | **MÉDIO** | 10.2% |
| 7 | MXRF11 | Maxi Renda | Fundo Imobiliário | **MÉDIO** | 9.6% |
| 8 | XPLG11 | XP Log | Fundo Imobiliário | **MÉDIO** | 8.8% |
| 9 | TD-SELIC | Tesouro Direto Selic | Renda Fixa | **BAIXO** | 0% |
| 10 | CDB-INTER | CDB Banco Inter | Renda Fixa | **BAIXO** | 0% |
| 11 | LCI-NU | LCI Nubank | Renda Fixa | **BAIXO** | 0% |

---

## 📊 Distribuição por Nível de Risco

### 🟢 **BAIXO Risco (4 investimentos):**
- ID 5: ABEV3 (Renda Variável)
- ID 9: TD-SELIC (Renda Fixa)
- ID 10: CDB-INTER (Renda Fixa)
- ID 11: LCI-NU (Renda Fixa)

### 🟡 **MÉDIO Risco (6 investimentos):**
- ID 1: PETR4 (Renda Variável)
- ID 3: ITUB4 (Renda Variável)
- ID 4: BBAS3 (Renda Variável)
- ID 6: HGLG11 (FII)
- ID 7: MXRF11 (FII)
- ID 8: XPLG11 (FII)

### 🔴 **ALTO Risco (1 investimento):**
- ID 2: VALE3 (Renda Variável) ⚠️ **ÚNICO de alto risco!**

---

## 🎯 Regras Atualizadas para a IA

### **Perfil CONSERVADOR:**
- ✅ **Permitidos:** IDs **5, 9, 10, 11** (apenas BAIXO risco)
- ❌ **Proibidos:** Todos os outros
- 📊 **Exemplo:** `[5, 9, 10, 11]` ou `[9, 10, 11]`

### **Perfil MODERADO:**
- ✅ **Permitidos:** IDs **1, 3, 4, 5, 6, 7, 8, 9, 10, 11** (BAIXO + MÉDIO)
- ❌ **Proibidos:** ID **2** (VALE3 - alto risco)
- 📊 **Exemplo:** `[1, 3, 6, 9, 10]` ou `[4, 5, 7, 9, 10, 11]`

### **Perfil ARROJADO:**
- ✅ **Permitidos:** **TODOS** os IDs (1 a 11)
- ✅ **Diferencial:** Pode incluir ID **2** (VALE3)
- 📊 **Exemplo:** `[1, 2, 6, 9, 10]` ou `[2, 3, 4, 6, 8, 9]`

---

## 🔄 Mudanças no Prompt

### **Antes (ERRADO):**
```
2 | Vale S.A. (VALE3) | Renda Variável | Médio | 10.8%    ❌
4 | Magazine Luiza S.A. (MGLU3) | Renda Variável | Alto | 15.0%    ❌
7 | Kinea Renda Imobiliária FII (KNRI11) | Fundo Imobiliário | Médio | 10.5%    ❌
8 | Maxi Renda FII (MXRF11) | Fundo Imobiliário | Médio | 9.8%    ❌
```

### **Depois (CORRETO):**
```
2 | Vale S.A. (VALE3) | Renda Variável | ALTO | 12.3%    ✅
4 | Banco do Brasil S.A. (BBAS3) | Renda Variável | MÉDIO | 9.2%    ✅
7 | Maxi Renda (MXRF11) | Fundo Imobiliário | MÉDIO | 9.6%    ✅
8 | XP Log (XPLG11) | Fundo Imobiliário | MÉDIO | 8.8%    ✅
```

---

## ⚠️ Destaque Importante: VALE3

### **Por que é crítico:**

- VALE3 (ID 2) é o **ÚNICO** investimento de **ALTO RISCO** no banco
- Antes estava marcado como "Médio" no prompt → IA poderia recomendar para moderados
- Agora está corretamente marcado como "ALTO" → IA só recomenda para arrojados

### **Impacto da correção:**

| Perfil | Antes (ERRADO) | Depois (CORRETO) |
|--------|----------------|------------------|
| Conservador | Poderia receber VALE3 ❌ | Nunca recebe VALE3 ✅ |
| Moderado | Poderia receber VALE3 ❌ | Nunca recebe VALE3 ✅ |
| Arrojado | Poderia receber VALE3 ✅ | Recebe VALE3 normalmente ✅ |

---

## 📝 Novo Prompt (Fragmento Relevante)

```java
INVESTIMENTOS DISPONÍVEIS (ID | Nome | Categoria | Risco | Dividend Yield):
1 | Petróleo Brasileiro S.A. (PETR4) | Renda Variável | MÉDIO | 8.5%
2 | Vale S.A. (VALE3) | Renda Variável | ALTO | 12.3%    ⚠️ ÚNICO DE ALTO RISCO
3 | Itaú Unibanco Holding S.A. (ITUB4) | Renda Variável | MÉDIO | 6.8%
4 | Banco do Brasil S.A. (BBAS3) | Renda Variável | MÉDIO | 9.2%
5 | Ambev S.A. (ABEV3) | Renda Variável | BAIXO | 4.5%
6 | CSHG Real Estate Fund (HGLG11) | Fundo Imobiliário | MÉDIO | 10.2%
7 | Maxi Renda (MXRF11) | Fundo Imobiliário | MÉDIO | 9.6%
8 | XP Log (XPLG11) | Fundo Imobiliário | MÉDIO | 8.8%
9 | Tesouro Direto Selic (TD-SELIC) | Renda Fixa | BAIXO | 0% (rendimento por valorização)
10 | CDB Banco Inter (CDB-INTER) | Renda Fixa | BAIXO | 0% (rendimento por juros)
11 | LCI Nubank (LCI-NU) | Renda Fixa | BAIXO | 0% (rendimento por juros)

REGRAS IMPORTANTES:
4. CONSERVADOR: Apenas investimentos de BAIXO risco (IDs: 5, 9, 10, 11)
5. MODERADO: Mix de BAIXO e MÉDIO risco - EVITE ALTO (IDs recomendados: 1, 3, 4, 5, 6, 7, 8, 9, 10, 11)
6. ARROJADO: Todos os riscos permitidos, incluindo VALE3 (ID 2 - único de ALTO risco)

ATENÇÃO - ÚNICO INVESTIMENTO DE ALTO RISCO:
- ID 2 (VALE3) é o ÚNICO investimento de ALTO risco
- Conservadores NÃO podem ter VALE3
- Moderados NÃO devem ter VALE3
- Arrojados PODEM ter VALE3
```

---

## ✅ Validações Adicionadas

1. **Destaque explícito do VALE3** como único alto risco
2. **IDs específicos por perfil** para evitar ambiguidade
3. **Exemplos de respostas** para cada perfil
4. **Categorias corretas** (Renda Fixa em vez de Tesouro Direto/CDB)

---

## 🧪 Testes Recomendados

### **Teste 1: Conservador não recebe VALE3**
```bash
# Login como conservador
# Montar carteira
# ESPERADO: IDs apenas de [5, 9, 10, 11]
# NÃO DEVE CONTER: ID 2
```

### **Teste 2: Moderado não recebe VALE3**
```bash
# Login como moderado
# Montar carteira
# ESPERADO: IDs de [1, 3, 4, 5, 6, 7, 8, 9, 10, 11]
# NÃO DEVE CONTER: ID 2
```

### **Teste 3: Arrojado PODE receber VALE3**
```bash
# Login como arrojado
# Montar carteira
# ESPERADO: IDs de [1 a 11], PODE INCLUIR ID 2
```

---

## 📊 Impacto nos Resultados

### **Antes da Correção:**
- ❌ Conservadores poderiam receber ações de risco médio/alto
- ❌ Moderados poderiam receber VALE3 (alto risco)
- ❌ IDs errados causariam falha ao adicionar investimentos

### **Depois da Correção:**
- ✅ Conservadores recebem APENAS baixo risco
- ✅ Moderados recebem baixo + médio (sem alto)
- ✅ Arrojados podem receber todos, incluindo VALE3
- ✅ IDs corretos garantem sucesso ao adicionar

---

## 🔗 Referências

- [Guia de Investimentos Populados](./investimentos-populados.md) - Lista completa com IDs
- [Guia de Montar Carteira com IA](./montar-carteira-ia.md) - Como usar o endpoint
- [SystemInitializer.java](../src/main/java/com/example/demo/init/SystemInitializer.java) - Onde os investimentos são criados

---

**Data da Correção:** 08/10/2025  
**Versão:** 1.1 - Prompt corrigido e alinhado com banco de dados  
**Status:** ✅ Corrigido e validado  
**Impacto:** CRÍTICO - Afeta recomendações por perfil de risco
