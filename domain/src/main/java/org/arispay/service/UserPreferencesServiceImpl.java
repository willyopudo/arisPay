package org.arispay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.arispay.data.NotificationPreferencesDto;
import org.arispay.data.ThemeCustomizationsDto;
import org.arispay.data.UserPreferencesDto;
import org.arispay.ports.api.UserPreferencesServicePort;
import org.arispay.ports.spi.UserPreferencesPersistencePort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service implementation for User Preferences
 * Note: This service is in the domain layer and should not directly depend on infrastructure classes
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserPreferencesServiceImpl implements UserPreferencesServicePort {

    private final UserPreferencesPersistencePort persistencePort;

    @Override
    @Cacheable(value = "userPreferences", key = "#p0")
    public Optional<UserPreferencesDto> getUserPreferences(Long userId) {
        log.debug("Fetching preferences for user: {}", userId);
        return persistencePort.findByUserId(userId)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    @CachePut(value = "userPreferences", key = "#p0.userId")
    public UserPreferencesDto saveUserPreferences(UserPreferencesDto preferencesDto) {
        log.info("Saving preferences for user: {}", preferencesDto.getUserId());

        var saved = persistencePort.save(preferencesDto);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public UserPreferencesDto createDefaultPreferences(Long userId) {
        log.info("Creating default preferences for user: {}", userId);

        // Check if preferences already exist
        if (persistencePort.existsByUserId(userId)) {
            log.warn("Preferences already exist for user: {}", userId);
            return getUserPreferences(userId).orElseThrow();
        }

        UserPreferencesDto defaultPreferences = new UserPreferencesDto();
        defaultPreferences.setUserId(userId);
        defaultPreferences.setLanguage("en");
        defaultPreferences.setTimezone("EAT");
        defaultPreferences.setCurrency("KES");
        defaultPreferences.setDateFormat("DD/MM/YYYY");
        defaultPreferences.setTimeFormat("24h");
        defaultPreferences.setTwoFactorEnabled(false);
        defaultPreferences.setEmailNotificationsEnabled(true);

        // Create default theme customizations using DTO
        ThemeCustomizationsDto themeDto = new ThemeCustomizationsDto();
        themeDto.setPrimaryColor("#7367F0");
        themeDto.setFontSize("medium");
        themeDto.setTheme("light");
        themeDto.setSkin("default");
        themeDto.setContentWidth("compact");
        themeDto.setNavLayout("vertical");
        themeDto.setNavDarkMode(false);
        themeDto.setRtl(false);
        defaultPreferences.setThemeCustomizations(themeDto);

        // Create default notification preferences using DTO
        NotificationPreferencesDto notificationDto = new NotificationPreferencesDto();
        notificationDto.setEmail(true);
        notificationDto.setPush(true);
        notificationDto.setSms(false);
        notificationDto.setFrequency("instant");
        defaultPreferences.setNotificationPreferences(notificationDto);

        return saveUserPreferences(defaultPreferences);
    }

    @Override
    @Transactional
    @CachePut(value = "userPreferences", key = "#p0")
    public UserPreferencesDto updateUserPreferences(Long userId, UserPreferencesDto preferencesDto) {
        log.info("Updating preferences for user: {}", userId);

        var existing = persistencePort.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Preferences not found for user: " + userId));

        // The adapter will handle the update with the DTO
        preferencesDto.setId(existing.getId());
        preferencesDto.setUserId(userId);
        if( preferencesDto.getThemeCustomizations() == null) {
            preferencesDto.setThemeCustomizations(existing.getThemeCustomizations());
        }

        var updated = persistencePort.update(userId, preferencesDto);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userPreferences", key = "#p0")
    public void deleteUserPreferences(Long userId) {
        log.info("Deleting preferences for user: {}", userId);
        persistencePort.deleteByUserId(userId);
    }

    @Override
    public boolean hasPreferences(Long userId) {
        return persistencePort.existsByUserId(userId);
    }

    // Helper method to convert entity to DTO (placeholder - will be implemented by adapter)
    private UserPreferencesDto convertToDto(Object entity) {
        // This is a temporary implementation
        // The actual conversion will happen in the adapter layer
        if (entity instanceof UserPreferencesDto) {
            return (UserPreferencesDto) entity;
        }
        throw new IllegalArgumentException("Cannot convert entity to DTO");
    }
}
