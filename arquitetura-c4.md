# Documentação de Arquitetura - iFood Pago
Projeto: `pagamento-service-resiliente`

## 1. C4 Nível 1: Diagrama de Contexto do Sistema (System Context)
Este diagrama mapeia os atores externos (Clientes, Administradores, Adquirentes) e como eles interagem com o ecossistema do iFood Pago.

```mermaid
C4Context
    title Diagrama de Contexto - iFood Pago (Resiliência e Pagamentos)

    Person(customer, "Cliente iFood", "Realiza pedidos e efetua o pagamento no aplicativo.")
    Person(admin, "Operador / SRE", "Monitora métricas de resiliência, alarmes e painéis de falhas.")

    System(ifood_pago, "Ecossistema iFood Pago", "Sistema distribuído responsável pelo processamento seguro e resiliente de pagamentos.")

    SystemDb_Ext(external_acquirers, "Adquirentes e Bandeiras Externas", "APIs de terceiros para autorização de cartões (sujeitas a instabilidades e latência).")
    System_Ext(fraud_bureau, "Bureau Antifraude", "Serviço externo de análise de risco e validação de fraude.")

    Rel(customer, ifood_pago, "Envia transações de pagamento", "HTTPS / JSON")
    Rel(admin, ifood_pago, "Monitora telemetria e circuit breakers", "Grafana / Prometheus")
    Rel(ifood_pago, external_acquirers, "Processa e autoriza transações / Aplica Circuit Breaker", "REST / gRPC")
    Rel(ifood_pago, fraud_bureau, "Valida risco de fraude", "REST / JSON")
