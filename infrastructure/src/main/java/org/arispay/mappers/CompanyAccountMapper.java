package org.arispay.mappers;

import java.util.List;

import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.entity.Company;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CompanyAccountMapper {

    static CompanyRepository getCompanyRepository() {
        return null;
    }

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    CompanyAccountDto companyAccountToCompanyAccountDto(CompanyAccount companyAccount);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    CompanyAccount companyAccountDtoToCompanyAccount(CompanyAccountDto companyAccountDto);

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    List<CompanyAccountDto> companyAccountListToCompanyAccountDtoList(List<CompanyAccount> companyAccountList);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    List<CompanyAccount> companyAccountDtoListToCompanyAccountList(List<CompanyAccountDto> companyAccountDtoList);

    @Named("idToCompany")
    public static Company idToCompany(long id) {
        return getCompanyRepository().getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}