package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for Notification Preferences
 * Contains all notification-related settings that can be customized by users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferencesDto implements Serializable {

    @JsonProperty("email")
    private Boolean email = true;

    @JsonProperty("push")
    private Boolean push = true;

    @JsonProperty("sms")
    private Boolean sms = false;

    @JsonProperty("frequency")
    private String frequency = "instant";
}
