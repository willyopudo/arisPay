package org.arispay.mappers;

import java.util.List;

import org.arispay.data.ClientDto;
import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.Client;
import org.arispay.entity.CompanyAccount;
import org.arispay.entity.Company;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring", uses = CompanyRepository.class)
public abstract class CompanyAccountMapper {
    @Autowired
    private CompanyRepository companyRepository;

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    public abstract CompanyAccountDto companyAccountToCompanyAccountDto(CompanyAccount companyAccount);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    public abstract CompanyAccount companyAccountDtoToCompanyAccount(CompanyAccountDto companyAccountDto);

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    public abstract List<CompanyAccountDto> companyAccountListToCompanyAccountDtoList(
            List<CompanyAccount> companyAccountList);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    public abstract List<CompanyAccount> companyAccountDtoListToCompanyAccountList(
            List<CompanyAccountDto> companyAccountDtoList);

    public Page<CompanyAccountDto> accountsPagetoAccountsDtoPage(Page<CompanyAccount> accountsPage) {
        List<CompanyAccountDto> dtoList = companyAccountListToCompanyAccountDtoList(accountsPage.getContent());  // Convert list
        return new PageImpl<>(dtoList, accountsPage.getPageable(), accountsPage.getTotalElements());
    }

    @Named("idToCompany")
    public Company idToCompany(long id) {
        return companyRepository.getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}