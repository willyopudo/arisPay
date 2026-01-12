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
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Claims claims = jwtTokenProvider.parseJwtClaims(token);
                if (jwtTokenProvider.validateClaims(claims)) {
                    String username = claims.getSubject();
                    Long companyId = claims.get("companyId", Long.class);

                    // Create custom principal with company info
                    UserPrincipal principal = new UserPrincipal(username, companyId);
                    accessor.setUser(principal);
                }
            }
        }
        return message;
    }
}
