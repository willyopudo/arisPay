package com.arisweb.services;

import com.arisweb.iservices.ICompanyCustomerService;
import com.arisweb.model.CompanyCustomer;
import com.arisweb.repository.ICompanyCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyCustomerService implements ICompanyCustomerService {
	@Autowired
	ICompanyCustomerRepository companyCustomer;

	@Override
	public List<CompanyCustomer> getAll() {
		return (List<CompanyCustomer>) companyCustomer.findAll();
	}

	@Override
	public CompanyCustomer getById(Long id) {
		return companyCustomer.findById(id).get();
	}

	@Override
	public void add(CompanyCustomer customer) {
		companyCustomer.save(customer);
	}

	@Override
	public void delete(int id) {
		companyCustomer.deleteById((long) id);
	}
}
