# 🎬 COPPERFILME

📄 **Documentação Swagger disponível em:**  
🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 Autenticação

O sistema utiliza autenticação via **JWT Token**.  
Para obter um token, faça uma requisição `POST` para o endpoint:

### Endpoint:

POST /auth/login

### Exemplo de payload:

```json
{
  "username": "Analista",
  "password": "analista@123"
}
``` 

## 👥 Credenciais de Usuários de Teste

| Usuário     | Username   | Senha           |
|-------------|------------|-----------------|
| Analista    | Analista   | analista\@123   |
| Revisor     | Revisor    | revisor\@123    |
| Aprovador 1 | Aprovador1 | aprovador1\@123 |
| Aprovador 2 | Aprovador2 | aprovador2\@123 |
| Aprovador 3 | Aprovador3 | aprovador3\@123 |

## ✅ Status possíveis no fluxo de aprovação

O sistema trabalha com os seguintes status ao longo do fluxo de avaliação dos scripts:

1. **AGUARDANDO_ANALISE** – O script foi enviado e aguarda que um analista inicie a análise.
2. **EM_ANALISE** – O analista está avaliando o conteúdo do script.
3. **AGUARDANDO_REVISAO** – O script foi analisado e aguarda a revisão.
4. **EM_REVISAO** – Um revisor está revisando o script.
5. **AGUARDANDO_APROVACAO** – O script revisado está aguardando aprovação final.
6. **EM_APROVACAO** – O script está em processo de aprovação por parte dos aprovadores.
7. **APROVADO** – O script foi aprovado com sucesso.
8. **RECUSADO** – O script foi recusado em alguma etapa do processo.