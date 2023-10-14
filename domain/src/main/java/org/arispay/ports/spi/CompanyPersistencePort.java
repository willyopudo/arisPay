package org.arispay.ports.spi;

import org.arispay.data.CompanyDto;

import java.util.List;

public interface CompanyPersistencePort {
	CompanyDto addCompany(CompanyDto companyDto);

	void deleteCompanyById(Long id);

	CompanyDto updateCompany(CompanyDto companyDto);

	List<CompanyDto> getCompanies();

	CompanyDto getCompanyById(Long bookId);
}
