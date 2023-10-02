package com.arisweb.iservices;

import com.arisweb.model.Company;

import java.util.List;

public interface ICompanyService {
	public List<Company> getAll();

	public Company getById(Long id);

	public void add(Company company);

	public void delete(int id);
}
