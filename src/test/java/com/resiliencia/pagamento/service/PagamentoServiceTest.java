package com.resiliencia.pagamento.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.resiliencia.pagamento.model.PagamentoRequest;
import com.resiliencia.pagamento.model.PagamentoResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class PagamentoServiceTest {

    @SpyBean
    private PagamentoService pagamentoService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("adquirenteCircuitBreaker");
        circuitBreaker.reset();
    }

    @Test
    void deveAcionarFallbackQuandoAdquirenteFalhar() {
        doThrow(new RuntimeException("Timeout na adquirente primária"))
                .when(pagamentoService).chamarGatewayPrimario(any(PagamentoRequest.class));

        PagamentoRequest request = new PagamentoRequest("pedido-999", 250.00);
        PagamentoResponse response = pagamentoService.executarPagamentoAdquirente(request);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isIn("APROVADO_BACKUP", "EM_PROCESSAMENTO_ASSINCRONO");
    }

    @Test
    void deveAbrirCircuitBreakerAposFalhasConsecutivas() {
        for (int i = 0; i < 5; i++) {
            try {
                circuitBreaker.executeSupplier(() -> {
                    throw new RuntimeException("Falha de conexão");
                });
            } catch (Exception ignored) {}
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}