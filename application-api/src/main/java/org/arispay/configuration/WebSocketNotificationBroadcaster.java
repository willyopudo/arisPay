package org.arispay.configuration;

import org.arispay.data.NotificationDto;
import org.arispay.ports.spi.NotificationBroadcastPort;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class WebSocketNotificationBroadcaster implements NotificationBroadcastPort {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcast(Long companyId, NotificationDto notification) {
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + companyId,
                notification
        );
    }
}
