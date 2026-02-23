package org.arispay.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.UserPreferencesDto;
import org.arispay.ports.api.UserPreferencesServicePort;
import org.arispay.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Preferences
 */
@RestController
@RequestMapping("/api/v1/user-preferences")
@CrossOrigin(origins = "http://localhost:3000")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Log4j2
public class UserPreferencesController {

    private final UserPreferencesServicePort userPreferencesService;

    /**
     * Get preferences for the authenticated user
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericHttpResponse<UserPreferencesDto>> getMyPreferences() {
        try {
            Long userId = getCurrentUserId();

            return userPreferencesService.getUserPreferences(userId)
                    .map(prefs -> ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences retrieved successfully", prefs)))
                    .orElseGet(() -> {
                        // Create default preferences if none exist
                        UserPreferencesDto defaultPrefs = userPreferencesService.createDefaultPreferences(userId);
                        return ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Default preferences created", defaultPrefs));
                    });
        } catch (Exception e) {
            log.error("Error retrieving preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Get preferences for a specific user (admin only)
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericHttpResponse<UserPreferencesDto>> getUserPreferences(@PathVariable Long userId) {
        try {
            return userPreferencesService.getUserPreferences(userId)
                    .map(prefs -> ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences retrieved successfully", prefs)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new GenericHttpResponse<>(HttpStatus.NOT_FOUND, "Preferences not found", null)));
        } catch (Exception e) {
            log.error("Error retrieving preferences for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Update preferences for the authenticated user
     */
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericHttpResponse<UserPreferencesDto>> updateMyPreferences(
            @Valid @RequestBody UserPreferencesDto preferencesDto) {
        try {
            Long userId = getCurrentUserId();
            preferencesDto.setUserId(userId); // Ensure user can only update their own preferences

            UserPreferencesDto updated;
            if (userPreferencesService.hasPreferences(userId)) {
                updated = userPreferencesService.updateUserPreferences(userId, preferencesDto);
            } else {
                updated = userPreferencesService.saveUserPreferences(preferencesDto);
            }

            return ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences updated successfully", updated));
        } catch (Exception e) {
            log.error("Error updating preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Create or update preferences for a specific user (admin only)
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericHttpResponse<UserPreferencesDto>> updateUserPreferences(
            @PathVariable Long userId,
            @Valid @RequestBody UserPreferencesDto preferencesDto) {
        try {
            preferencesDto.setUserId(userId);

            UserPreferencesDto updated;
            if (userPreferencesService.hasPreferences(userId)) {
                updated = userPreferencesService.updateUserPreferences(userId, preferencesDto);
            } else {
                updated = userPreferencesService.saveUserPreferences(preferencesDto);
            }

            return ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences updated successfully", updated));
        } catch (Exception e) {
            log.error("Error updating preferences for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Create default preferences for the authenticated user
     */
    @PostMapping("/default")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericHttpResponse<UserPreferencesDto>> createDefaultPreferences() {
        try {
            Long userId = getCurrentUserId();
            UserPreferencesDto created = userPreferencesService.createDefaultPreferences(userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new GenericHttpResponse<>(HttpStatus.CREATED, "Default preferences created successfully", created));
        } catch (Exception e) {
            log.error("Error creating default preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Delete preferences for the authenticated user
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericHttpResponse<Void>> deleteMyPreferences() {
        try {
            Long userId = getCurrentUserId();
            userPreferencesService.deleteUserPreferences(userId);
            return ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting preferences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Delete preferences for a specific user (admin only)
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericHttpResponse<Void>> deleteUserPreferences(@PathVariable Long userId) {
        try {
            userPreferencesService.deleteUserPreferences(userId);
            return ResponseEntity.ok(new GenericHttpResponse<>(HttpStatus.OK, "Preferences deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting preferences for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting preferences: " + e.getMessage(), null));
        }
    }

    /**
     * Helper method to extract user ID from security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new IllegalStateException("Unable to extract user ID from security context");
    }
}
