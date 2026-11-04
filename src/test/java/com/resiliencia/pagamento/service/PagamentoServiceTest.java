package com.resiliencia.pagamento.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PagamentoServiceTest {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("pagamentoService");
        circuitBreaker.reset();
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        String resultado = pagamentoService.processarPagamento(150.0);
        assertThat(resultado).contains("processado com sucesso pelo gateway");
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void deveAcionarFallbackEmCasoDeErro() {
        String resultado = pagamentoService.processarPagamento(-10.0);
        assertThat(resultado).contains("ALERTA DE RESILIÊNCIA");
    }
}