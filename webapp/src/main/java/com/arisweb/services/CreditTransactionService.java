package com.arisweb.services;

import com.arisweb.iservices.ICreditTransactionService;
import com.arisweb.model.CreditTransaction;
import com.arisweb.repository.ICreditTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditTransactionService implements ICreditTransactionService {
	@Autowired
	ICreditTransactionRepository creditTransactionRepo;

	@Override
	public List<CreditTransaction> getAll() {
		return (List<CreditTransaction>) creditTransactionRepo.findAll();
	}

	@Override
	public CreditTransaction getById(Long id) {
		return creditTransactionRepo.findById(id).get();
	}

	@Override
	public void add(CreditTransaction credit) {
		creditTransactionRepo.save(credit);
	}

	@Override
	public void delete(int id) {
		creditTransactionRepo.deleteById((long) id);
	}
}
