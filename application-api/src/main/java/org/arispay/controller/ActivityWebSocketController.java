package org.arispay.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.ActivityEventDto;
import org.arispay.helpers.UserPrincipal;
import org.arispay.ports.api.ActivityServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ActivityWebSocketController {

    private static final Logger logger = LogManager.getLogger(ActivityWebSocketController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ActivityServicePort activityService;

    @MessageMapping("/activity-subscribe")
    public void subscribeToActivity(Principal principal) {
        try {
            if (!(principal instanceof UserPrincipal)) {
                logger.error("Invalid principal type: {}", principal.getClass().getName());
                return;
            }

            UserPrincipal userPrincipal = (UserPrincipal) principal;
            Long companyId = userPrincipal.getCompanyId();
            String username = userPrincipal.getName();

            logger.info("User {} from company {} subscribing to activity feed", username, companyId);

            // Send last 4 historical activities to the user
            List<ActivityEventDto> recentActivities = activityService.getRecentActivities(companyId, 4);

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/activity-history",
                    recentActivities
            );

            logger.info("Sent {} historical activities to user {}", recentActivities.size(), username);

        } catch (Exception e) {
            logger.error("Error in activity subscription: {}", e.getMessage(), e);
        }
    }
}
