package com.arisweb.services;

import com.arisweb.iservices.ICompanyService;
import com.arisweb.model.Company;
import com.arisweb.model.Machine;
import com.arisweb.repository.ICompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService implements ICompanyService {
	@Autowired
	ICompanyRepository companyRepo;

	@Override
	public List<Company> getAll() {
		return (List<Company>) companyRepo.findAll();
	}

	@Override
	public Company getById(Long id) {
		return companyRepo.findById(id).get();
	}

	@Override
	public void add(Company company) {
		companyRepo.save(company);
	}

	@Override
	public void delete(int id) {
		companyRepo.deleteById((long) id);
	}
}
