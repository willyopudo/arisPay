package org.arispay.adapters;

import org.arispay.data.CreditTxnDto;
import org.arispay.entity.CreditTxn;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CreditTxnRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditTxnJpaAdapter implements GenericServicePort<CreditTxnDto> {
	@Autowired
	private CreditTxnRepository creditTxnRepo;


	@Override
	public CreditTxnDto add(CreditTxnDto obj) {
		CreditTxn creditTxn = ObjectMapperUtils.map(obj, CreditTxn.class);

		CreditTxn creditTxnSaved = creditTxnRepo.save(creditTxn);

		return ObjectMapperUtils.map(creditTxnSaved, CreditTxnDto.class);
	}

	@Override
	public void deleteById(Long id) {
		creditTxnRepo.deleteById(id);
	}

	@Override
	public CreditTxnDto update(CreditTxnDto obj) {
		return add(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CreditTxnDto> getAll() {
		List<CreditTxn> creditTxnList = creditTxnRepo.findAll();

		return ObjectMapperUtils.mapAll(creditTxnList, CreditTxnDto.class);
	}

	@Override
	public CreditTxnDto getById(Long id) {
		Optional<CreditTxn> creditTxn = creditTxnRepo.findById(id);
		if (creditTxn.isPresent()) {
			return ObjectMapperUtils.map(creditTxn, CreditTxnDto.class);
		} else
			return null;

	}

	@Override
	public CreditTxnDto findByName(String name) {
		return null;
	}
}
