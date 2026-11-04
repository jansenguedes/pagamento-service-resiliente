## 2. C4 Nível 2: Diagrama de Contêineres (Container Diagram)
Este diagrama detalha a arquitetura interna de microsserviços em Spring Boot.

```mermaid
flowchart LR
    Customer["Cliente iFood (Person)"]

    subgraph Gateway [Borda]
        APIGateway["API Gateway / BFF<br/>Spring Cloud Gateway<br/>Roteamento e autenticação"]
    end

    subgraph Domain [Domínio iFood Pago]
        PaymentService["Pagamento Service<br/>Java, Spring Boot, Resilience4j<br/>Circuit Breaker, Retry e Bulkhead"]
        FallbackService["Fallback Service<br/>Java, Spring Boot, Mensageria<br/>Filas de reprocessamento"]
        Redis[(Cache Redis<br/>Estados de circuitos)]
    end

    subgraph Ext [Sistemas Externos]
        AcquirerPrimary["Adquirente Primário"]
        AcquirerSecondary["Adquirente Secundário"]
    end

    Customer -->|HTTPS| APIGateway
    APIGateway -->|gRPC / REST| PaymentService
    PaymentService -->|Consulta estado| Redis
    PaymentService -->|Transação protegida| AcquirerPrimary
    PaymentService -->|Em caso de falha| FallbackService
    FallbackService -->|Pagamento alternativo| AcquirerSecondary
