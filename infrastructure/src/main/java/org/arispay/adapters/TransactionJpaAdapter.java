package org.arispay.adapters;

import org.arispay.data.TransactionDto;
import org.arispay.entity.Transaction;
import org.arispay.mappers.TransactionMapper;
import org.arispay.ports.spi.TransactionPersistencePort;
import org.arispay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionJpaAdapter implements TransactionPersistencePort {
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionMapper transactionMapper;

	@Override
	public TransactionDto addTransaction(TransactionDto transactionDto) {
		Transaction transaction = transactionMapper.transactionDtoToTransaction(transactionDto);
		return transactionMapper.transactionToTransactionDto(transactionRepository.save(transaction));
	}

	@Override
	public void deleteTransactionById(Long id) {
		transactionRepository.deleteById(id);
	}

	@Override
	public TransactionDto updateTransaction(TransactionDto transactionDto) {
		return addTransaction(transactionDto);
	}

	@Override
	public List<TransactionDto> getTransactions() {
		List<Transaction> transactionsList = transactionRepository.findAll();
		return transactionMapper.transactionListToTransactionDtoList(transactionsList);
	}

	@Override
	public TransactionDto getTransactionById(Long id) {
		Optional<Transaction> transaction = transactionRepository.findById(id);

		return transaction.map(transactionMapper::transactionToTransactionDto).orElse(null);
	}

}
