package org.arispay.ports.spi;

import java.util.List;

import org.arispay.data.CompanyAccountDto;

public interface CompanyAccountPersistencePort {
    CompanyAccountDto addBankAccount(CompanyAccountDto bankAccountDto);

    void deleteBankAccountById(Long id);

    CompanyAccountDto updateBankAccount(CompanyAccountDto bankAccountDto);

    List<CompanyAccountDto> getBankAccounts();

    CompanyAccountDto getBankAccountById(Long id);

    CompanyAccountDto getBankAccountByAccountNumber(String accountNumber);
}
