package org.arispay.ports.api;

import org.arispay.data.NotificationDto;
import org.arispay.data.TransactionDto;

import java.util.List;

public interface NotificationServicePort {

    NotificationDto createAndBroadcast(Long companyId, NotificationDto notification);

    NotificationDto createPaymentReceivedNotification(TransactionDto transaction);

    NotificationDto createBulkPaymentNotification(int count, double totalAmount, Long companyId, String status);

    List<NotificationDto> getNotifications(Long companyId, int limit);

    long getUnreadCount(Long companyId);

    void markAsRead(Long id);

    void markAllAsRead(Long companyId);

    void markAsUnread(Long id);

    void deleteNotification(Long id);
}
