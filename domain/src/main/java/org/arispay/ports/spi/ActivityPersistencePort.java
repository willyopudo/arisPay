package org.arispay.ports.spi;

import org.arispay.data.ActivityEventDto;

import java.util.List;

public interface ActivityPersistencePort {

    /**
     * Save activity event to database
     */
    void saveActivity(ActivityEventDto event);

    /**
     * Get recent activities for a company
     */
    List<ActivityEventDto> getRecentActivities(Long companyId, int limit);
}
