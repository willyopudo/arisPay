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

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    CompanyAccountDto bankAccountToBankAccountDto(CompanyAccount bankAccount);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    CompanyAccount bankAccountDtoToBankAccount(CompanyAccountDto bankAccountDto);

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    List<CompanyAccountDto> bankAccountListToBankAccountDtoList(List<CompanyAccount> bankAccountList);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    List<CompanyAccount> bankAccountDtoListToBankAccountList(List<CompanyAccountDto> bankAccountDtoList);

    @Named("idToCompany")
    public static Company idToCompany(long id) {
        return getCompanyRepository().getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}