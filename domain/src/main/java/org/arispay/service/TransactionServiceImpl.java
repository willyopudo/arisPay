package org.arispay.service;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionServicePort;
import org.arispay.ports.spi.TransactionPersistencePort;

public class TransactionServiceImpl implements TransactionServicePort {

    private TransactionPersistencePort transactionPersistencePort;

    public TransactionServiceImpl(TransactionPersistencePort transactionPersistencePort) {
        this.transactionPersistencePort = transactionPersistencePort;
    }

    @Override
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        return transactionPersistencePort.addTransaction(transactionDto);
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionPersistencePort.deleteTransactionById(id);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        return transactionPersistencePort.updateTransaction(transactionDto);
    }

    @Override
    public List<TransactionDto> getTransactions() {
        return transactionPersistencePort.getTransactions();
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionPersistencePort.getTransactionById(id);
    }
}
