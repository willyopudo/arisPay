package com.arisweb.iservices;

import com.arisweb.model.CompanyAccount;

import java.util.List;

public interface ICompanyAccountService {
	public List<CompanyAccount> getAll();

	public CompanyAccount getById(Long id);

	public void add(CompanyAccount account);

	public void delete(int id);
}
