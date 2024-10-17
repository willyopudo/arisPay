package org.arispay.data.dtoauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.CompanyDto;
import org.arispay.data.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private CompanyDto companyDto;
    private UserDto userDto;
    private CompanyAccountDto companyAccountDto;
}
