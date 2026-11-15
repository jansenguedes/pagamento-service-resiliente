package com.resiliencia.pagamento.model;

public class PagamentoRequest {
    private String pedidoId;
    private double valor;

    public PagamentoRequest() {}

    public PagamentoRequest(String pedidoId, double valor) {
        this.pedidoId = pedidoId;
        this.valor = valor;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}