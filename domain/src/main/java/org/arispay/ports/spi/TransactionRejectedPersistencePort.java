package org.arispay.ports.spi;

import java.util.List;
import java.util.Optional;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRejectedPersistencePort {
    TransactionDto addTransaction(TransactionDto clientDto);

    void deleteTransactionById(Long id);

    TransactionDto updateTransaction(TransactionDto clientDto);

    Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter);

    TransactionDto getTransactionById(Long id);

    Optional<ISummary> getTransactionRejectedSummaries(Long companyId);
}
