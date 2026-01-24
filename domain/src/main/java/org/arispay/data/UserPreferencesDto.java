package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for User Preferences
 * Note: customSettings is stored as JSON string for extensibility
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private Long userId;

    /**
     * Theme customizations as nested object
     */
    @JsonProperty("themeCustomizations")
    private ThemeCustomizationsDto themeCustomizations;

    /**
     * Notification preferences as nested object
     */
    @JsonProperty("notificationPreferences")
    private NotificationPreferencesDto notificationPreferences;

    @JsonProperty("language")
    private String language;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("dateFormat")
    private String dateFormat;

    @JsonProperty("timeFormat")
    private String timeFormat;

    @JsonProperty("twoFactorEnabled")
    private Boolean twoFactorEnabled;

    @JsonProperty("emailNotificationsEnabled")
    private Boolean emailNotificationsEnabled;

    /**
     * Custom settings stored as JSON string for extensibility
     */
    @JsonProperty("customSettings")
    private String customSettings;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
