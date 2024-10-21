package org.arispay.data.dtoauth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.CompanyDto;
import org.arispay.data.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDto {
    private CompanyDto companyDto;
    private UserDto userDto;
    private CompanyAccountDto companyAccountDto;
}
