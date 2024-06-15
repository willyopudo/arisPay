package org.arispay.ports.api;

public interface CompanyAccountServicePort<T> extends GenericServicePort<T> {
    T getByAccountNumber(String accountNumber);
}
