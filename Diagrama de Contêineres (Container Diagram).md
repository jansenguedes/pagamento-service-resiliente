## 2. C4 Nível 2: Diagrama de Contêineres (Container Diagram)
Este diagrama detalha a arquitetura interna de microsserviços em Spring Boot.

```mermaid
flowchart TD
    Customer["Cliente iFood (Person)"]

    subgraph Gateway [Borda]
        APIGateway["API Gateway / BFF<br/>Spring Cloud Gateway<br/>Roteamento e autenticação"]
    end

    subgraph Domain [Domínio iFood Pago - Cloud-Native]
        PaymentService["Pagamento Service<br/>Java, Spring Boot, Resilience4j<br/>Circuit Breaker, Retry e Bulkhead"]
        FallbackService["Fallback & Recovery Service<br/>Java, Spring Boot, Mensageria<br/>Filas de reprocessamento"]
        Redis[(Cache de Estados<br/>Redis<br/>Estados de circuitos)]
    end

    subgraph Ext [Sistemas Externos]
        AcquirerPrimary["Adquirente Primário<br/>External System"]
        AcquirerSecondary["Adquirente Secundário / Fallback<br/>External System"]
    end

    Customer -->|HTTPS| APIGateway
    APIGateway -->|gRPC / REST| PaymentService
    PaymentService -->|Consulta / Atualiza estado| Redis
    PaymentService -->|Envia transação protegida| AcquirerPrimary
    PaymentService -->|Aciona em caso de falha/timeout| FallbackService
    FallbackService -->|Roteia pagamento alternativo| AcquirerSecondary
