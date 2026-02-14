package org.arispay.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.arispay.data.CompanyDto;
import org.arispay.ports.api.CompanyServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CompanyController {

	@Autowired
	private final CompanyServicePort companyServicePort;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('COMPANY_USER') or hasRole('ADMIN')")
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		return companyServicePort.addCompany(companyDto);
	}

	@PutMapping
	@PreAuthorize("hasRole('COMPANY_USER') or hasRole('ADMIN')")
	public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) {
		return companyServicePort.updateCompany(companyDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getCompanyByID(@PathVariable long id) {
		return ResponseEntity.ok(companyServicePort.getCompanyById(id));
	}

	@GetMapping
	@PreAuthorize("hasRole('COMPANY_USER') or hasRole('ADMIN')")
	public ResponseEntity<List<CompanyDto>> getAllCompanies() {
		return ResponseEntity.ok(companyServicePort.getCompanies());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('COMPANY_USER') or hasRole('ADMIN')")
	public void deleteCompanyByID(@PathVariable long id) {
		companyServicePort.deleteCompanyById(id);
	}

}
