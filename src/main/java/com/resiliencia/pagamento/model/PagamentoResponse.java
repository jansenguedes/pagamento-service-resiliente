package com.resiliencia.pagamento.model;

public class PagamentoResponse {
    private String pedidoId;
    private String status;
    private String mensagem;

    public PagamentoResponse(String pedidoId, String status, String mensagem) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.mensagem = mensagem;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }
}