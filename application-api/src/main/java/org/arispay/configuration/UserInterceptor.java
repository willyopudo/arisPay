package org.arispay.configuration;

import io.jsonwebtoken.Claims;
import org.arispay.auth.JwtUtil;
import org.arispay.helpers.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class UserInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                String token = accessor.getFirstNativeHeader("Authorization");

                if (token == null) {
                    System.err.println("WebSocket CONNECT: No Authorization header found");
                    return message;
                }

                if (!token.startsWith("Bearer ")) {
                    System.err.println("WebSocket CONNECT: Authorization header doesn't start with 'Bearer '");
                    return message;
                }

                token = token.substring(7);

                Claims claims = jwtTokenProvider.parseJwtClaims(token);

                if (claims == null) {
                    System.err.println("WebSocket CONNECT: Failed to parse JWT claims");
                    return message;
                }

                if (!jwtTokenProvider.validateClaims(claims)) {
                    System.err.println("WebSocket CONNECT: JWT claims validation failed");
                    return message;
                }

                String username = claims.getSubject();
                Long companyId = claims.get("companyId", Long.class);

                if (username == null || companyId == null) {
                    System.err.println("WebSocket CONNECT: Username or companyId is null - username: " + username + ", companyId: " + companyId);
                    return message;
                }

                // Create custom principal with company info
                UserPrincipal principal = new UserPrincipal(username, companyId);
                accessor.setUser(principal);

                System.out.println("WebSocket CONNECT: Successfully authenticated user " + username + " from company " + companyId);

            } catch (Exception e) {
                System.err.println("WebSocket CONNECT: Exception during authentication - " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                // Continue without authentication
            }
        }
        return message;
    }
}
