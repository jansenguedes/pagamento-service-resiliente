package com.resiliencia.pagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PagamentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagamentoApplication.class, args);
        System.out.println("🚀 Microsserviço de Pagamentos de Alta Resiliência a iniciar com sucesso!");
    }
}