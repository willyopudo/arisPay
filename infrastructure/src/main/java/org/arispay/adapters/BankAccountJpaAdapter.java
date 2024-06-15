package org.arispay.adapters;

import org.arispay.data.BankAccountDto;
import org.arispay.entity.BankAccount;
import org.arispay.mappers.BankAccountMapper;
import org.arispay.ports.spi.BankAccountPersistencePort;
import org.arispay.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountJpaAdapter implements BankAccountPersistencePort {
	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private BankAccountMapper bankAccountMapper;

	@Override
	public BankAccountDto addBankAccount(BankAccountDto bankAccountDto) {
		BankAccount bankAccount = bankAccountMapper.bankAccountDtoToBankAccount(bankAccountDto);
		return bankAccountMapper.bankAccountToBankAccountDto(bankAccountRepository.save(bankAccount));
	}

	@Override
	public void deleteBankAccountById(Long id) {
		bankAccountRepository.deleteById(id);
	}

	@Override
	public BankAccountDto updateBankAccount(BankAccountDto bankAccountDto) {
		return addBankAccount(bankAccountDto);
	}

	@Override
	public List<BankAccountDto> getBankAccounts() {
		List<BankAccount> bankAccounts = bankAccountRepository.findAll();
		return bankAccountMapper.bankAccountListToBankAccountDtoList(bankAccounts);
	}

	@Override
	public BankAccountDto getBankAccountById(Long id) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
		return bankAccount.map(
				bankAccountMapper::bankAccountToBankAccountDto).orElse(null);
	}

	@Override
	public BankAccountDto getBankAccountByAccountNumber(String accountNumber) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
		return bankAccount.map(
				bankAccountMapper::bankAccountToBankAccountDto).orElse(null);
	}

}
