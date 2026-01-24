package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyDto {
    private Long id;
    private Long companyId;
    private String companyName;
    private boolean isDefault;
}
