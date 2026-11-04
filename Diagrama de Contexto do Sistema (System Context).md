# Documentação de Arquitetura - iFood Pago
Projeto: `pagamento-service-resiliente`

## 1. C4 Nível 1: Diagrama de Contexto do Sistema (System Context)
Este diagrama mapeia os atores externos (Clientes, Administradores, Adquirentes) e como eles interagem com o ecossistema do iFood Pago.

```mermaid
flowchart TD
    subgraph External [Ambiente Externo]
        Customer[("Cliente iFood<br/><b>[Person]</b><br/>Realiza pedidos e pagamentos")]
        Admin[("Operador / SRE<br/><b>[Person]</b><br/>Monitora métricas e resiliência")]
        Acquirers["Adquirentes e Bandeiras Externas<br/><b>[External System]</b><br/>APIs de autorização de cartões"]
        Bureau["Bureau Antifraude<br/><b>[External System]</b><br/>Validação de risco e fraude"]
    end

    subgraph System [Ecossistema iFood Pago]
        Pago["iFood Pago<br/><b>[Software System]</b><br/>Processamento seguro e resiliente de pagamentos"]
    end

    Customer -->|Envia transações| Pago
    Admin -->|Monitora telemetria| Pago
    Pago -->|Processa transações / Circuit Breaker| Acquirers
    Pago -->|Valida risco| Bureau
