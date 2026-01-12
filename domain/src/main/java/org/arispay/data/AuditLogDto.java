package org.arispay.data;

import java.time.OffsetDateTime;

public class AuditLogDto {
    private Long auditId;

    private Long companyId;

    private String user;

    private String entityType;

    private Long entityId;

    private String auditAction;

    private String actionStatus;

    private String eventType;

    private String newValue;

    private String ipAddress;

    private String userAgent;

    private String authMethod;

    private String role;

    private String sourceSystem;

    private Boolean sensitiveDataFlag = false;

    private String retentionPolicy = "7y";

    private OffsetDateTime eventTime;

    private OffsetDateTime createdAt;

}
