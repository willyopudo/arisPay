package org.arispay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {
	@Column(nullable = false)
	protected byte recordStatus = 0;

	@Column(nullable = false)
	protected byte isEnabled = 1;

	@Column(nullable = false)
	protected String createdBy = "system";

	@Column(nullable = false, updatable = false)
	protected LocalDateTime createdDate = LocalDateTime.now();

	protected String modifiedBy;

	protected LocalDateTime modifiedDate;
}
