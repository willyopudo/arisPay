package org.arispay.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.NotificationDto;
import org.arispay.entity.Company;
import org.arispay.entity.Notification;
import org.arispay.ports.spi.NotificationPersistencePort;
import org.arispay.repository.CompanyRepository;
import org.arispay.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationJpaAdapter implements NotificationPersistencePort {

    private static final Logger logger = LogManager.getLogger(NotificationJpaAdapter.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NotificationDto save(NotificationDto notificationDto) {
        try {
            Company company = companyRepository.findById(notificationDto.getCompanyId()).orElse(null);
            if (company == null) {
                logger.warn("Company not found for notification: {}", notificationDto.getCompanyId());
                return null;
            }

            String metadataJson = null;
            if (notificationDto.getMetadata() != null && !notificationDto.getMetadata().isEmpty()) {
                try {
                    metadataJson = objectMapper.writeValueAsString(notificationDto.getMetadata());
                } catch (JsonProcessingException e) {
                    logger.error("Error serializing metadata to JSON: {}", e.getMessage());
                }
            }

            Notification notification = Notification.builder()
                    .company(company)
                    .eventType(notificationDto.getEventType())
                    .title(notificationDto.getTitle())
                    .subtitle(notificationDto.getSubtitle())
                    .icon(notificationDto.getIcon())
                    .color(notificationDto.getColor())
                    .isSeen(notificationDto.getIsSeen() != null ? notificationDto.getIsSeen() : false)
                    .eventTimestamp(notificationDto.getTimestamp())
                    .metadata(metadataJson)
                    .build();

            notification = notificationRepository.save(notification);
            notificationDto.setId(notification.getId());

            logger.debug("Notification saved: {}", notificationDto.getEventType());
            return notificationDto;
        } catch (Exception e) {
            logger.error("Error saving notification: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(Long companyId, int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            return notificationRepository.findByCompanyIdOrderByEventTimestampDesc(companyId, pageable).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving notifications: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long companyId) {
        return notificationRepository.countUnreadByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.markAsRead(id);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long companyId) {
        notificationRepository.markAllAsReadByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void markAsUnread(Long id) {
        notificationRepository.markAsUnread(id);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .eventType(notification.getEventType())
                .title(notification.getTitle())
                .subtitle(notification.getSubtitle())
                .icon(notification.getIcon())
                .color(notification.getColor())
                .timestamp(notification.getEventTimestamp())
                .companyId(notification.getCompany().getId())
                .isSeen(notification.getIsSeen())
                .metadata(parseMetadata(notification.getMetadata()))
                .build();
    }

    private java.util.Map<String, Object> parseMetadata(String metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(metadata, HashMap.class);
        } catch (Exception e) {
            logger.error("Error parsing metadata: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
