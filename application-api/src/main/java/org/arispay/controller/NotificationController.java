package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.arispay.auth.JwtUtil;
import org.arispay.data.NotificationDto;
import org.arispay.ports.api.NotificationServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Notifications", description = "Notification management API")
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationServicePort notificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @RequestParam(defaultValue = "20") int limit,
            HttpServletRequest request) {
        Claims claims = jwtUtil.resolveClaims(request);
        Long companyId = claims.get("companyId", Long.class);
        return ResponseEntity.ok(notificationService.getNotifications(companyId, limit));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        Claims claims = jwtUtil.resolveClaims(request);
        Long companyId = claims.get("companyId", Long.class);
        long count = notificationService.getUnreadCount(companyId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(HttpServletRequest request) {
        Claims claims = jwtUtil.resolveClaims(request);
        Long companyId = claims.get("companyId", Long.class);
        notificationService.markAllAsRead(companyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unread")
    public ResponseEntity<Void> markAsUnread(@PathVariable Long id) {
        notificationService.markAsUnread(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}
