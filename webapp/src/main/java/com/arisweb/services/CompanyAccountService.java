package com.arisweb.services;

import com.arisweb.iservices.ICompanyAccountService;
import com.arisweb.model.CompanyAccount;
import com.arisweb.repository.ICompanyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyAccountService implements ICompanyAccountService {
	@Autowired
	ICompanyAccountRepository companyAccountRepo;

	@Override
	public List<CompanyAccount> getAll() {
		return (List<CompanyAccount>) companyAccountRepo.findAll();
	}

	@Override
	public CompanyAccount getById(Long id) {
		return companyAccountRepo.findById(id).get();
	}

	@Override
	public void add(CompanyAccount companyAccount) {
		companyAccountRepo.save(companyAccount);
	}

	@Override
	public void delete(int id) {
		companyAccountRepo.deleteById((long) id);
	}
}
