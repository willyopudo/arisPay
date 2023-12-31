package org.arispay.adapters;

import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CompanyAccountRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyAccountJpaAdapter implements GenericServicePort<CompanyAccountDto> {
	@Autowired
	CompanyAccountRepository companyAccountRepository;


	@Override
	public CompanyAccountDto add(CompanyAccountDto obj) {
		CompanyAccount companyAccount = ObjectMapperUtils.map(obj, CompanyAccount.class);

		CompanyAccount companyAccountSaved = companyAccountRepository.save(companyAccount);

		return ObjectMapperUtils.map(companyAccountSaved, CompanyAccountDto.class);
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

		return ObjectMapperUtils.mapAll(companyAccountList, CompanyAccountDto.class);
	}

	@Override
	public CompanyAccountDto getById(Long id) {
		Optional<CompanyAccount> companyAccount = companyAccountRepository.findById(id);
		if (companyAccount.isPresent()) {
			return ObjectMapperUtils.map(companyAccount, CompanyAccountDto.class);
		} else
			return null;

	}

	@Override
	public CompanyAccountDto findByName(String name) {
		return null;
	}
}
