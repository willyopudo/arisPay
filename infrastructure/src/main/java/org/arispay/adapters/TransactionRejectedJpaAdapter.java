package org.arispay.adapters;

import org.arispay.data.TransactionDto;
import org.arispay.entity.TransactionRejected;
import org.arispay.mappers.TransactionRejectedMapper;
import org.arispay.ports.spi.TransactionPersistencePort;
import org.arispay.repository.TransactionRejectedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionRejectedJpaAdapter implements TransactionPersistencePort {
	@Autowired
	private TransactionRejectedRepository transactionRejectedRepository;

	@Autowired
	private TransactionRejectedMapper transactionMapper;

	@Override
	public TransactionDto addTransaction(TransactionDto transactionDto) {
		TransactionRejected transaction = transactionMapper.transactionDtoToTransactionRejected(transactionDto);
		return transactionMapper.transactionRejectedToTransactionDto(transactionRejectedRepository.save(transaction));
	}

	@Override
	public void deleteTransactionById(Long id) {
		transactionRejectedRepository.deleteById(id);
	}

	@Override
	public TransactionDto updateTransaction(TransactionDto transactionDto) {
		return addTransaction(transactionDto);
	}

	@Override
	public List<TransactionDto> getTransactions() {
		List<TransactionRejected> transactionsList = transactionRejectedRepository.findAll();
		return transactionMapper.transactionRejectedListToTransactionDtoList(transactionsList);
	}

	@Override
	public TransactionDto getTransactionById(Long id) {
		Optional<TransactionRejected> transaction = transactionRejectedRepository.findById(id);

		return transaction.map(transactionMapper::transactionRejectedToTransactionDto).orElse(null);
	}

}
