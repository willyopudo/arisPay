package org.arispay.ports.api;

import java.util.List;

import org.arispay.data.BankAccountDto;

public interface BankAccountServicePort {
    BankAccountDto addBankAccount(BankAccountDto bankAccountDto);

    void deleteBankAccountById(Long id);

    BankAccountDto updateBankAccount(BankAccountDto bankAccountDto);

    List<BankAccountDto> getBankAccounts();

    BankAccountDto getBankAccountById(Long id);

    BankAccountDto getBankAccountByAccountNumber(String accountNumber);
}
