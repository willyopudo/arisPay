package org.arispay.service;

import java.util.List;
import java.util.Optional;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionServicePort;
import org.arispay.ports.spi.TransactionPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class TransactionServiceImpl implements TransactionServicePort {

    private final TransactionPersistencePort transactionPersistencePort;

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
    public Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter) {
        return transactionPersistencePort.getTransactions(companyId, pageable, filter);
    }

    @Override
    public TransactionDto queryTransactions(Long companyId, GenericFilterDto filters) {
        return transactionPersistencePort.queryTransactions(companyId, filters);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionPersistencePort.getTransactionById(id);
    }

    @Override
    public Optional<ISummary> getTransactionSummaries(Long companyId) {
        return transactionPersistencePort.getTransactionSummaries(companyId);
    }
}
