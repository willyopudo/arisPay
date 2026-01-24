package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.arispay.converters.JsonStringConverter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity for storing user preferences and settings
 * Supports JSON storage for flexible theme customizations and notification preferences
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_preferences")
public class UserPreferences extends AuditableEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Theme customizations stored as JSON
     * Example: {"primaryColor": "#1976d2", "darkMode": true, "fontSize": "medium"}
     */
    @Column(name = "theme_customizations", columnDefinition = "TEXT")
    @Convert(converter = JsonStringConverter.class)
    private String themeCustomizations;

    /**
     * Notification preferences stored as JSON
     * Example: {"email": true, "push": false, "sms": true, "frequency": "daily"}
     */
    @Column(name = "notification_preferences", columnDefinition = "TEXT")
    @Convert(converter = JsonStringConverter.class)
    private String notificationPreferences;

    /**
     * Language preference
     */
    @Column(name = "language", length = 10)
    private String language;

    /**
     * Timezone preference
     */
    @Column(name = "timezone", length = 50)
    private String timezone;

    /**
     * Currency preference
     */
    @Column(name = "currency", length = 10)
    private String currency;

    /**
     * Date format preference (e.g., "DD/MM/YYYY", "MM/DD/YYYY")
     */
    @Column(name = "date_format", length = 20)
    private String dateFormat;

    /**
     * Time format preference (e.g., "12h", "24h")
     */
    @Column(name = "time_format", length = 10)
    private String timeFormat;

    /**
     * Two-factor authentication enabled
     */
    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled;

    /**
     * Email notifications enabled
     */
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled;

    /**
     * Additional custom settings stored as JSON for extensibility
     * Can store any additional key-value pairs not covered by specific fields
     */
    @Column(name = "custom_settings", columnDefinition = "TEXT")
    @Convert(converter = JsonStringConverter.class)
    private String customSettings;
}
