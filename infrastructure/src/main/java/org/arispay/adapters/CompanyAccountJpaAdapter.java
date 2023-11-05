package org.arispay.adapters;

import org.arispay.data.CompanyAccountDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CompanyAccountRepository;
import org.arispay.utils.ObjMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyAccountJpaAdapter implements GenericServicePort<CompanyAccountDto> {
	@Autowired
	CompanyAccountRepository companyAccountRepository;

	@Autowired
	ObjMapperUtils objMapperUtils;


	@Override
	public CompanyAccountDto add(CompanyAccountDto obj) {
		CompanyAccount companyAccount = (CompanyAccount) objMapperUtils.convertToEntity(new CompanyAccount(), obj);

		CompanyAccount companyAccountSaved = companyAccountRepository.save(companyAccount);

		return (CompanyAccountDto) objMapperUtils.convertToDto(companyAccountSaved, new CompanyAccountDto());
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

		return (List<CompanyAccountDto>) objMapperUtils.convertToDto(companyAccountList, new CompanyAccountDto());
	}

	@Override
	public CompanyAccountDto getById(Long id) {
		Optional<CompanyAccount> companyAccount = companyAccountRepository.findById(id);
		if (companyAccount.isPresent()) {
			return (CompanyAccountDto) objMapperUtils.convertToDto(companyAccount, new CompanyAccountDto());
		} else
			return null;

	}
}
