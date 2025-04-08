# API de Integração com o HubSpot

Aplicação Spring Boot para integração com a plataforma HubSpot, oferecendo autenticação OAuth2, gerenciamento de contatos no CRM e processamento de webhooks.

---

## 🛠️ Pré-requisitos

- [Java 17+](https://ngrok.com/download)
- [Maven 3.8+](https://maven.apache.org/)
- [Conta de desenvolvedor no HubSpot](https://developers.hubspot.com/)
- [Ngrok](https://ngrok.com/download) (para testes locais)

## 📌 Visão Rápida (Quick Start)

Siga esses passos para executar o projeto localmente:

1. **Configure as variáveis** no `application.yml`:
   ```yaml
   hubspot:
     client-id: seu-client-id  # Obtenha em [HubSpot Developers](https://developers.hubspot.com/)
     redirect-uri: https://seu-ngrok.ngrok.io/auth/callback
   ```
- Pode ser configurado diretamente no IntelliJ IDEA (caso esteja usando) ou no terminal:

  - Via terminal:
  ```bash
  export HUBSPOT_CLIENT_ID=seu-client-id
  export HUBSPOT_CLIENT_SECRET=seu-client-secret
  export HUBSPOT_REDIRECT_URI=https://seu-ngrok.ngrok.io/auth/callback
  ```
  - Via IntelliJ IDEA:
    - Vá em `Run` > `Edit Configurations...`
    
    ![1 - Edit configurations.png](src/main/resources/img/1%20-%20Edit%20configurations.png)
    - Selecione a configuração do projeto e adicione as variáveis de ambiente.
    
    ![2 - Edite as Environment Variables.png](src/main/resources/img/2%20-%20Edite%20as%20Environment%20Variables.png)

2. **Inicie o Ngrok** (em um terminal separado):
```bash
ngrok http 8080
```

3. **Execute a aplicação**:
```bash
mvn spring-boot:run
```

## 🔄 Fluxo de Autenticação OAuth2

1. Acesse a URL de autenticação: [Gerar URL](http://localhost:8080/auth/authorize)
2. Faça login no HubSpot e autorize a aplicação.
3. Após a autorização, o token é salvo no banco de dados e válido por 30 minutos (configurado para 1 minuto para fins de teste).
4. Você será redirecionado para o endpoint `/auth/callback` com o token de acesso. 
5. O token que ainda estiver vigente será utilizado para fazer chamadas à API do HubSpot.

## 📚 Documentação e Ferramentas

### 🔍 Documentação da API
Acesse a interface interativa do Swagger para explorar todos os endpoints:  
→ [Swagger UI](http://localhost:8080/swagger-ui.html)

### 🗃️ Banco de Dados H2 (Ambiente de Desenvolvimento)
Console administrativo do H2 Database:  
→ [H2 Console](http://localhost:8080/h2-console) - *(Disponível apenas em ambiente local durante a execução da aplicação - dados são apagados ao reiniciar a aplicação)*

**Credenciais de acesso:**
```properties
JDBC URL: jdbc:h2:mem:hubspotdb
Usuário:  sa
Senha:    (deixe em branco)
```

## 📡 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET`  | `/auth/authorize` | Inicia o fluxo de autorização |
| `GET`  | `/auth/callback` | Processa o callback de autorização |
| `POST` | `/api/hubspot/contacts` | Cria um novo contato no HubSpot |
| `POST` | `/api/hubspot/webhook` | Processa webhooks do HubSpot |


## 🏗️ Decisões Técnicas
1. Autenticação OAuth2
   - Fluxo: Código de autorização (Authorization Code Flow). 
   - Tokens: Armazenados no H2 com renovação em 1 minuto (para testes).

2. Feign Client 
   - Cliente HTTP declarativo para chamadas à API do HubSpot.
   - Vantagem: Integração simplificada com Spring.

3. Webhooks 
   - Validação de assinatura para segurança.

📚 Bibliotecas Utilizadas

1. **[Spring Boot](https://spring.io/projects/spring-boot)**
2. **[OpenFeign](https://spring.io/projects/spring-cloud-openfeign)** - Cliente HTTP
3. **[H2 Database](https://www.h2database.com/)** - Banco de dados em memória
4. **[Swagger](https://swagger.io/)** - Documentação de API
5. **[Lombok](https://projectlombok.org/)** - Redução de _código boilerplate_.

## 🔧 Melhorias Futuras

1. **Renovação de Token Automático**: evitar a necessidade de reautenticação manual.
2. **Salvar o Contato e outros objetos em banco persistente**: Substituir o H2 por PostgreSQL, MySQL, etc.
3. **Monitoramento**: Integração com algum serviço de monitoramento (ex: Grafana, Kibana, NewRelic) e obter Health checks detalhados.
4. **Segurança**: Usar Snyk ou outra ferramenta para verificar vulnerabilidades das dependências.
5. **Banco de Dados**: Usar Migrations do Liquibase para versionamento de banco de dados.
6. **Gerar documentação com plugin do Maven**: Usar o plugin do Maven para gerar a documentação da API, evitando poluir as classes.
7. **Testes**: Criar testes unitários e de integração para garantir a qualidade do código.

## ✅ Conclusão
Esta aplicação fornece uma base sólida para integração com o HubSpot, utilizando boas práticas de desenvolvimento e arquitetura. As melhorias sugeridas visam aumentar a robustez, segurança e escalabilidade da aplicação.
