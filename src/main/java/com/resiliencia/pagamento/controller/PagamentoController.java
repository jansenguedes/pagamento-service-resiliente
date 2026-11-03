package com.resiliencia.pagamento.controller;

import com.resiliencia.pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<String> efetuarPagamento(@RequestParam Double valor) {
        String resultado = pagamentoService.processarPagamento(valor);
        return ResponseEntity.ok(resultado);
    }
}