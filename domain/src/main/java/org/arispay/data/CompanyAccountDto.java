package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAccountDto {

	private Long company;

	private String accountNumber;

	private String accountName;

	private String bankCode;

	private String bankName;
}
