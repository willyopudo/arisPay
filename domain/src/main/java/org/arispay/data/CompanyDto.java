package org.arispay.data;

import jakarta.validation.constraints.Email;
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

	private String businessRegNum;

	@Email
	private String email;

	private String phoneNumber;

	private String category;

	private String identifierType;

	private Float balance;
}
