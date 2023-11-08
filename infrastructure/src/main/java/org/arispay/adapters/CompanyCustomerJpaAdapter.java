package org.arispay.adapters;

import org.arispay.data.CompanyCustomerDto;
import org.arispay.entity.CompanyCustomer;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.CompanyCustomerRepository;
import org.arispay.utils.ObjMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyCustomerJpaAdapter implements GenericServicePort<CompanyCustomerDto> {
	@Autowired
	CompanyCustomerRepository companyCustomerRepo;

	@Autowired
	ObjMapperUtils objMapperUtils;

	@Override
	public CompanyCustomerDto add(CompanyCustomerDto obj) {
		CompanyCustomer companyCustomer = (CompanyCustomer) objMapperUtils.convertToEntity(new CompanyCustomer(), obj);

		CompanyCustomer companyCustomerSaved = companyCustomerRepo.save(companyCustomer);

		return (CompanyCustomerDto) objMapperUtils.convertToDto(companyCustomerSaved, new CompanyCustomerDto());
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

		return (List<CompanyCustomerDto>) objMapperUtils.convertToDto(companyCustomerList, new CompanyCustomerDto());
	}

	@Override
	public CompanyCustomerDto getById(Long id) {
		Optional<CompanyCustomer> companyCustomer = companyCustomerRepo.findById(id);
		if (companyCustomer.isPresent()) {
			return (CompanyCustomerDto) objMapperUtils.convertToDto(companyCustomer, new CompanyCustomerDto());
		} else
			return null;

	}

	@Override
	public CompanyCustomerDto findByName(String name) {
		return null;
	}
}
