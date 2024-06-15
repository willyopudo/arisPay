package org.arispay.ports.spi;

public interface CompanyAccountPersistencePort<T> extends GenericPersistencePort<T>{
    T getByAccountNumber(String accountNumber);
}
