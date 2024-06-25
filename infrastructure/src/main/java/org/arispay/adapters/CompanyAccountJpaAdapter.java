package org.arispay.adapters;

import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.mappers.CompanyAccountMapper;
import org.arispay.ports.spi.CompanyAccountPersistencePort;
import org.arispay.ports.spi.GenericPersistencePort;
import org.arispay.repository.CompanyAccountRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyAccountJpaAdapter implements CompanyAccountPersistencePort<CompanyAccountDto> {
	@Autowired
	private CompanyAccountRepository companyAccountRepository;
	@Autowired
	CompanyAccountMapper companyAccountMapper;


	@Override
	public CompanyAccountDto add(CompanyAccountDto obj) {
		CompanyAccount companyAccount = ObjectMapperUtils.map(obj, CompanyAccount.class);

		CompanyAccount companyAccountSaved = companyAccountRepository.save(companyAccount);

		return companyAccountMapper.companyAccountToCompanyAccountDto(companyAccountSaved);
	}

	@Override
	public void deleteById(Long id) {
		companyAccountRepository.deleteById(id);
	}

	@Override
	public CompanyAccountDto update(CompanyAccountDto obj) {
		return add(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CompanyAccountDto> getAll() {
		List<CompanyAccount> companyAccountList = companyAccountRepository.findAll();

		return companyAccountMapper.companyAccountListToCompanyAccountDtoList(companyAccountList);
	}

	@Override
	public CompanyAccountDto getById(Long id) {
		Optional<CompanyAccount> companyAccount = companyAccountRepository.findById(id);
		if (companyAccount.isPresent()) {
			return companyAccountMapper.companyAccountToCompanyAccountDto(companyAccount.orElse(null));
		} else
			return null;

	}
	@Override
	public CompanyAccountDto getByAccountNumber(String accountNumber) {
		Optional<CompanyAccount> companyAccount = companyAccountRepository.findByAccountNumber(accountNumber);
		return companyAccountMapper.companyAccountToCompanyAccountDto(companyAccount.orElse(null));
	}

	@Override
	public CompanyAccountDto getByName(String name) {
		return null;
	}
}
