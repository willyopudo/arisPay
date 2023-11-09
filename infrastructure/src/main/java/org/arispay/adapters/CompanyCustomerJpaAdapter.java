package org.arispay.adapters;

import org.arispay.data.CompanyCustomerDto;
import org.arispay.entity.CompanyCustomer;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CompanyCustomerRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyCustomerJpaAdapter implements GenericServicePort<CompanyCustomerDto> {
	@Autowired
	CompanyCustomerRepository companyCustomerRepo;


	@Override
	public CompanyCustomerDto add(CompanyCustomerDto obj) {
		CompanyCustomer companyCustomer = ObjectMapperUtils.map(obj, CompanyCustomer.class);

		CompanyCustomer companyCustomerSaved = companyCustomerRepo.save(companyCustomer);

		return ObjectMapperUtils.map(companyCustomerSaved, CompanyCustomerDto.class);
	}

	@Override
	public void deleteById(Long id) {
		companyCustomerRepo.deleteById(id);
	}

	@Override
	public CompanyCustomerDto update(CompanyCustomerDto obj) {
		return add(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CompanyCustomerDto> getAll() {
		List<CompanyCustomer> companyCustomerList = companyCustomerRepo.findAll();

		return ObjectMapperUtils.mapAll(companyCustomerList, CompanyCustomerDto.class);
	}

	@Override
	public CompanyCustomerDto getById(Long id) {
		Optional<CompanyCustomer> companyCustomer = companyCustomerRepo.findById(id);
		if (companyCustomer.isPresent()) {
			return ObjectMapperUtils.map(companyCustomer, CompanyCustomerDto.class);
		} else
			return null;

	}

	@Override
	public CompanyCustomerDto findByName(String name) {
		return null;
	}
}
