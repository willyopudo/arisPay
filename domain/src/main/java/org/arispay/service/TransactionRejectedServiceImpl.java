package org.arispay.service;

import java.util.List;
import java.util.Optional;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionRejectedServicePort;
import org.arispay.ports.spi.TransactionRejectedPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter) {
        return transactionRejectedPersistencePort.getTransactions(companyId, pageable, filter);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRejectedPersistencePort.getTransactionById(id);
    }

    @Override
    public Optional<ISummary> getTransactionRejectedSummaries(Long companyId) {
        return transactionRejectedPersistencePort.getTransactionRejectedSummaries(companyId);
    }
}
