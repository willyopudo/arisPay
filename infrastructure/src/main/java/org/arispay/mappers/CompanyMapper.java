package org.arispay.mappers;

import org.arispay.data.CompanyDto;
import org.arispay.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	// CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

	//Dummy comment
	CompanyDto companyToCompanyDto(Company company);

	Company companyDtoToCompany(CompanyDto companyDto);

	List<CompanyDto> companyListToCompanyDtoList(List<Company> companyList);

	List<Company> CompanyDtoListTocompanyList(List<CompanyDto> CompanyDtoList);
}
