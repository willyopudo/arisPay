package org.arispay.ports.api;

import java.util.List;

import org.arispay.data.TransactionDto;

public interface TransactionServicePort {
    TransactionDto addTransaction(TransactionDto transactionDto);

    void deleteTransactionById(Long id);

    TransactionDto updateTransaction(TransactionDto transactionDto);

    List<TransactionDto> getTransactions();

    TransactionDto getTransactionById(Long id);
}
