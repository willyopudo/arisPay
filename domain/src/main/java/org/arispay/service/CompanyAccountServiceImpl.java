package org.arispay.service;

import lombok.RequiredArgsConstructor;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.spi.CompanyAccountPersistencePort;

@RequiredArgsConstructor
public class CompanyAccountServiceImpl<T> extends GenericServiceImpl<T> implements CompanyAccountServicePort<T> {
    private CompanyAccountPersistencePort<T> companyAccountPersistencePort;

    public CompanyAccountServiceImpl(CompanyAccountPersistencePort<T> companyAccountPersistencePort) {
        this.companyAccountPersistencePort = companyAccountPersistencePort;
    }

    @Override
    public T getByAccountNumber(String accountNumber) {
        return companyAccountPersistencePort.getByAccountNumber(accountNumber);
    }
}
