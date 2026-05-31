# Sistema de Reserva de Salas (API REST)

Projeto desenvolvido para a disciplina de Integração de Sistemas de Software (Avaliação A2-1). Esta é uma API RESTful em Java que gerencia o cadastro de salas e o agendamento de reservas, aplicando os conceitos de comunicação síncrona (consumo de API externa) e assíncrona (mensageria).

## 👥 Equipe
* Odair José Pereira de Oliveira Junior
* [Nome do Integrante 2]
* [Nome do Integrante 3]

## 🎯 Descrição do Sistema e Regras de Negócio
O sistema atua como um intermediário para gerenciamento de infraestrutura educacional. Suas principais integrações e lógicas incluem:

1. **Consumo de API Externa (ViaCEP):** Ao cadastrar uma nova sala informando apenas o CEP, o sistema consulta a API pública do ViaCEP de forma síncrona para preencher automaticamente o endereço.
2. **Validação de Regra de Negócio:** O sistema restringe o cadastro de salas à área de atuação da instituição. Se o CEP informado não pertencer ao estado do Paraná (PR), a API bloqueia a operação e retorna erro.
3. **Mensageria Assíncrona (RabbitMQ):** Ao aprovar e criar uma reserva no banco de dados, a API principal publica imediatamente uma mensagem em uma fila e devolve o *Status 201 Created* ao usuário. Um serviço paralelo (Consumer) captura essa mensagem em segundo plano e simula o processamento e disparo de um e-mail de confirmação, garantindo alta disponibilidade e baixo acoplamento.

---

## 🛠️ Tecnologias e Pré-requisitos
Para executar este projeto localmente, você precisará ter os seguintes softwares instalados:

* **Java JDK 11** (ou superior) - Necessário para a biblioteca nativa `java.net.http.HttpClient`.
* **MySQL Server** (versão 8.x recomendada).
* **Erlang OTP 27.x** (Importante: Versões 28 ou 29 apresentam instabilidade com a versão atual do RabbitMQ no Windows).
* **RabbitMQ Server** (versão 4.3.1).
* **IDE Java** (Eclipse, VS Code ou IntelliJ) com suporte a projetos Maven.
* **Postman** ou Insomnia para testar os endpoints.

---

## ⚙️ Configuração do Ambiente

### 1. Banco de Dados (MySQL)
Crie um banco de dados chamado `iss_reserva` e execute o script abaixo para criar as tabelas necessárias:

```sql
CREATE DATABASE iss_reserva;
USE iss_reserva;

CREATE TABLE sala (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    cep VARCHAR(20),
    localizacao VARCHAR(255)
);

CREATE TABLE reserva (
    id VARCHAR(50) PRIMARY KEY,
    sala_id VARCHAR(50) NOT NULL,
    professor VARCHAR(100) NOT NULL,
    data_reserva VARCHAR(20) NOT NULL,
    horario VARCHAR(20) NOT NULL,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);
```

### 2. Mensageria (RabbitMQ)
1. Certifique-se de que o **Erlang 27** e o **RabbitMQ 4.3.1** estão instalados.
2. Ative o painel de gerenciamento abrindo o *RabbitMQ Command Prompt* como Administrador e executando:
   `rabbitmq-plugins enable rabbitmq_management`
3. Inicie o servidor do RabbitMQ executando:
   `rabbitmq-server`
4. Confirme que o serviço está no ar acessando o painel web em `http://localhost:15672` (Login: `guest` / Senha: `guest`).

---

## ▶️ Como Executar o Projeto

Como o sistema possui uma arquitetura com processos assíncronos, é necessário iniciar dois serviços simultaneamente:

1. Importe o projeto como um **Projeto Maven** na sua IDE para que as dependências (`jackson-databind`, `mysql-connector-j`, `amqp-client`) sejam baixadas automaticamente.
2. **Execute a API REST:** Rode o arquivo `ApiServer.java`. O console deverá exibir: *"Servidor rodando na porta 8000..."*.
3. **Execute o Consumer (Serviço de E-mail):** Em um terminal ou console separado na mesma IDE, rode o arquivo `EmailConsumer.java`. O console deverá exibir: *"[*] Serviço de E-mail iniciado. Aguardando mensagens na fila..."*.

---

## 📡 Lista de Endpoints

Abaixo estão os principais endpoints para testar o fluxo de integração do projeto na porta `8000`.

### Salas (`/salas`)
* `GET /salas`: Lista todas as salas cadastradas.
* `GET /salas/{id}`: Retorna os detalhes de uma sala específica.
* `DELETE /salas/{id}`: Remove uma sala.
* `POST /salas`: Cria uma nova sala.
  * *Observação:* Informe um CEP do Paraná para o cadastro ser aprovado e buscar o endereço automaticamente.
  * **Body esperado (JSON):**
    ```json
    {
        "nome": "Laboratório de Redes",
        "capacidade": 40,
        "cep": "80010010" 
    }
    ```

### Reservas (`/reservas`)
* `GET /reservas`: Lista todas as reservas.
* `GET /reservas/{id}`: Retorna os detalhes de uma reserva específica.
* `DELETE /reservas/{id}`: Remove uma reserva.
* `POST /reservas`: Cria uma reserva e dispara a mensagem no RabbitMQ.
  * *Observação:* A API valida conflitos de horário. Se a reserva for bem-sucedida, observe o console do `EmailConsumer` registrar o recebimento da fila e simular o disparo de confirmação.
  * **Body esperado (JSON):**
    ```json
    {
        "salaId": "ID_GERADO_NA_CRIACAO_DA_SALA",
        "professor": "Prof. Marlon",
        "dataReserva": "2026-06-05",
        "horario": "19:00"
    }
    ```
