package org.arispay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.NotificationDto;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.NotificationServicePort;
import org.arispay.ports.spi.NotificationBroadcastPort;
import org.arispay.ports.spi.NotificationPersistencePort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationServiceImpl implements NotificationServicePort {

    private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    private final NotificationPersistencePort notificationPersistencePort;
    private final NotificationBroadcastPort notificationBroadcastPort;

    public NotificationServiceImpl(NotificationPersistencePort notificationPersistencePort,
                                   NotificationBroadcastPort notificationBroadcastPort) {
        this.notificationPersistencePort = notificationPersistencePort;
        this.notificationBroadcastPort = notificationBroadcastPort;
    }

    @Override
    public NotificationDto createAndBroadcast(Long companyId, NotificationDto notification) {
        try {
            notification.setCompanyId(companyId);
            if (notification.getIsSeen() == null) {
                notification.setIsSeen(false);
            }
            if (notification.getTimestamp() == null) {
                notification.setTimestamp(LocalDateTime.now());
            }

            NotificationDto saved = notificationPersistencePort.save(notification);

            logger.info("Broadcasting notification to company {}: {}", companyId, notification.getEventType());
            notificationBroadcastPort.broadcast(companyId, saved);

            return saved;
        } catch (Exception e) {
            logger.error("Error creating/broadcasting notification to company {}: {}", companyId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public NotificationDto createPaymentReceivedNotification(TransactionDto transaction) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("transactionId", transaction.getId());
        metadata.put("amount", transaction.getTranAmount());
        metadata.put("bankTranRef", transaction.getBankTranRef());
        metadata.put("paymentMode", transaction.getPaymentMode());

        String transactionType = "C".equals(transaction.getCrDrInd()) ? "Collection" : "Disbursement";

        NotificationDto notification = NotificationDto.builder()
                .eventType("PAYMENT_RECEIVED")
                .title("Payment Received")
                .subtitle(String.format("%s of %.2f via %s",
                        transactionType,
                        transaction.getTranAmount(),
                        transaction.getPaymentMode() != null ? transaction.getPaymentMode() : "Unknown"))
                .icon("tabler-cash")
                .color("success")
                .timestamp(LocalDateTime.now())
                .isSeen(false)
                .metadata(metadata)
                .build();

        return createAndBroadcast(transaction.getCompanyId(), notification);
    }

    @Override
    public NotificationDto createBulkPaymentNotification(int count, double totalAmount, Long companyId, String status) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("count", count);
        metadata.put("totalAmount", totalAmount);
        metadata.put("status", status);

        String eventType;
        String title;
        String subtitle;
        String icon;
        String color;

        if ("INITIATED".equals(status)) {
            eventType = "BULK_PAYMENT_INITIATED";
            title = "Bulk Payment Initiated";
            subtitle = String.format("Processing %d payments totaling %.2f", count, totalAmount);
            icon = "tabler-send";
            color = "info";
        } else {
            eventType = "BULK_PAYMENT_COMPLETED";
            title = "Bulk Payment Completed";
            subtitle = String.format("Completed %d payments totaling %.2f", count, totalAmount);
            icon = "tabler-circle-check";
            color = "success";
        }

        NotificationDto notification = NotificationDto.builder()
                .eventType(eventType)
                .title(title)
                .subtitle(subtitle)
                .icon(icon)
                .color(color)
                .timestamp(LocalDateTime.now())
                .isSeen(false)
                .metadata(metadata)
                .build();

        return createAndBroadcast(companyId, notification);
    }

    @Override
    public List<NotificationDto> getNotifications(Long companyId, int limit) {
        return notificationPersistencePort.getNotifications(companyId, limit);
    }

    @Override
    public long getUnreadCount(Long companyId) {
        return notificationPersistencePort.getUnreadCount(companyId);
    }

    @Override
    public void markAsRead(Long id) {
        notificationPersistencePort.markAsRead(id);
    }

    @Override
    public void markAllAsRead(Long companyId) {
        notificationPersistencePort.markAllAsRead(companyId);
    }

    @Override
    public void markAsUnread(Long id) {
        notificationPersistencePort.markAsUnread(id);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationPersistencePort.deleteNotification(id);
    }
}
