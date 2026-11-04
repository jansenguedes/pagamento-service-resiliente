flowchart TD
    Customer[("Cliente iFood<br/><b>[Person]</b>")]

    subgraph Gateway [Borda]
        APIGateway["API Gateway / BFF<br/><b>[Spring Cloud Gateway]</b><br/>Roteamento e autenticação de borda"]
    end

    subgraph Domain [Domínio iFood Pago - Cloud-Native]
        PaymentService["Pagamento Service<br/><b>[Java, Spring Boot, Resilience4j]</b><br/>Orquestração, Circuit Breaker, Retry e Bulkhead"]
        FallbackService["Fallback & Recovery Service<br/><b>[Java, Spring Boot, Mensageria]</b><br/>Filas de reprocessamento e contingência"]
        Redis[(("Cache de Estados<br/><b>[Redis]</b><br/>Estados de circuitos e cache rápido"))]
    end

    subgraph Ext [Sistemas Externos]
        AcquirerPrimary["Adquirente Primário<br/><b>[External System]</b>"]
        AcquirerSecondary["Adquirente Secundário / Fallback<br/><b>[External System]</b>"]
    end

    Customer -->|HTTPS| APIGateway
    APIGateway -->|gRPC / REST| PaymentService
    PaymentService -->|Consulta / Atualiza estado| Redis
    PaymentService -->|Envia transação (Protegido)| AcquirerPrimary
    PaymentService -->|Aciona em caso de falha/timeout| FallbackService
    FallbackService -->|Roteia pagamento alternativo| AcquirerSecondary
