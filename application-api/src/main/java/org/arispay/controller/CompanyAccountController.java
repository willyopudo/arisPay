package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.ClientDto;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.CompanyDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyAccountController {
    @Autowired
    CompanyAccountServicePort<CompanyAccountDto> companyAccountService;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(CompanyAccountController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyAccountDto addCompanyAccount(@RequestBody CompanyAccountDto companyAccount) {
        return companyAccountService.add(companyAccount);
    }

    @GetMapping
    public ResponseEntity<Page<CompanyAccountDto>> getAllCompanyAccounts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int itemsPerPage,
//                                                         @RequestParam(name = "status", required = false, defaultValue = "") String status,
//                                                         @RequestParam(name = "identifierType", required = false, defaultValue = "") String identifierType,
                                                         @RequestParam(name = "search", required = false) String search,
                                                         @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                         @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
                                                         HttpServletRequest request,
                                                         Authentication authentication) {

        logger.info("Authentication: {}", authentication.getAuthorities());

        Claims claims = jwtUtil.resolveClaims(request);

        // Determine sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        GenericFilterDto filterDto = new GenericFilterDto(
//                List.of( status, identifierType),
               null,
                search,
                direction,
                sortBy
        );

        Long companyId = claims.get("companyId", Long.class);

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(companyAccountService.getAll(companyId, pageable, filterDto));
    }
}
