package org.arispay.mappers;

import java.util.List;

import org.arispay.data.BankAccountDto;
import org.arispay.entity.BankAccount;
import org.arispay.entity.Company;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    static CompanyRepository getCompanyRepository() {
        return null;
    }

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    BankAccountDto bankAccountToBankAccountDto(BankAccount bankAccount);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    BankAccount bankAccountDtoToBankAccount(BankAccountDto bankAccountDto);

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    List<BankAccountDto> bankAccountListToBankAccountDtoList(List<BankAccount> bankAccountList);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    List<BankAccount> bankAccountDtoListToBankAccountList(List<BankAccountDto> bankAccountDtoList);

    @Named("idToCompany")
    public static Company idToCompany(long id) {
        return getCompanyRepository().getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}