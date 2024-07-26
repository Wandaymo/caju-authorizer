package br.com.wandaymo.caju.authorizer.api.controller;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.api.dto.TransactionResponseDTO;
import br.com.wandaymo.caju.authorizer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<TransactionResponseDTO> purchase(@RequestBody TransactionDTO transactionDTO) {

        return ResponseEntity.ok(transactionService.purchase(transactionDTO));
    }
}
