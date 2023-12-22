package org.arispay.service;

import org.arispay.data.CompanyDto;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.spi.CompanyPersistencePort;

import java.util.List;

public class  CompanyServiceImpl implements CompanyServicePort {

	private CompanyPersistencePort companyPersistencePort;

	public CompanyServiceImpl(CompanyPersistencePort companyPersistencePort) {
		this.companyPersistencePort = companyPersistencePort;
	}

	@Override
	public CompanyDto addCompany(CompanyDto companyDto) {
		return companyPersistencePort.addCompany(companyDto);
	}

	@Override
	public void deleteCompanyById(Long id) {
		companyPersistencePort.deleteCompanyById(id);
	}

	@Override
	public CompanyDto updateCompany(CompanyDto companyDto) {
		return companyPersistencePort.updateCompany(companyDto);
	}

	@Override
	public List<CompanyDto> getCompanies() {
		return companyPersistencePort.getCompanies();
	}

	@Override
	public CompanyDto getCompanyById(Long bookId) {
		return companyPersistencePort.getCompanyById(bookId);
	}
}
