package org.arispay.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditableEntity {
	@Column(nullable = false)
	protected byte recordStatus;

	@Column(nullable = false)
	protected byte isEnabled;

	@Column(nullable = false)
	protected String createdBy;

	@Column(nullable = false)
	protected Date createdDate = new java.util.Date();

	protected String modifiedBy;

	protected Date modifiedDate = new java.util.Date();
}
