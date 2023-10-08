package com.arisweb.iservices;

import com.arisweb.model.CreditTransaction;

import java.util.List;

public interface ICreditTransactionService {
	public List<CreditTransaction> getAll();

	public CreditTransaction getById(Long id);

	public void add(CreditTransaction txn);

	public void delete(int id);
}
