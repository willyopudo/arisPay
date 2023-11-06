package org.arispay.adapters;

import org.arispay.data.CreditTxnDto;
import org.arispay.entity.CreditTxn;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.utils.ObjMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditTxnJpaAdapter implements GenericServicePort<CreditTxnDto> {
	@Autowired
	JpaRepository<CreditTxn, Long> jpaRepository;

	@Autowired
	ObjMapperUtils objMapperUtils;

	@Override
	public CreditTxnDto add(CreditTxnDto obj) {
		CreditTxn creditTxn = (CreditTxn) objMapperUtils.convertToEntity(new CreditTxn(), obj);

		CreditTxn creditTxnSaved = jpaRepository.save(creditTxn);

		return (CreditTxnDto) objMapperUtils.convertToDto(creditTxnSaved, new CreditTxnDto());
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public CreditTxnDto update(CreditTxnDto obj) {
		return add(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CreditTxnDto> getAll() {
		List<CreditTxn> creditTxnList = jpaRepository.findAll();

		return (List<CreditTxnDto>) objMapperUtils.convertToDto(creditTxnList, new CreditTxnDto());
	}

	@Override
	public CreditTxnDto getById(Long id) {
		Optional<CreditTxn> creditTxn = jpaRepository.findById(id);
		if (creditTxn.isPresent()) {
			return (CreditTxnDto) objMapperUtils.convertToDto(creditTxn, new CreditTxnDto());
		} else
			return null;

	}
}
