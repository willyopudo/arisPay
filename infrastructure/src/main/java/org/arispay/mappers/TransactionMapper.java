package org.arispay.mappers;

import java.util.List;

import org.arispay.data.ClientDto;
import org.arispay.data.TransactionDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.CompanyAccount;
import org.arispay.entity.Transaction;
import org.arispay.repository.ClientRepository;
import org.arispay.repository.CompanyAccountRepository;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring", uses = {ClientRepository.class, CompanyRepository.class, CompanyAccountRepository.class})
public abstract class TransactionMapper {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAccountRepository companyAccountRepository;

    //temp comment

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    @Mapping(source = "companyAccount", target = "bankAccount", qualifiedByName = "companyAccountToCollectionAccount")
    @Mapping(source = "companyAccount", target = "bank", qualifiedByName = "companyAccountBankToBank")
    public abstract TransactionDto transactionToTransactionDto(Transaction transaction);


    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    @Mapping(source = "bankAccount", target = "companyAccount", qualifiedByName = "collectionAccountToCompanyAccount")
    public abstract Transaction transactionDtoToTransaction(TransactionDto transactionDto);

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    @Mapping(source = "companyAccount", target = "bankAccount", qualifiedByName = "companyAccountToCollectionAccount")
    @Mapping(source = "companyAccount", target = "bank", qualifiedByName = "companyAccountBankToBank")
    public abstract List<TransactionDto> transactionListToTransactionDtoList(List<Transaction> clientList);

    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
    @Mapping(source = "bankAccount", target = "companyAccount", qualifiedByName = "collectionAccountToCompanyAccount")
    public abstract List<Transaction> transactionDtoListToTransactionList(List<TransactionDto> ClientDtoList);

    public Page<TransactionDto> transactionsPagetoTransactionsDtoPage(Page<Transaction> transactionPage) {
        List<TransactionDto> dtoList = transactionListToTransactionDtoList(transactionPage.getContent());  // Convert list
        return new PageImpl<>(dtoList, transactionPage.getPageable(), transactionPage.getTotalElements());
    }

    @Named("idToClient")
    public Client idToClient(String id) {
        return clientRepository.findByClientId(id).orElse(null);
    }

    @Named("clientToId")
    public static String clientToId(Client client) {
        return client != null ? client.getClientId() : null;
    }

    @Named("idToCompany")
    public Company idToCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }

    @Named("tranIdToArisTranRef")
    public static String tranIdToArisTranRef(long id) {
        return "ARIS" + String.format("%0" + 9 + "d", id);
    }

    @Named("collectionAccountToCompanyAccount")
    public CompanyAccount collectionAccountToCompanyAccount(String acc) {
        return companyAccountRepository.findByAccountNumber(acc).orElse(null);
    }

    @Named("companyAccountToCollectionAccount")
    public static String companyAccountToCollectionAccount(CompanyAccount companyAccount) {
        return companyAccount != null ? companyAccount.getAccountNumber() : null;
    }

    @Named("companyAccountBankToBank")
    public static String companyAccountBankToBank(CompanyAccount companyAccount) {
        return companyAccount != null ? companyAccount.getBank().getBankCode() + " " + companyAccount.getBank().getBankName() : null;
    }
}
