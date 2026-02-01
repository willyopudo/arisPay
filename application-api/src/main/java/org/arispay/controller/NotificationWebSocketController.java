package org.arispay.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.NotificationDto;
import org.arispay.helpers.UserPrincipal;
import org.arispay.ports.api.NotificationServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class NotificationWebSocketController {

    private static final Logger logger = LogManager.getLogger(NotificationWebSocketController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationServicePort notificationService;

    @MessageMapping("/notification-subscribe")
    public void subscribeToNotifications(Principal principal) {
        try {
            if (!(principal instanceof UserPrincipal)) {
                logger.error("Invalid principal type: {}", principal.getClass().getName());
                return;
            }

            UserPrincipal userPrincipal = (UserPrincipal) principal;
            Long companyId = userPrincipal.getCompanyId();
            String username = userPrincipal.getName();

            logger.info("User {} from company {} subscribing to notifications", username, companyId);

            List<NotificationDto> recentNotifications = notificationService.getNotifications(companyId, 20);

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notification-history",
                    recentNotifications
            );

            logger.info("Sent {} historical notifications to user {}", recentNotifications.size(), username);

        } catch (Exception e) {
            logger.error("Error in notification subscription: {}", e.getMessage(), e);
        }
    }
}
