package com.resiliencia.pagamento.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    private static final String REGISTRY_NAME = "pagamentoService";

    @CircuitBreaker(name = REGISTRY_NAME, fallbackMethod = "pagamentoFallback")
    @Retry(name = REGISTRY_NAME) // Adiciona a política de tentativas automáticas configurada no yml
    @Bulkhead(name = REGISTRY_NAME, type = Bulkhead.Type.SEMAPHORE)
    public String processarPagamento(Double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor de pagamento inválido!");
        }
        
        return "Pagamento de R$ " + valor + " processado com sucesso pelo gateway!";
    }

    public String pagamentoFallback(Double valor, Throwable t) {
        return "ALERTA DE RESILIÊNCIA: O serviço de pagamentos está instável no momento. " +
               "A transação no valor de R$ " + valor + " foi encaminhada para a fila de reprocessamento (Fallback). " +
               "Motivo: " + t.getMessage();
    }
}