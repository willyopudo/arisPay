package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDto {

	private Long id;

	private String name;

	private String code;

	private String shortCode;

	private Float balance;

	private byte recordStatus;

	private byte isEnabled;

	private String createdBy;

	private Date createdDate;
}
