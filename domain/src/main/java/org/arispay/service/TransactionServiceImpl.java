package org.arispay.service;

import java.util.List;
import java.util.Optional;

import org.arispay.data.ActivityEventDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.ActivityServicePort;
import org.arispay.ports.api.TransactionServicePort;
import org.arispay.ports.spi.TransactionPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TransactionServiceImpl implements TransactionServicePort {

    private final TransactionPersistencePort transactionPersistencePort;
    private final ActivityServicePort activityService;

    public TransactionServiceImpl(TransactionPersistencePort transactionPersistencePort, ActivityServicePort activityService) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.activityService = activityService;
    }

    @Override
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        TransactionDto saved = transactionPersistencePort.addTransaction(transactionDto);

        // Broadcast WebSocket event
        try {
            String username = getCurrentUsername();
            ActivityEventDto event = activityService.createTransactionEvent(
                    saved,
                    "TRANSACTION_CREATED",
                    username
            );
            activityService.broadcastActivityToCompany(saved.getCompanyId(), event);
        } catch (Exception e) {
            // Log but don't fail the transaction if WebSocket fails
            System.err.println("Failed to broadcast transaction event: " + e.getMessage());
        }

        return saved;
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionPersistencePort.deleteTransactionById(id);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        TransactionDto updated = transactionPersistencePort.updateTransaction(transactionDto);

        // Broadcast WebSocket event
        try {
            String username = getCurrentUsername();
            ActivityEventDto event = activityService.createTransactionEvent(
                    updated,
                    "TRANSACTION_UPDATED",
                    username
            );
            activityService.broadcastActivityToCompany(updated.getCompanyId(), event);
        } catch (Exception e) {
            // Log but don't fail the transaction if WebSocket fails
            System.err.println("Failed to broadcast transaction update event: " + e.getMessage());
        }

        return updated;
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

    /**
     * Helper method to get current username from Spring Security context
     */
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
            // Ignore
        }
        return "System";
    }
}
