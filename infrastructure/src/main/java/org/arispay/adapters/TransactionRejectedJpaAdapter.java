package org.arispay.adapters;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.ISummary;
import org.arispay.data.TransactionDto;
import org.arispay.entity.Transaction;
import org.arispay.entity.TransactionRejected;
import org.arispay.mappers.TransactionRejectedMapper;
import org.arispay.ports.spi.TransactionRejectedPersistencePort;
import org.arispay.repository.QueryTransactionRejectedRepository;
import org.arispay.repository.TransactionRejectedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionRejectedJpaAdapter implements TransactionRejectedPersistencePort {
	@Autowired
	private TransactionRejectedRepository transactionRejectedRepository;

	@Autowired
	private TransactionRejectedMapper transactionMapper;

	@Autowired
	private QueryTransactionRejectedRepository queryTransactionRejectedRepository;

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
	public Page<TransactionDto> getTransactions(Long companyId, Pageable pageable, GenericFilterDto filter) {

		//Specification<Transaction> transactionSpecification = TransactionSpecification.buildComplexSpecification(companyId, null, filter);

		// Create sort for standard fields if specified
		if (filter.getSortBy() != null && filter.getDirection() != null) {
			Sort sort = Sort.by(filter.getDirection(), filter.getSortBy());
			pageable = PageRequest.of(
					pageable.getPageNumber(),
					pageable.getPageSize(),
					sort
			);
		}
		Page<TransactionRejected> transactionList = queryTransactionRejectedRepository.searchWithFullText(companyId, pageable, filter);
		return transactionMapper.transactionsRejectedPagetoTransactionsDtoPage(transactionList);

	}

	@Override
	public TransactionDto getTransactionById(Long id) {
		Optional<TransactionRejected> transaction = transactionRejectedRepository.findById(id);

		return transaction.map(transactionMapper::transactionRejectedToTransactionDto).orElse(null);
	}

	@Override
	public Optional<ISummary> getTransactionRejectedSummaries(Long companyId) {
		return transactionRejectedRepository.getTransactionRejectedSummaries(companyId);
	}

}
