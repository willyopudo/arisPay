package org.arispay.service;

import java.util.List;

import org.arispay.data.CompanyAccountDto;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.spi.CompanyAccountPersistencePort;

public class CompanyAccountServiceImpl implements CompanyAccountServicePort {

    private CompanyAccountPersistencePort bankAccountPersistencePort;

    @Override
    public CompanyAccountDto addBankAccount(CompanyAccountDto bankAccountDto) {
        return bankAccountPersistencePort.addBankAccount(bankAccountDto);
    }

    @Override
    public void deleteBankAccountById(Long id) {
        bankAccountPersistencePort.deleteBankAccountById(id);
    }

    @Override
    public CompanyAccountDto updateBankAccount(CompanyAccountDto bankAccountDto) {
        return bankAccountPersistencePort.updateBankAccount(bankAccountDto);
    }

    @Override
    public List<CompanyAccountDto> getBankAccounts() {
        return bankAccountPersistencePort.getBankAccounts();
    }

    @Override
    public CompanyAccountDto getBankAccountById(Long id) {
        return bankAccountPersistencePort.getBankAccountById(id);
    }

    @Override
    public CompanyAccountDto getBankAccountByAccountNumber(String accountNumber) {
        return bankAccountPersistencePort.getBankAccountByAccountNumber(accountNumber);
    }

}
