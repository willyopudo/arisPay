package org.arispay.ports.api;

import org.arispay.data.CompanyDto;

import java.util.List;

public interface CompanyServicePort {
	CompanyDto addCompany(CompanyDto companyDto);

	void deleteCompanyById(Long id);

	CompanyDto updateCompany(CompanyDto companyDto);

	List<CompanyDto> getCompanies();

	CompanyDto getCompanyById(Long bookId);
}
