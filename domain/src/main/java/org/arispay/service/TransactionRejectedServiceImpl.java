package org.arispay.service;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionRejectedServicePort;
import org.arispay.ports.spi.TransactionRejectedPersistencePort;

public class TransactionRejectedServiceImpl implements TransactionRejectedServicePort {
    private TransactionRejectedPersistencePort transactionRejectedPersistencePort;

    public TransactionRejectedServiceImpl(TransactionRejectedPersistencePort transactionRejectedPersistencePort) {
        this.transactionRejectedPersistencePort = transactionRejectedPersistencePort;
    }

    @Override
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        return transactionRejectedPersistencePort.addTransaction(transactionDto);
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionRejectedPersistencePort.deleteTransactionById(id);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        return transactionRejectedPersistencePort.updateTransaction(transactionDto);
    }

    @Override
    public List<TransactionDto> getTransactions() {
        return transactionRejectedPersistencePort.getTransactions();
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRejectedPersistencePort.getTransactionById(id);
    }
}
