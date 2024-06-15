package org.arispay.service;

import java.util.List;

import org.arispay.data.BankAccountDto;
import org.arispay.ports.api.BankAccountServicePort;
import org.arispay.ports.spi.BankAccountPersistencePort;

public class BankAccountServiceImpl implements BankAccountServicePort {

    private BankAccountPersistencePort bankAccountPersistencePort;

    @Override
    public BankAccountDto addBankAccount(BankAccountDto bankAccountDto) {
        return bankAccountPersistencePort.addBankAccount(bankAccountDto);
    }

    @Override
    public void deleteBankAccountById(Long id) {
        bankAccountPersistencePort.deleteBankAccountById(id);
    }

    @Override
    public BankAccountDto updateBankAccount(BankAccountDto bankAccountDto) {
        return bankAccountPersistencePort.updateBankAccount(bankAccountDto);
    }

    @Override
    public List<BankAccountDto> getBankAccounts() {
        return bankAccountPersistencePort.getBankAccounts();
    }

    @Override
    public BankAccountDto getBankAccountById(Long id) {
        return bankAccountPersistencePort.getBankAccountById(id);
    }

    @Override
    public BankAccountDto getBankAccountByAccountNumber(String accountNumber) {
        return bankAccountPersistencePort.getBankAccountByAccountNumber(accountNumber);
    }

}
