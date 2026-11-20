package com.resiliencia.pagamento.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.resiliencia.pagamento.model.PagamentoRequest;
import com.resiliencia.pagamento.model.PagamentoResponse;
import com.resiliencia.pagamento.exception.AdquirenteIndisponivelException;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    @CircuitBreaker(name = "adquirenteCircuitBreaker", fallbackMethod = "processarFallbackInteligente")
    @Bulkhead(name = "adquirenteBulkhead", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "processarFallbackInteligente")
    public PagamentoResponse executarPagamentoAdquirente(PagamentoRequest request) {
        logger.info("Enviando transação para a adquirente primária. Pedido ID: {}", request.getPedidoId());
        
        chamarGatewayPrimario(request);

        return new PagamentoResponse(request.getPedidoId(), "APROVADO", "Transação autorizada pela adquirente primária.");
    }

    public void chamarGatewayPrimario(PagamentoRequest request) {
        // Lógica de integração HTTP real
        // Simulação de falha para experimento de caos em staging:
        //throw new AdquirenteIndisponivelException("Timeout / indisponibilidade na adquirente primária");
    }

    public PagamentoResponse processarFallbackInteligente(PagamentoRequest request, Throwable t) {
        logger.warn("Circuit Breaker aberto ou falha detectada para o pedido {}. Motivo: {}. Executando Fallback Inteligente...", 
                    request.getPedidoId(), t.getMessage());

        try {
            logger.info("Tentando processamento alternativo via Adquirente de Backup para o pedido: {}", request.getPedidoId());
            return processarAdquirenteBackup(request);
        } catch (Exception ex) {
            logger.error("Adquirente de backup também falhou. Encaminhando para fila de reprocessamento assíncrono (DLQ).");
        }

        return new PagamentoResponse(
            request.getPedidoId(), 
            "EM_PROCESSAMENTO_ASSINCRONO", 
            "A adquirente principal está temporariamente indisponível. Sua transação foi enfileirada para processamento prioritário."
        );
    }

    private PagamentoResponse processarAdquirenteBackup(PagamentoRequest request) {
        return new PagamentoResponse(request.getPedidoId(), "APROVADO_BACKUP", "Transação processada com sucesso pela Adquirente Secundária.");
    }
}