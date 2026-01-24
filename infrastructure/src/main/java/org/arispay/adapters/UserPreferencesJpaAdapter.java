package org.arispay.adapters;

import lombok.RequiredArgsConstructor;
import org.arispay.data.UserPreferencesDto;
import org.arispay.entity.User;
import org.arispay.entity.UserPreferences;
import org.arispay.mappers.UserPreferencesMapper;
import org.arispay.ports.spi.UserPreferencesPersistencePort;
import org.arispay.repository.UserPreferencesRepository;
import org.arispay.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * JPA Adapter for User Preferences Persistence
 */
@Component
@RequiredArgsConstructor
public class UserPreferencesJpaAdapter implements UserPreferencesPersistencePort {

    private final UserPreferencesRepository repository;
    private final UserPreferencesMapper mapper;
    private final UserRepository userRepository;

    @Override
    public Optional<UserPreferencesDto> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public UserPreferencesDto save(UserPreferencesDto preferencesDto) {
        UserPreferences entity = mapper.toEntity(preferencesDto);

        // Ensure user exists and is set
        User user = userRepository.findById(preferencesDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + preferencesDto.getUserId()));
        entity.setUser(user);

        UserPreferences saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserPreferencesDto update(Long userId, UserPreferencesDto preferencesDto) {
        UserPreferences existing = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Preferences not found for user: " + userId));

        // Convert the DTO to entity to get proper JSON conversions
        UserPreferences updates = mapper.toEntity(preferencesDto);

        // Update only non-null fields
        if (updates.getThemeCustomizations() != null) {
            existing.setThemeCustomizations(updates.getThemeCustomizations());
        }
        if (updates.getNotificationPreferences() != null) {
            existing.setNotificationPreferences(updates.getNotificationPreferences());
        }
        if (preferencesDto.getLanguage() != null) {
            existing.setLanguage(preferencesDto.getLanguage());
        }
        if (preferencesDto.getTimezone() != null) {
            existing.setTimezone(preferencesDto.getTimezone());
        }
        if (preferencesDto.getCurrency() != null) {
            existing.setCurrency(preferencesDto.getCurrency());
        }
        if (preferencesDto.getDateFormat() != null) {
            existing.setDateFormat(preferencesDto.getDateFormat());
        }
        if (preferencesDto.getTimeFormat() != null) {
            existing.setTimeFormat(preferencesDto.getTimeFormat());
        }
        if (preferencesDto.getTwoFactorEnabled() != null) {
            existing.setTwoFactorEnabled(preferencesDto.getTwoFactorEnabled());
        }
        if (preferencesDto.getEmailNotificationsEnabled() != null) {
            existing.setEmailNotificationsEnabled(preferencesDto.getEmailNotificationsEnabled());
        }
        if (preferencesDto.getCustomSettings() != null) {
            existing.setCustomSettings(preferencesDto.getCustomSettings());
        }

        UserPreferences updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        repository.deleteByUserId(userId);
    }
}
