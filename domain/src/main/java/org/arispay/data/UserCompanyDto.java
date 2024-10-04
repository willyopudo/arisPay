package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyDto {
    private Long id;
    private Integer companyId;
    private boolean isDefault;
}
