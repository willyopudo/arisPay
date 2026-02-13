package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private String eventType;
    private String title;
    private String subtitle;
    private String icon;
    private String color;
    private LocalDateTime timestamp;
    private Long companyId;
    private Boolean isSeen;
    private Map<String, Object> metadata;
}
