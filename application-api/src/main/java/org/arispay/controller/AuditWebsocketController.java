package org.arispay.controller;

import org.arispay.helpers.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class AuditWebsocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/audit-subscribe")
    public void auditSubscribe(Principal principal) {
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        Long companyId = userPrincipal.getCompanyId();


    }
}
