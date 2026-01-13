package org.arispay.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.ActivityEventDto;
import org.arispay.entity.ActivityLog;
import org.arispay.entity.Company;
import org.arispay.ports.spi.ActivityPersistencePort;
import org.arispay.repository.ActivityLogRepository;
import org.arispay.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityJpaAdapter implements ActivityPersistencePort {

    private static final Logger logger = LogManager.getLogger(ActivityJpaAdapter.class);

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveActivity(ActivityEventDto event) {
        try {
            Company company = companyRepository.findById(event.getCompanyId())
                    .orElse(null);

            if (company == null) {
                logger.warn("Company not found for activity log: {}", event.getCompanyId());
                return;
            }

            String metadataJson = null;
            if (event.getMetadata() != null && !event.getMetadata().isEmpty()) {
                try {
                    metadataJson = objectMapper.writeValueAsString(event.getMetadata());
                } catch (JsonProcessingException e) {
                    logger.error("Error serializing metadata to JSON: {}", e.getMessage());
                }
            }

            ActivityLog log = ActivityLog.builder()
                    .company(company)
                    .eventType(event.getEventType())
                    .title(event.getTitle())
                    .description(event.getDescription())
                    .eventTimestamp(event.getTimestamp())
                    .metadata(metadataJson)
                    .build();

            activityLogRepository.save(log);
            logger.debug("Activity log saved: {}", event.getEventType());
        } catch (Exception e) {
            logger.error("Error saving activity log: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<ActivityEventDto> getRecentActivities(Long companyId, int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            return activityLogRepository.findTopNByCompanyIdOrderByEventTimestampDesc(companyId, pageable).stream()
                    .map(log -> ActivityEventDto.builder()
                            .eventType(log.getEventType())
                            .title(log.getTitle())
                            .description(log.getDescription())
                            .timestamp(log.getEventTimestamp())
                            .userName(log.getUser() != null ? log.getUser().getFirstName() + " " + log.getUser().getLastName() : "System")
                            .companyId(log.getCompany().getId())
                            .metadata(parseMetadata(log.getMetadata()))
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving recent activities: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Helper method to parse JSON metadata string to Map
     */
    private java.util.Map<String, Object> parseMetadata(String metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(metadata, java.util.HashMap.class);
        } catch (Exception e) {
            logger.error("Error parsing metadata: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
