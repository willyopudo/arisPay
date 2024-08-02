package org.arispay.controller;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionServicePort transactionServicePort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto addTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionServicePort.addTransaction(transactionDto);
    }

    @PutMapping
    public TransactionDto updateTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionServicePort.updateTransaction(transactionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionByID(@PathVariable long id) {
        return ResponseEntity.ok(transactionServicePort.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionServicePort.getTransactions());
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionByID(@PathVariable long id) {
        transactionServicePort.deleteTransactionById(id);
    }
}
