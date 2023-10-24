package com.arisweb.controller;

import lombok.RequiredArgsConstructor;
import org.arispay.data.CompanyDto;
import org.arispay.ports.api.CompanyServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {

	@Autowired
	private final CompanyServicePort companyServicePort;

	@PostMapping("/companies")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(
			@RequestBody CompanyDto companyDto
	) {
		companyServicePort.addCompany(companyDto);
	}

	@GetMapping("/companies")
	public ResponseEntity<List<CompanyDto>> findAllCompanies() {
		return ResponseEntity.ok(companyServicePort.getCompanies());
	}

	//	@GetMapping("/school/{school-id}")
//	public ResponseEntity<List<Company>> findAllStudents(
//			@PathVariable("school-id") Integer companyId
//	) {
//		return ResponseEntity.ok(service.findAllStudentsBySchool(schoolId));
//	}
	@GetMapping("/companies/{id}")
	public ResponseEntity<CompanyDto> findAllStudents(
			@PathVariable("id") Long companyId
	) {
		return ResponseEntity.ok(companyServicePort.getCompanyById(companyId));
	}

}
