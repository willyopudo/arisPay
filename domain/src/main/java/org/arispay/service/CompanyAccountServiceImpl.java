package org.arispay.service;

import lombok.RequiredArgsConstructor;
import org.arispay.data.GenericFilterDto;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.spi.CompanyAccountPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CompanyAccountServiceImpl<T>  implements CompanyAccountServicePort<T> {
    private CompanyAccountPersistencePort<T> companyAccountPersistencePort;

    public CompanyAccountServiceImpl(CompanyAccountPersistencePort<T> companyAccountPersistencePort) {
        this.companyAccountPersistencePort = companyAccountPersistencePort;
    }

    @Override
    public T getByAccountNumber(String accountNumber) {
        return companyAccountPersistencePort.getByAccountNumber(accountNumber);
    }

    @Override
    public Page<T> getAll(Long companyId, Pageable pageable, GenericFilterDto filterDto) {
        return companyAccountPersistencePort.getAll(companyId, pageable, filterDto);
    }

    @Override
    public T add(T obj) {
        return companyAccountPersistencePort.add(obj);
    }

    @Override
    public void deleteById(Long id) {
        companyAccountPersistencePort.deleteById(id);
    }

    @Override
    public T update(T obj) {
        return companyAccountPersistencePort.update(obj);
    }

    @Override
    public List<T> getAll() {
        return companyAccountPersistencePort.getAll();
    }


    @Override
    public T getById(Long id) {
        return companyAccountPersistencePort.getById(id);
    }

    @Override
    public T findByName(String name) {
        return companyAccountPersistencePort.getByName(name);
    }
}
