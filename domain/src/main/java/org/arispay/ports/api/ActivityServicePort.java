package org.arispay.ports.api;

import org.arispay.data.ActivityEventDto;
import org.arispay.data.ClientDto;
import org.arispay.data.TransactionDto;

import java.util.List;

public interface ActivityServicePort {

    /**
     * Broadcast activity event to all users in a specific company
     */
    void broadcastActivityToCompany(Long companyId, ActivityEventDto event);

    /**
     * Send activity event to a specific user
     */
    void sendActivityToUser(String username, ActivityEventDto event);

    /**
     * Create transaction activity event
     */
    ActivityEventDto createTransactionEvent(TransactionDto transaction, String eventType, String userName);

    /**
     * Create client activity event
     */
    ActivityEventDto createClientEvent(ClientDto client, String eventType, String userName, Long companyId);

    /**
     * Create bulk operation activity event
     */
    ActivityEventDto createBulkEvent(int count, double totalAmount, String userName, Long companyId);

    /**
     * Get recent activities for a company
     */
    List<ActivityEventDto> getRecentActivities(Long companyId, int limit);
}
