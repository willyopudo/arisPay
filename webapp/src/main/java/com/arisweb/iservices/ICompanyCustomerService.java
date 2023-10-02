package com.arisweb.iservices;

import com.arisweb.model.CompanyCustomer;

import java.util.List;

public interface ICompanyCustomerService {
	public List<CompanyCustomer> getAll();

	public CompanyCustomer getById(Long id);

	public void add(CompanyCustomer customer);

	public void delete(int id);
}
