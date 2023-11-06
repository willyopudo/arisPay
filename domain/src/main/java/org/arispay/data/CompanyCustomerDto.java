package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.arispay.utils.DtoEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCustomerDto implements DtoEntity {
	private Long id;

	private Long companyId;

	private String identifierRef;

	private String description;
}
