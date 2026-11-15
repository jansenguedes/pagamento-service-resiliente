package com.resiliencia.pagamento.exception;

public class AdquirenteIndisponivelException extends RuntimeException {
    public AdquirenteIndisponivelException(String message) {
        super(message);
    }
}