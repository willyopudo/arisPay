package org.arispay.ports.api;
import java.util.Optional;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionServicePort {
    TransactionDto addTransaction(TransactionDto transactionDto);

    void deleteTransactionById(Long id);

    TransactionDto updateTransaction(TransactionDto transactionDto);

    Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter);

    TransactionDto queryTransactions(Long companyId, GenericFilterDto filters);

    TransactionDto getTransactionById(Long id);

    Optional<ISummary> getTransactionSummaries(Long companyId);
}
