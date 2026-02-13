package org.arispay.ports.spi;

import org.arispay.data.NotificationDto;

import java.util.List;

public interface NotificationPersistencePort {

    NotificationDto save(NotificationDto notification);

    List<NotificationDto> getNotifications(Long companyId, int limit);

    long getUnreadCount(Long companyId);

    void markAsRead(Long id);

    void markAllAsRead(Long companyId);

    void markAsUnread(Long id);

    void deleteNotification(Long id);
}
