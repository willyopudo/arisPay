package org.arispay.adapters;

import org.arispay.entity.Company;
import org.arispay.data.CompanyDto;
import org.arispay.mappers.CompanyMapper;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.arispay.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyJpaAdapter implements CompanyPersistencePort {
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public CompanyDto addCompany(CompanyDto companyDto) {

		Company company = companyMapper.companyDtoToCompany(companyDto);

		Company companySaved = companyRepository.save(company);

		return companyMapper.companyToCompanyDto(companySaved);
	}

	@Override
	public void deleteCompanyById(Long id) {
		companyRepository.deleteById(id);
	}

	@Override
	public CompanyDto updateCompany(CompanyDto companyDto) {
		return addCompany(companyDto);
	}

	@Override
	public List<CompanyDto> getCompanies() {

		List<Company> companyList = companyRepository.findAll();

		return companyMapper.companyListToCompanyDtoList(companyList);
	}

	@Override
	public CompanyDto getCompanyById(Long companyId) {

		Optional<Company> company = companyRepository.findById(companyId);

		return company.map(companyMapper::companyToCompanyDto).orElse(null);

	}
}
