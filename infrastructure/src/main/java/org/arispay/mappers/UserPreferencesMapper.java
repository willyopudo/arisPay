package org.arispay.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.arispay.data.NotificationPreferencesDto;
import org.arispay.data.ThemeCustomizationsDto;
import org.arispay.data.UserPreferencesDto;
import org.arispay.entity.UserPreferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for UserPreferences entity and DTO
 */
@Mapper(componentModel = "spring")
public abstract class UserPreferencesMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createdDate", target = "createdAt")
    @Mapping(source = "modifiedDate", target = "updatedAt")
    @Mapping(source = "themeCustomizations", target = "themeCustomizations", qualifiedByName = "jsonToThemeDto")
    @Mapping(source = "notificationPreferences", target = "notificationPreferences", qualifiedByName = "jsonToNotificationDto")
    public abstract UserPreferencesDto toDto(UserPreferences entity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(source = "themeCustomizations", target = "themeCustomizations", qualifiedByName = "themeDtoToJson")
    @Mapping(source = "notificationPreferences", target = "notificationPreferences", qualifiedByName = "notificationDtoToJson")
    public abstract UserPreferences toEntity(UserPreferencesDto dto);

    @Named("jsonToThemeDto")
    protected ThemeCustomizationsDto jsonToThemeDto(String json) {
        if (json == null || json.isEmpty()) {
            return new ThemeCustomizationsDto();
        }
        try {
            return objectMapper.readValue(json, ThemeCustomizationsDto.class);
        } catch (JsonProcessingException e) {
            return new ThemeCustomizationsDto();
        }
    }

    @Named("jsonToNotificationDto")
    protected NotificationPreferencesDto jsonToNotificationDto(String json) {
        if (json == null || json.isEmpty()) {
            return new NotificationPreferencesDto();
        }
        try {
            return objectMapper.readValue(json, NotificationPreferencesDto.class);
        } catch (JsonProcessingException e) {
            return new NotificationPreferencesDto();
        }
    }

    @Named("themeDtoToJson")
    protected String themeDtoToJson(ThemeCustomizationsDto dto) {
        if (dto == null) {
            dto = new ThemeCustomizationsDto();
        }
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Named("notificationDtoToJson")
    protected String notificationDtoToJson(NotificationPreferencesDto dto) {
        if (dto == null) {
            dto = new NotificationPreferencesDto();
        }
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
