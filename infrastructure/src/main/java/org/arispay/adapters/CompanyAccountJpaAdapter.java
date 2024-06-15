package org.arispay.adapters;

import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.mappers.CompanyAccountMapper;
import org.arispay.ports.spi.CompanyAccountPersistencePort;
import org.arispay.repository.CompanyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyAccountJpaAdapter implements CompanyAccountPersistencePort {
	@Autowired
	private CompanyAccountRepository bankAccountRepository;

	@Autowired
	private CompanyAccountMapper bankAccountMapper;

	@Override
	public CompanyAccountDto addBankAccount(CompanyAccountDto bankAccountDto) {
		CompanyAccount bankAccount = bankAccountMapper.bankAccountDtoToBankAccount(bankAccountDto);
		return bankAccountMapper.bankAccountToBankAccountDto(bankAccountRepository.save(bankAccount));
	}

	@Override
	public void deleteBankAccountById(Long id) {
		bankAccountRepository.deleteById(id);
	}

	@Override
	public CompanyAccountDto updateBankAccount(CompanyAccountDto bankAccountDto) {
		return addBankAccount(bankAccountDto);
	}

	@Override
	public List<CompanyAccountDto> getBankAccounts() {
		List<CompanyAccount> bankAccounts = bankAccountRepository.findAll();
		return bankAccountMapper.bankAccountListToBankAccountDtoList(bankAccounts);
	}

	@Override
	public CompanyAccountDto getBankAccountById(Long id) {
		Optional<CompanyAccount> bankAccount = bankAccountRepository.findById(id);
		return bankAccount.map(
				bankAccountMapper::bankAccountToBankAccountDto).orElse(null);
	}

	@Override
	public CompanyAccountDto getBankAccountByAccountNumber(String accountNumber) {
		Optional<CompanyAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
		return bankAccount.map(
				bankAccountMapper::bankAccountToBankAccountDto).orElse(null);
	}

}
