package com.arisweb.controller;

import com.arisweb.iservices.ICompanyService;
import com.arisweb.model.Company;
import com.arisweb.services.CompanyService;
import lombok.RequiredArgsConstructor;
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
	private final CompanyService service;

	@PostMapping("/companies")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(
			@RequestBody Company company
	) {
		service.add(company);
	}

	@GetMapping("/companies")
	public ResponseEntity<List<Company>> findAllCompanies() {
		return ResponseEntity.ok(service.getAll());
	}

	//	@GetMapping("/school/{school-id}")
//	public ResponseEntity<List<Company>> findAllStudents(
//			@PathVariable("school-id") Integer companyId
//	) {
//		return ResponseEntity.ok(service.findAllStudentsBySchool(schoolId));
//	}
	@GetMapping("/companies/{id}")
	public ResponseEntity<Company> findAllStudents(
			@PathVariable("id") Long companyId
	) {
		return ResponseEntity.ok(service.getById(companyId));
	}

}
