# 🌳 Exemplo Visual - Árvore de Comentários

## Estrutura Hierárquica Completa

```
📊 Investimento: Tesouro Selic 2025 (ID: 1)
│
├─ 💬 [ID: 1] João Silva (01/10/2025 10:00)
│  └─ "Excelente investimento para iniciantes! 🎯"
│     │
│     ├─ 💬 [ID: 5] Maria Santos (02/10/2025 11:30)
│     │  └─ "Concordo! Comecei com esse também. Qual a rentabilidade que você está tendo?"
│     │     │
│     │     ├─ 💬 [ID: 10] João Silva (02/10/2025 12:00)
│     │     │  └─ "Estou com aproximadamente 10% ao ano, você?"
│     │     │     │
│     │     │     └─ 💬 [ID: 15] Maria Santos (02/10/2025 14:30)
│     │     │        └─ "Mesma coisa! Muito bom. Vou aumentar minha posição. 💰"
│     │     │
│     │     └─ 💬 [ID: 11] Pedro Oliveira (03/10/2025 09:00)
│     │        └─ "Eu também! E o melhor é a liquidez diária. 👍"
│     │
│     └─ 💬 [ID: 6] Ana Costa (02/10/2025 15:00)
│        └─ "Melhor que poupança, com certeza! E tem garantia do Tesouro."
│           │
│           └─ 💬 [ID: 12] Admin Sistema (03/10/2025 10:00)
│              └─ "Exatamente! E é isento de taxa de administração. 📊"
│
├─ 💬 [ID: 2] Carlos Mendes (01/10/2025 16:00)
│  └─ "Alguém sabe sobre o prazo de resgate? É imediato? 🤔"
│     │
│     ├─ 💬 [ID: 7] Admin Sistema (01/10/2025 16:30)
│     │  └─ "É D+0! O dinheiro cai na sua conta no mesmo dia da solicitação. 🚀"
│     │     │
│     │     └─ 💬 [ID: 13] Carlos Mendes (01/10/2025 17:00)
│     │        └─ "Perfeito! Obrigado pela informação! 🙏"
│     │
│     └─ 💬 [ID: 8] Roberto Lima (02/10/2025 08:00)
│        └─ "Sim, é muito rápido. Já resgatei várias vezes."
│
└─ 💬 [ID: 3] Fernanda Rocha (03/10/2025 11:00)
   └─ "Estou pensando em diversificar. Esse é bom para começar? 🤷‍♀️"
      │
      ├─ 💬 [ID: 9] João Silva (03/10/2025 11:30)
      │  └─ "Com certeza! É o melhor para reserva de emergência. 💯"
      │
      ├─ 💬 [ID: 14] Admin Sistema (03/10/2025 12:00)
      │  └─ "Sim! É a base de qualquer carteira. Baixo risco e boa liquidez. 📈"
      │     │
      │     └─ 💬 [ID: 16] Fernanda Rocha (03/10/2025 13:00)
      │        └─ "Obrigada! Vou investir hoje mesmo! 🎉"
      │
      └─ 💬 [ID: 17] Maria Santos (03/10/2025 14:00)
         └─ "Eu comecei com R$ 100 e fui aumentando aos poucos. Recomendo!"
```

---

## 📊 Estatísticas da Árvore

| Métrica | Valor |
|---------|-------|
| **Comentários Raiz** | 3 |
| **Total de Respostas** | 14 |
| **Total Geral** | 17 comentários |
| **Profundidade Máxima** | 4 níveis |
| **Comentário com Mais Respostas** | ID: 1 (4 respostas diretas) |

---

## 🔍 Estrutura JSON - Exemplo Completo

### Resposta da API: `GET /comentarios/investimento/1`

```json
{
  "investimentoId": 1,
  "totalComentarios": 3,
  "comentarios": [
    {
      "id": 1,
      "conteudo": "Excelente investimento para iniciantes! 🎯",
      "usuarioId": 2,
      "nomeUsuario": "João Silva",
      "emailUsuario": "joao@email.com",
      "investimentoId": 1,
      "nomeInvestimento": "Tesouro Selic 2025",
      "simboloInvestimento": "TESOURO_SELIC",
      "dataCriacao": "01/10/2025 10:00",
      "dataAtualizacao": null,
      "editado": false,
      "ativo": true,
      "comentarioPaiId": null,
      "numeroRespostas": 2,
      "respostas": [
        {
          "id": 5,
          "conteudo": "Concordo! Comecei com esse também. Qual a rentabilidade que você está tendo?",
          "usuarioId": 3,
          "nomeUsuario": "Maria Santos",
          "emailUsuario": "maria@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "02/10/2025 11:30",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 1,
          "numeroRespostas": 2,
          "respostas": [
            {
              "id": 10,
              "conteudo": "Estou com aproximadamente 10% ao ano, você?",
              "usuarioId": 2,
              "nomeUsuario": "João Silva",
              "emailUsuario": "joao@email.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "02/10/2025 12:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 5,
              "numeroRespostas": 1,
              "respostas": [
                {
                  "id": 15,
                  "conteudo": "Mesma coisa! Muito bom. Vou aumentar minha posição. 💰",
                  "usuarioId": 3,
                  "nomeUsuario": "Maria Santos",
                  "emailUsuario": "maria@email.com",
                  "investimentoId": 1,
                  "nomeInvestimento": "Tesouro Selic 2025",
                  "simboloInvestimento": "TESOURO_SELIC",
                  "dataCriacao": "02/10/2025 14:30",
                  "dataAtualizacao": null,
                  "editado": false,
                  "ativo": true,
                  "comentarioPaiId": 10,
                  "numeroRespostas": 0,
                  "respostas": []
                }
              ]
            },
            {
              "id": 11,
              "conteudo": "Eu também! E o melhor é a liquidez diária. 👍",
              "usuarioId": 4,
              "nomeUsuario": "Pedro Oliveira",
              "emailUsuario": "pedro@email.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "03/10/2025 09:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 5,
              "numeroRespostas": 0,
              "respostas": []
            }
          ]
        },
        {
          "id": 6,
          "conteudo": "Melhor que poupança, com certeza! E tem garantia do Tesouro.",
          "usuarioId": 5,
          "nomeUsuario": "Ana Costa",
          "emailUsuario": "ana@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "02/10/2025 15:00",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 1,
          "numeroRespostas": 1,
          "respostas": [
            {
              "id": 12,
              "conteudo": "Exatamente! E é isento de taxa de administração. 📊",
              "usuarioId": 1,
              "nomeUsuario": "Admin Sistema",
              "emailUsuario": "admin@investimentos.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "03/10/2025 10:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 6,
              "numeroRespostas": 0,
              "respostas": []
            }
          ]
        }
      ]
    },
    {
      "id": 2,
      "conteudo": "Alguém sabe sobre o prazo de resgate? É imediato? 🤔",
      "usuarioId": 6,
      "nomeUsuario": "Carlos Mendes",
      "emailUsuario": "carlos@email.com",
      "investimentoId": 1,
      "nomeInvestimento": "Tesouro Selic 2025",
      "simboloInvestimento": "TESOURO_SELIC",
      "dataCriacao": "01/10/2025 16:00",
      "dataAtualizacao": null,
      "editado": false,
      "ativo": true,
      "comentarioPaiId": null,
      "numeroRespostas": 2,
      "respostas": [
        {
          "id": 7,
          "conteudo": "É D+0! O dinheiro cai na sua conta no mesmo dia da solicitação. 🚀",
          "usuarioId": 1,
          "nomeUsuario": "Admin Sistema",
          "emailUsuario": "admin@investimentos.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "01/10/2025 16:30",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 2,
          "numeroRespostas": 1,
          "respostas": [
            {
              "id": 13,
              "conteudo": "Perfeito! Obrigado pela informação! 🙏",
              "usuarioId": 6,
              "nomeUsuario": "Carlos Mendes",
              "emailUsuario": "carlos@email.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "01/10/2025 17:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 7,
              "numeroRespostas": 0,
              "respostas": []
            }
          ]
        },
        {
          "id": 8,
          "conteudo": "Sim, é muito rápido. Já resgatei várias vezes.",
          "usuarioId": 7,
          "nomeUsuario": "Roberto Lima",
          "emailUsuario": "roberto@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "02/10/2025 08:00",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 2,
          "numeroRespostas": 0,
          "respostas": []
        }
      ]
    },
    {
      "id": 3,
      "conteudo": "Estou pensando em diversificar. Esse é bom para começar? 🤷‍♀️",
      "usuarioId": 8,
      "nomeUsuario": "Fernanda Rocha",
      "emailUsuario": "fernanda@email.com",
      "investimentoId": 1,
      "nomeInvestimento": "Tesouro Selic 2025",
      "simboloInvestimento": "TESOURO_SELIC",
      "dataCriacao": "03/10/2025 11:00",
      "dataAtualizacao": null,
      "editado": false,
      "ativo": true,
      "comentarioPaiId": null,
      "numeroRespostas": 3,
      "respostas": [
        {
          "id": 9,
          "conteudo": "Com certeza! É o melhor para reserva de emergência. 💯",
          "usuarioId": 2,
          "nomeUsuario": "João Silva",
          "emailUsuario": "joao@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "03/10/2025 11:30",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 3,
          "numeroRespostas": 0,
          "respostas": []
        },
        {
          "id": 14,
          "conteudo": "Sim! É a base de qualquer carteira. Baixo risco e boa liquidez. 📈",
          "usuarioId": 1,
          "nomeUsuario": "Admin Sistema",
          "emailUsuario": "admin@investimentos.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "03/10/2025 12:00",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 3,
          "numeroRespostas": 1,
          "respostas": [
            {
              "id": 16,
              "conteudo": "Obrigada! Vou investir hoje mesmo! 🎉",
              "usuarioId": 8,
              "nomeUsuario": "Fernanda Rocha",
              "emailUsuario": "fernanda@email.com",
              "investimentoId": 1,
              "nomeInvestimento": "Tesouro Selic 2025",
              "simboloInvestimento": "TESOURO_SELIC",
              "dataCriacao": "03/10/2025 13:00",
              "dataAtualizacao": null,
              "editado": false,
              "ativo": true,
              "comentarioPaiId": 14,
              "numeroRespostas": 0,
              "respostas": []
            }
          ]
        },
        {
          "id": 17,
          "conteudo": "Eu comecei com R$ 100 e fui aumentando aos poucos. Recomendo!",
          "usuarioId": 3,
          "nomeUsuario": "Maria Santos",
          "emailUsuario": "maria@email.com",
          "investimentoId": 1,
          "nomeInvestimento": "Tesouro Selic 2025",
          "simboloInvestimento": "TESOURO_SELIC",
          "dataCriacao": "03/10/2025 14:00",
          "dataAtualizacao": null,
          "editado": false,
          "ativo": true,
          "comentarioPaiId": 3,
          "numeroRespostas": 0,
          "respostas": []
        }
      ]
    }
  ]
}
```

---

## 🎨 Exemplo de Renderização Frontend (React)

```jsx
const ComentarioArvore = ({ comentario, nivel = 0 }) => {
  const marginLeft = nivel * 20; // Indentação por nível

  return (
    <div style={{ marginLeft: `${marginLeft}px`, marginTop: '10px' }}>
      <div className="comentario-card">
        <div className="comentario-header">
          <strong>{comentario.nomeUsuario}</strong>
          <span className="data">{comentario.dataCriacao}</span>
        </div>
        <p className="comentario-conteudo">{comentario.conteudo}</p>
        <div className="comentario-footer">
          <span>💬 {comentario.numeroRespostas} respostas</span>
          <button onClick={() => responder(comentario.id)}>Responder</button>
        </div>
      </div>

      {/* Renderizar respostas recursivamente */}
      {comentario.respostas && comentario.respostas.length > 0 && (
        <div className="respostas">
          {comentario.respostas.map(resposta => (
            <ComentarioArvore 
              key={resposta.id} 
              comentario={resposta} 
              nivel={nivel + 1} 
            />
          ))}
        </div>
      )}
    </div>
  );
};

// Uso
<div className="comentarios-investimento">
  {comentariosRaiz.map(comentario => (
    <ComentarioArvore key={comentario.id} comentario={comentario} />
  ))}
</div>
```

---

## 📱 Exemplo de Interface Visual

```
┌─────────────────────────────────────────────────────────────┐
│ 💬 Comentários sobre Tesouro Selic 2025                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ 👤 João Silva · 01/10/2025 10:00                          │
│ Excelente investimento para iniciantes! 🎯                 │
│ [💬 2 respostas] [↩️ Responder]                            │
│                                                             │
│   ├─ 👤 Maria Santos · 02/10/2025 11:30                   │
│   │  Concordo! Qual a rentabilidade que você está tendo?  │
│   │  [💬 2 respostas] [↩️ Responder]                       │
│   │                                                         │
│   │    ├─ 👤 João Silva · 02/10/2025 12:00                │
│   │    │  Estou com aproximadamente 10% ao ano            │
│   │    │  [💬 1 resposta] [↩️ Responder]                  │
│   │    │                                                   │
│   │    │    └─ 👤 Maria Santos · 02/10/2025 14:30        │
│   │    │       Mesma coisa! Vou aumentar minha posição 💰│
│   │    │       [↩️ Responder]                             │
│   │    │                                                   │
│   │    └─ 👤 Pedro Oliveira · 03/10/2025 09:00           │
│   │       E o melhor é a liquidez diária! 👍             │
│   │       [↩️ Responder]                                  │
│   │                                                         │
│   └─ 👤 Ana Costa · 02/10/2025 15:00                      │
│      Melhor que poupança, com certeza!                    │
│      [💬 1 resposta] [↩️ Responder]                        │
│                                                             │
│        └─ 👤 Admin · 03/10/2025 10:00                     │
│           Exatamente! Isento de taxa de administração 📊  │
│           [↩️ Responder]                                   │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ 👤 Carlos Mendes · 01/10/2025 16:00                       │
│ Alguém sabe sobre o prazo de resgate? 🤔                  │
│ [💬 2 respostas] [↩️ Responder]                            │
│                                                             │
│   ├─ 👤 Admin · 01/10/2025 16:30                          │
│   │  É D+0! Cai no mesmo dia 🚀                           │
│   │  [💬 1 resposta] [↩️ Responder]                        │
│   │                                                         │
│   │    └─ 👤 Carlos Mendes · 01/10/2025 17:00            │
│   │       Perfeito! Obrigado! 🙏                          │
│   │       [↩️ Responder]                                   │
│   │                                                         │
│   └─ 👤 Roberto Lima · 02/10/2025 08:00                   │
│      Sim, já resgatei várias vezes. Muito rápido!         │
│      [↩️ Responder]                                        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 Comportamento das Ações

### Ao Clicar em "Responder"
```javascript
function responder(comentarioId) {
  // Abre campo de resposta abaixo do comentário
  const form = `
    <form onsubmit="enviarResposta(${comentarioId})">
      <textarea placeholder="Escreva sua resposta..."></textarea>
      <button type="submit">Enviar</button>
      <button type="button" onclick="cancelar()">Cancelar</button>
    </form>
  `;
  
  document.getElementById(`comentario-${comentarioId}`).innerHTML += form;
}

async function enviarResposta(comentarioPaiId) {
  const conteudo = document.querySelector('textarea').value;
  
  await fetch('/comentarios', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      conteudo: conteudo,
      investimentoId: investimentoAtual,
      comentarioPaiId: comentarioPaiId
    })
  });
  
  // Recarregar árvore de comentários
  recarregarComentarios();
}
```

---

## ✅ Resultado Esperado

Após a implementação, o sistema permite:

1. ✅ Comentários em níveis ilimitados
2. ✅ Visualização hierárquica clara
3. ✅ Contador de respostas em cada nível
4. ✅ Navegação intuitiva pela árvore
5. ✅ Responder qualquer comentário/resposta
6. ✅ Editar/excluir mantém integridade
7. ✅ Performance otimizada (carregamento recursivo)

---

**Similar a redes sociais modernas! 🎉**
