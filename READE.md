# 🧬 API de Validação de Identidade (Biometria)

Esta é uma API REST desenvolvida em Java com Spring Boot para um sistema de validação de identidade por biometria. O projeto permite o cadastro de usuários e a simulação do processo de validação biométrica com análise de status.

---

## 🚀 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA** (Persistência de dados)
* **MySQL** (Banco de dados via Docker)
* **Docker & Docker Compose** (Ambiente isolado)
* **JUnit 5 & Spring Boot Test** (Testes automatizados de integração)
* **Lombok** (Produtividade e código limpo)

---

## 🛠️ Arquitetura e Organização do Código

O projeto foi estruturado seguindo o padrão de **Arquitetura em Camadas**, garantindo uma separação clara de responsabilidades:

* `controller`: Porta de entrada da aplicação, responsável por expor os endpoints REST e receber as requisições HTTP.
* `service`: Camada de negócio, onde residem todas as regras, validações e a lógica de simulação da biometria.
* `domain.model`: Entidades mapeadas que representam as tabelas do banco de dados.
* `repository`: Interfaces que utilizam o Spring Data JPA para comunicação com o banco de dados.

### 🧠 Decisões Técnicas & Regras de Negócio Implementadas
1.  **Chaves Primárias com UUID:** Utilizadas para garantir maior segurança nas URLs dos endpoints, evitando exposição de IDs sequenciais.
2.  **Simulação Realista (80/20):** O motor de validação biométrica foi implementado na camada de serviço utilizando probabilidades, simulando cenários reais onde a maioria das requisições é aprovada (`APPROVED`), mas prevendo uma margem de recusa técnica ou suspeita de fraude (`REJECTED`).
3.  **Tratamento de Exceções:** Implementação de retornos HTTP semânticos (ex: `404 Not Found` para usuários inexistentes e `409 Conflict` para tentativas de documentos duplicados ou revalidação de biometrias já processadas).

---

## 📦 Como Executar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.
* Java 17 instalado (caso queira rodar localmente fora do container).

### Passo a Passo

1.  **Subir o Banco de Dados (Docker):**
    No terminal, na raiz do projeto onde está o arquivo `docker-compose.yml`, execute:
    ```bash
    docker-compose up -d
    ```

2.  **Executar a Aplicação:**
    Abra o projeto na sua IDE de preferência (VS Code, IntelliJ, etc.) e execute a classe principal `BiometriaApplication.java`, ou utilize o comando Maven no terminal:
    ```bash
    ./mvnw spring-boot:run
    ```
    A API estará disponível em: `http://localhost:8080`

---

## 🧪 Como Rodar os Testes Automatizados

Foi desenvolvida uma suíte de testes de integração utilizando **JUnit 5** e `@SpringBootTest` que valida todo o fluxo da aplicação de forma automatizada (Criação de usuário, validação de documento duplicado, listagem e alteração de status da biometria).

Para rodar os testes via terminal, execute:
```bash
./mvnw test

Ou abra o arquivo src/test/java/com/bruna/biometria/BiometriaApplicationTests.java na sua IDE e clique em Run Test.

🔌 Endpoints da API
1. Criar Usuário
Rota: POST /users

Corpo da Requisição (JSON):

{
  "name": "Bruna Christina",
  "document": "12345678900"
}

Resposta de Sucesso (201 Created): Retorna o usuário com o status ⁠PENDING⁠ e o ⁠id⁠ gerado

2. Listar Usuários
 Rota: ⁠GET /users⁠
 Resposta (200 OK): Retorna a lista de todos os usuários cadastrados.

3. Buscar Usuário por ID
 Rota: ⁠GET /users/{id}⁠
 Resposta (200 OK): Retorna os detalhes do usuário correspondente ao UUID informado.
 
4. Validar Biometria
 Rota: ⁠POST /users/{id}/validate⁠
 Resposta (200 OK): Executa a simulação e retorna o usuário atualizado com o status final (⁠APPROVED⁠ ou ⁠REJECTED⁠).
