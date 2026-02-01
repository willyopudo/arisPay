package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String subtitle;

    @Column(length = 50)
    private String icon;

    @Column(length = 30)
    private String color;

    @Column(name = "is_seen", nullable = false)
    private Boolean isSeen = false;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;
}
