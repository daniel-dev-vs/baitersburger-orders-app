# Baiters Burger - Orders API

## Visão Geral

O **Baiters Burger Orders API** é um microserviço de gerenciamento de pedidos desenvolvido como parte do ecossistema Baiters Burger. Este serviço é responsável por gerenciar todo o ciclo de vida dos pedidos, desde a criação até a conclusão, incluindo integração com sistema de pagamento e validação de clientes e produtos.

## Arquitetura

O projeto segue os princípios da **Clean Architecture** (Arquitetura Limpa), promovendo separação de responsabilidades e facilitando manutenção e testes.

### Camadas da Aplicação

```
┌─────────────────────────────────────────────────────────┐
│                   Presentation Layer                    │
│              (Controllers & DTOs)                       │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│                 Application Layer                       │
│           (Use Cases & Gateways Interfaces)            │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│                   Domain Layer                          │
│              (Entities & Business Rules)                │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│               Infrastructure Layer                      │
│      (Gateways Impl, APIs, Database, External APIs)    │
└─────────────────────────────────────────────────────────┘
```

### Estrutura de Pacotes

```
br. com.fiap.baitersburger_orders
├── domain
│   └── entities          # Entidades de domínio (Order, OrderStatus)
├── application
│   ├── usecases         # Interfaces dos casos de uso
│   │   └── impl         # Implementações dos casos de uso
│   ├── gateways         # Interfaces dos gateways
│   └── exception        # Exceções customizadas
└── infra
    ├── controllers      # Controllers REST
    ├── dtos            # Data Transfer Objects
    │   ├── order
    │   └── mercadopago
    ├── gateways        # Implementações dos gateways
    ├── apis            # Integrações com APIs externas
    │   └── feign       # Clients Feign
    ├── persistence     # Camada de persistência (DynamoDB)
    └── config          # Configurações Spring
```

## Tecnologias Utilizadas

### Backend Framework
- **Java 25**
- **Spring Boot 3.5.7**
- **Spring Cloud 2025.0.0**
- **Spring Web** - REST APIs
- **Spring Cloud OpenFeign** - Cliente HTTP declarativo
- **Spring Validation** - Validação de dados
- **Spring Actuator** - Health checks e métricas

### Banco de Dados
- **AWS DynamoDB** - Banco de dados NoSQL
- **AWS SDK DynamoDB 2.20.0**
- **AWS SDK DynamoDB Enhanced 2.20.0**

### Testes
- **JUnit 5** (JUnit Platform 1.11.4)
- **Cucumber 7.15.0** - Testes BDD
- **REST Assured 5.5.0** - Testes de API
- **Mockito** - Mocks e testes unitários
- **JaCoCo 0.8.14** - Cobertura de código

### Build & Deploy
- **Maven** - Gerenciador de dependências
- **Docker** - Containerização
- **Terraform** - Infrastructure as Code (AWS)

## Funcionalidades Principais

### 1. Gerenciamento de Pedidos

#### Criar Pedido
- **Endpoint**: `POST /orders`
- **Descrição**: Cria um novo pedido validando cliente e produtos
- **Fluxo**:
  1. Valida existência do cliente (se CPF informado)
  2. Valida existência dos produtos
  3. Cria o pedido no banco de dados
  4. Gera QR Code para pagamento (Mercado Pago)
  5. Atualiza pedido com QR Code
  6. Retorna pedido criado

#### Buscar Pedidos
- **Endpoint**: `GET /orders`
- **Descrição**: Lista todos os pedidos ou filtra por status
- **Parâmetros**:  `?status={status}` (opcional)

#### Buscar Pedido por ID
- **Endpoint**:  `GET /orders/{orderId}`
- **Descrição**: Retorna detalhes de um pedido específico

#### Atualizar Status do Pedido
- **Endpoint**: `PATCH /orders/{orderId}`
- **Descrição**: Atualiza o status do pedido
- **Statuses disponíveis**: 
  - `REQUESTED` - Pedido solicitado
  - `RECEIVED` - Pedido recebido (pagamento confirmado)
  - `PREPARING` - Em preparação
  - `READY` - Pronto
  - `FINISHED` - Finalizado

### 2. Integração com Pagamento

#### Webhook Mercado Pago
- **Endpoint**: `POST /orders/webhooks`
- **Descrição**:  Recebe notificações do Mercado Pago sobre pagamentos
- **Ação**: Atualiza status do pedido para `RECEIVED` quando pagamento confirmado

### 3. Integrações Externas

#### Customer Service
- Valida existência de clientes por CPF
- Client Feign:  `CustomerClient`

#### Product Service
- Valida existência de produtos
- Client Feign: `ProductClient`

#### Mercado Pago API
- Geração de QR Codes para pagamento
- API:  `MercadoPagoApi`

## Casos de Uso Implementados

| Use Case | Descrição |
|----------|-----------|
| `CreateOrderUseCase` | Criação de novos pedidos com validações |
| `GetOrderUseCase` | Busca de pedidos (todos, por ID, por status) |
| `UpdateOrderUseCase` | Atualização de status dos pedidos |
| `CreateQRCodeUseCase` | Geração de QR Code para pagamento |
| `GetCustomerUseCase` | Validação de existência de clientes |
| `GetProductUseCase` | Validação de existência de produtos |

## Modelo de Dados

### Entidade:  Order

```java
{
  "id": "UUID",
  "productsId": ["string"],
  "totalPrice": "BigDecimal",
  "createdAt": "LocalDateTime",
  "status": "OrderStatus",
  "customerCpf": "string",
  "qrCode": "string"
}
```

## Configuração

### Variáveis de Ambiente Obrigatórias

```properties
# Mercado Pago
MERCADO_PAGO_ACCESS_TOKEN=<token>
MERCADO_PAGO_EXTERNAL_POS_ID=<pos_id>
MERCADO_PAGO_URL=<url>

# Microserviços
CUSTOMER_SERVICE_URL=<url>
PRODUCT_SERVICE_URL=<url>

# AWS DynamoDB
AWS_REGION=<region>
TABLE_ORDER=<table_name>
AWS_ACCESS_KEY=<access_key>
AWS_SECRET_KEY=<secret_key>

# Feature Flags
VALIDATE_SERVICE=false  # true para validar serviços externos
```

## Docker

O projeto inclui um `Dockerfile` para containerização da aplicação.

### Build da Imagem
```bash
cd baitersburger-orders
docker build -t baitersburger-orders:latest . 
```

### Executar Container
```bash
docker run -p 8080:8080 \
  -e MERCADO_PAGO_ACCESS_TOKEN=<token> \
  -e CUSTOMER_SERVICE_URL=<url> \
  -e PRODUCT_SERVICE_URL=<url> \
  -e AWS_ACCESS_KEY=<key> \
  -e AWS_SECRET_KEY=<secret> \
  baitersburger-orders:latest
```

## Infraestrutura (Terraform)

O diretório `/terraform` contém a infraestrutura como código para provisionar recursos na AWS:

- **DynamoDB Table** - Tabela de pedidos
- **ECS/EKS** - Orquestração de containers
- **Networking** - VPC, Subnets, Security Groups
- **IAM Roles** - Permissões de acesso

### Deploy da Infraestrutura
```bash
cd terraform
terraform init
terraform plan
terraform apply
```

## Testes

### Executar Testes Unitários
```bash
cd baitersburger-orders
mvn test
```

### Executar Testes com Cobertura
```bash
mvn clean test -Pcoverage
```

### Testes BDD (Cucumber)
Os testes de comportamento estão localizados em `src/test/resources/features/`.

### Estrutura de Testes
- **Unit Tests**: Testes unitários dos use cases e gateways
- **Integration Tests**: Testes de integração com REST Assured
- **BDD Tests**: Testes de comportamento com Cucumber

## Monitoramento

### Spring Actuator
A aplicação expõe endpoints de monitoramento:

- **Health Check**: `GET /actuator/health`
- **Metrics**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`

## Segurança

- Validação de entrada com Spring Validation
- Tratamento de exceções customizado
- Logs de auditoria
- Configuração de CORS (se necessário)

## Padrões de Status de Pedido

```
REQUESTED → RECEIVED → PREPARING → READY → FINISHED
```

## Padrões de Código

- **Clean Architecture**:  Separação clara de responsabilidades
- **SOLID Principles**: Código modular e testável
- **Dependency Injection**: Gerenciado pelo Spring
- **DTOs**: Separação entre camadas
- **Gateway Pattern**: Abstrações para integrações externas

## Como Executar Localmente

### Pré-requisitos
- Java 25
- Maven 3.x
- Docker (opcional)
- AWS CLI configurado (para DynamoDB local ou remoto)

### Passos

1. **Clone o repositório**
```bash
git clone https://github.com/daniel-dev-vs/baitersburger-orders-app.git
cd baitersburger-orders-app
git checkout feature/unit-test
```

2. **Configure as variáveis de ambiente**
```bash
export MERCADO_PAGO_ACCESS_TOKEN=your_token
export CUSTOMER_SERVICE_URL=http://localhost:8081
export PRODUCT_SERVICE_URL=http://localhost:8082
# ... outras variáveis
```

3. **Build do projeto**
```bash
cd baitersburger-orders
mvn clean install
```

4. **Execute a aplicação**
```bash
mvn spring-boot:run
```

5. **Acesse a API**
```
http://localhost:8080/orders
```

## Documentação Adicional

Para mais informações sobre endpoints e contratos da API, consulte:
- [Ver código no GitHub](https://github.com/daniel-dev-vs/baitersburger-orders-app/tree/feature/unit-test)

## Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto faz parte do ecossistema Baiters Burger.

## Autores

Desenvolvido por **daniel-dev-vs** e equipe FIAP.

---

**Versão**: 0.0.1-SNAPSHOT  
**Última atualização**:  Janeiro 2026
