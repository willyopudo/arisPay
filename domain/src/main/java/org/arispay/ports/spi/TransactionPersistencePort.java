package org.arispay.ports.spi;

import java.util.List;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionPersistencePort {
    TransactionDto addTransaction(TransactionDto clientDto);

    void deleteTransactionById(Long id);

    TransactionDto updateTransaction(TransactionDto clientDto);

    Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter);

    TransactionDto getTransactionById(Long id);
}
