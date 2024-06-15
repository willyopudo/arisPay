package org.arispay.ports.api;

import java.util.List;

import org.arispay.data.TransactionDto;

public interface TransactionRejectedServicePort {
    TransactionDto addTransaction(TransactionDto clientDto);

    void deleteTransactionById(Long id);

    TransactionDto updateTransaction(TransactionDto clientDto);

    List<TransactionDto> getTransactions();

    TransactionDto getTransactionById(Long id);
}
