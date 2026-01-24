package org.arispay.ports.spi;

import org.arispay.data.UserPreferencesDto;

import java.util.Optional;

/**
 * SPI Port for User Preferences Persistence
 */
public interface UserPreferencesPersistencePort {

    /**
     * Find user preferences by user ID
     * @param userId the user ID
     * @return Optional of UserPreferencesDto
     */
    Optional<UserPreferencesDto> findByUserId(Long userId);

    /**
     * Save user preferences
     * @param preferencesDto the user preferences DTO
     * @return saved UserPreferencesDto
     */
    UserPreferencesDto save(UserPreferencesDto preferencesDto);

    /**
     * Update user preferences
     * @param userId the user ID
     * @param preferencesDto the preferences DTO with updates
     * @return updated UserPreferencesDto
     */
    UserPreferencesDto update(Long userId, UserPreferencesDto preferencesDto);

    /**
     * Check if preferences exist for a user
     * @param userId the user ID
     * @return true if preferences exist
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete preferences by user ID
     * @param userId the user ID
     */
    void deleteByUserId(Long userId);
}
