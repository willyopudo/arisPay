package org.arispay.ports.api;

import org.arispay.data.UserPreferencesDto;

import java.util.Optional;

/**
 * Port interface for User Preferences Service
 */
public interface UserPreferencesServicePort {

    /**
     * Get user preferences by user ID
     * @param userId the user ID
     * @return Optional of UserPreferencesDto
     */
    Optional<UserPreferencesDto> getUserPreferences(Long userId);

    /**
     * Create or update user preferences
     * @param preferencesDto the preferences DTO
     * @return saved UserPreferencesDto
     */
    UserPreferencesDto saveUserPreferences(UserPreferencesDto preferencesDto);

    /**
     * Create default preferences for a new user
     * @param userId the user ID
     * @return created UserPreferencesDto
     */
    UserPreferencesDto createDefaultPreferences(Long userId);

    /**
     * Update specific preference field
     * @param userId the user ID
     * @param preferencesDto partial preferences to update
     * @return updated UserPreferencesDto
     */
    UserPreferencesDto updateUserPreferences(Long userId, UserPreferencesDto preferencesDto);

    /**
     * Delete user preferences
     * @param userId the user ID
     */
    void deleteUserPreferences(Long userId);

    /**
     * Check if user has preferences
     * @param userId the user ID
     * @return true if preferences exist
     */
    boolean hasPreferences(Long userId);
}
