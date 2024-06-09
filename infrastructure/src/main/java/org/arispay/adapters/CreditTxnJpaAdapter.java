package org.arispay.adapters;

import org.arispay.data.TransactionDto;
import org.arispay.entity.Transaction;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CreditTxnRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditTxnJpaAdapter implements GenericServicePort<TransactionDto> {
	@Autowired
	private CreditTxnRepository creditTxnRepo;


	@Override
	public TransactionDto add(TransactionDto obj) {
		Transaction transaction = ObjectMapperUtils.map(obj, Transaction.class);

		Transaction transactionSaved = creditTxnRepo.save(transaction);

		return ObjectMapperUtils.map(transactionSaved, TransactionDto.class);
	}

	@Override
	public void deleteById(Long id) {
		creditTxnRepo.deleteById(id);
	}

	@Override
	public TransactionDto update(TransactionDto obj) {
		return add(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransactionDto> getAll() {
		List<Transaction> transactionList = creditTxnRepo.findAll();

		return ObjectMapperUtils.mapAll(transactionList, TransactionDto.class);
	}

	@Override
	public TransactionDto getById(Long id) {
		Optional<Transaction> creditTxn = creditTxnRepo.findById(id);
		if (creditTxn.isPresent()) {
			return ObjectMapperUtils.map(creditTxn, TransactionDto.class);
		} else
			return null;

	}

	@Override
	public TransactionDto findByName(String name) {
		return null;
	}
}
