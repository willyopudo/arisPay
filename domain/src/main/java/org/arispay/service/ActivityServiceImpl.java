package org.arispay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.ActivityEventDto;
import org.arispay.data.ClientDto;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.ActivityServicePort;
import org.arispay.ports.spi.ActivityPersistencePort;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityServicePort {

    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);

    private final ActivityPersistencePort activityPersistencePort;
    private final SimpMessagingTemplate messagingTemplate;

    public ActivityServiceImpl(ActivityPersistencePort activityPersistencePort,
                              SimpMessagingTemplate messagingTemplate) {
        this.activityPersistencePort = activityPersistencePort;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcastActivityToCompany(Long companyId, ActivityEventDto event) {
        try {
            // Save to database first
            activityPersistencePort.saveActivity(event);

            // Then broadcast via WebSocket
            logger.info("Broadcasting activity event to company {}: {}", companyId, event.getEventType());
            messagingTemplate.convertAndSend(
                    "/topic/activity/" + companyId,
                    event
            );
        } catch (Exception e) {
            logger.error("Error broadcasting activity to company {}: {}", companyId, e.getMessage(), e);
        }
    }

    @Override
    public void sendActivityToUser(String username, ActivityEventDto event) {
        try {
            logger.info("Sending activity event to user {}: {}", username, event.getEventType());
            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/activity",
                    event
            );
        } catch (Exception e) {
            logger.error("Error sending activity to user {}: {}", username, e.getMessage(), e);
        }
    }

    @Override
    public ActivityEventDto createTransactionEvent(TransactionDto transaction, String eventType, String userName) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("transactionId", transaction.getId());
        metadata.put("amount", transaction.getTranAmount());
        metadata.put("paymentMode", transaction.getPaymentMode());
        metadata.put("crDrInd", transaction.getCrDrInd());

        String transactionType = "C".equals(transaction.getCrDrInd()) ? "Collection" : "Disbursement";

        return ActivityEventDto.builder()
                .eventType(eventType)
                .title(getEventTitle(eventType))
                .description(String.format("%s of %.2f via %s",
                        transactionType,
                        transaction.getTranAmount(),
                        transaction.getPaymentMode() != null ? transaction.getPaymentMode() : "Unknown"))
                .timestamp(LocalDateTime.now())
                .userName(userName != null ? userName : "System")
                .companyId(transaction.getCompanyId())
                .metadata(metadata)
                .build();
    }

    @Override
    public ActivityEventDto createClientEvent(ClientDto client, String eventType, String userName, Long companyId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("clientId", client.getId());
        metadata.put("clientName", client.getClientName());

        return ActivityEventDto.builder()
                .eventType(eventType)
                .title(getEventTitle(eventType))
                .description(String.format("Client '%s' has been %s",
                        client.getClientName(),
                        eventType.contains("CREATED") ? "created" : "updated"))
                .timestamp(LocalDateTime.now())
                .userName(userName != null ? userName : "System")
                .companyId(companyId)
                .metadata(metadata)
                .build();
    }

    @Override
    public ActivityEventDto createBulkEvent(int count, double totalAmount, String userName, Long companyId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("count", count);
        metadata.put("totalAmount", totalAmount);

        return ActivityEventDto.builder()
                .eventType("BULK_DISBURSEMENT")
                .title("Bulk Disbursement Processed")
                .description(String.format("Processed %d disbursements totaling %.2f",
                        count,
                        totalAmount))
                .timestamp(LocalDateTime.now())
                .userName(userName != null ? userName : "System")
                .companyId(companyId)
                .metadata(metadata)
                .build();
    }

    @Override
    public List<ActivityEventDto> getRecentActivities(Long companyId, int limit) {
        return activityPersistencePort.getRecentActivities(companyId, limit);
    }

    /**
     * Get display title for event type
     */
    private String getEventTitle(String eventType) {
        return switch (eventType) {
            case "TRANSACTION_CREATED" -> "Transaction Created";
            case "TRANSACTION_UPDATED" -> "Transaction Updated";
            case "BULK_DISBURSEMENT" -> "Bulk Disbursement";
            case "CLIENT_CREATED" -> "Client Created";
            case "CLIENT_UPDATED" -> "Client Updated";
            case "USER_UPDATED" -> "User Updated";
            case "ACCOUNT_LINKED" -> "Account Linked";
            default -> "Activity";
        };
    }
}
