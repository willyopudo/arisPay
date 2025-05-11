package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.*;
import org.arispay.ports.api.BankServicePort;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.repository.CompanyAccountRepository;
import org.javatuples.Triplet;
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

    @Autowired
    BankServicePort bankService;

    @Autowired
    CompanyAccountRepository companyAccountRepository;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(CompanyAccountController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyAccountDto addCompanyAccount(@RequestBody CompanyAccountDto companyAccount) {
        return companyAccountService.add(companyAccount);
    }

    @GetMapping
    public ResponseEntity<Triplet<Page<CompanyAccountDto>, List<SelectDto>, ISummary>> getAllCompanyAccounts(@RequestParam(defaultValue = "1") int page,
                                                                                               @RequestParam(defaultValue = "5") int itemsPerPage,
                                                                                               @RequestParam(name = "status", required = false, defaultValue = "") String status,
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

        if (sortBy.startsWith("bank")) {
            // Need to handle differently with joins
            sortBy = "bank.bankCode";
        } else if (sortBy.startsWith("status")) {
            sortBy = "recordStatus";
        }

        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( status),
                search,
                direction,
                sortBy
        );

        Long companyId = claims.get("companyId", Long.class);

        ISummary companyAccountSummary = companyAccountRepository.getCompanyAccountSummaries(companyId).orElse(null);

        List<SelectDto> banks = bankService.getBanks();

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(new Triplet<>(companyAccountService.getAll(companyId, pageable, filterDto), banks, companyAccountSummary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompanyAccount(@RequestBody CompanyAccountDto companyAccountDto, @PathVariable long id) {
        if(companyAccountDto.getId() != id) {
            return ResponseEntity.badRequest().body("Id in path and body do not match");
        }
        try{
            CompanyAccountDto updatedClient = companyAccountService.update(companyAccountDto);
            return ResponseEntity.ok(updatedClient);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyAccountById(@PathVariable long id) {
        try{
        companyAccountService.deleteById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Company account deleted successfully");
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
}
