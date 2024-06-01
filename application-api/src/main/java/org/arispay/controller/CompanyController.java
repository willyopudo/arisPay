package org.arispay.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.arispay.data.CompanyDto;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

	@Autowired
	private final CompanyServicePort companyServicePort;

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		return companyServicePort.addCompany(companyDto);
	}

	@PutMapping("")
	public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) {
		return companyServicePort.updateCompany(companyDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getCompanyByID(@PathVariable long id) {
		return ResponseEntity.ok(companyServicePort.getCompanyById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<CompanyDto>> getAllCompanies() {
		return ResponseEntity.ok(companyServicePort.getCompanies());
	}

	@DeleteMapping("/{id}")
	public void deleteCompanyByID(@PathVariable long id) {
		companyServicePort.deleteCompanyById(id);
	}

}
