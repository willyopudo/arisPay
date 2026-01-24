package org.arispay.repository;

import org.arispay.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for UserPreferences entity
 */
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    /**
     * Find user preferences by user ID
     * @param userId the user ID
     * @return Optional of UserPreferences
     */
    Optional<UserPreferences> findByUserId(Long userId);

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
