package org.arispay.service;

import lombok.RequiredArgsConstructor;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.spi.CompanyAccountPersistencePort;

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
