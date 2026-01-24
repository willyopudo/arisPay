package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for Theme Customizations
 * Contains all theme-related settings that can be customized by users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeCustomizationsDto implements Serializable {

    @JsonProperty("primaryColor")
    private String primaryColor = "#7367F0";

    @JsonProperty("fontSize")
    private String fontSize = "medium"; // small, medium, large

    @JsonProperty("theme")
    private String theme = "light"; // light, dark, system

    @JsonProperty("skin")
    private String skin = "default"; // default, bordered

    @JsonProperty("contentWidth")
    private String contentWidth = "compact"; // wide, compact

    @JsonProperty("navLayout")
    private String navLayout = "vertical"; // vertical, horizontal, collapsed

    @JsonProperty("navDarkMode")
    private Boolean navDarkMode = false; // Semi-dark navbar toggle

    @JsonProperty("rtl")
    private boolean rtl = false; // Right-to-left support
}
