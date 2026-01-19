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
public class ActivityEventDto {
    private String eventType; // e.g., "TRANSACTION_CREATED", "BULK_DISBURSEMENT"
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private String userName;
    private Long companyId; // For filtering by company
    private Map<String, Object> metadata; // Additional contextual data
}
