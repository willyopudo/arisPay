package org.arispay.adapters;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.TransactionDto;
import org.arispay.entity.Client;
import org.arispay.entity.Transaction;
import org.arispay.mappers.TransactionMapper;
import org.arispay.ports.spi.TransactionPersistencePort;
import org.arispay.repository.TransactionRepository;
import org.arispay.specifications.ClientSpecification;
import org.arispay.specifications.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter) {

		Specification<Transaction> transactionSpecification = TransactionSpecification.buildComplexSpecification(companyId, null, filter);

		// Create sort for standard fields if specified
		if (filter.getSortBy() != null && filter.getDirection() != null) {
			Sort sort = Sort.by(filter.getDirection(), filter.getSortBy());
			pageable = PageRequest.of(
					pageable.getPageNumber(),
					pageable.getPageSize(),
					sort
			);
		}
		Page<Transaction> transactionList = transactionRepository.findAll(transactionSpecification, pageable);
		return transactionMapper.transactionsPagetoTransactionsDtoPage(transactionList);

	}

	@Override
	public TransactionDto getTransactionById(Long id) {
		Optional<Transaction> transaction = transactionRepository.findById(id);

		return transaction.map(transactionMapper::transactionToTransactionDto).orElse(null);
	}

}
