package org.arispay.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.CompanyDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company_accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CompanyAccountController {
    @Autowired
    CompanyAccountServicePort<CompanyAccountDto> companyAccountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyAccountDto addCompanyAccount(@RequestBody CompanyAccountDto companyAccount) {
        return companyAccountService.add(companyAccount);
    }
}
