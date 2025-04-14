package org.arispay.mappers;

import java.util.List;

import org.arispay.data.ClientDto;
import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.Bank;
import org.arispay.entity.Client;
import org.arispay.entity.CompanyAccount;
import org.arispay.entity.Company;
import org.arispay.enums.RecordStatus;
import org.arispay.repository.BankRepository;
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

    @Autowired
    private BankRepository bankRepository;

    //Added Currency

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "recordStatus", target = "status", qualifiedByName = "RecordStatusToString")
    @Mapping(source = "company", target = "companyName", qualifiedByName = "companyToName")
    @Mapping(source = "bank", target = "bankCode", qualifiedByName = "bankToCode")
    @Mapping(source = "bank", target = "bankName", qualifiedByName = "bankToName")
    public abstract CompanyAccountDto companyAccountToCompanyAccountDto(CompanyAccount companyAccount);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    @Mapping(source = "status", target = "recordStatus", qualifiedByName = "StringToRecordStatus")
    @Mapping(source = "bankCode", target = "bank", qualifiedByName = "codeToBank")
    public abstract CompanyAccount companyAccountDtoToCompanyAccount(CompanyAccountDto companyAccountDto);

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "recordStatus", target = "status", qualifiedByName = "RecordStatusToString")
    @Mapping(source = "company", target = "companyName", qualifiedByName = "companyToName")
    @Mapping(source = "bank", target = "bankCode", qualifiedByName = "bankToCode")
    @Mapping(source = "bank", target = "bankName", qualifiedByName = "bankToName")
    public abstract List<CompanyAccountDto> companyAccountListToCompanyAccountDtoList(
            List<CompanyAccount> companyAccountList);

    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    @Mapping(source = "status", target = "recordStatus", qualifiedByName = "StringToRecordStatus")
    @Mapping(source = "bankCode", target = "bank", qualifiedByName = "codeToBank")
    public abstract List<CompanyAccount> companyAccountDtoListToCompanyAccountList(
            List<CompanyAccountDto> companyAccountDtoList);

    public Page<CompanyAccountDto> accountsPagetoAccountsDtoPage(Page<CompanyAccount> accountsPage) {
        List<CompanyAccountDto> dtoList = companyAccountListToCompanyAccountDtoList(accountsPage.getContent());  // Convert list
        return new PageImpl<>(dtoList, accountsPage.getPageable(), accountsPage.getTotalElements());
    }

    @Named("companyToName")
    public static String companyToName(Company company) {
        return company.getName();
    }

    @Named("idToCompany")
    public Company idToCompany(long id) {
        return companyRepository.getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }

    @Named("codeToBank")
    public Bank codeToBank(String code) {
        return bankRepository.findByBankCode(code).orElse(null);
    }

    @Named("bankToCode")
    public static String bankToCode(Bank bank) {
        return bank.getBankCode();
    }

    @Named("bankToName")
    public static String bankToName(Bank bank) {
        return bank.getBankName();
    }

    @Named("StringToRecordStatus")
    public RecordStatus StringToRecordStatus(String recordStatus) {
        return RecordStatus.fromString(recordStatus);
    }

    @Named("RecordStatusToString")
    public String RecordStatusToString(RecordStatus recordStatus) {
        return recordStatus.toString();
    }
}