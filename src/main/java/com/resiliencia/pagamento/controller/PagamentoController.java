package com.resiliencia.pagamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resiliencia.pagamento.model.PagamentoRequest;
import com.resiliencia.pagamento.model.PagamentoResponse;
import com.resiliencia.pagamento.service.PagamentoService;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponse> processarPagamento(@RequestBody PagamentoRequest request) {
        PagamentoResponse response = pagamentoService.executarPagamentoAdquirente(request);
        return ResponseEntity.ok(response);
    }
}