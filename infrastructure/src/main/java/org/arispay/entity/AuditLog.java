package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;


@Entity
@Table(name = "audit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "audit_action", nullable = false, length = 50)
    private String auditAction;

    @Column(name = "action_status", nullable = false, length = 20)
    private String actionStatus;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_value", columnDefinition = "jsonb")
    private String previousValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_value", columnDefinition = "jsonb")
    private String newValue;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "auth_method", length = 30)
    private String authMethod;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "source_system", length = 50)
    private String sourceSystem;

    @Column(name = "sensitive_data_flag")
    private Boolean sensitiveDataFlag = false;

    @Column(name = "retention_policy", length = 10)
    private String retentionPolicy = "7y";

    @Column(name = "event_time", nullable = false)
    private OffsetDateTime eventTime;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "event_hash", nullable = false, columnDefinition = "TEXT")
    private String eventHash;

    @Column(name = "previous_event_hash", columnDefinition = "TEXT")
    private String previousEventHash;
}
