package org.arispay.ports.spi;

import org.arispay.data.NotificationDto;

public interface NotificationBroadcastPort {
    void broadcast(Long companyId, NotificationDto notification);
}
