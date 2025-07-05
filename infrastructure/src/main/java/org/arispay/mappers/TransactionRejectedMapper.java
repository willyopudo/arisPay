package org.arispay.mappers;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.entity.Company;
import org.arispay.entity.CompanyAccount;
import org.arispay.entity.Transaction;
import org.arispay.entity.TransactionRejected;
import org.arispay.repository.ClientRepository;
import org.arispay.repository.CompanyAccountRepository;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring", uses = ClientRepository.class)
public abstract class TransactionRejectedMapper {


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAccountRepository companyAccountRepository;

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    public abstract TransactionDto transactionRejectedToTransactionDto(TransactionRejected transaction);


    @Mapping(source = "bankAccount", target = "companyAccount", qualifiedByName = "bankAccountToCompanyAccount")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    public abstract TransactionRejected transactionDtoToTransactionRejected(TransactionDto transactionDto);

    public Page<TransactionDto> transactionsRejectedPagetoTransactionsDtoPage(Page<TransactionRejected> transactionPage) {
        List<TransactionDto> dtoList = transactionRejectedListToTransactionDtoList(transactionPage.getContent());  // Convert list
        return new PageImpl<>(dtoList, transactionPage.getPageable(), transactionPage.getTotalElements());
    }

    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    public abstract List<TransactionDto> transactionRejectedListToTransactionDtoList(
            List<TransactionRejected> transactionRejecteds);


    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    @Mapping(source = "bankAccount", target = "companyAccount", qualifiedByName = "bankAccountToCompanyAccount")
    public abstract List<TransactionRejected> transactionDtoListToTransactionRejectedList(
            List<TransactionDto> transactionDtos);


    @Named("tranIdToArisTranRef")
    public static String tranIdToArisTranRef(long id) {
        return "ARISF" + String.format("%0" + 9 + "d", id);
    }

    @Named("idToCompany")
    public Company idToCompany(Long id) {
        return id != null ? companyRepository.findById(id).orElse(null) : null;
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company !=null ? company.getId() : null;
    }

    @Named("bankAccountToCompanyAccount")
    public CompanyAccount idToCompanyAccount(String comapnyAccount) {
        return comapnyAccount != null ? companyAccountRepository.findByAccountNumber(comapnyAccount).orElse(null) : null;
    }

}
